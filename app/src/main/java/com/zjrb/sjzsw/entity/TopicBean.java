package com.zjrb.sjzsw.entity;

import java.util.List;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/7 14
 */

public class TopicBean {

    /**
     * code : 0
     * total : 15
     * msg : 获取成功
     * data : {"topicLists":[{"id":154,"title":"20171107颁奖典礼2","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=154","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":1,"streamUsers":"linsq,chenshaohua"},{"id":153,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=153","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":152,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=152","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":151,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=151","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":150,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=150","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":149,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=149","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":148,"title":"20171107颁奖典礼","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=148","reporter":"林上钦","beginTime":1510022435,"endTime":1510022495,"videoType":0,"status":0,"streamUsers":"linsq,chenshaohua"},{"id":128,"title":"test","intro":"1","cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=128","reporter":"1","beginTime":1490868000,"endTime":1497837235,"videoType":0,"status":2,"streamUsers":null},{"id":126,"title":"罗琛的直播测试","intro":"呵呵呵呵","cover":"http://omssbzwng.bkt.clouddn.com/live/cover/1490837801346.jpg","url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=126","reporter":"罗琛","beginTime":1490837580,"endTime":1490857031,"videoType":0,"status":2,"streamUsers":null},{"id":124,"title":"11","intro":null,"cover":null,"url":"http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=124","reporter":"zhang","beginTime":1490783946,"endTime":1490783946,"videoType":0,"status":0,"streamUsers":null}]}
     */

    private int code;
    private int total;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TopicListsBean> topicLists;

        public List<TopicListsBean> getTopicLists() {
            return topicLists;
        }

        public void setTopicLists(List<TopicListsBean> topicLists) {
            this.topicLists = topicLists;
        }

        public static class TopicListsBean {
            /**
             * id : 154
             * title : 20171107颁奖典礼2
             * intro : null
             * cover : null
             * url : http://newslive.zjol.com.cn/video/videoplayer/index.html?liveid=154
             * reporter : 林上钦
             * beginTime : 1510022435
             * endTime : 1510022495
             * videoType : 0
             * status : 1
             * streamUsers : linsq,chenshaohua
             */

            private int id;
            private String title;
            private String intro;
            private String cover;
            private String url;
            private String reporter;
            private long beginTime;
            private long endTime;
            private int videoType;
            private int status;
            private String streamUsers;

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getReporter() {
                return reporter;
            }

            public void setReporter(String reporter) {
                this.reporter = reporter;
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

            public int getVideoType() {
                return videoType;
            }

            public void setVideoType(int videoType) {
                this.videoType = videoType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStreamUsers() {
                return streamUsers;
            }

            public void setStreamUsers(String streamUsers) {
                this.streamUsers = streamUsers;
            }
        }
    }
}