/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nostra13.universalimageloader.cache.disc.impl.ext;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A cache that uses a bounded amount of space on a filesystem. Each cache
 * entry has a string key and a fixed number of values. Each key must match
 * the regex <strong>[a-z0-9_-]{1,64}</strong>. Values are byte sequences,
 * accessible as streams or files. Each value must be between {@code 0} and
 * {@code Integer.MAX_VALUE} bytes in length.
 *
 * <p>The cache stores its data in a directory on the filesystem. This
 * directory must be exclusive to the cache; the cache may delete or overwrite
 * files from its directory. It is an error for multiple processes to use the
 * same cache directory at the same time.
 *
 * <p>This cache limits the number of bytes that it will store on the
 * filesystem. When the number of stored bytes exceeds the limit, the cache will
 * remove entries in the background until the limit is satisfied. The limit is
 * not strict: the cache may temporarily exceed it while waiting for files to be
 * deleted. The limit does not include filesystem overhead or the cache
 * journal so space-sensitive applications should set a conservative limit.
 *
 * <p>Clients call {@link #edit} to create or update the values of an entry. An
 * entry may have only one editor at one time; if a value is not available to be
 * edited then {@link #edit} will return null.
 * <ul>
 * <li>When an entry is being <strong>created</strong> it is necessary to
 * supply a full set of values; the empty value should be used as a
 * placeholder if necessary.
 * <li>When an entry is being <strong>edited</strong>, it is not necessary
 * to supply data for every value; values default to their previous
 * value.
 * </ul>
 * Every {@link #edit} call must be matched by a call to {@link Editor#commit}
 * or {@link Editor#abort}. Committing is atomic: a read observes the full set
 * of values as they were before or after the commit, but never a mix of values.
 *
 * <p>Clients call {@link #get} to read a snapshot of an entry. The read will
 * observe the value at the time that {@link #get} was called. Updates and
 * removals after the call do not impact ongoing reads.
 *
 * <p>This class is tolerant of some I/O errors. If files are missing from the
 * filesystem, the corresponding entries will be dropped from the cache. If
 * an error occurs while writing a cache value, the edit will fail silently.
 * Callers should handle other problems by catching {@code IOException} and
 * responding appropriately.
 */
final class DiskLruCache implements java.io.Closeable {
	static final String JOURNAL_FILE = "journal";
	static final String JOURNAL_FILE_TEMP = "journal.tmp";
	static final String JOURNAL_FILE_BACKUP = "journal.bkp";
	static final String MAGIC = "libcore.io.DiskLruCache";
	static final String VERSION_1 = "1";
	static final long ANY_SEQUENCE_NUMBER = -1;
	static final java.util.regex.Pattern LEGAL_KEY_PATTERN = java.util.regex.Pattern.compile("[a-z0-9_-]{1,64}");
	private static final String CLEAN = "CLEAN";
	private static final String DIRTY = "DIRTY";
	private static final String REMOVE = "REMOVE";
	private static final String READ = "READ";

    /*
     * This cache uses a journal file named "journal". A typical journal file
     * looks like this:
     *     libcore.io.DiskLruCache
     *     1
     *     100
     *     2
     *
     *     CLEAN 3400330d1dfc7f3f7f4b8d4d803dfcf6 832 21054
     *     DIRTY 335c4c6028171cfddfbaae1a9c313c52
     *     CLEAN 335c4c6028171cfddfbaae1a9c313c52 3934 2342
     *     REMOVE 335c4c6028171cfddfbaae1a9c313c52
     *     DIRTY 1ab96a171faeeee38496d8b330771a7a
     *     CLEAN 1ab96a171faeeee38496d8b330771a7a 1600 234
     *     READ 335c4c6028171cfddfbaae1a9c313c52
     *     READ 3400330d1dfc7f3f7f4b8d4d803dfcf6
     *
     * The first five lines of the journal form its header. They are the
     * constant string "libcore.io.DiskLruCache", the disk cache's version,
     * the application's version, the value count, and a blank line.
     *
     * Each of the subsequent lines in the file is a record of the state of a
     * cache entry. Each line contains space-separated values: a state, a key,
     * and optional state-specific values.
     *   o DIRTY lines track that an entry is actively being created or updated.
     *     Every successful DIRTY action should be followed by a CLEAN or REMOVE
     *     action. DIRTY lines without a matching CLEAN or REMOVE indicate that
     *     temporary files may need to be deleted.
     *   o CLEAN lines track a cache entry that has been successfully published
     *     and may be read. A publish line is followed by the lengths of each of
     *     its values.
     *   o READ lines track accesses for LRU.
     *   o REMOVE lines track entries that have been deleted.
     *
     * The journal file is appended to as cache operations occur. The journal may
     * occasionally be compacted by dropping redundant lines. A temporary file named
     * "journal.tmp" will be used during compaction; that file should be deleted if
     * it exists when the cache is opened.
     */

	private final java.io.File directory;
	private final java.io.File journalFile;
	private final java.io.File journalFileTmp;
	private final java.io.File journalFileBackup;
	private final int appVersion;
	private long maxSize;
	private int maxFileCount;
	private final int valueCount;
	private long size = 0;
	private int fileCount = 0;
	private java.io.Writer journalWriter;
	private final java.util.LinkedHashMap<String, com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry> lruEntries =
			new java.util.LinkedHashMap<String, com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry>(0, 0.75f, true);
	private int redundantOpCount;

	/**
	 * To differentiate between old and current snapshots, each entry is given
	 * a sequence number each time an edit is committed. A snapshot is stale if
	 * its sequence number is not equal to its entry's sequence number.
	 */
	private long nextSequenceNumber = 0;

	/** This cache uses a single background thread to evict entries. */
	final java.util.concurrent.ThreadPoolExecutor executorService =
			new java.util.concurrent.ThreadPoolExecutor(0, 1, 60L, java.util.concurrent.TimeUnit.SECONDS, new java.util.concurrent.LinkedBlockingQueue<Runnable>());
	private final java.util.concurrent.Callable<Void> cleanupCallable = new java.util.concurrent.Callable<Void>() {
		public Void call() throws Exception {
			synchronized (com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.this) {
				if (journalWriter == null) {
					return null; // Closed.
				}
				trimToSize();
				trimToFileCount();
				if (journalRebuildRequired()) {
					rebuildJournal();
					redundantOpCount = 0;
				}
			}
			return null;
		}
	};

	private DiskLruCache(java.io.File directory, int appVersion, int valueCount, long maxSize, int maxFileCount) {
		this.directory = directory;
		this.appVersion = appVersion;
		this.journalFile = new java.io.File(directory, JOURNAL_FILE);
		this.journalFileTmp = new java.io.File(directory, JOURNAL_FILE_TEMP);
		this.journalFileBackup = new java.io.File(directory, JOURNAL_FILE_BACKUP);
		this.valueCount = valueCount;
		this.maxSize = maxSize;
		this.maxFileCount = maxFileCount;
	}

	/**
	 * Opens the cache in {@code directory}, creating a cache if none exists
	 * there.
	 *
	 * @param directory a writable directory
	 * @param valueCount the number of values per cache entry. Must be positive.
	 * @param maxSize the maximum number of bytes this cache should use to store
	 * @param maxFileCount the maximum file count this cache should store
	 * @throws IOException if reading or writing the cache directory fails
	 */
	public static com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache open(java.io.File directory, int appVersion, int valueCount, long maxSize, int maxFileCount)
			throws java.io.IOException {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		if (maxFileCount <= 0) {
			throw new IllegalArgumentException("maxFileCount <= 0");
		}
		if (valueCount <= 0) {
			throw new IllegalArgumentException("valueCount <= 0");
		}

		// If a bkp file exists, use it instead.
		java.io.File backupFile = new java.io.File(directory, JOURNAL_FILE_BACKUP);
		if (backupFile.exists()) {
			java.io.File journalFile = new java.io.File(directory, JOURNAL_FILE);
			// If journal file also exists just delete backup file.
			if (journalFile.exists()) {
				backupFile.delete();
			} else {
				renameTo(backupFile, journalFile, false);
			}
		}

		// Prefer to pick up where we left off.
		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache cache = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache(directory, appVersion, valueCount, maxSize, maxFileCount);
		if (cache.journalFile.exists()) {
			try {
				cache.readJournal();
				cache.processJournal();
				cache.journalWriter = new java.io.BufferedWriter(
						new java.io.OutputStreamWriter(new java.io.FileOutputStream(cache.journalFile, true), com.nostra13.universalimageloader.cache.disc.impl.ext.Util.US_ASCII));
				return cache;
			} catch (java.io.IOException journalIsCorrupt) {
				System.out
						.println("DiskLruCache "
								+ directory
								+ " is corrupt: "
								+ journalIsCorrupt.getMessage()
								+ ", removing");
				cache.delete();
			}
		}

		// Create a new empty cache.
		directory.mkdirs();
		cache = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache(directory, appVersion, valueCount, maxSize, maxFileCount);
		cache.rebuildJournal();
		return cache;
	}

	private void readJournal() throws java.io.IOException {
		StrictLineReader reader = new StrictLineReader(new java.io.FileInputStream(journalFile), com.nostra13.universalimageloader.cache.disc.impl.ext.Util.US_ASCII);
		try {
			String magic = reader.readLine();
			String version = reader.readLine();
			String appVersionString = reader.readLine();
			String valueCountString = reader.readLine();
			String blank = reader.readLine();
			if (!MAGIC.equals(magic)
					|| !VERSION_1.equals(version)
					|| !Integer.toString(appVersion).equals(appVersionString)
					|| !Integer.toString(valueCount).equals(valueCountString)
					|| !"".equals(blank)) {
				throw new java.io.IOException("unexpected journal header: [" + magic + ", " + version + ", "
						+ valueCountString + ", " + blank + "]");
			}

			int lineCount = 0;
			while (true) {
				try {
					readJournalLine(reader.readLine());
					lineCount++;
				} catch (java.io.EOFException endOfJournal) {
					break;
				}
			}
			redundantOpCount = lineCount - lruEntries.size();
		} finally {
			com.nostra13.universalimageloader.cache.disc.impl.ext.Util.closeQuietly(reader);
		}
	}

	private void readJournalLine(String line) throws java.io.IOException {
		int firstSpace = line.indexOf(' ');
		if (firstSpace == -1) {
			throw new java.io.IOException("unexpected journal line: " + line);
		}

		int keyBegin = firstSpace + 1;
		int secondSpace = line.indexOf(' ', keyBegin);
		final String key;
		if (secondSpace == -1) {
			key = line.substring(keyBegin);
			if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
				lruEntries.remove(key);
				return;
			}
		} else {
			key = line.substring(keyBegin, secondSpace);
		}

		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = lruEntries.get(key);
		if (entry == null) {
			entry = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry(key);
			lruEntries.put(key, entry);
		}

		if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
			String[] parts = line.substring(secondSpace + 1).split(" ");
			entry.readable = true;
			entry.currentEditor = null;
			entry.setLengths(parts);
		} else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
			entry.currentEditor = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor(entry);
		} else if (secondSpace == -1 && firstSpace == READ.length() && line.startsWith(READ)) {
			// This work was already done by calling lruEntries.get().
		} else {
			throw new java.io.IOException("unexpected journal line: " + line);
		}
	}

	/**
	 * Computes the initial size and collects garbage as a part of opening the
	 * cache. Dirty entries are assumed to be inconsistent and will be deleted.
	 */
	private void processJournal() throws java.io.IOException {
		deleteIfExists(journalFileTmp);
		for (java.util.Iterator<com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry> i = lruEntries.values().iterator(); i.hasNext(); ) {
			com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = i.next();
			if (entry.currentEditor == null) {
				for (int t = 0; t < valueCount; t++) {
					size += entry.lengths[t];
					fileCount++;
				}
			} else {
				entry.currentEditor = null;
				for (int t = 0; t < valueCount; t++) {
					deleteIfExists(entry.getCleanFile(t));
					deleteIfExists(entry.getDirtyFile(t));
				}
				i.remove();
			}
		}
	}

	/**
	 * Creates a new journal that omits redundant information. This replaces the
	 * current journal if it exists.
	 */
	private synchronized void rebuildJournal() throws java.io.IOException {
		if (journalWriter != null) {
			journalWriter.close();
		}

		java.io.Writer writer = new java.io.BufferedWriter(
				new java.io.OutputStreamWriter(new java.io.FileOutputStream(journalFileTmp), com.nostra13.universalimageloader.cache.disc.impl.ext.Util.US_ASCII));
		try {
			writer.write(MAGIC);
			writer.write("\n");
			writer.write(VERSION_1);
			writer.write("\n");
			writer.write(Integer.toString(appVersion));
			writer.write("\n");
			writer.write(Integer.toString(valueCount));
			writer.write("\n");
			writer.write("\n");

			for (com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry : lruEntries.values()) {
				if (entry.currentEditor != null) {
					writer.write(DIRTY + ' ' + entry.key + '\n');
				} else {
					writer.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
				}
			}
		} finally {
			writer.close();
		}

		if (journalFile.exists()) {
			renameTo(journalFile, journalFileBackup, true);
		}
		renameTo(journalFileTmp, journalFile, false);
		journalFileBackup.delete();

		journalWriter = new java.io.BufferedWriter(
				new java.io.OutputStreamWriter(new java.io.FileOutputStream(journalFile, true), com.nostra13.universalimageloader.cache.disc.impl.ext.Util.US_ASCII));
	}

	private static void deleteIfExists(java.io.File file) throws java.io.IOException {
		if (file.exists() && !file.delete()) {
			throw new java.io.IOException();
		}
	}

	private static void renameTo(java.io.File from, java.io.File to, boolean deleteDestination) throws java.io.IOException {
		if (deleteDestination) {
			deleteIfExists(to);
		}
		if (!from.renameTo(to)) {
			throw new java.io.IOException();
		}
	}

	/**
	 * Returns a snapshot of the entry named {@code key}, or null if it doesn't
	 * exist is not currently readable. If a value is returned, it is moved to
	 * the head of the LRU queue.
	 */
	public synchronized com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Snapshot get(String key) throws java.io.IOException {
		checkNotClosed();
		validateKey(key);
		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = lruEntries.get(key);
		if (entry == null) {
			return null;
		}

		if (!entry.readable) {
			return null;
		}

		// Open all streams eagerly to guarantee that we see a single published
		// snapshot. If we opened streams lazily then the streams could come
		// from different edits.
		java.io.File[] files = new java.io.File[valueCount];
		java.io.InputStream[] ins = new java.io.InputStream[valueCount];
		try {
			java.io.File file;
			for (int i = 0; i < valueCount; i++) {
				file = entry.getCleanFile(i);
				files[i] = file;
				ins[i] = new java.io.FileInputStream(file);
			}
		} catch (java.io.FileNotFoundException e) {
			// A file must have been deleted manually!
			for (int i = 0; i < valueCount; i++) {
				if (ins[i] != null) {
					com.nostra13.universalimageloader.cache.disc.impl.ext.Util.closeQuietly(ins[i]);
				} else {
					break;
				}
			}
			return null;
		}

		redundantOpCount++;
		journalWriter.append(READ + ' ' + key + '\n');
		if (journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}

		return new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Snapshot(key, entry.sequenceNumber, files, ins, entry.lengths);
	}

	/**
	 * Returns an editor for the entry named {@code key}, or null if another
	 * edit is in progress.
	 */
	public com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor edit(String key) throws java.io.IOException {
		return edit(key, ANY_SEQUENCE_NUMBER);
	}

	private synchronized com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor edit(String key, long expectedSequenceNumber) throws java.io.IOException {
		checkNotClosed();
		validateKey(key);
		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = lruEntries.get(key);
		if (expectedSequenceNumber != ANY_SEQUENCE_NUMBER && (entry == null
				|| entry.sequenceNumber != expectedSequenceNumber)) {
			return null; // Snapshot is stale.
		}
		if (entry == null) {
			entry = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry(key);
			lruEntries.put(key, entry);
		} else if (entry.currentEditor != null) {
			return null; // Another edit is in progress.
		}

		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor editor = new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor(entry);
		entry.currentEditor = editor;

		// Flush the journal before creating files to prevent file leaks.
		journalWriter.write(DIRTY + ' ' + key + '\n');
		journalWriter.flush();
		return editor;
	}

	/** Returns the directory where this cache stores its data. */
	public java.io.File getDirectory() {
		return directory;
	}

	/**
	 * Returns the maximum number of bytes that this cache should use to store
	 * its data.
	 */
	public synchronized long getMaxSize() {
		return maxSize;
	}

	/** Returns the maximum number of files that this cache should store */
	public synchronized int getMaxFileCount() {
		return maxFileCount;
	}

	/**
	 * Changes the maximum number of bytes the cache can store and queues a job
	 * to trim the existing store, if necessary.
	 */
	public synchronized void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
		executorService.submit(cleanupCallable);
	}

	/**
	 * Returns the number of bytes currently being used to store the values in
	 * this cache. This may be greater than the max size if a background
	 * deletion is pending.
	 */
	public synchronized long size() {
		return size;
	}

	/**
	 * Returns the number of files currently being used to store the values in
	 * this cache. This may be greater than the max file count if a background
	 * deletion is pending.
	 */
	public synchronized long fileCount() {
		return fileCount;
	}

	private synchronized void completeEdit(com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor editor, boolean success) throws java.io.IOException {
		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = editor.entry;
		if (entry.currentEditor != editor) {
			throw new IllegalStateException();
		}

		// If this edit is creating the entry for the first time, every index must have a value.
		if (success && !entry.readable) {
			for (int i = 0; i < valueCount; i++) {
				if (!editor.written[i]) {
					editor.abort();
					throw new IllegalStateException("Newly created entry didn't create value for index " + i);
				}
				if (!entry.getDirtyFile(i).exists()) {
					editor.abort();
					return;
				}
			}
		}

		for (int i = 0; i < valueCount; i++) {
			java.io.File dirty = entry.getDirtyFile(i);
			if (success) {
				if (dirty.exists()) {
					java.io.File clean = entry.getCleanFile(i);
					dirty.renameTo(clean);
					long oldLength = entry.lengths[i];
					long newLength = clean.length();
					entry.lengths[i] = newLength;
					size = size - oldLength + newLength;
					fileCount++;
				}
			} else {
				deleteIfExists(dirty);
			}
		}

		redundantOpCount++;
		entry.currentEditor = null;
		if (entry.readable | success) {
			entry.readable = true;
			journalWriter.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
			if (success) {
				entry.sequenceNumber = nextSequenceNumber++;
			}
		} else {
			lruEntries.remove(entry.key);
			journalWriter.write(REMOVE + ' ' + entry.key + '\n');
		}
		journalWriter.flush();

		if (size > maxSize || fileCount > maxFileCount || journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}
	}

	/**
	 * We only rebuild the journal when it will halve the size of the journal
	 * and eliminate at least 2000 ops.
	 */
	private boolean journalRebuildRequired() {
		final int redundantOpCompactThreshold = 2000;
		return redundantOpCount >= redundantOpCompactThreshold //
				&& redundantOpCount >= lruEntries.size();
	}

	/**
	 * Drops the entry for {@code key} if it exists and can be removed. Entries
	 * actively being edited cannot be removed.
	 *
	 * @return true if an entry was removed.
	 */
	public synchronized boolean remove(String key) throws java.io.IOException {
		checkNotClosed();
		validateKey(key);
		com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry = lruEntries.get(key);
		if (entry == null || entry.currentEditor != null) {
			return false;
		}

		for (int i = 0; i < valueCount; i++) {
			java.io.File file = entry.getCleanFile(i);
			if (file.exists() && !file.delete()) {
				throw new java.io.IOException("failed to delete " + file);
			}
			size -= entry.lengths[i];
			fileCount--;
			entry.lengths[i] = 0;
		}

		redundantOpCount++;
		journalWriter.append(REMOVE + ' ' + key + '\n');
		lruEntries.remove(key);

		if (journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}

		return true;
	}

	/** Returns true if this cache has been closed. */
	public synchronized boolean isClosed() {
		return journalWriter == null;
	}

	private void checkNotClosed() {
		if (journalWriter == null) {
			throw new IllegalStateException("cache is closed");
		}
	}

	/** Force buffered operations to the filesystem. */
	public synchronized void flush() throws java.io.IOException {
		checkNotClosed();
		trimToSize();
		trimToFileCount();
		journalWriter.flush();
	}

	/** Closes this cache. Stored values will remain on the filesystem. */
	public synchronized void close() throws java.io.IOException {
		if (journalWriter == null) {
			return; // Already closed.
		}
		for (com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry : new java.util.ArrayList<com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry>(lruEntries.values())) {
			if (entry.currentEditor != null) {
				entry.currentEditor.abort();
			}
		}
		trimToSize();
		trimToFileCount();
		journalWriter.close();
		journalWriter = null;
	}

	private void trimToSize() throws java.io.IOException {
		while (size > maxSize) {
			java.util.Map.Entry<String, com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry> toEvict = lruEntries.entrySet().iterator().next();
			remove(toEvict.getKey());
		}
	}

	private void trimToFileCount() throws java.io.IOException {
		while (fileCount > maxFileCount) {
			java.util.Map.Entry<String, com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry> toEvict = lruEntries.entrySet().iterator().next();
			remove(toEvict.getKey());
		}
	}

	/**
	 * Closes the cache and deletes all of its stored values. This will delete
	 * all files in the cache directory including files that weren't created by
	 * the cache.
	 */
	public void delete() throws java.io.IOException {
		close();
		com.nostra13.universalimageloader.cache.disc.impl.ext.Util.deleteContents(directory);
	}

	private void validateKey(String key) {
		java.util.regex.Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + key + "\"");
		}
	}

	private static String inputStreamToString(java.io.InputStream in) throws java.io.IOException {
		return com.nostra13.universalimageloader.cache.disc.impl.ext.Util.readFully(new java.io.InputStreamReader(in, com.nostra13.universalimageloader.cache.disc.impl.ext.Util.UTF_8));
	}

	/** A snapshot of the values for an entry. */
	public final class Snapshot implements java.io.Closeable {
		private final String key;
		private final long sequenceNumber;
		private java.io.File[] files;
		private final java.io.InputStream[] ins;
		private final long[] lengths;

		private Snapshot(String key, long sequenceNumber, java.io.File[] files, java.io.InputStream[] ins, long[] lengths) {
			this.key = key;
			this.sequenceNumber = sequenceNumber;
			this.files = files;
			this.ins = ins;
			this.lengths = lengths;
		}

		/**
		 * Returns an editor for this snapshot's entry, or null if either the
		 * entry has changed since this snapshot was created or if another edit
		 * is in progress.
		 */
		public com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor edit() throws java.io.IOException {
			return com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.this.edit(key, sequenceNumber);
		}

		/** Returns file with the value for {@code index}. */
		public java.io.File getFile(int index) {
			return files[index];
		}

		/** Returns the unbuffered stream with the value for {@code index}. */
		public java.io.InputStream getInputStream(int index) {
			return ins[index];
		}

		/** Returns the string value for {@code index}. */
		public String getString(int index) throws java.io.IOException {
			return inputStreamToString(getInputStream(index));
		}

		/** Returns the byte length of the value for {@code index}. */
		public long getLength(int index) {
			return lengths[index];
		}

		public void close() {
			for (java.io.InputStream in : ins) {
				com.nostra13.universalimageloader.cache.disc.impl.ext.Util.closeQuietly(in);
			}
		}
	}

	private static final java.io.OutputStream NULL_OUTPUT_STREAM = new java.io.OutputStream() {
		@Override
		public void write(int b) throws java.io.IOException {
			// Eat all writes silently. Nom nom.
		}
	};

	/** Edits the values for an entry. */
	public final class Editor {
		private final com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry;
		private final boolean[] written;
		private boolean hasErrors;
		private boolean committed;

		private Editor(com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Entry entry) {
			this.entry = entry;
			this.written = (entry.readable) ? null : new boolean[valueCount];
		}

		/**
		 * Returns an unbuffered input stream to read the last committed value,
		 * or null if no value has been committed.
		 */
		public java.io.InputStream newInputStream(int index) throws java.io.IOException {
			synchronized (com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.this) {
				if (entry.currentEditor != this) {
					throw new IllegalStateException();
				}
				if (!entry.readable) {
					return null;
				}
				try {
					return new java.io.FileInputStream(entry.getCleanFile(index));
				} catch (java.io.FileNotFoundException e) {
					return null;
				}
			}
		}

		/**
		 * Returns the last committed value as a string, or null if no value
		 * has been committed.
		 */
		public String getString(int index) throws java.io.IOException {
			java.io.InputStream in = newInputStream(index);
			return in != null ? inputStreamToString(in) : null;
		}

		/**
		 * Returns a new unbuffered output stream to write the value at
		 * {@code index}. If the underlying output stream encounters errors
		 * when writing to the filesystem, this edit will be aborted when
		 * {@link #commit} is called. The returned output stream does not throw
		 * IOExceptions.
		 */
		public java.io.OutputStream newOutputStream(int index) throws java.io.IOException {
			synchronized (com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.this) {
				if (entry.currentEditor != this) {
					throw new IllegalStateException();
				}
				if (!entry.readable) {
					written[index] = true;
				}
				java.io.File dirtyFile = entry.getDirtyFile(index);
				java.io.FileOutputStream outputStream;
				try {
					outputStream = new java.io.FileOutputStream(dirtyFile);
				} catch (java.io.FileNotFoundException e) {
					// Attempt to recreate the cache directory.
					directory.mkdirs();
					try {
						outputStream = new java.io.FileOutputStream(dirtyFile);
					} catch (java.io.FileNotFoundException e2) {
						// We are unable to recover. Silently eat the writes.
						return NULL_OUTPUT_STREAM;
					}
				}
				return new com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor.FaultHidingOutputStream(outputStream);
			}
		}

		/** Sets the value at {@code index} to {@code value}. */
		public void set(int index, String value) throws java.io.IOException {
			java.io.Writer writer = null;
			try {
				writer = new java.io.OutputStreamWriter(newOutputStream(index), com.nostra13.universalimageloader.cache.disc.impl.ext.Util.UTF_8);
				writer.write(value);
			} finally {
				com.nostra13.universalimageloader.cache.disc.impl.ext.Util.closeQuietly(writer);
			}
		}

		/**
		 * Commits this edit so it is visible to readers.  This releases the
		 * edit lock so another edit may be started on the same key.
		 */
		public void commit() throws java.io.IOException {
			if (hasErrors) {
				completeEdit(this, false);
				remove(entry.key); // The previous entry is stale.
			} else {
				completeEdit(this, true);
			}
			committed = true;
		}

		/**
		 * Aborts this edit. This releases the edit lock so another edit may be
		 * started on the same key.
		 */
		public void abort() throws java.io.IOException {
			completeEdit(this, false);
		}

		public void abortUnlessCommitted() {
			if (!committed) {
				try {
					abort();
				} catch (java.io.IOException ignored) {
				}
			}
		}

		private class FaultHidingOutputStream extends java.io.FilterOutputStream {
			private FaultHidingOutputStream(java.io.OutputStream out) {
				super(out);
			}

			@Override public void write(int oneByte) {
				try {
					out.write(oneByte);
				} catch (java.io.IOException e) {
					hasErrors = true;
				}
			}

			@Override public void write(byte[] buffer, int offset, int length) {
				try {
					out.write(buffer, offset, length);
				} catch (java.io.IOException e) {
					hasErrors = true;
				}
			}

			@Override public void close() {
				try {
					out.close();
				} catch (java.io.IOException e) {
					hasErrors = true;
				}
			}

			@Override public void flush() {
				try {
					out.flush();
				} catch (java.io.IOException e) {
					hasErrors = true;
				}
			}
		}
	}

	private final class Entry {
		private final String key;

		/** Lengths of this entry's files. */
		private final long[] lengths;

		/** True if this entry has ever been published. */
		private boolean readable;

		/** The ongoing edit or null if this entry is not being edited. */
		private com.nostra13.universalimageloader.cache.disc.impl.ext.DiskLruCache.Editor currentEditor;

		/** The sequence number of the most recently committed edit to this entry. */
		private long sequenceNumber;

		private Entry(String key) {
			this.key = key;
			this.lengths = new long[valueCount];
		}

		public String getLengths() throws java.io.IOException {
			StringBuilder result = new StringBuilder();
			for (long size : lengths) {
				result.append(' ').append(size);
			}
			return result.toString();
		}

		/** Set lengths using decimal numbers like "10123". */
		private void setLengths(String[] strings) throws java.io.IOException {
			if (strings.length != valueCount) {
				throw invalidLengths(strings);
			}

			try {
				for (int i = 0; i < strings.length; i++) {
					lengths[i] = Long.parseLong(strings[i]);
				}
			} catch (NumberFormatException e) {
				throw invalidLengths(strings);
			}
		}

		private java.io.IOException invalidLengths(String[] strings) throws java.io.IOException {
			throw new java.io.IOException("unexpected journal line: " + java.util.Arrays.toString(strings));
		}

		public java.io.File getCleanFile(int i) {
			return new java.io.File(directory, key + "." + i);
		}

		public java.io.File getDirtyFile(int i) {
			return new java.io.File(directory, key + "." + i + ".tmp");
		}
	}
}
