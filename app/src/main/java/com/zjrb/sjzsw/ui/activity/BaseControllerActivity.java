package com.zjrb.sjzsw.ui.activity;

import com.zjrb.sjzsw.controller.BaseController;
import com.zjrb.sjzsw.controller.LifecycleManage;

/**
 * Created by jinzifu on 2017/9/3.
 * 业务控制activity基类
 */

public abstract class BaseControllerActivity extends BaseActivity {
    private LifecycleManage lifecycleManage = new LifecycleManage();

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
    protected  <Controller extends BaseController> Controller getController(String key) {
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
    }
}
