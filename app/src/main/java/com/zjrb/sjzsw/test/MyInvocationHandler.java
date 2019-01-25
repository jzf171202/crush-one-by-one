package com.zjrb.sjzsw.test;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 方法委托类——实现InvocationHandler接口
 */
public class MyInvocationHandler implements InvocationHandler {
    private MyProxy myProxy;

    /**
     * 通过构造器注入被目标类实例
     * @param myProxy
     */
    public MyInvocationHandler(MyProxy myProxy) {
        this.myProxy = myProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("test", "面向AOP编程——准备中");//模拟前置操作
        Object object = method.invoke(this.myProxy, args);//通过反射调用目标类方法
        Log.d("test", "面向AOP编程——结束后");//模拟后置操作
        return object;
    }
}
