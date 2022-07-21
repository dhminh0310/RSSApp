package com.example.rssapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rssapp.R;
import com.example.rssapp.data.db.AppDatabase;
import com.example.rssapp.data.model.Feed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentNewsDetails extends Fragment {

    private WebView webView;
    private FloatingActionButton btnSave;
    private Feed feed = null;
    private boolean isSavedFeed = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mappingView(view);
        handleActionClick();
        setUpWebView();
    }

    private void mappingView(View view) {
        webView = view.findViewById(R.id.webViewFeedFragment);
        btnSave = view.findViewById(R.id.btnSaveFragment);
        if (isSavedFeed) {
            btnSave.setVisibility(View.GONE);
        }
    }

    private void setUpWebView() {
        webView.setWebViewClient(new WebViewClient());
        if (feed != null) {
            webView.loadUrl(feed.getUrl());
        }
    }

    private void handleActionClick() {
        btnSave.setOnClickListener(view -> {
            if (feed == null) return;

            boolean isInsertSuccess = AppDatabase.getInstance(getActivity()).insertFeed(feed);
            if (isInsertSuccess) {
                Toast.makeText(getActivity(), "Save feed successfully !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Save feed error, please try again !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFeed(Feed feed){
        this.feed = feed;
        webView.loadUrl(feed.getUrl());
    }
}
