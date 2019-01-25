package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.biz.other.IProxy;
import com.zjrb.sjzsw.databinding.AcTestBinding;
import com.zjrb.sjzsw.test.MyInvocationHandler;
import com.zjrb.sjzsw.test.MyProxy;

import java.lang.reflect.Proxy;

public class TestActvity extends BaseActivity<AcTestBinding> implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        t.test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                testProxy();
                break;
        }
    }

    private void testProxy() {
        MyProxy myProxy = new MyProxy();
        //ClassLoader 每次生成动态代理类对象时都需要指定一个类装载器对象
        IProxy iProxy = (IProxy) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                myProxy.getClass().getInterfaces(),
                new MyInvocationHandler(myProxy));
        iProxy.print();
    }
}
