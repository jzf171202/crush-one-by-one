/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.cache.disc.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Base disk cache.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see FileNameGenerator
 * @since 1.0.0
 */
public abstract class BaseDiskCache implements DiskCache {
	/** {@value */
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value */
	public static final android.graphics.Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = android.graphics.Bitmap.CompressFormat.PNG;
	/** {@value */
	public static final int DEFAULT_COMPRESS_QUALITY = 100;

	private static final String ERROR_ARG_NULL = " argument must be not null";
	private static final String TEMP_IMAGE_POSTFIX = ".tmp";

	protected final java.io.File cacheDir;
	protected final java.io.File reserveCacheDir;

	protected final FileNameGenerator fileNameGenerator;

	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	protected android.graphics.Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
	protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

	/** @param cacheDir Directory for file caching */
	public BaseDiskCache(java.io.File cacheDir) {
		this(cacheDir, null);
	}

	/**
	 * @param cacheDir        Directory for file caching
	 * @param reserveCacheDir null-ok; Reserve directory for file caching. It's used when the primary directory isn't available.
	 */
	public BaseDiskCache(java.io.File cacheDir, java.io.File reserveCacheDir) {
		this(cacheDir, reserveCacheDir, com.nostra13.universalimageloader.core.DefaultConfigurationFactory.createFileNameGenerator());
	}

	/**
	 * @param cacheDir          Directory for file caching
	 * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when the primary directory isn't available.
	 * @param fileNameGenerator {@linkplain com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator
	 *                          Name generator} for cached files
	 */
	public BaseDiskCache(java.io.File cacheDir, java.io.File reserveCacheDir, FileNameGenerator fileNameGenerator) {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException("fileNameGenerator" + ERROR_ARG_NULL);
		}

		this.cacheDir = cacheDir;
		this.reserveCacheDir = reserveCacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}

	@Override
	public java.io.File getDirectory() {
		return cacheDir;
	}

	@Override
	public java.io.File get(String imageUri) {
		return getFile(imageUri);
	}

	@Override
	public boolean save(String imageUri, java.io.InputStream imageStream, com.nostra13.universalimageloader.utils.IoUtils.CopyListener listener) throws java.io.IOException {
		java.io.File imageFile = getFile(imageUri);
		java.io.File tmpFile = new java.io.File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
		boolean loaded = false;
		try {
			java.io.OutputStream os = new java.io.BufferedOutputStream(new java.io.FileOutputStream(tmpFile), bufferSize);
			try {
				loaded = com.nostra13.universalimageloader.utils.IoUtils.copyStream(imageStream, os, listener, bufferSize);
			} finally {
				com.nostra13.universalimageloader.utils.IoUtils.closeSilently(os);
			}
		} finally {
			if (loaded && !tmpFile.renameTo(imageFile)) {
				loaded = false;
			}
			if (!loaded) {
				tmpFile.delete();
			}
		}
		return loaded;
	}

	@Override
	public boolean save(String imageUri, android.graphics.Bitmap bitmap) throws java.io.IOException {
		java.io.File imageFile = getFile(imageUri);
		java.io.File tmpFile = new java.io.File(imageFile.getAbsolutePath() + TEMP_IMAGE_POSTFIX);
		java.io.OutputStream os = new java.io.BufferedOutputStream(new java.io.FileOutputStream(tmpFile), bufferSize);
		boolean savedSuccessfully = false;
		try {
			savedSuccessfully = bitmap.compress(compressFormat, compressQuality, os);
		} finally {
			com.nostra13.universalimageloader.utils.IoUtils.closeSilently(os);
			if (savedSuccessfully && !tmpFile.renameTo(imageFile)) {
				savedSuccessfully = false;
			}
			if (!savedSuccessfully) {
				tmpFile.delete();
			}
		}
		bitmap.recycle();
		return savedSuccessfully;
	}

	@Override
	public boolean remove(String imageUri) {
		return getFile(imageUri).delete();
	}

	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public void clear() {
		java.io.File[] files = cacheDir.listFiles();
		if (files != null) {
			for (java.io.File f : files) {
				f.delete();
			}
		}
	}

	/** Returns file object (not null) for incoming image URI. File object can reference to non-existing file. */
	protected java.io.File getFile(String imageUri) {
		String fileName = fileNameGenerator.generate(imageUri);
		java.io.File dir = cacheDir;
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			if (reserveCacheDir != null && (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
				dir = reserveCacheDir;
			}
		}
		return new java.io.File(dir, fileName);
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setCompressFormat(android.graphics.Bitmap.CompressFormat compressFormat) {
		this.compressFormat = compressFormat;
	}

	public void setCompressQuality(int compressQuality) {
		this.compressQuality = compressQuality;
	}
}