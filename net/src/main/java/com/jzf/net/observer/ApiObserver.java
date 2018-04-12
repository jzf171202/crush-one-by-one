package com.jzf.net.observer;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.jzf.net.BuildConfig;
import com.jzf.net.api.BaseResponse;
import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.ApiCallBack;
import com.jzf.net.ui.LoadingDialog;

import io.reactivex.disposables.Disposable;


/**
 * @author jinzifu
 *         <p>
 *         不支持rxjava 2.x背压的，不是好的网络库
 */
public class ApiObserver<T> extends BaseObserver<BaseResponse<T>> {
    private ApiCallBack mApiCallBack;
    private Context mContext;
    private LoadingDialog mLoadingDialog;

    public ApiObserver(Context mContext, ApiCallBack listener) {
        this.mApiCallBack = listener;
        this.mContext = mContext;
        mLoadingDialog = new LoadingDialog(mContext);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        showDialog();
    }

    @Override
    public void onNext(BaseResponse<T> tBaseResponse) {
        super.onNext(tBaseResponse);
        if (tBaseResponse.isOk()) {
            if (mApiCallBack != null) {
                T t = tBaseResponse.getData();
                mApiCallBack.onSuccess(t);
            }
        } else {
            onError(new ApiException.ResponeThrowable(tBaseResponse.getCode(), tBaseResponse.getMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissDialog();
        ApiException.ResponeThrowable responeThrowable = (ApiException.ResponeThrowable) e;
        switch (responeThrowable.code) {
            default:
                if (BuildConfig.DEBUG) {
                    showTost(responeThrowable.message);
                }
                break;
        }
        if (mApiCallBack != null) {
            mApiCallBack.onError(responeThrowable);
        }
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissDialog();
        if (mApiCallBack != null) {
            mApiCallBack.onComplete();
        }
    }

    @Override
    public void unSubscribe() {
        super.unSubscribe();
        dismissDialog();
    }

    public void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void showDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private void showTost(String string) {
        if (!TextUtils.isEmpty(string)) {
            Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
        }
    }
}
