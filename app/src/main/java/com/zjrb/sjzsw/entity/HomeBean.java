package com.zjrb.sjzsw.entity;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/7 14
 */

public class HomeBean {
    private String title;
    private String status;
    private String starttime;
    private String endtime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public HomeBean(String title, String status, String starttime, String endtime) {
        this.title = title;
        this.status = status;
        this.starttime = starttime;
        this.endtime = endtime;
    }
}