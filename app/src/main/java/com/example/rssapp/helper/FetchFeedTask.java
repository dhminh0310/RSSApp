package com.example.rssapp.helper;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.rssapp.data.model.Feed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

    private String urlLink;
    private List<Feed> data;
    private IFetchFeedCallback callback;

    public FetchFeedTask(String urlLink) {
        this.urlLink = urlLink;
    }

    @Override
    protected void onPreExecute() {
        if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
            urlLink = "http://" + urlLink;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (TextUtils.isEmpty(urlLink))
            return false;

        try {
            URL url = new URL(urlLink);
            InputStream inputStream = url.openConnection().getInputStream();
            data = parseFeed(inputStream);
            return true;
        } catch (IOException | XmlPullParserException e) {
            Log.e("<<<ttt>>>", "Error", e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            if (callback != null) callback.onFetchFeedComplete(data);
        } else {
            if (callback != null) callback.onFetchFeedError();
        }
    }

    private List<Feed> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String channel = null;
        String title = null;
        String link = null;
        String description = null;
        String imgUrl = null;
        boolean isItem = false;
        List<Feed> feeds = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                if (name.equalsIgnoreCase("channel") && eventType == XmlPullParser.START_TAG) {
                    while (
                            xmlPullParser.getEventType() == XmlPullParser.TEXT ||
                                    !(xmlPullParser.getName().equalsIgnoreCase("title") &&
                                            xmlPullParser.getEventType() == XmlPullParser.END_TAG)
                    ) {
                        if (xmlPullParser.getEventType() == XmlPullParser.TEXT) {
                            channel = xmlPullParser.getText();
                        }
                        xmlPullParser.next();
                    }
                }

                if (name.equalsIgnoreCase("description")
                        && eventType == XmlPullParser.START_TAG
                        && isItem) {
                    while (
                            xmlPullParser.getEventType() == XmlPullParser.TEXT ||
                                    xmlPullParser.getEventType() == XmlPullParser.CDSECT ||
                                    !(xmlPullParser.getName().equalsIgnoreCase("description") &&
                                            xmlPullParser.getEventType() == XmlPullParser.END_TAG)
                    ) {
                        if(xmlPullParser.getEventType() == XmlPullParser.CDSECT){
                            String descriptionString = xmlPullParser.getText();
                            int startIndexImg = descriptionString.indexOf("src=");
                            int endIndexImgSingleQuote = descriptionString.indexOf("'", startIndexImg + 6);
                            int endIndexImgDoubleQuote = descriptionString.indexOf("\"", startIndexImg + 6);

                            if(startIndexImg > - 1 && (endIndexImgSingleQuote > - 1 || endIndexImgDoubleQuote > -1)){
                                if(endIndexImgSingleQuote > -1){
                                    imgUrl = descriptionString.substring(
                                            startIndexImg + 5,
                                            endIndexImgSingleQuote
                                    );
                                }else{
                                    imgUrl = descriptionString.substring(
                                            startIndexImg + 5,
                                            endIndexImgDoubleQuote
                                    );
                                }
                            }

                            int startIndexDescription = descriptionString.lastIndexOf(">");
                            if(startIndexDescription > -1){
                                description = descriptionString.substring(
                                        startIndexDescription + 1
                                );
                            }
                            break;
                        }

                        if (xmlPullParser.getName() != null &&
                                xmlPullParser.getName().equalsIgnoreCase("img") &&
                                xmlPullParser.getEventType() == XmlPullParser.START_TAG
                        ) {
                            imgUrl = xmlPullParser.getAttributeValue("", "src");
                        }

                        if (xmlPullParser.getEventType() == XmlPullParser.TEXT) {
                            description = xmlPullParser.getText();
                        }
                        xmlPullParser.nextToken();
                    }
                }

                if (isItem && xmlPullParser.next() == XmlPullParser.TEXT) {
                    String result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    }
                }

                if (title != null && link != null && description != null && channel != null && imgUrl != null) {
                    if (isItem) {
                        Feed feed = new Feed(title, link, description, channel, imgUrl);
                        feeds.add(feed);
                    }

                    title = null;
                    link = null;
                    description = null;
                    imgUrl = null;
                    isItem = false;
                }
            }
            return feeds;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        return feeds;
    }

    public interface IFetchFeedCallback {
        void onFetchFeedComplete(List<Feed> data);

        void onFetchFeedError();
    }

    public void setCallback(IFetchFeedCallback callback) {
        this.callback = callback;
    }
}
