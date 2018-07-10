package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.biz.viewBiz.IVWeather;
import com.zjrb.sjzsw.databinding.FrNetBinding;
import com.zjrb.sjzsw.presenter.WeatherPresenter;


/**
 * @author jinzifu
 * @date 2017/9/17
 */

public class NetFragment extends BaseFragment<FrNetBinding> implements IVWeather {
    private WeatherPresenter mWeatherPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        registerPresenter(mWeatherPresenter = new WeatherPresenter(context));
    }

    @Override
    protected void initView(View root) {
        t.netLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeatherPresenter.getWeather();
            }
        });
    }

    @Override
    public void showWeather(final String string) {
        t.infoShow.setText("" + string);
    }
}
