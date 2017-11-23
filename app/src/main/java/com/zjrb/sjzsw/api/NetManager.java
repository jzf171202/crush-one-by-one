package com.zjrb.sjzsw.api;

import com.zjrb.sjzsw.listener.NetListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * GET与POST请求。二者的区别在于：
 * a:) get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
 * b:) post与get的不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
 * Created by jinzifu on 2017/9/17.
 */

public class NetManager {
    /**
     * post 请求
     *
     * @param url
     * @param paragm
     */
    public static void post(final String url, final String paragm, final NetListener netListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url1 = new URL(url);
                    httpURLConnection = (HttpURLConnection) url1.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setDoOutput(true); // Post请求必须设置允许输出 默认false
                    httpURLConnection.setUseCaches(false);// Post请求不能使用缓存
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive"); //设置客户端与服务连接类型
                    httpURLConnection.setInstanceFollowRedirects(true);  //设置本次连接是否自动处理重定向
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");   //设置请求中的媒体类型信息。

                    //请求参数添加
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    outputStream.write(paragm.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    int responseCode = httpURLConnection.getResponseCode();
                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String response = getStringFromInputStream(inputStream);
                        if (null != netListener) {
                            netListener.onResponseListener(response);
                        }
                    } else {
                        if (null != netListener) {
                            netListener.onErrorListener(httpURLConnection.getResponseCode());
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * get 请求
     *
     * @param url
     * @return
     */
    public static void get(final String url, final NetListener netListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url1 = new URL(url);
                    httpURLConnection = (HttpURLConnection) url1.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setUseCaches(true);  // 设置是否使用缓存  默认是true
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive"); //设置客户端与服务连接类型
                    httpURLConnection.setInstanceFollowRedirects(true);  //设置本次连接是否自动处理重定向
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");   //设置请求中的媒体类型信息。

                    int responseCode = httpURLConnection.getResponseCode();
                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        //此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法。所以在开发中不调用上述的connect()也可以)。
                        // getInputStream()也是同理。
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String response = getStringFromInputStream(inputStream);
                        if (null != netListener) {
                            netListener.onResponseListener(response);
                        }
                    } else {
                        if (null != netListener) {
                            netListener.onErrorListener(httpURLConnection.getResponseCode());
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 输入流转string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        String response = byteArrayOutputStream.toString();
        inputStream.close();
        byteArrayOutputStream.close();
        return response;
    }
}
