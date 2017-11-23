package com.zjrb.sjzsw.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/14 22
 */

public class TopicListBean implements Serializable {
    /**
     * id : 154
     * title : 20171107颁奖典礼2
     * beginTime : 1510022435
     * endTime : 1510022495
     * intro : null
     * cover : null
     * status : 1
     * streamUsers : [{"userName":"chenshaohua","trueName":"陈少华"},{"userName":"linsq","trueName":"林上钦"}]
     * reporters : [{"userName":"chenshaohua","trueName":"陈少华"},{"userName":"linsq","trueName":"林上钦"}]
     * directors : [{"userName":"chenshaohua","trueName":"陈少华"},{"userName":"linsq","trueName":"林上钦"}]
     * editors : [{"userName":"chenshaohua","trueName":"陈少华"},{"userName":"linsq","trueName":"林上钦"}]
     */

    private int id;
    private String title;
    private long beginTime;
    private long endTime;
    private String intro;
    private String cover;
    private String url;
    private int status;
    private int videoType;
    private List<StreamUsersBean> streamUsers;
    private List<StreamUsersBean> reporters;
    private List<StreamUsersBean> directors;
    private List<StreamUsersBean> editors;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<StreamUsersBean> getStreamUsers() {
        return streamUsers;
    }

    public void setStreamUsers(List<StreamUsersBean> streamUsers) {
        this.streamUsers = streamUsers;
    }

    public List<StreamUsersBean> getReporters() {
        return reporters;
    }

    public void setReporters(List<StreamUsersBean> reporters) {
        this.reporters = reporters;
    }

    public List<StreamUsersBean> getDirectors() {
        return directors;
    }

    public void setDirectors(List<StreamUsersBean> directors) {
        this.directors = directors;
    }

    public List<StreamUsersBean> getEditors() {
        return editors;
    }

    public void setEditors(List<StreamUsersBean> editors) {
        this.editors = editors;
    }

    public static class StreamUsersBean implements Serializable {
        /**
         * userName : chenshaohua
         * trueName : 陈少华
         */

        private String userName;
        private String trueName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public StreamUsersBean() {
        }

        public StreamUsersBean(String userName, String trueName) {
            this.userName = userName;
            this.trueName = trueName;
        }
    }
}
