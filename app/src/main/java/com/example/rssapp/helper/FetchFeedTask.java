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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

    public FetchFeedTask(String urlLink) {
        this.urlLink = urlLink;
    }

    private String urlLink;
    private List<Feed> data;
    private IFetchFeedCallback callback;

    @Override
    protected void onPreExecute() {
        urlLink = "https://vnexpress.net/rss";
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (TextUtils.isEmpty(urlLink))
            return false;

        try {
            if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;

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
            // Fill RecyclerView
            //mRecyclerView.setAdapter(new adapter(mData));
            if (callback != null) callback.onFetchFeedComplete(data);
        } else {
            if (callback != null) callback.onFetchFeedError();
        }
    }

    private List<Feed> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<Feed> items = new ArrayList<>();

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

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        //viewModel item = new viewModel(title, link, description);
                        Feed item = new Feed();
                        items.add(item);
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        return items;
    }

    public interface IFetchFeedCallback {
        void onFetchFeedComplete(List<Feed> data);

        void onFetchFeedError();
    }

    public void setCallback(IFetchFeedCallback callback) {
        this.callback = callback;
    }
}
