package com.zjrb.sjzsw.presenter;

import android.content.Context;
import android.util.Log;

import com.jzf.net.observer.BaseObserver;
import com.jzf.net.observer.ObserverManager;
import com.zjrb.sjzsw.biz.ILifeCycle;
import com.zjrb.sjzsw.biz.presenterBiz.IPBase;


/**
 * Created by jinzifu on 2017/9/1.
 * 业务逻辑类基类
 * 这种类只做业务逻辑处理，只要不传view进来就可以完成的事情都可以在这里做
 * 这是我没用mvp的原因：https://juejin.im/post/58b25e588d6d810057ed3659
 */

public class BasePresenter implements ILifeCycle, IPBase {
    private final String TAG = getClass().getSimpleName();
    public Context mContext;
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
            Log.d(TAG, "registerObserver = " + key);
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
            Log.d(TAG, "removeObserver = " + key);
            mObserverManager.unRegister(key);
        }
    }

    @Override
    public void onStart() {

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
        mObserverManager.onDestroy();
    }
}
