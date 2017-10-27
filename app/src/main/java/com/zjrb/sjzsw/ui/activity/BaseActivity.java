package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by jinzifu on 2017/8/23.
 */

public abstract class BaseActivity extends FragmentActivity {
    protected Context context;

    /**
     * 获取根布局的资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        init(savedInstanceState);
    }

    /**
     * 通用toast展示
     *
     * @param string
     */
    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
