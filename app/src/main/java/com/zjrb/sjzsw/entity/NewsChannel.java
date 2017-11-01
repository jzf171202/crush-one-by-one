package com.zjrb.sjzsw.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Thinkpad on 2017/11/1.
 */
@Entity
public class NewsChannel {

    /** Not-null value. */
    @Id
    private String newsChannelName;
    /** Not-null value. */
    private String newsChannelId;
    /** Not-null value. */
    private String newsChannelType;
    private boolean newsChannelSelect;
    private int newsChannelIndex;
    @Generated(hash = 1418944551)
    public NewsChannel(String newsChannelName, String newsChannelId,
            String newsChannelType, boolean newsChannelSelect,
            int newsChannelIndex) {
        this.newsChannelName = newsChannelName;
        this.newsChannelId = newsChannelId;
        this.newsChannelType = newsChannelType;
        this.newsChannelSelect = newsChannelSelect;
        this.newsChannelIndex = newsChannelIndex;
    }
    @Generated(hash = 566079451)
    public NewsChannel() {
    }
    public String getNewsChannelName() {
        return this.newsChannelName;
    }
    public void setNewsChannelName(String newsChannelName) {
        this.newsChannelName = newsChannelName;
    }
    public String getNewsChannelId() {
        return this.newsChannelId;
    }
    public void setNewsChannelId(String newsChannelId) {
        this.newsChannelId = newsChannelId;
    }
    public String getNewsChannelType() {
        return this.newsChannelType;
    }
    public void setNewsChannelType(String newsChannelType) {
        this.newsChannelType = newsChannelType;
    }
    public boolean getNewsChannelSelect() {
        return this.newsChannelSelect;
    }
    public void setNewsChannelSelect(boolean newsChannelSelect) {
        this.newsChannelSelect = newsChannelSelect;
    }
    public int getNewsChannelIndex() {
        return this.newsChannelIndex;
    }
    public void setNewsChannelIndex(int newsChannelIndex) {
        this.newsChannelIndex = newsChannelIndex;
    }
}
