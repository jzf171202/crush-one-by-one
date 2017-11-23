package com.zjrb.sjzsw.listener;

/**
 * Created by jinzifu on 2017/9/23.
 */

public interface NetListener {
    void onResponseListener(String string);

    void onErrorListener(int responseCode);
}
