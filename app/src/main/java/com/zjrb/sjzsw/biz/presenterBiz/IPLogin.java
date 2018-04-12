package com.zjrb.sjzsw.biz.presenterBiz;

/**
 * Created by jinzifu on 2018/4/12.
 */

public interface IPLogin extends IPBase {
    /**
     * 登录
     *
     * @param username
     * @param password
     */
    void login(String username, String password);

    /**
     * URL编码加密
     *
     * @param password
     * @return
     */
    String encode(String password);
}
