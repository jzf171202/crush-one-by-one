package com.zjrb.sjzsw.observer;

import com.jzf.net.observer.BaseObserver;
import com.zjrb.sjzsw.entity.LoginEntity;

import io.reactivex.disposables.Disposable;

/**
 * Created by jinzifu on 2018/4/11.
 */

public class LoginObserver extends BaseObserver<LoginEntity> {
    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
    }

    @Override
    public void onNext(LoginEntity loginEntity) {
        super.onNext(loginEntity);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }
}
