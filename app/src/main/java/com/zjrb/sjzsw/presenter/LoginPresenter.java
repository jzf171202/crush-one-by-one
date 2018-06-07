package com.zjrb.sjzsw.presenter;

import android.content.Context;

import com.jzf.net.api.BaseResponse;
import com.jzf.net.api.HttpClient;
import com.jzf.net.biz.ApiCallBack;
import com.jzf.net.exception.ApiException;
import com.jzf.net.observer.ApiObserver;
import com.zjrb.sjzsw.api.ApiManager;
import com.zjrb.sjzsw.biz.presenterBiz.IPLogin;
import com.zjrb.sjzsw.biz.viewBiz.IVLogin;
import com.zjrb.sjzsw.entity.LoginEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.reactivex.Observable;

/**
 * 类描述：登录业务类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/27 2020
 */

public class LoginPresenter extends BasePresenter<IVLogin> implements IPLogin {

    public LoginPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void login(String username, String password) {
        // TODO: 2018/6/8 解决rxjava包在住项目必须重复依赖的问题
        Observable<BaseResponse<LoginEntity>> observable = ApiManager.getApiService().login(username, encode(password));
        HttpClient.getInstance().execute(observable, registerObserver(new ApiObserver(mContext, new ApiCallBack<LoginEntity>() {

            @Override
            public void onSuccess(LoginEntity loginEntity) {
                if (null != loginEntity && isViewAttached()) {
                    v.showInfo(loginEntity);
                }
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(ApiException.ResponeThrowable e) {
            }
        })));
    }

    @Override
    public String encode(String password) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int i = 0; i < password.length(); i++) {
                String item = password.charAt(i) + "";
                String output = URLEncoder.encode(item.trim(), "UTF-8");
                if (output.equals(item)) {
                    output = Integer.toHexString(password.charAt(i));
                }
                stringBuilder.append(output);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        password = stringBuilder.toString().replaceAll("\\%", "").toUpperCase();
        return password;
    }
}
