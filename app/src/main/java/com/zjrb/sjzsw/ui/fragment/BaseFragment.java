package com.zjrb.sjzsw.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zjrb.sjzsw.controller.BaseController;
import com.zjrb.sjzsw.controller.LifecycleManage;

/**
 * Created by jinzifu on 2017/9/1.
 * 业务控制fragment基类
 */

public abstract class BaseFragment extends Fragment {
    protected LifecycleManage lifecycleManage = new LifecycleManage();

    protected Context context;

    /**
     * 获取跟布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract void init(View view, @Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        init(rootView, savedInstanceState);
        return rootView;
    }

    /**
     * 通用toast展示
     *
     * @param string
     */
    protected void showToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
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
    public <Controller extends BaseController> Controller getController(String key) {
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
