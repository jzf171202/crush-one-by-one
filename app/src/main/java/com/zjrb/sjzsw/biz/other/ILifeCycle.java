package com.zjrb.sjzsw.biz.other;

import android.content.Intent;

/**
 * Created by jinzifu on 2017/9/1.
 */

public interface ILifeCycle {

    void onNewIntent(Intent intent);

    /**
     * 绑定Activity/fragment onResume 阶段
     */
    void onResume();

    /**
     * 绑定Activity/fragment onPause 阶段
     */
    void onPause();

    /**
     * 绑定Activity/fragment onStop 阶段
     */
    void onStop();

    /**
     * 绑定Activity/fragment onDestroy 阶段
     */
    void onDestroy();
}
