package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述：从seekBar入门自定义view的fragment
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/12 1535
 */

public class SeekBarFragment extends BaseFragment {
    private static final String TAG = "SeekBarFragment";
    @BindView(R.id.indicatorSeekBar)
    IndicatorSeekBar indicatorSeekBar;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_seekbar;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        indicatorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "indicatorSeekBar==" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
}
