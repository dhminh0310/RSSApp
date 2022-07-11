package com.example.rssapp.data.model;

import java.io.Serializable;

public class Feed implements Serializable {
    private int id;
    private String title;
    private String url;
    private String description;
    private String channel;
    private String imgUrl;

    public Feed(int id, String title, String url, String description, String channel, String imgUrl) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.channel = channel;
        this.imgUrl = imgUrl;
    }

    public Feed(String title, String url, String description, String channel, String imgUrl) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.channel = channel;
        this.imgUrl = imgUrl;
    }

    public Feed() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
