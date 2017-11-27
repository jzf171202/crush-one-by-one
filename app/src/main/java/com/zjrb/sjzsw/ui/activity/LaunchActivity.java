package com.zjrb.sjzsw.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.common.Constant;
import com.zjrb.sjzsw.utils.ActivityUtil;
import com.zjrb.sjzsw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/24 1136
 */

public class LaunchActivity extends BaseActivity {
    @BindView(R.id.launch_img)
    ImageView launchImg;
    @BindView(R.id.lauch_text)
    TextView lauchText;
    private Handler hander = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.ac_launch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if (checkPermission(Constant.permissionArray, Constant.PERMISSION_CODE_ALL)) {
            toNext();
        }
    }

    private void toNext() {
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.to(LaunchActivity.this, MainActivity.class);
                finish();
            }
        }, 2000);
    }

    /**
     * 动态监测权限并批量申请
     *
     * @param permission
     * @param code
     * @return true表示没有权限需要申请
     */
    public boolean checkPermission(String[] permission, int code) {
        if (permission != null && permission.length > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < permission.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission[i]);
                }
            }
            if (!ListUtil.isListEmpty(list)) {
                ActivityCompat.requestPermissions(this, list.toArray(new String[list.size()]), code);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_CODE_ALL:
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] < 0) {
                        flag = false;
                    }
                }
                if (flag) {
                    toNext();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hander.removeCallbacksAndMessages(null);
    }
}
