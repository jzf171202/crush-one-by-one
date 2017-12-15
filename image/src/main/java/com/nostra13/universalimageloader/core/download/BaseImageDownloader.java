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
package com.nostra13.universalimageloader.core.download;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public class BaseImageDownloader implements com.nostra13.universalimageloader.core.download.ImageDownloader {
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	protected static final int MAX_REDIRECT_COUNT = 5;

	protected static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";

	private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. " + "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final android.content.Context context;
	protected final int connectTimeout;
	protected final int readTimeout;

	public BaseImageDownloader(android.content.Context context) {
		this(context, DEFAULT_HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_READ_TIMEOUT);
	}

	public BaseImageDownloader(android.content.Context context, int connectTimeout, int readTimeout) {
		this.context = context.getApplicationContext();
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	@Override
	public java.io.InputStream getStream(String imageUri, Object extra) throws java.io.IOException {
		switch (com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.ofUri(imageUri)) {
			case HTTP:
			case HTTPS:
				return getStreamFromNetwork(imageUri, extra);
			case FILE:
				return getStreamFromFile(imageUri, extra);
			case CONTENT:
				return getStreamFromContent(imageUri, extra);
			case ASSETS:
				return getStreamFromAssets(imageUri, extra);
			case DRAWABLE:
				return getStreamFromDrawable(imageUri, extra);
			case UNKNOWN:
			default:
				return getStreamFromOtherSource(imageUri, extra);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in the network).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected java.io.InputStream getStreamFromNetwork(String imageUri, Object extra) throws java.io.IOException {
		java.net.HttpURLConnection conn = createConnection(imageUri, extra);

		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"), extra);
			redirectCount++;
		}

		java.io.InputStream imageStream;
		try {
			imageStream = conn.getInputStream();
		} catch (java.io.IOException e) {
			// Read all data to allow reuse connection (http://bit.ly/1ad35PY)
			com.nostra13.universalimageloader.utils.IoUtils.readAndCloseStream(conn.getErrorStream());
			throw e;
		}
		if (!shouldBeProcessed(conn)) {
			com.nostra13.universalimageloader.utils.IoUtils.closeSilently(imageStream);
			throw new java.io.IOException("Image request failed with response code " + conn.getResponseCode());
		}

		return new com.nostra13.universalimageloader.core.assist.ContentLengthInputStream(new java.io.BufferedInputStream(imageStream, BUFFER_SIZE), conn.getContentLength());
	}

	/**
	 * @param conn Opened request connection (response code is available)
	 * @return <b>true</b> - if data from connection is correct and should be read and processed;
	 *         <b>false</b> - if response contains irrelevant data and shouldn't be processed
	 * @throws IOException
	 */
	protected boolean shouldBeProcessed(java.net.HttpURLConnection conn) throws java.io.IOException {
		return conn.getResponseCode() == 200;
	}

	/**
	 * Create {@linkplain HttpURLConnection HTTP connection} for incoming URL
	 *
	 * @param url   URL to connect to
	 * @param extra Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *              DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@linkplain HttpURLConnection Connection} for incoming URL. Connection isn't established so it still configurable.
	 * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
	 *                     URL.
	 */
	protected java.net.HttpURLConnection createConnection(String url, Object extra) throws java.io.IOException {
		String encodedUrl = android.net.Uri.encode(url, ALLOWED_URI_CHARS);
		java.net.HttpURLConnection conn = (java.net.HttpURLConnection) new java.net.URL(encodedUrl).openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located on the local file system or SD card).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs reading from file system
	 */
	protected java.io.InputStream getStreamFromFile(String imageUri, Object extra) throws java.io.IOException {
		String filePath = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.crop(imageUri);
		if (isVideoFileUri(imageUri)) {
			return getVideoThumbnailStream(filePath);
		} else {
			java.io.BufferedInputStream imageStream = new java.io.BufferedInputStream(new java.io.FileInputStream(filePath), BUFFER_SIZE);
			return new com.nostra13.universalimageloader.core.assist.ContentLengthInputStream(imageStream, (int) new java.io.File(filePath).length());
		}
	}

	@android.annotation.TargetApi(android.os.Build.VERSION_CODES.FROYO)
	private java.io.InputStream getVideoThumbnailStream(String filePath) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
			android.graphics.Bitmap bitmap = android.media.ThumbnailUtils
					.createVideoThumbnail(filePath, android.provider.MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
			if (bitmap != null) {
				java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
				bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 0, bos);
				return new java.io.ByteArrayInputStream(bos.toByteArray());
			}
		}
		return null;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is accessed using {@link ContentResolver}).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws FileNotFoundException if the provided URI could not be opened
	 */
	protected java.io.InputStream getStreamFromContent(String imageUri, Object extra) throws java.io.FileNotFoundException {
		android.content.ContentResolver res = context.getContentResolver();

		android.net.Uri uri = android.net.Uri.parse(imageUri);
		if (isVideoContentUri(uri)) { // video thumbnail
			Long origId = Long.valueOf(uri.getLastPathSegment());
			android.graphics.Bitmap bitmap = android.provider.MediaStore.Video.Thumbnails
					.getThumbnail(res, origId, android.provider.MediaStore.Images.Thumbnails.MINI_KIND, null);
			if (bitmap != null) {
				java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
				bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 0, bos);
				return new java.io.ByteArrayInputStream(bos.toByteArray());
			}
		} else if (imageUri.startsWith(CONTENT_CONTACTS_URI_PREFIX)) { // contacts photo
			return getContactPhotoStream(uri);
		}

		return res.openInputStream(uri);
	}

	@android.annotation.TargetApi(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected java.io.InputStream getContactPhotoStream(android.net.Uri uri) {
		android.content.ContentResolver res = context.getContentResolver();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return android.provider.ContactsContract.Contacts.openContactPhotoInputStream(res, uri, true);
		} else {
			return android.provider.ContactsContract.Contacts.openContactPhotoInputStream(res, uri);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in assets of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException if some I/O error occurs file reading
	 */
	protected java.io.InputStream getStreamFromAssets(String imageUri, Object extra) throws java.io.IOException {
		String filePath = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.ASSETS.crop(imageUri);
		return context.getAssets().open(filePath);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in drawable resources of application).
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 */
	protected java.io.InputStream getStreamFromDrawable(String imageUri, Object extra) {
		String drawableIdString = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.DRAWABLE.crop(imageUri);
		int drawableId = Integer.parseInt(drawableIdString);
		return context.getResources().openRawResource(drawableId);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI from other source with unsupported scheme. Should be overriden by
	 * successors to implement image downloading from special sources.<br />
	 * This method is called only if image URI has unsupported scheme. Throws {@link UnsupportedOperationException} by
	 * default.
	 *
	 * @param imageUri Image URI
	 * @param extra    Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *                 DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException                   if some I/O error occurs
	 * @throws UnsupportedOperationException if image URI has unsupported scheme(protocol)
	 */
	protected java.io.InputStream getStreamFromOtherSource(String imageUri, Object extra) throws java.io.IOException {
		throw new UnsupportedOperationException(String.format(ERROR_UNSUPPORTED_SCHEME, imageUri));
	}

	private boolean isVideoContentUri(android.net.Uri uri) {
		String mimeType = context.getContentResolver().getType(uri);
		return mimeType != null && mimeType.startsWith("video/");
	}

	private boolean isVideoFileUri(String uri) {
		String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(uri);
		String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		return mimeType != null && mimeType.startsWith("video/");
	}
}
