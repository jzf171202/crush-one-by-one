package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Thinkpad on 2017/10/30.
 */

abstract class BaseActivity2 extends AppCompatActivity {
    protected Context context;

    /**
     * 获取根布局的资源ID
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
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

