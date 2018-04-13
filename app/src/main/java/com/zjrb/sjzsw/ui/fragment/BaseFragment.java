package com.zjrb.sjzsw.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jzf.net.biz.IVBase;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.presenter.BasePresenter;
import com.zjrb.sjzsw.presenter.PresenterManager;
import com.zjrb.sjzsw.utils.ScreenUtil;

/**
 * Created by jinzifu on 2017/9/1.
 * 业务控制fragment基类
 */

public abstract class BaseFragment extends Fragment implements IVBase {
    protected PresenterManager mPresenterManager = new PresenterManager();
    public Context mContext;
    private View rootView;
    private ViewGroup container;

    /**
     * 获取跟布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     * @return
     */
    protected abstract void init(@Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), viewGroup, false);
        initStatusBar();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    /**
     * fragment中沉浸式状态栏设置
     */
    private void initStatusBar() {
        container = (ViewGroup) rootView.findViewById(R.id.container);
        //低于API19的情况设置非偏移高度，不支持沉浸式状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (container != null) {
                ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                layoutParams.height = ScreenUtil.dip2px(mContext, 50);
                container.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 注册控制器
     *
     * @param basePresenter
     */
    protected void registerPresenter(BasePresenter basePresenter) {
        if (basePresenter != null) {
            String key = basePresenter.getClass().getSimpleName();
            mPresenterManager.register(key, basePresenter);
            basePresenter.attachView(this);
        }
    }

    /**
     * 获取注册的控制器
     *
     * @param key
     * @return
     */
    public BasePresenter getPresenter(String key) {
        return (BasePresenter) mPresenterManager.get(key);
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenterManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenterManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenterManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterManager.onDestroy();
    }

    protected void showToast(String string) {
        if (!getActivity().isFinishing()) {
            Toast toast = Toast.makeText(mContext, string, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}