package com.zjrb.sjzsw.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.BaseController;
import com.zjrb.sjzsw.controller.LifecycleManage;
import com.zjrb.sjzsw.utils.ScreenUtil;

/**
 * Created by jinzifu on 2017/9/1.
 * 业务控制fragment基类
 */

public abstract class BaseFragment extends Fragment {
    protected LifecycleManage lifecycleManage = new LifecycleManage();
    protected Context context;
    private View rootView;
    private ViewGroup container;

    /**
     * 获取跟布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), viewGroup, false);
        initStatusBar();
        return rootView;
    }

    /**
     * fragment中沉浸式状态栏设置
     */
    private void initStatusBar() {
        container = rootView.findViewById(R.id.container);
        //低于API19的情况设置非偏移高度，不支持沉浸式状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (container != null) {
                ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                layoutParams.height = ScreenUtil.dip2px(context, 50);
                container.setLayoutParams(layoutParams);
            }
        }
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
