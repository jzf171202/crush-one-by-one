package com.zjrb.sjzsw.model;

public class GirlsItemModel {
    private int id;
    private String name;
    private String url;
    private int type;

    public GirlsItemModel(int id, String name, String url, int type) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
