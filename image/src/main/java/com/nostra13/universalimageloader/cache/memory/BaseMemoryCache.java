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
package com.nostra13.universalimageloader.cache.memory;

import android.graphics.Bitmap;

import java.lang.ref.Reference;
import java.util.*;

/**
 * Base memory cache. Implements common functionality for memory cache. Provides object references (
 * {@linkplain Reference not strong}) storing.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public abstract class BaseMemoryCache implements com.nostra13.universalimageloader.cache.memory.MemoryCache {

	/** Stores not strong references to objects */
	private final java.util.Map<String, java.lang.ref.Reference<android.graphics.Bitmap>> softMap = java.util.Collections.synchronizedMap(new java.util.HashMap<String, java.lang.ref.Reference<android.graphics.Bitmap>>());

	@Override
	public android.graphics.Bitmap get(String key) {
		android.graphics.Bitmap result = null;
		java.lang.ref.Reference<android.graphics.Bitmap> reference = softMap.get(key);
		if (reference != null) {
			result = reference.get();
		}
		return result;
	}

	@Override
	public boolean put(String key, android.graphics.Bitmap value) {
		softMap.put(key, createReference(value));
		return true;
	}

	@Override
	public android.graphics.Bitmap remove(String key) {
		java.lang.ref.Reference<android.graphics.Bitmap> bmpRef = softMap.remove(key);
		return bmpRef == null ? null : bmpRef.get();
	}

	@Override
	public java.util.Collection<String> keys() {
		synchronized (softMap) {
			return new java.util.HashSet<String>(softMap.keySet());
		}
	}

	@Override
	public void clear() {
		softMap.clear();
	}

	/** Creates {@linkplain Reference not strong} reference of value */
	protected abstract java.lang.ref.Reference<android.graphics.Bitmap> createReference(android.graphics.Bitmap value);
}
