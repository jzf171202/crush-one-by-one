package com.zjrb.sjzsw.presenter;

import com.jzf.net.api.BaseResponse;
import com.jzf.net.api.HttpClient;
import com.zjrb.sjzsw.api.ApiManager;
import com.zjrb.sjzsw.entity.LoginEntity;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/27 2020
 */

public class LoginPresenter extends BasePresenter {

    public void login(String username, String password, Observer observer) {
        Observable<BaseResponse<LoginEntity>> observable = ApiManager.getApiService().login(username, password);
        HttpClient.getInstance().execute(observable, observer);
    }
}
