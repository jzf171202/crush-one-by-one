package com.zjrb.sjzsw.entity;

/**
 * Created by Thinkpad on 2017/11/7.
 */

public class LoginBean {

    private String code;
    private String msg;

    public LoginBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
