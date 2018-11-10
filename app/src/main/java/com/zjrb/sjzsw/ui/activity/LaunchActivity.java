package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcLaunchBinding;
import com.zjrb.sjzsw.utils.AppUtil;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/24 1136
 */

public class LaunchActivity extends BaseActivity<AcLaunchBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_launch;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String versionName = AppUtil.getAppVersion()[1];
        t.lauchText.setText("V  " + versionName);
        toNext();
    }

    private void toNext() {
        t.lauchText.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }
}
