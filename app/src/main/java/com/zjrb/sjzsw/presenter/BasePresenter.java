package com.zjrb.sjzsw.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zjrb.sjzsw.biz.viewBiz.IVBase;
import com.jzf.net.observer.BaseObserver;
import com.jzf.net.observer.ObserverManager;
import com.zjrb.sjzsw.biz.other.ILifeCycle;


/**
 * Created by jinzifu on 2017/9/1.
 * 业务逻辑类基类
 */

public class BasePresenter<V extends IVBase> implements ILifeCycle {
    public Context mContext;
    public V v;

    private ObserverManager mObserverManager = new ObserverManager();

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 注册observer控制器
     *
     * @param baseObserver
     */
    public BaseObserver registerObserver(BaseObserver baseObserver) {
        if (null != baseObserver) {
            String key = baseObserver.getClass().getSimpleName();
            mObserverManager.register(key, baseObserver);
        }
        return baseObserver;
    }

    /**
     * 自定义key的observer控制器
     * 优势：解决多个同Observer的（key是相同的）导致Observer覆盖的问题
     *
     * @param key
     * @param baseObserver
     * @return
     */
    public BaseObserver registerObserver(String key, BaseObserver baseObserver) {
        if (null != baseObserver) {
            if (TextUtils.isEmpty(key)) {
                key = baseObserver.getClass().getSimpleName();
            }
            mObserverManager.register(key, baseObserver);
        }
        return baseObserver;
    }


    /**
     * 反注册observer控制器
     *
     * @param key
     * @param baseObserver
     */
    public void removeObserver(String key, BaseObserver baseObserver) {
        if (null != baseObserver) {
            if (TextUtils.isEmpty(key)) {
                key = baseObserver.getClass().getSimpleName();
            }
            mObserverManager.unRegister(key);
        }
    }


    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        //避免内存泄露的关键代码：移除任务，断开引用
        mObserverManager.onDestroy();
        detachView();
        mContext = null;
    }

    /**
     * 绑定V层的强引用
     *
     * @param v
     */
    public void attachView(V v) {
        this.v = v;
    }

    /**
     * 断开V层的强引用
     */
    public void detachView() {
        this.v = null;
    }

    /**
     * 检测V层的引用是否为null,每次调用V层方法须调用此方法检测
     *
     * @return
     */
    public boolean isViewAttached() {
        return this.v != null;
    }
}
