package com.zjrb.sjzsw.biz;

/**
 * Created by jinzifu on 2017/9/23.
 */

public interface INetCallBack {
    void onResponseListener(String string);

    void onErrorListener(int responseCode);
}
