package com.zjrb.sjzsw.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片加载工具类
 */
public class ImageUtil {

    /**
     * 采样率加载Bitmap
     *
     * @param resources
     * @param resourcesId 图片资源ID
     * @param requestWidth 需要适配的宽度
     * @param requestHeight  需要适配的高度
     */
    public static Bitmap decodeBitmapFromResource(Resources resources,
                                                  int resourcesId, int requestWidth, int requestHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //true 只会解析图片的原始宽、高，不会真正的加载图片。
        options.inJustDecodeBounds = true;
        //获取加载图片显示在ImageView上的采样率
        options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight);
        //false 可以获取图片的Bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resourcesId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int requestWidth, int requestHeight) {
        if (options == null) return 1;
        final int originWidth = options.outWidth;
        final int originHeight = options.outHeight;
        int inSampleSize = 1;
        boolean flag = (originWidth > requestWidth * inSampleSize)
                && (originHeight > requestHeight * inSampleSize);
        do {
            inSampleSize += 1;
        } while (flag);
        return inSampleSize;
    }
}
