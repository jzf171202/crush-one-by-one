package com.jzf.net.observer;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：Observer管理器
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/10/27
 */

public class ObserverManager {
    private Map<String, BaseObserver> mObserverMap;

    public ObserverManager() {
        mObserverMap = new HashMap<String, BaseObserver>();
    }

    /**
     * 注册Observer
     *
     * @param key
     * @param baseObserver
     */
    public void register(String key, BaseObserver baseObserver) {
        mObserverMap.put(key, baseObserver);
    }

    /**
     * 反注册Observer
     *
     * @param key
     * @return true 反注册成功
     */
    public boolean unRegister(String key) {
        BaseObserver baseObserver = mObserverMap.get(key);
        if (baseObserver != null) {
            baseObserver.unSubscribe();
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
    public BaseObserver get(String key) {
        return mObserverMap.get(key);
    }

    /**
     * 反注册所有Observer
     */
    public void onDestroy() {
        for (Map.Entry<String, BaseObserver> entry : mObserverMap.entrySet()) {
            entry.getValue().unSubscribe();
        }
        mObserverMap.clear();
    }
}
