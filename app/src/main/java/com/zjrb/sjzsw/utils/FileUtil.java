package com.zjrb.sjzsw.utils;

import android.os.Environment;

import com.zjrb.sjzsw.App;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/28 1731
 */

public class FileUtil {

    /**
     * 获取本地文件
     *
     * @param uniqueName
     *         子目录
     *
     * @return
     */
    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = App.getAppContext().getExternalCacheDir().getPath();
        } else {
            cachePath = App.getAppContext().getCacheDir().getPath();
        }
        File file = new File(cachePath + File.separator + uniqueName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * MD5 算法
     *
     * @param key
     *
     * @return
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
