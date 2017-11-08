package com.zjrb.sjzsw.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.zjrb.sjzsw.ui.activity.WebViewActivity;

/**
 * Created by jinzifu on 2017/7/7.
 * 一个用于多数activity跳转的管理类
 */

public class ActivityUtil {

    public static void start(Context context, Intent intent) {
        if (context != null && intent != null) {
            context.startActivity(intent);
        }
    }

    public static void next(Context context, Class<? extends AppCompatActivity> actClas) {
        Intent intent = new Intent(context, actClas);
        start(context, intent);
    }

    public static void nextWeb(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        start(context, intent);
    }

}
