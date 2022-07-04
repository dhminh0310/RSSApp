package com.example.rssapp.ui.newsDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.example.rssapp.R;

public class NewsDetailsActivity extends AppCompatActivity {

    private Toolbar tbDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        mappingView();
        setUpToolbarDetail();
    }

    private void mappingView() {
        tbDetails = (Toolbar) findViewById(R.id.tbDetails);
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
}