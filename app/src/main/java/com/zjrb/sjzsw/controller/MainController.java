package com.zjrb.sjzsw.controller;

import android.content.Context;

import com.jzf.net.api.HttpClient;
import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.api.ApiManager;

/**
 * Created by jinzifu on 2017/10/18.
 * Email:jinzifu123@163.com
 * 类描述:
 */

public class MainController extends BaseController {

    public MainController(Context context) {
        super(context);
    }

    /**
     * 获取美女列表
     *
     * @param s              userkey
     * @param i              每页个数
     * @param commonObserver
     */
    public void getGrils(String s, int i, BaseObserver commonObserver) {
        HttpClient.getInstance().execute(ApiManager.getApiService().getGirls(s, i), commonObserver);
    }
}
