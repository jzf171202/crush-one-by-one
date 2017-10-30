package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zjrb.sjzsw.ActivityStackManager;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.BaseController;
import com.zjrb.sjzsw.controller.LifecycleManage;
import com.zjrb.sjzsw.utils.ScreenUtil;

/**
 * Created by jinzifu on 2017/9/3.
 * 业务控制activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    private LifecycleManage lifecycleManage = new LifecycleManage();
    protected Context context;
    private View rootView;
    private ViewGroup container;

    /**
     * 获取根布局的资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(rootView);
        container = rootView.findViewById(R.id.container);
        ActivityStackManager.addActivity(this);
        context = this;
        initStatusBar();
        init(savedInstanceState);
    }


    /**
     * 沉浸式状态栏
     */
    protected void initStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        } else {
            //低于API19的情况设置非偏移高度，不支持沉浸式状态栏
            if (container != null) {
                ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                layoutParams.height = ScreenUtil.dip2px(this, 50);
                container.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 注册控制器
     *
     * @param controller
     */
    protected void registerController(BaseController controller) {
        if (controller != null) {
            lifecycleManage.register(controller.getClass().getSimpleName(), controller);
        }
    }

    /**
     * 获取注册的控制器
     *
     * @param key
     * @return
     */
    protected <Controller extends BaseController> Controller getController(String key) {
        return (Controller) lifecycleManage.get(key);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleManage.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleManage.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleManage.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleManage.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleManage.onDestroy();
        ActivityStackManager.finishActivity(this);
    }

    /**
     * 通用toast展示
     *
     * @param string
     */
    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

}
