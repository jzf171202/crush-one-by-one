package com.jzf.net.observer;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jinzifu on 2018/4/11.
 */

public class BaseObserver<T> implements Observer<T> {
    public Context mContext;
    private Disposable mDisposable;

    public BaseObserver(Context mContext) {
        this.mContext = mContext;
    }

    public BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    public void unSubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
