package com.jzf.net.observer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;


/**
 * 类描述：Observer管理器
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/10/27
 */

public class ObserverManager {
    private Map<String, Observer> mObserverMap;

    public ObserverManager() {
        mObserverMap = new HashMap<String, Observer>();
    }

    /**
     * 注册Observer
     *
     * @param key
     * @param commonObserver
     */
    public void register(String key, Observer commonObserver) {
        mObserverMap.put(key, commonObserver);
    }

    /**
     * 反注册Observer
     *
     * @param key
     * @return true 反注册成功
     */
    public boolean unRegister(String key) {
        Observer observer = mObserverMap.get(key);
        if (observer != null) {
//            observer.unSubscribe();
            mObserverMap.remove(key);
            return true;
        }
        return false;
    }

    /**
     * 获取单个Observer
     *
     * @param key
     * @return
     */
    public Observer get(String key) {
        return mObserverMap.get(key);
    }

    /**
     * 反注册所有Observer
     */
    public void onDestroy() {
        for (Map.Entry<String, Observer> entry : mObserverMap.entrySet()) {
//            entry.getValue().unSubscribe();
        }
        mObserverMap.clear();
    }
}
