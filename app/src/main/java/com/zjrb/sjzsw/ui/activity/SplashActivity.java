package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.name)
    TextView name;

    @Override
    protected int getLayoutId() {
        return R.layout.splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        start();
    }

    private void start() {
        ActivityUtil.next(SplashActivity.this, LoginActivity.class);
    }
}
