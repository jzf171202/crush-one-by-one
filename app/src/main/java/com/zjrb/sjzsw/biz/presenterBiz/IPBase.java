package com.zjrb.sjzsw.biz.presenterBiz;

import com.jzf.net.biz.IVBase;

/**
 * P层业务逻辑接口基类
 * Created by jinzifu on 2018/4/12.
 */

public interface IPBase<V extends IVBase> {

    void attachView(V v);

    void detachView();

    V getView();

    boolean isViewAttached();
}
