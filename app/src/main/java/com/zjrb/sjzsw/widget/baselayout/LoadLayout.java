package com.zjrb.sjzsw.widget.baselayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.sjzsw.R;

import butterknife.BindView;

/**
 * 类描述：加载反馈页面
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/8 14
 */

public class LoadLayout extends BaseFrameLayout {
    @BindView(R.id.empty_img)
    ImageView emptyImg;
    @BindView(R.id.emptyretry)
    TextView emptyretry;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.error_img)
    ImageView errorImg;
    @BindView(R.id.error_retry)
    TextView errorRetry;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    public LoadLayout(@NonNull Context context) {
        super(context);
    }

    public LoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.load_layout;
    }

    public void showEmptyView() {
        setVisibility(VISIBLE);
        emptyLayout.setVisibility(VISIBLE);
        errorLayout.setVisibility(GONE);
    }

    public void showErrorView() {
        setVisibility(VISIBLE);
        emptyLayout.setVisibility(GONE);
        errorLayout.setVisibility(VISIBLE);
    }

    public LinearLayout getEmptyLayout() {
        return emptyLayout;
    }

    public LinearLayout getErrorLayout() {
        return errorLayout;
    }
}
