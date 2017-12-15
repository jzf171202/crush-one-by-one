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
package com.nostra13.universalimageloader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes images to {@link Bitmap}, scales them to needed size
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see ImageDecodingInfo
 * @since 1.8.3
 */
public class BaseImageDecoder implements com.nostra13.universalimageloader.core.decode.ImageDecoder {

	protected static final String LOG_SUBSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
	protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
	protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d\u00B0 [%2$s]";
	protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
	protected static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
	protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";

	protected final boolean loggingEnabled;

	/**
	 * @param loggingEnabled Whether debug logs will be written to LogCat. Usually should match {@link
	 *                       com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder#writeDebugLogs()
	 *                       ImageLoaderConfiguration.writeDebugLogs()}
	 */
	public BaseImageDecoder(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	/**
	 * Decodes image from URI into {@link Bitmap}. Image is scaled close to incoming {@linkplain ImageSize target size}
	 * during decoding (depend on incoming parameters).
	 *
	 * @param decodingInfo Needed data for decoding image
	 * @return Decoded bitmap
	 * @throws IOException                   if some I/O exception occurs during image reading
	 * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
	 */
	@Override
	public android.graphics.Bitmap decode(ImageDecodingInfo decodingInfo) throws java.io.IOException {
		android.graphics.Bitmap decodedBitmap;
		com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ImageFileInfo imageInfo;

		java.io.InputStream imageStream = getImageStream(decodingInfo);
		if (imageStream == null) {
			com.nostra13.universalimageloader.utils.L.e(ERROR_NO_IMAGE_STREAM, decodingInfo.getImageKey());
			return null;
		}
		try {
			imageInfo = defineImageSizeAndRotation(imageStream, decodingInfo);
			imageStream = resetStream(imageStream, decodingInfo);
			android.graphics.BitmapFactory.Options decodingOptions = prepareDecodingOptions(imageInfo.imageSize, decodingInfo);
			decodedBitmap = android.graphics.BitmapFactory.decodeStream(imageStream, null, decodingOptions);
		} finally {
			com.nostra13.universalimageloader.utils.IoUtils.closeSilently(imageStream);
		}

		if (decodedBitmap == null) {
			com.nostra13.universalimageloader.utils.L.e(ERROR_CANT_DECODE_IMAGE, decodingInfo.getImageKey());
		} else {
			decodedBitmap = considerExactScaleAndOrientatiton(decodedBitmap, decodingInfo, imageInfo.exif.rotation,
					imageInfo.exif.flipHorizontal);
		}
		return decodedBitmap;
	}

	protected java.io.InputStream getImageStream(ImageDecodingInfo decodingInfo) throws java.io.IOException {
		return decodingInfo.getDownloader().getStream(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
	}

	protected com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ImageFileInfo defineImageSizeAndRotation(java.io.InputStream imageStream, ImageDecodingInfo decodingInfo)
			throws java.io.IOException {
		android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		android.graphics.BitmapFactory.decodeStream(imageStream, null, options);

		com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo exif;
		String imageUri = decodingInfo.getImageUri();
		if (decodingInfo.shouldConsiderExifParams() && canDefineExifParams(imageUri, options.outMimeType)) {
			exif = defineExifOrientation(imageUri);
		} else {
			exif = new com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo();
		}
		return new com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ImageFileInfo(new com.nostra13.universalimageloader.core.assist.ImageSize(options.outWidth, options.outHeight, exif.rotation), exif);
	}

	private boolean canDefineExifParams(String imageUri, String mimeType) {
		return "image/jpeg".equalsIgnoreCase(mimeType) && (Scheme.ofUri(imageUri) == Scheme.FILE);
	}

	protected com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo defineExifOrientation(String imageUri) {
		int rotation = 0;
		boolean flip = false;
		try {
			android.media.ExifInterface exif = new android.media.ExifInterface(Scheme.FILE.crop(imageUri));
			int exifOrientation = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_NORMAL);
			switch (exifOrientation) {
				case android.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
					flip = true;
				case android.media.ExifInterface.ORIENTATION_NORMAL:
					rotation = 0;
					break;
				case android.media.ExifInterface.ORIENTATION_TRANSVERSE:
					flip = true;
				case android.media.ExifInterface.ORIENTATION_ROTATE_90:
					rotation = 90;
					break;
				case android.media.ExifInterface.ORIENTATION_FLIP_VERTICAL:
					flip = true;
				case android.media.ExifInterface.ORIENTATION_ROTATE_180:
					rotation = 180;
					break;
				case android.media.ExifInterface.ORIENTATION_TRANSPOSE:
					flip = true;
				case android.media.ExifInterface.ORIENTATION_ROTATE_270:
					rotation = 270;
					break;
			}
		} catch (java.io.IOException e) {
			com.nostra13.universalimageloader.utils.L.w("Can't read EXIF tags from file [%s]", imageUri);
		}
		return new com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo(rotation, flip);
	}

	protected android.graphics.BitmapFactory.Options prepareDecodingOptions(com.nostra13.universalimageloader.core.assist.ImageSize imageSize, ImageDecodingInfo decodingInfo) {
		com.nostra13.universalimageloader.core.assist.ImageScaleType scaleType = decodingInfo.getImageScaleType();
		int scale;
		if (scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.NONE) {
			scale = 1;
		} else if (scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.NONE_SAFE) {
			scale = com.nostra13.universalimageloader.utils.ImageSizeUtils.computeMinImageSampleSize(imageSize);
		} else {
			com.nostra13.universalimageloader.core.assist.ImageSize targetSize = decodingInfo.getTargetSize();
			boolean powerOf2 = scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.IN_SAMPLE_POWER_OF_2;
			scale = com.nostra13.universalimageloader.utils.ImageSizeUtils.computeImageSampleSize(imageSize, targetSize, decodingInfo.getViewScaleType(), powerOf2);
		}
		if (scale > 1 && loggingEnabled) {
			com.nostra13.universalimageloader.utils.L.d(LOG_SUBSAMPLE_IMAGE, imageSize, imageSize.scaleDown(scale), scale, decodingInfo.getImageKey());
		}

		android.graphics.BitmapFactory.Options decodingOptions = decodingInfo.getDecodingOptions();
		decodingOptions.inSampleSize = scale;
		return decodingOptions;
	}

	protected java.io.InputStream resetStream(java.io.InputStream imageStream, ImageDecodingInfo decodingInfo) throws java.io.IOException {
		if (imageStream.markSupported()) {
			try {
				imageStream.reset();
				return imageStream;
			} catch (java.io.IOException ignored) {
			}
		}
		com.nostra13.universalimageloader.utils.IoUtils.closeSilently(imageStream);
		return getImageStream(decodingInfo);
	}

	protected android.graphics.Bitmap considerExactScaleAndOrientatiton(android.graphics.Bitmap subsampledBitmap, ImageDecodingInfo decodingInfo,
                                                                        int rotation, boolean flipHorizontal) {
		android.graphics.Matrix m = new android.graphics.Matrix();
		// Scale to exact size if need
		com.nostra13.universalimageloader.core.assist.ImageScaleType scaleType = decodingInfo.getImageScaleType();
		if (scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY || scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY_STRETCHED) {
			com.nostra13.universalimageloader.core.assist.ImageSize srcSize = new com.nostra13.universalimageloader.core.assist.ImageSize(subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), rotation);
			float scale = com.nostra13.universalimageloader.utils.ImageSizeUtils.computeImageScale(srcSize, decodingInfo.getTargetSize(), decodingInfo
					.getViewScaleType(), scaleType == com.nostra13.universalimageloader.core.assist.ImageScaleType.EXACTLY_STRETCHED);
			if (Float.compare(scale, 1f) != 0) {
				m.setScale(scale, scale);

				if (loggingEnabled) {
					com.nostra13.universalimageloader.utils.L.d(LOG_SCALE_IMAGE, srcSize, srcSize.scale(scale), scale, decodingInfo.getImageKey());
				}
			}
		}
		// Flip bitmap if need
		if (flipHorizontal) {
			m.postScale(-1, 1);

			if (loggingEnabled) com.nostra13.universalimageloader.utils.L.d(LOG_FLIP_IMAGE, decodingInfo.getImageKey());
		}
		// Rotate bitmap if need
		if (rotation != 0) {
			m.postRotate(rotation);

			if (loggingEnabled) com.nostra13.universalimageloader.utils.L.d(LOG_ROTATE_IMAGE, rotation, decodingInfo.getImageKey());
		}

		android.graphics.Bitmap finalBitmap = android.graphics.Bitmap.createBitmap(subsampledBitmap, 0, 0, subsampledBitmap.getWidth(), subsampledBitmap
				.getHeight(), m, true);
		if (finalBitmap != subsampledBitmap) {
			subsampledBitmap.recycle();
		}
		return finalBitmap;
	}

	protected static class ExifInfo {

		public final int rotation;
		public final boolean flipHorizontal;

		protected ExifInfo() {
			this.rotation = 0;
			this.flipHorizontal = false;
		}

		protected ExifInfo(int rotation, boolean flipHorizontal) {
			this.rotation = rotation;
			this.flipHorizontal = flipHorizontal;
		}
	}

	protected static class ImageFileInfo {

		public final com.nostra13.universalimageloader.core.assist.ImageSize imageSize;
		public final com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo exif;

		protected ImageFileInfo(com.nostra13.universalimageloader.core.assist.ImageSize imageSize, com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo exif) {
			this.imageSize = imageSize;
			this.exif = exif;
		}
	}
}
