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

        if (!checkPermission(Constant.permissionArray, Constant.PERMISSION_CODE_ALL)) {
            toNext();
        }
//        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE);
//        if (flag) {//小米手机上只要拒绝一次，都是返回false，因此兼容思路可以在进行危险权限的代码里加try-catch，并在catch异常里弹出重新申请权限的对话框
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

    /**
     * 动态监测权限并批量申请
     *
     * @param permission
     * @param code
     * @return false表示没有权限需要申请
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
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PERMISSION_CODE_ALL:
                toNext();
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
