package com.zjrb.sjzsw.biz.other;

/**
 * Created by jinzifu on 2017/9/23.
 */

public interface INetCallBack {
    void onResponseListener(String string);

    void onErrorListener(int responseCode);
}
