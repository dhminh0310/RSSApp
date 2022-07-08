package com.example.rssapp.data.model;

import java.io.Serializable;

public class Feed implements Serializable {

    private String title;
    private String url;
    private String description;
    private String channel;
    private String imgUrl;

    public Feed(String channel, String title, String url, String description, String imgUrl) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.channel = channel;
        this.imgUrl = imgUrl;
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
