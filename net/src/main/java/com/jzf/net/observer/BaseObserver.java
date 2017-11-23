package com.jzf.net.observer;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.jzf.net.BuildConfig;
import com.jzf.net.api.BaseResponse;
import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.OnResultCallBack;
import com.jzf.net.ui.LoadingDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author jinzifu
 */
public class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private OnResultCallBack mOnResultListener;
    private Disposable mDisposable;
    private Context context;
    private LoadingDialog mLoadingDialog;

    public BaseObserver(Context context, OnResultCallBack listener) {
        this.mOnResultListener = listener;
        this.context = context;
        mLoadingDialog = new LoadingDialog(context);
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


    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        showDialog();
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        if (response.isOk()) {
            if (mOnResultListener != null) {
                T t = response.getData();
                mOnResultListener.onSuccess(t);
            }
        } else {
            onError(new ApiException.ResponeThrowable(response.getCode(), response.getMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {
        dismissDialog();
        ApiException.ResponeThrowable responeThrowable = (ApiException.ResponeThrowable) e;
        switch (responeThrowable.code) {
            default:
                if (BuildConfig.DEBUG) {
                    showTost(responeThrowable.message);
                }
                break;
        }
        if (mOnResultListener != null) {
            mOnResultListener.onError(responeThrowable);
        }
    }

    @Override
    public void onComplete() {
        dismissDialog();
        if (mOnResultListener != null) {
            mOnResultListener.onComplete();
        }
    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
    }

    /**
     * 展示错误状态吐司
     *
     * @param string
     */
    private void showTost(String string) {
        if (!TextUtils.isEmpty(string)) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }
    }
}
