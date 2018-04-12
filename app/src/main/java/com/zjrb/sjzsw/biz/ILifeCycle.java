package com.zjrb.sjzsw.biz;

/**
 * Created by jinzifu on 2017/9/1.
 */

public interface ILifeCycle {
    /**
     * 绑定Activity/fragment生命周期 onStart 阶段
     */
    void onStart();

    /**
     * 绑定Activity/fragment生命周期 onResume 阶段
     */
    void onResume();

    /**
     * 绑定Activity/fragment生命周期 onPause 阶段
     */
    void onPause();

    /**
     * 绑定Activity/fragment生命周期 onStop 阶段
     */
    void onStop();

    /**
     * 绑定Activity/fragment生命周期 onDestroy 阶段
     */
    void onDestroy();
}
