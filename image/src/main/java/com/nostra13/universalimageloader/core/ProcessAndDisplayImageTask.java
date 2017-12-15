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

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.L;

/**
 * Presents process'n'display image task. Processes image {@linkplain Bitmap} and display it in {@link ImageView} using
 * {@link DisplayBitmapTask}.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
final class ProcessAndDisplayImageTask implements Runnable {

	private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";

	private final com.nostra13.universalimageloader.core.ImageLoaderEngine engine;
	private final android.graphics.Bitmap bitmap;
	private final com.nostra13.universalimageloader.core.ImageLoadingInfo imageLoadingInfo;
	private final android.os.Handler handler;

	public ProcessAndDisplayImageTask(com.nostra13.universalimageloader.core.ImageLoaderEngine engine, android.graphics.Bitmap bitmap, com.nostra13.universalimageloader.core.ImageLoadingInfo imageLoadingInfo,
                                      android.os.Handler handler) {
		this.engine = engine;
		this.bitmap = bitmap;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;
	}

	@Override
	public void run() {
		com.nostra13.universalimageloader.utils.L.d(LOG_POSTPROCESS_IMAGE, imageLoadingInfo.memoryCacheKey);

		com.nostra13.universalimageloader.core.process.BitmapProcessor processor = imageLoadingInfo.options.getPostProcessor();
		android.graphics.Bitmap processedBitmap = processor.process(bitmap);
		com.nostra13.universalimageloader.core.DisplayBitmapTask displayBitmapTask = new com.nostra13.universalimageloader.core.DisplayBitmapTask(processedBitmap, imageLoadingInfo, engine,
				com.nostra13.universalimageloader.core.assist.LoadedFrom.MEMORY_CACHE);
		com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.runTask(displayBitmapTask, imageLoadingInfo.options.isSyncLoading(), handler, engine);
	}
}
