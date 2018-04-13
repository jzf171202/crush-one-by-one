package com.zjrb.sjzsw.presenter;

import android.text.TextUtils;

import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.biz.INetCallBack;
import com.zjrb.sjzsw.biz.presenterBiz.IPWeather;
import com.zjrb.sjzsw.biz.viewBiz.IVWeather;

/**
 * Created by jinzifu on 2018/4/13.
 */

public class WeatherPresenter extends BasePresenter<IVWeather> implements IPWeather {

    @Override
    public void getWeather(String url) {
        NetManager.get(url, new INetCallBack() {
            @Override
            public void onResponseListener(String string) {
                if (!TextUtils.isEmpty(string) && isViewAttached()) {
                    v.showWeather(string);
                }
            }

            @Override
            public void onErrorListener(int responseCode) {
            }
        });
    }
}
