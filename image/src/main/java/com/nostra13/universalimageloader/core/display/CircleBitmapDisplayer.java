/*******************************************************************************
 * Copyright 2015 Sergey Tarasevich
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
package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Can display bitmap cropped by a circle. This implementation works only with ImageViews wrapped
 * in ImageViewAware.
 * <br />
 * If this implementation doesn't meet your needs then consider
 * <a href="https://github.com/vinc3m1/RoundedImageView">RoundedImageView</a> or
 * <a href="https://github.com/Pkmmte/CircularImageView">CircularImageView</a> projects for usage.
 *
 * @author Qualtagh, Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.5
 */
public class CircleBitmapDisplayer implements com.nostra13.universalimageloader.core.display.BitmapDisplayer {

	protected final Integer strokeColor;
	protected final float strokeWidth;

	public CircleBitmapDisplayer() {
		this(null);
	}

	public CircleBitmapDisplayer(Integer strokeColor) {
		this(strokeColor, 0);
	}

	public CircleBitmapDisplayer(Integer strokeColor, float strokeWidth) {
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
	}

	@Override
	public void display(android.graphics.Bitmap bitmap, ImageAware imageAware, com.nostra13.universalimageloader.core.assist.LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer.CircleDrawable(bitmap, strokeColor, strokeWidth));
	}

	public static class CircleDrawable extends android.graphics.drawable.Drawable {

		protected float radius;

		protected final android.graphics.RectF mRect = new android.graphics.RectF();
		protected final android.graphics.RectF mBitmapRect;
		protected final android.graphics.BitmapShader bitmapShader;
		protected final android.graphics.Paint paint;
		protected final android.graphics.Paint strokePaint;
		protected final float strokeWidth;
		protected float strokeRadius;

		public CircleDrawable(android.graphics.Bitmap bitmap, Integer strokeColor, float strokeWidth) {
			radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;

			bitmapShader = new android.graphics.BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
			mBitmapRect = new android.graphics.RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

			paint = new android.graphics.Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
			paint.setFilterBitmap(true);
			paint.setDither(true);

			if (strokeColor == null) {
				strokePaint = null;
			} else {
				strokePaint = new android.graphics.Paint();
				strokePaint.setStyle(android.graphics.Paint.Style.STROKE);
				strokePaint.setColor(strokeColor);
				strokePaint.setStrokeWidth(strokeWidth);
				strokePaint.setAntiAlias(true);
			}
			this.strokeWidth = strokeWidth;
			strokeRadius = radius - strokeWidth / 2;
		}

		@Override
		protected void onBoundsChange(android.graphics.Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(0, 0, bounds.width(), bounds.height());
			radius = Math.min(bounds.width(), bounds.height()) / 2;
			strokeRadius = radius - strokeWidth / 2;

			// Resize the original bitmap to fit the new bound
			android.graphics.Matrix shaderMatrix = new android.graphics.Matrix();
			shaderMatrix.setRectToRect(mBitmapRect, mRect, android.graphics.Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(shaderMatrix);
		}

		@Override
		public void draw(android.graphics.Canvas canvas) {
			canvas.drawCircle(radius, radius, radius, paint);
			if (strokePaint != null) {
				canvas.drawCircle(radius, radius, strokeRadius, strokePaint);
			}
		}

		@Override
		public int getOpacity() {
			return android.graphics.PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			paint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(android.graphics.ColorFilter cf) {
			paint.setColorFilter(cf);
		}
	}
}
