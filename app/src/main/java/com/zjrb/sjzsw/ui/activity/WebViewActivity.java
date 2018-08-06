package com.zjrb.sjzsw.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcWebviewBinding;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 类描述：通用webview展示类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/7 16
 */

public class WebViewActivity extends BaseActivity<AcWebviewBinding> implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_webview;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initSetting();
        t.topBar.leftImage.setOnClickListener(this);
        t.topBar.titleText.setOnClickListener(this);
        t.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //url 为post时不执行
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("jianshu")){
                        t.webview.loadUrl("https://juejin.im/entry/5a9fb2eaf265da237506714d");
                    }
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        t.webview.loadUrl("https://www.jianshu.com/");
    }

    /**
     * 设置WebSettings
     */
    private void initSetting() {
        WebSettings settings = t.webview.getSettings();
        settings.setJavaScriptEnabled(true);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftImage:
                finish();
                break;
            case R.id.titleText:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && t.webview.canGoBack()) {
            t.webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
