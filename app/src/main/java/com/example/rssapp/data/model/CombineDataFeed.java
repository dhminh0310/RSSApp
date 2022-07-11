package com.example.rssapp.data.model;

import com.example.rssapp.helper.ViewHolderType;

import java.io.Serializable;

public class CombineDataFeed extends Feed {
    private ViewHolderType type;

    public CombineDataFeed(ViewHolderType type, String channel) {
        super();
        this.type = type;
        setChannel(channel);
    }

    public CombineDataFeed(ViewHolderType type, int id, String title, String url, String description, String channel, String imgUrl) {
        super(id, title, url, description, channel, imgUrl);
        this.type = type;
    }

    public ViewHolderType getType() {
        return type;
    }

    public void setType(ViewHolderType type) {
        this.type = type;
    }
}
