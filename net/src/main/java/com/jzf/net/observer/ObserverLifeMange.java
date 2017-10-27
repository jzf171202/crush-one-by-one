package com.jzf.net.observer;

import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：网络请求订阅管理器
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/10/27
 */

public class ObserverLifeMange {
    private Map<String, BaseObserver> commonObserverMap;

    public ObserverLifeMange() {
        commonObserverMap = new HashMap<String, BaseObserver>();
    }

    /**
     * 将Observer注册到订阅管理器
     *
     * @param key
     * @param commonObserver
     */
    public void register(String key, BaseObserver commonObserver) {
        commonObserverMap.put(key, commonObserver);
    }

    /**
     * 网络请求——单个取消订阅
     *
     * @param key
     */
    public void unregister(String key) {
        BaseObserver baseObserver = commonObserverMap.get(key);
        if (baseObserver != null) {
            baseObserver.unSubscribe();
        }
        commonObserverMap.remove(key);
    }

    /**
     * 获取某个Observer
     *
     * @param key
     * @return
     */
    public BaseObserver get(String key) {
        return commonObserverMap.get(key);
    }

    /**
     * 网络请求——集体取消订阅
     */
    public void onDestroy() {
        for (Map.Entry<String, BaseObserver> entry : commonObserverMap.entrySet()) {
            entry.getValue().unSubscribe();
        }
        commonObserverMap.clear();
    }
}
