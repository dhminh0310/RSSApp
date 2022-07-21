package com.example.rssapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Feed implements Serializable, Parcelable {
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

    protected Feed(Parcel in) {
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        description = in.readString();
        channel = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(description);
        parcel.writeString(channel);
        parcel.writeString(imgUrl);
    }
}
