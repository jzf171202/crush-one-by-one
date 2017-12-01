package com.zjrb.sjzsw.controller;

import android.content.Context;

import com.jzf.net.api.HttpClient;
import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.api.ApiManager;
import com.zjrb.sjzsw.entity.LoginEntity;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/27 2020
 */

public class LoginController extends BaseController {
    public LoginController(Context context) {
        super(context);
    }

    public void login(String username, String password, BaseObserver<LoginEntity> baseObserver) {
        HttpClient.getInstance().execute(ApiManager.getApiService().login(username, password), baseObserver);
    }
}
