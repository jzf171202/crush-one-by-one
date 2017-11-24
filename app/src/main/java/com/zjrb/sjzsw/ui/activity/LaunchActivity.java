package com.zjrb.sjzsw.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.common.Constant;
import com.zjrb.sjzsw.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private String[] permissionArray = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected int getLayoutId() {
        return R.layout.ac_launch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

//        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
//        if (flag) {
//            showToast("您应该申请此权限，否则APP无法正常使用");
//        }
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

    @OnClick({R.id.launch_img, R.id.lauch_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.launch_img:
                checkPermission(permissionArray, Constant.PERMISSION_CODE_ALL);
                break;
            case R.id.lauch_text:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_CODE_ALL:
                if (grantResults != null && grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        switch (grantResults[i]) {
                            case 0:
                                showToast("" + permissionArray[i] + "权限申请成功");
                                break;
                            case -1:
                                showToast("" + permissionArray[i] + "权限申请失败");
                                break;
                            default:
                                break;
                        }
                    }
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
