package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.biz.viewBiz.IVWeather;
import com.zjrb.sjzsw.presenter.WeatherPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author jinzifu
 * @date 2017/9/17
 */

public class NetFragment extends BaseFragment implements IVWeather {
    @BindView(R.id.info_show)
    TextView infoShow;
    Unbinder unbinder;
    // TODO: 2018/7/5  去掉butterknife,封装dataBinding架构层

    private WeatherPresenter mWeatherPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_net;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        registerPresenter(mWeatherPresenter = new WeatherPresenter(mContext));
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

    @OnClick({R.id.net_load})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.net_load:
                mWeatherPresenter.getWeather();
                break;
            default:
                break;
        }
    }

    @Override
    public void showWeather(final String string) {
        // object.toString()解析成的字符串格式错误，不可再次转换成实体类
        //{shidu=53%, pm25=64.0, pm10=107.0, quality=轻度污染, wendu=29, ganmao=儿童、老年人及心脏、呼吸系统疾病患者人群应减少长时间或高强度户外锻炼, yesterday={date=04日星期三, sunrise=04:50, high=高温 36.0℃, low=低温 25.0℃, sunset=19:46, aqi=124.0, fx=西南风, fl=<3级, type=多云, notice=阴晴之间，谨防紫外线侵扰}, forecast=[{date=05日星期四, sunrise=04:51, high=高温 37.0℃, low=低温 24.0℃, sunset=19:46, aqi=75.0, fx=东南风, fl=<3级, type=多云, notice=阴晴之间，谨防紫外线侵扰}, {date=06日星期五, sunrise=04:51, high=高温 33.0℃, low=低温 23.0℃, sunset=19:46, aqi=69.0, fx=东南风, fl=<3级, type=雷阵雨, notice=带好雨具，别在树下躲雨}, {date=07日星期六, sunrise=04:52, high=高温 31.0℃, low=低温 21.0℃, sunset=19:46, aqi=59.0, fx=东南风, fl=3-4级, type=雷阵雨, notice=带好雨具，别在树下躲雨}, {date=08日星期日, sunrise=04:52, high=高温 27.0℃, low=低温 20.0℃, sunset=19:45, aqi=58.0, fx=东北风, fl=<3级, type=雷阵雨, notice=带好雨具，别在树下躲雨}, {date=09日星期一, sunrise=04:53, high=高温 29.0℃, low=低温 21.0℃, sunset=19:45, aqi=62.0, fx=东南风, fl=<3级, type=多云, notice=阴晴之间，谨防紫外线侵扰}]}
        infoShow.setText("" + string);
    }
}
