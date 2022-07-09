package com.example.rssapp.data.model;

import com.example.rssapp.helper.ViewHolderType;

import java.io.Serializable;

public class CombineDataFeed implements Serializable {
    private ViewHolderType type;
    private String title;
    private String url;
    private String description;
    private String channel;
    private String imgUrl;

    public CombineDataFeed(ViewHolderType type, String channel) {
        this.type = type;
        this.channel = channel;
    }

    public CombineDataFeed(ViewHolderType type, String title, String url, String description, String channel, String imgUrl) {
        this.type = type;
        this.title = title;
        this.url = url;
        this.description = description;
        this.channel = channel;
        this.imgUrl = imgUrl;
    }

    public ViewHolderType getType() {
        return type;
    }

    public void setType(ViewHolderType type) {
        this.type = type;
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
