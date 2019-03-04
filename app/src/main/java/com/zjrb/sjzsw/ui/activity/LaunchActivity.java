package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

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
        t.lauchText.postDelayed(() -> {
//            startActivity(new Intent(LaunchActivity.this, WebViewActivity.class));
            String uri = "app://book:8888/bookDetail?bookId=10011002";
            jumpActivity(isIntentUri(uri), 100);
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String string = data.getStringExtra("result");
        if (requestCode == 100) {
            Log.d("test", string);
        }
    }
}
