package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.FrCustomLayoutBinding;

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
        t.button2.setOnClickListener(v -> showToast("点击2"));
    }
}
