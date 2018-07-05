package com.zjrb.sjzsw.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jzf.net.api.HttpClient;
import com.jzf.net.biz.ApiCallBack;
import com.jzf.net.exception.ApiException;
import com.jzf.net.observer.ApiObserver;
import com.zjrb.sjzsw.api.ApiManager;
import com.zjrb.sjzsw.api.NetManager;
import com.zjrb.sjzsw.biz.other.INetCallBack;
import com.zjrb.sjzsw.biz.presenterBiz.IPWeather;
import com.zjrb.sjzsw.biz.viewBiz.IVWeather;

/**
 * Created by jinzifu on 2018/4/13.
 */

public class WeatherPresenter extends BasePresenter<IVWeather> implements IPWeather {

    public WeatherPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void getWeather() {
        byRetrofitNet();
    }

    /**
     * retrofit网络请求
     */
    private void byRetrofitNet() {
        HttpClient.getInstance().execute(ApiManager.getApiService().getWeather(),
                registerObserver(new ApiObserver(mContext, new ApiCallBack<Object>() {

                    @Override
                    public void onSuccess(Object object) {
                        if (isViewAttached()) {
                            v.showWeather(object.toString());
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                    }
                })));
    }

    /**
     * 自定义的网络请求
     *
     * @param url
     */
    private void byCustomNet(String url) {
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
