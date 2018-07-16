package com.jzf.net.observer;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.jzf.net.BuildConfig;
import com.jzf.net.api.BaseResponse;
import com.jzf.net.biz.ApiCallBack;
import com.jzf.net.exception.ApiException;
import com.jzf.net.ui.LoadingDialog;

import io.reactivex.disposables.Disposable;

public class ApiObserver<T> extends BaseObserver<BaseResponse<T>> {
    private ApiCallBack apiCallBack;
    private LoadingDialog loadingDialog;

    public ApiObserver(Context context, ApiCallBack apiCallBack) {
        super(context);
        this.apiCallBack = apiCallBack;
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        showDialog();
    }

    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        super.onNext(baseResponse);
        if (baseResponse.isOk()) {
            if (apiCallBack != null) {
                T t = baseResponse.getData();
                apiCallBack.onSuccess(t);
            }
        } else {
            onError(new ApiException(baseResponse.getCode(), baseResponse.getMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        hideDialog();
        ApiException apiException = (ApiException) e;
        switch (apiException.code) {
            default:
                if (BuildConfig.DEBUG) {
                    showTost(apiException.message);
                }
                break;
        }
        if (apiCallBack != null) {
            apiCallBack.onError(apiException);
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        hideDialog();
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        hideDialog();
    }

    public void hideDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public void showDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    private void showTost(String string) {
        if (!TextUtils.isEmpty(string)) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }
    }
}
