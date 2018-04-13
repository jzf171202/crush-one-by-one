package com.zjrb.sjzsw.presenter;

import android.content.Intent;

import com.zjrb.sjzsw.biz.other.ILifeCycle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinzifu on 2017/9/1.
 * PresenterManager管理器
 * 类描述：此模式并不能避免内存泄漏，只是提供了一种业务层和视图层解耦的思路，并支持绑定业务层与视图层的生命周期
 */

public class PresenterManager implements ILifeCycle {
    private Map<String, ILifeCycle> presenterMap;

    public PresenterManager() {
        presenterMap = new HashMap<String, ILifeCycle>();
    }

    public void register(String key, ILifeCycle ILifeCycle) {
        presenterMap.put(key, ILifeCycle);
    }

    public void unregister(String key) {
        presenterMap.remove(key);
    }


    @Override
    public void onNewIntent(Intent intent) {
        for (Map.Entry<String, ILifeCycle> entry : presenterMap.entrySet()) {
            entry.getValue().onNewIntent(intent);
        }
    }

    @Override
    public void onResume() {
        for (Map.Entry<String, ILifeCycle> entry : presenterMap.entrySet()) {
            entry.getValue().onResume();
        }
    }

    @Override
    public void onPause() {
        for (Map.Entry<String, ILifeCycle> entry : presenterMap.entrySet()) {
            entry.getValue().onPause();
        }
    }

    @Override
    public void onStop() {
        for (Map.Entry<String, ILifeCycle> entry : presenterMap.entrySet()) {
            entry.getValue().onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (Map.Entry<String, ILifeCycle> entry : presenterMap.entrySet()) {
            entry.getValue().onDestroy();
        }
        presenterMap.clear();//移除所有ctrl元素，但并未置空对象引用与回收对象内存
    }

    public ILifeCycle get(String key) {
        return presenterMap.get(key);
    }
}
