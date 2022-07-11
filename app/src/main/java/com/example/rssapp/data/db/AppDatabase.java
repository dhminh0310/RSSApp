package com.example.rssapp.data.db;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rssapp.data.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {
    private static AppDatabase instance;

    private static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;
    private static final String TABLE_FEED_NAME = "feed";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final String DESCRIPTION = "description";
    private static final String CHANNEL = "channel";
    private static final String IMAGE_URL = "imgUrl";

    private AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (AppDatabase.class){
                instance = new AppDatabase(context.getApplicationContext());
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String QUERY_CREATE_TABLE_FEED = "CREATE TABLE " + TABLE_FEED_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                URL + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                CHANNEL + " TEXT, " +
                IMAGE_URL + " TEXT )";

        sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_FEED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean insertFeed(Feed feed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, feed.getTitle());
        contentValues.put(URL, feed.getUrl());
        contentValues.put(DESCRIPTION, feed.getDescription());
        contentValues.put(CHANNEL, feed.getChannel());
        contentValues.put(IMAGE_URL, feed.getImgUrl());

        long id = db.insert(
                TABLE_FEED_NAME,
                null,
                contentValues
        );
        db.close();
        return id > -1;
    }

    @SuppressLint("Recycle")
    public List<Feed> getAllFeeds() {
        List<Feed> feeds = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FEED_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Feed feed = new Feed(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                feeds.add(feed);
            }while (cursor.moveToNext());
        }
        return feeds;
    }

    public List<Feed> getFeedsSortedByChannel() {
        List<Feed> feeds = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FEED_NAME + " ORDER BY " + CHANNEL + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Feed feed = new Feed(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                feeds.add(feed);
            }while (cursor.moveToNext());
        }
        return feeds;
    }

    public List<Feed> searchFeedWithTitle(String title) {
        List<Feed> feeds = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FEED_NAME + " WHERE " + TITLE + " LIKE '" + "%" + title + "%'" + " ORDER BY " + CHANNEL + " DESC" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Feed feed = new Feed(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                feeds.add(feed);
            }while (cursor.moveToNext());
        }
        return feeds;
    }

    public boolean deleteFeed(int feedId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long id = db.delete(
                TABLE_FEED_NAME,
                ID + " = " + feedId,
                null
        );
        db.close();
        return id > -1;
    }
}

