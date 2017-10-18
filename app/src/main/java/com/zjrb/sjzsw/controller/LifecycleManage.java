package com.zjrb.sjzsw.controller;

import com.zjrb.sjzsw.listener.LifeCycle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinzifu on 2017/9/1.
 * 生命周期管理器
 */

public class LifecycleManage implements LifeCycle {
    private Map<String, LifeCycle> lifeCycleMap;

    public LifecycleManage() {
        lifeCycleMap = new HashMap<String, LifeCycle>();
    }

    public void register(String key, LifeCycle lifeCycle) {
        lifeCycleMap.put(key, lifeCycle);
    }

    public void unregister(String key) {
        lifeCycleMap.remove(key);
    }


    @Override
    public void onStart() {
        for (Map.Entry<String, LifeCycle> entry : lifeCycleMap.entrySet()) {
            entry.getValue().onStart();
        }
    }

    @Override
    public void onResume() {
        for (Map.Entry<String, LifeCycle> entry : lifeCycleMap.entrySet()) {
            entry.getValue().onResume();
        }
    }

    @Override
    public void onPause() {
        for (Map.Entry<String, LifeCycle> entry : lifeCycleMap.entrySet()) {
            entry.getValue().onPause();
        }
    }

    @Override
    public void onStop() {
        for (Map.Entry<String, LifeCycle> entry : lifeCycleMap.entrySet()) {
            entry.getValue().onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (Map.Entry<String, LifeCycle> entry : lifeCycleMap.entrySet()) {
            entry.getValue().onDestroy();
        }
        lifeCycleMap.clear();//移除所有ctrl元素
    }

    public LifeCycle get(String key) {
        return lifeCycleMap.get(key);
    }
}
