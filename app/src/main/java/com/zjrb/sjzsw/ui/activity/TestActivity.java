package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.zjrb.sjzsw.R;

/**
 * Created by Thinkpad on 2017/10/27.
 */

public class TestActivity extends BaseActivity {
    private Button button;
    @Override
    protected int getLayoutId() {
        return R.layout.ac_test;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        button = (Button) findViewById(R.id.button);
    }
}
