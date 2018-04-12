package com.zjrb.sjzsw.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/29 1120
 */

public class DownloadManager {

    /**
     * 大文件下载，存储到输出流OutputStream
     *
     * @param url
     * @param outputStream
     * @return
     */
    public static boolean downloadFileRunnable(final String url, OutputStream outputStream) {
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedInputStream = new BufferedInputStream(inputStream, 8 * 1024);
            bufferedOutputStream = new BufferedOutputStream(outputStream, 8 * 1024);
            int lengh;
            while ((lengh = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(lengh);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
