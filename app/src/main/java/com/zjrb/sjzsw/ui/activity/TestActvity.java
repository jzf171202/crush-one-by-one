package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcTestBinding;
import com.zjrb.sjzsw.test.Count;
import com.zjrb.sjzsw.test.Sync;

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
                Count.INSTANT.count = 100;
                new Thread(() -> new Sync().c()).start();
                new Thread(() -> new Sync().c()).start();
                break;
        }
    }
}
