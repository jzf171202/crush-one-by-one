/*******************************************************************************
 * Copyright 2013-2014 Sergey Tarasevich
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
package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;

/**
 * Wrapper for Android {@link ImageView ImageView}. Keeps weak reference of ImageView to prevent memory
 * leaks.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.0
 */
public class ImageViewAware extends com.nostra13.universalimageloader.core.imageaware.ViewAware {

	/**
	 * Constructor. <br />
	 * References {@link #ImageViewAware(ImageView, boolean) ImageViewAware(imageView, true)}.
	 *
	 * @param imageView {@link ImageView ImageView} to work with
	 */
	public ImageViewAware(android.widget.ImageView imageView) {
		super(imageView);
	}

	/**
	 * Constructor
	 *
	 * @param imageView           {@link ImageView ImageView} to work with
	 * @param checkActualViewSize <b>true</b> - then {@link #getWidth()} and {@link #getHeight()} will check actual
	 *                            size of ImageView. It can cause known issues like
	 *                            <a href="https://github.com/nostra13/Android-Universal-Image-Loader/issues/376">this</a>.
	 *                            But it helps to save memory because memory cache keeps bitmaps of actual (less in
	 *                            general) size.
	 *                            <p/>
	 *                            <b>false</b> - then {@link #getWidth()} and {@link #getHeight()} will <b>NOT</b>
	 *                            consider actual size of ImageView, just layout parameters. <br /> If you set 'false'
	 *                            it's recommended 'android:layout_width' and 'android:layout_height' (or
	 *                            'android:maxWidth' and 'android:maxHeight') are set with concrete values. It helps to
	 *                            save memory.
	 *                            <p/>
	 */
	public ImageViewAware(android.widget.ImageView imageView, boolean checkActualViewSize) {
		super(imageView, checkActualViewSize);
	}

	/**
	 * {@inheritDoc}
	 * <br />
	 * 3) Get <b>maxWidth</b>.
	 */
	@Override
	public int getWidth() {
		int width = super.getWidth();
		if (width <= 0) {
			android.widget.ImageView imageView = (android.widget.ImageView) viewRef.get();
			if (imageView != null) {
				width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check maxWidth parameter
			}
		}
		return width;
	}

	/**
	 * {@inheritDoc}
	 * <br />
	 * 3) Get <b>maxHeight</b>
	 */
	@Override
	public int getHeight() {
		int height = super.getHeight();
		if (height <= 0) {
			android.widget.ImageView imageView = (android.widget.ImageView) viewRef.get();
			if (imageView != null) {
				height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check maxHeight parameter
			}
		}
		return height;
	}

	@Override
	public com.nostra13.universalimageloader.core.assist.ViewScaleType getScaleType() {
		android.widget.ImageView imageView = (android.widget.ImageView) viewRef.get();
		if (imageView != null) {
			return com.nostra13.universalimageloader.core.assist.ViewScaleType.fromImageView(imageView);
		}
		return super.getScaleType();
	}

	@Override
	public android.widget.ImageView getWrappedView() {
		return (android.widget.ImageView) super.getWrappedView();
	}

	@Override
	protected void setImageDrawableInto(android.graphics.drawable.Drawable drawable, android.view.View view) {
		((android.widget.ImageView) view).setImageDrawable(drawable);
		if (drawable instanceof android.graphics.drawable.AnimationDrawable) {
			((android.graphics.drawable.AnimationDrawable)drawable).start();
		}
	}

	@Override
	protected void setImageBitmapInto(android.graphics.Bitmap bitmap, android.view.View view) {
		((android.widget.ImageView) view).setImageBitmap(bitmap);
	}

	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			java.lang.reflect.Field field = android.widget.ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			com.nostra13.universalimageloader.utils.L.e(e);
		}
		return value;
	}
}
