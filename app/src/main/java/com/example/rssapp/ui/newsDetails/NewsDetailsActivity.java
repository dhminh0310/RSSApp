package com.example.rssapp.ui.newsDetails;

import static com.example.rssapp.helper.Constant.FEED_EXTRA_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.rssapp.R;
import com.example.rssapp.data.db.AppDatabase;
import com.example.rssapp.data.model.Feed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity {

    private Toolbar tbDetails;
    private WebView webView;
    private FloatingActionButton btnSave;
    private Feed feed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        feed = (Feed) getIntent().getSerializableExtra(FEED_EXTRA_NAME);
        mappingView();
        setUpToolbarDetail();
        setUpWebView();
        handleActionClick();
    }

    private void mappingView() {
        tbDetails = findViewById(R.id.tbDetails);
        webView = findViewById(R.id.webViewFeed);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setUpToolbarDetail() {
        setSupportActionBar(tbDetails);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tbDetails.setNavigationIcon(R.drawable.ic_back);

        tbDetails.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setUpWebView() {
        if(feed != null){
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(feed.getUrl());
        }
    }

    private void handleActionClick() {
        btnSave.setOnClickListener(view -> {
            boolean isInsertSuccess = AppDatabase.getInstance(this).insertFeed(feed);
            if(isInsertSuccess){
                Toast.makeText(this, "Save feed successfully !", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Save feed error, please try again !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}