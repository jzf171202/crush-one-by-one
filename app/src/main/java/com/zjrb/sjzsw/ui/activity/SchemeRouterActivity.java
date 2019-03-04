package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class SchemeRouterActivity extends AppCompatActivity {
    protected int REQUEST_CODE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSchemeIntent();
    }

    /**
     * 解析Scheme协议的uri数据
     * "app://book:8888/bookDetail?bookId=10011002"
     */
    protected void getSchemeIntent() {
        Uri uri = getIntent().getData();
        if (uri == null) return;
        uri.toString();
        String scheme = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();


        List<String> pathSegments = uri.getPathSegments();
        String query = uri.getQuery();
        String bookId = uri.getQueryParameter("bookId");
        Log.d("test", bookId);
    }

    /**
     * 解析uri，并处理点击事件或跳转页面
     *
     * @param uri
     * @return true 表示用户自己处理，false表示webview默认加载
     */
    protected boolean JsonUrI(String uri) {
        Intent intent = isIntentUri(uri);
        if (intent != null) {
            jumpActivity(intent, REQUEST_CODE);//REQUEST_CODE要及时重置
        } else {
            // TODO: 2019/2/25 根据URI处理点击事件，返回布尔值
            //注意：卡在这里了，发现还没有addJavascriptInterface映射对象的方案易扩展。说明shouldOverrideUrlLoading不太适合H5调原生
        }
        return true;
    }

    /**
     * 检查是否是页面跳转URI
     *
     * @param uri
     * @return
     */
    protected Intent isIntentUri(String uri) {
        if (TextUtils.isEmpty(uri)) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
        if (activities != null && !activities.isEmpty()) {
            return intent;
        }
        return null;
    }

    /**
     * 页面跳转视图
     *
     * @param intent
     * @param requestCode
     */
    protected void jumpActivity(Intent intent, int requestCode) {
        if (intent == null) return;
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }
}
