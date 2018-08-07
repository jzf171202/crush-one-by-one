package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private Handler hander = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.ac_launch;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        String versionCode = AppUtil.getAppVersion()[0];
        t.lauchText.setText("V " + versionCode);
        toNext();
    }

    private void toNext() {
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hander.removeCallbacksAndMessages(null);
    }
}
