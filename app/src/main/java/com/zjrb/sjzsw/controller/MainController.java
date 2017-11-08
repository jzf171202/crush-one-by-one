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

    public void getProgramList(int pageNo, BaseObserver baseObserver) {
        HttpClient.getInstance().execute(ApiManager.getApiService().getProgramList(pageNo, "10"), baseObserver);
    }
}
