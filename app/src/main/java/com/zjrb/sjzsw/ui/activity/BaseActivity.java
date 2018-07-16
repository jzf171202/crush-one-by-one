package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zjrb.sjzsw.biz.viewBiz.IVBase;
import com.zjrb.sjzsw.manager.ActivityStackManager;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.presenter.BasePresenter;
import com.zjrb.sjzsw.presenter.PresenterManager;
import com.zjrb.sjzsw.utils.ScreenUtil;

/**
 * Created by jinzifu on 2017/9/3.
 * 业务控制activity基类
 */

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements IVBase {
    protected final String TAG = getClass().getSimpleName();
    protected Context context;
    private PresenterManager presenterManager = new PresenterManager();
    private ViewGroup container;
    protected T t;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = DataBindingUtil.setContentView(this, getLayoutId());
        container = t.getRoot().findViewById(R.id.container);
        ActivityStackManager.addActivity(this);
        context = this;
        initStatusBar();
        init(savedInstanceState);
    }

    protected abstract void init(Bundle savedInstanceState);

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
     * 注册控制器，绑定V与P层
     *
     * @param basePresenter
     */
    protected void registerPresenter(BasePresenter basePresenter) {
        if (basePresenter != null) {
            String key = basePresenter.getClass().getSimpleName();
            presenterManager.register(key, basePresenter);
            basePresenter.attachView(this);
        }
    }

    /**
     * 获取注册的控制器
     *
     * @param key
     * @return
     */
    protected <Controller extends BasePresenter> Controller getPresenter(String key) {
        return (Controller) presenterManager.get(key);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenterManager.onNewIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenterManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterManager.onDestroy();
        ActivityStackManager.finishActivity(this);
    }

    /**
     * 通用toast展示
     *
     * @param string
     */
    protected void showToast(String string) {
        if (!this.isFinishing()) {
            Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
