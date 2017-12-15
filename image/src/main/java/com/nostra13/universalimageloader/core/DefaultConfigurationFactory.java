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
package com.nostra13.universalimageloader.core;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory for providing of default options for {@linkplain ImageLoaderConfiguration configuration}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class DefaultConfigurationFactory {

	/** Creates default implementation of task executor */
	public static java.util.concurrent.Executor createExecutor(int threadPoolSize, int threadPriority,
                                                               com.nostra13.universalimageloader.core.assist.QueueProcessingType tasksProcessingType) {
		boolean lifo = tasksProcessingType == com.nostra13.universalimageloader.core.assist.QueueProcessingType.LIFO;
		java.util.concurrent.BlockingQueue<Runnable> taskQueue =
				lifo ? new com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque<Runnable>() : new java.util.concurrent.LinkedBlockingQueue<Runnable>();
		return new java.util.concurrent.ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, java.util.concurrent.TimeUnit.MILLISECONDS, taskQueue,
				createThreadFactory(threadPriority, "uil-pool-"));
	}

	/** Creates default implementation of task distributor */
	public static java.util.concurrent.Executor createTaskDistributor() {
		return java.util.concurrent.Executors.newCachedThreadPool(createThreadFactory(Thread.NORM_PRIORITY, "uil-pool-d-"));
	}

	/** Creates {@linkplain HashCodeFileNameGenerator default implementation} of FileNameGenerator */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/**
	 * Creates default implementation of {@link DiskCache} depends on incoming parameters
	 */
	public static DiskCache createDiskCache(android.content.Context context, FileNameGenerator diskCacheFileNameGenerator,
                                            long diskCacheSize, int diskCacheFileCount) {
		java.io.File reserveCacheDir = createReserveDiskCacheDir(context);
		if (diskCacheSize > 0 || diskCacheFileCount > 0) {
			java.io.File individualCacheDir = com.nostra13.universalimageloader.utils.StorageUtils.getIndividualCacheDirectory(context);
			try {
				return new LruDiskCache(individualCacheDir, reserveCacheDir, diskCacheFileNameGenerator, diskCacheSize,
						diskCacheFileCount);
			} catch (java.io.IOException e) {
				com.nostra13.universalimageloader.utils.L.e(e);
				// continue and create unlimited cache
			}
		}
		java.io.File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory(context);
		return new UnlimitedDiskCache(cacheDir, reserveCacheDir, diskCacheFileNameGenerator);
	}

	/** Creates reserve disk cache folder which will be used if primary disk cache folder becomes unavailable */
	private static java.io.File createReserveDiskCacheDir(android.content.Context context) {
		java.io.File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory(context, false);
		java.io.File individualDir = new java.io.File(cacheDir, "uil-images");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return cacheDir;
	}

	/**
	 * Creates default implementation of {@link MemoryCache} - {@link LruMemoryCache}<br />
	 * Default cache size = 1/8 of available app memory.
	 */
	public static MemoryCache createMemoryCache(android.content.Context context, int memoryCacheSize) {
		if (memoryCacheSize == 0) {
			android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
			int memoryClass = am.getMemoryClass();
			if (hasHoneycomb() && isLargeHeap(context)) {
				memoryClass = getLargeMemoryClass(am);
			}
			memoryCacheSize = 1024 * 1024 * memoryClass / 8;
		}
		return new LruMemoryCache(memoryCacheSize);
	}

	private static boolean hasHoneycomb() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}

	@android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
	private static boolean isLargeHeap(android.content.Context context) {
		return (context.getApplicationInfo().flags & android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP) != 0;
	}

	@android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
	private static int getLargeMemoryClass(android.app.ActivityManager am) {
		return am.getLargeMemoryClass();
	}

	/** Creates default implementation of {@link ImageDownloader} - {@link BaseImageDownloader} */
	public static com.nostra13.universalimageloader.core.download.ImageDownloader createImageDownloader(android.content.Context context) {
		return new com.nostra13.universalimageloader.core.download.BaseImageDownloader(context);
	}

	/** Creates default implementation of {@link ImageDecoder} - {@link BaseImageDecoder} */
	public static com.nostra13.universalimageloader.core.decode.ImageDecoder createImageDecoder(boolean loggingEnabled) {
		return new com.nostra13.universalimageloader.core.decode.BaseImageDecoder(loggingEnabled);
	}

	/** Creates default implementation of {@link BitmapDisplayer} - {@link SimpleBitmapDisplayer} */
	public static com.nostra13.universalimageloader.core.display.BitmapDisplayer createBitmapDisplayer() {
		return new com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer();
	}

	/** Creates default implementation of {@linkplain ThreadFactory thread factory} for task executor */
	private static java.util.concurrent.ThreadFactory createThreadFactory(int threadPriority, String threadNamePrefix) {
		return new com.nostra13.universalimageloader.core.DefaultConfigurationFactory.DefaultThreadFactory(threadPriority, threadNamePrefix);
	}

	private static class DefaultThreadFactory implements java.util.concurrent.ThreadFactory {

		private static final java.util.concurrent.atomic.AtomicInteger poolNumber = new java.util.concurrent.atomic.AtomicInteger(1);

		private final ThreadGroup group;
		private final java.util.concurrent.atomic.AtomicInteger threadNumber = new java.util.concurrent.atomic.AtomicInteger(1);
		private final String namePrefix;
		private final int threadPriority;

		DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
			this.threadPriority = threadPriority;
			group = Thread.currentThread().getThreadGroup();
			namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon()) t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}
	}
}
