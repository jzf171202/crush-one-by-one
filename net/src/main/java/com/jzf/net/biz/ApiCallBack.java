package com.jzf.net.biz;

import com.jzf.net.exception.ApiException;

public interface ApiCallBack<T> {
    void onSuccess(T t);

    void onError(ApiException e);
}
