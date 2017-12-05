package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.sjzsw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/2 1556
 */

public class ThreadFragment extends BaseFragment {
    @BindView(R.id.text)
    TextView text;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_thread;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ok1, R.id.ok2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok1:
                break;
            case R.id.ok2:
                break;
        }
    }
}
