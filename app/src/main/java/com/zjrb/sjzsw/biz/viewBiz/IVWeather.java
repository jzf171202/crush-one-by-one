package com.zjrb.sjzsw.biz.viewBiz;

import com.jzf.net.biz.IVBase;

/**
 * Created by jinzifu on 2018/4/13.
 */

public interface IVWeather extends IVBase {

    /**
     * 展示天气情况
     *
     * @param string
     */
    void showWeather(String string);
}
