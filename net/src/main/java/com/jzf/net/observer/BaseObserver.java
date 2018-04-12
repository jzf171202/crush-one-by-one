package com.jzf.net.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by jinzifu on 2018/4/11.
 */

public class BaseObserver<T> implements Observer<T> {
    private Disposable mDisposable;

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
