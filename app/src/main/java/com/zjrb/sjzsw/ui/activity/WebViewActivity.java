package com.zjrb.sjzsw.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcWebviewBinding;
import com.zjrb.sjzsw.jsBridge.AppBridgeManager;

import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends BaseActivity<AcWebviewBinding> {
    private String url = "https://www.jianshu.com";

    @Override
    protected int getLayoutId() {
        return R.layout.ac_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initWebSetting();
        t.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStarted=" + url);
            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    //判定重定向
//                    WebView.HitTestResult testResult = view.getHitTestResult();
//                    if (testResult == null ||
//                            testResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
//                        return false;
//                    }
//                    //解析URL Scheme ,处理点击或跳转事件
//                    Uri uri = request.getUrl();
//                    return uri != null ? JsonUrI(uri.toString()) : false;
//                }
//                return false;
//            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                //非UI线程，若return null 则自行请求网络加载资源
                WebResourceResponse webResourceResponse = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    String url = request.getUrl().toString();
                    if (url.contains("baidu")) {
                        try {
                            InputStream inputStream = getAssets().open("lauch.png");
                            webResourceResponse =
                                    new WebResourceResponse("image/png", "UTF-8", inputStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                return super.shouldInterceptRequest(view, request);
                return webResourceResponse;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished=" + url);
            }
        });

        t.webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                t.topBar.titleText.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                t.progressbar.setProgress(newProgress);
                if (newProgress == 100) {
                    t.progressbar.setVisibility(View.GONE);
                } else {
                    if (t.progressbar.getVisibility() == View.GONE) {
                        t.progressbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        t.webview.loadUrl(this.url);

        //H5调用Android
        t.webview.addJavascriptInterface(new AppBridgeManager(t.webview), "app_bridge");
    }

    /**
     * 设置WebSettings
     */
    private WebSettings initWebSetting() {
        WebSettings settings = t.webview.getSettings();
        //设置是否允许执行javaScript脚本，默认false
        settings.setJavaScriptEnabled(true);
        //设置是否支持缩放，默认true
        settings.setSupportZoom(true);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //把图片加载放在最后来加载渲染
        settings.setBlockNetworkImage(false);
        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
        t.webview.requestFocusFromTouch();
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //自适应屏幕
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 19) {
            //是否下载图片资源，默认true
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        //开启硬件加速，并优化出现的闪屏现象
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            t.webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        return settings;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.webview.stopLoading();
        t.webview.removeAllViews();
        t.webview.destroy();
    }
}
