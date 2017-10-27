package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.zjrb.sjzsw.ActivityStackManager;
import com.zjrb.sjzsw.controller.BaseController;
import com.zjrb.sjzsw.controller.LifecycleManage;

/**
 * Created by jinzifu on 2017/9/3.
 * 业务控制activity基类
 */

public abstract class BaseActivity extends FragmentActivity {
    private LifecycleManage lifecycleManage = new LifecycleManage();
    protected Context context;

    /**
     * 获取根布局的资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        ActivityStackManager.addActivity(this);
        init(savedInstanceState);
    }

    /**
     * 通用toast展示
     *
     * @param string
     */
    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
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
}
