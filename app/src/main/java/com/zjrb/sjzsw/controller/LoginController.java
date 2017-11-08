package com.zjrb.sjzsw.controller;

import android.content.Context;

import com.jzf.net.api.HttpClient;
import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.api.ApiManager;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class LoginController extends BaseController {

    public LoginController(Context context) {
        super(context);
    }

    /**
     * 获取美女列表
     *
     * @param username
     * @param password
     * @param commonObserver
     */
    public void login(String username, String password, BaseObserver commonObserver) {
        HttpClient.getInstance().execute(ApiManager.getApiService().login(username, password), commonObserver);
    }
}