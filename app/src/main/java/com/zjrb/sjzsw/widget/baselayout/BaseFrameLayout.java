package com.zjrb.sjzsw.widget.baselayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zjrb.sjzsw.listener.OnChildViewClickListener;

import butterknife.ButterKnife;

/**
 * Created by jinzifu on 2017/6/10.
 */

public abstract class BaseFrameLayout extends FrameLayout {
    private Context context;

    public BaseFrameLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected OnChildViewClickListener onChildViewClickListener;


    protected void init(Context context, AttributeSet attrs) {
        if (layoutId() != 0) {
            this.context = context;
            LayoutInflater.from(context).inflate(layoutId(), this, true);
            ButterKnife.bind(this);
            initView();
            initListener();
            initData();
        }
    }

    public void setOnChildViewClickListener(OnChildViewClickListener onChildViewClickListener) {
        this.onChildViewClickListener = onChildViewClickListener;
    }

    /**
     * 实现此方法以在业务类中处理点击事件
     * @param childView
     * @param action
     * @param obj
     */
    protected void onChildViewClick(View childView, String action, Object obj) {
        if (onChildViewClickListener != null) {
            onChildViewClickListener.onChildViewClick(childView, action, obj);
        }
    }

    /**
     * 获取组合布局的资源ID
     * @return
     */
    protected abstract int layoutId();

    protected void initView() {
    }

    protected void initData() {
    }

    protected void initListener() {
    }

    protected void toast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
