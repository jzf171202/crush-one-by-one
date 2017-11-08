package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.manager.ThreadManager;
import com.zjrb.sjzsw.utils.ActivityUtil;
import com.zjrb.sjzsw.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void start() {
        //判断距离上次操作是否超出30分钟，如果超出，则跳到登录界面
        if(SpUtil.containsKey("currenttime") && System.currentTimeMillis() - SpUtil.getLong("currenttime", 0) > 30*60*1000)
        {
        ActivityUtil.next(SplashActivity.this, LoginActivity.class);
        }
        else
        {
            ActivityUtil.next(SplashActivity.this, MainActivity.class);
        }
    }
}
