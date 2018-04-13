package com.jzf.net.biz;

import com.jzf.net.exception.ApiException;

/**
 * @author jinzifu
 */
public interface ApiCallBack<T> {

    /**
     * 成功返回
     *
     * @param t
     */
    void onSuccess(T t);

    /**
     * 返回完成
     */
    void onComplete();

    /**
     * 返回异常
     *
     * @param e
     */
    void onError(ApiException.ResponeThrowable e);
}
