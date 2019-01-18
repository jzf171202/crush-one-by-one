package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.FrCustomLayoutBinding;
import com.zjrb.sjzsw.utils.ImageUtil;

public class CustomFragment extends BaseFragment<FrCustomLayoutBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.fr_custom_layout;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initView(View root) {
        ImageUtil.decodeBitmapFromResource(
                getResources(),
                R.drawable.icon_simple,
                100,
                100);
    }
}
