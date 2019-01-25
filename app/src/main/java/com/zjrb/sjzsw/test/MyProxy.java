package com.zjrb.sjzsw.test;

import android.util.Log;

import com.zjrb.sjzsw.biz.other.IProxy;

/**
 * 目标类——实现目标业务接口
 */
public class MyProxy implements IProxy {
    @Override
    public void print() {
        Log.d("test","面向AOP编程——进行中");
    }
}
