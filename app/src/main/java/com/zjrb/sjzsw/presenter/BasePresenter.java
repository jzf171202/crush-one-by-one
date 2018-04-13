package com.zjrb.sjzsw.presenter;

import android.content.Context;
import android.content.Intent;

import com.jzf.net.biz.IVBase;
import com.jzf.net.observer.BaseObserver;
import com.jzf.net.observer.ObserverManager;
import com.zjrb.sjzsw.biz.ILifeCycle;


/**
 * Created by jinzifu on 2017/9/1.
 * 业务逻辑类基类
 * 这种类只做业务逻辑处理，只要不传view进来就可以完成的事情都可以在这里做
 * 这是我没用mvp的原因：https://juejin.im/post/58b25e588d6d810057ed3659
 */

public class BasePresenter<V extends IVBase> implements ILifeCycle {
    public Context mContext;
    public V v;

    private ObserverManager mObserverManager = new ObserverManager();

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public BasePresenter() {
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
     * 反注册observer控制器
     *
     * @param baseObserver
     */
    public void removeObserver(BaseObserver baseObserver) {
        if (null != baseObserver) {
            String key = baseObserver.getClass().getSimpleName();
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
     * 绑定V层的引用
     *
     * @param v
     */
    public void attachView(V v) {
        this.v = v;
    }

    /**
     * 断开V层的引用
     */
    public void detachView() {
        this.v = null;
    }

    /**
     * 获取V层的引用
     *
     * @return
     */
    public V getView() {
        return this.v;
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
