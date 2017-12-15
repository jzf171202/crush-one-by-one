/*******************************************************************************
 * Copyright 2013 Sergey Tarasevich
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

import android.graphics.*;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Can display bitmap with rounded corners and vignette effect. This implementation works only with ImageViews wrapped
 * in ImageViewAware.
 * <br />
 * This implementation is inspired by
 * <a href="http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/">
 * Romain Guy's article</a>. It rounds images using custom drawable drawing. Original bitmap isn't changed.
 * <br />
 * <br />
 * If this implementation doesn't meet your needs then consider
 * <a href="https://github.com/vinc3m1/RoundedImageView">this project</a> for usage.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.1
 */
public class RoundedVignetteBitmapDisplayer extends com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer {

	public RoundedVignetteBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
		super(cornerRadiusPixels, marginPixels);
	}

	@Override
	public void display(android.graphics.Bitmap bitmap, ImageAware imageAware, com.nostra13.universalimageloader.core.assist.LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer.RoundedVignetteDrawable(bitmap, cornerRadius, margin));
	}

	protected static class RoundedVignetteDrawable extends com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer.RoundedDrawable {

		RoundedVignetteDrawable(android.graphics.Bitmap bitmap, int cornerRadius, int margin) {
			super(bitmap, cornerRadius, margin);
		}

		@Override
		protected void onBoundsChange(android.graphics.Rect bounds) {
			super.onBoundsChange(bounds);
			android.graphics.RadialGradient vignette = new android.graphics.RadialGradient(
					mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
					new int[]{0, 0, 0x7f000000}, new float[]{0.0f, 0.7f, 1.0f},
					android.graphics.Shader.TileMode.CLAMP);

			android.graphics.Matrix oval = new android.graphics.Matrix();
			oval.setScale(1.0f, 0.7f);
			vignette.setLocalMatrix(oval);

			paint.setShader(new android.graphics.ComposeShader(bitmapShader, vignette, android.graphics.PorterDuff.Mode.SRC_OVER));
		}
	}
}
