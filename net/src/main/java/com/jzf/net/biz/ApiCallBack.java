package com.jzf.net.biz;

import com.jzf.net.exception.ApiException;

/**
 * 网络请求回调P层通用类，可以再写一个ApiObserver类并另自定义接口方法回调P层。只通过P层处理M层数据。
 * @param <T>
 */
public interface ApiCallBack<T> {
    void onSuccess(T t);

    void onError(ApiException e);
}
