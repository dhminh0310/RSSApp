package com.example.rssapp.helper;

import static com.example.rssapp.helper.Constant.VIEW_HOLDER_TYPE_FEED;
import static com.example.rssapp.helper.Constant.VIEW_HOLDER_TYPE_FEED_WITH_SECTION;

public enum ViewHolderType {
    FEED(VIEW_HOLDER_TYPE_FEED),
    SECTION(VIEW_HOLDER_TYPE_FEED_WITH_SECTION)
    ;

    private final int value;

    ViewHolderType(final int value){
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
