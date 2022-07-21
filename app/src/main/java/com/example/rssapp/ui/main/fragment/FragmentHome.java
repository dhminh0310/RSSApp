package com.example.rssapp.ui.main.fragment;

import static com.example.rssapp.helper.Constant.FEED_EXTRA_NAME;
import static com.example.rssapp.helper.Constant.LIST_FEEDS_EXTRA_NAME;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssapp.R;
import com.example.rssapp.data.model.Feed;
import com.example.rssapp.helper.FetchFeedTask;
import com.example.rssapp.ui.main.MainActivity;
import com.example.rssapp.ui.main.adapter.RecyclerViewFeedAdapter;
import com.example.rssapp.ui.newsDetails.NewsDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment
        implements FetchFeedTask.IFetchFeedCallback, RecyclerViewFeedAdapter.IAdapterCallback {

    private EditText edtUrl;
    private ImageButton btnFetchData;
    private RecyclerView rvFeeds;
    private FrameLayout loadingView;
    private ProgressBar pbLoading;
    private RecyclerViewFeedAdapter adapter;
    private ArrayList<Feed> listFeeds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(LIST_FEEDS_EXTRA_NAME)){
            listFeeds = savedInstanceState.getParcelableArrayList(LIST_FEEDS_EXTRA_NAME);
        }

        mappingView(view);
        handleActionClick();
        showPreviousDataFeeds();
    }

    private void mappingView(View view) {
        edtUrl = view.findViewById(R.id.edtUrl);
        btnFetchData = view.findViewById(R.id.btnFetchData);
        rvFeeds = view.findViewById(R.id.rvData);
        loadingView = view.findViewById(R.id.loadingView);
        pbLoading = view.findViewById(R.id.pbLoading);
        adapter = new RecyclerViewFeedAdapter();
        adapter.setCallback(this);
    }

    private void showPreviousDataFeeds() {
        if(listFeeds.size() > 0){
            adapter.setListFeed(listFeeds);
            rvFeeds.setAdapter(adapter);
        }
    }

    private void handleActionClick() {
        btnFetchData.setOnClickListener(view -> {
            String url = edtUrl.getText().toString().trim();
            if (!url.isEmpty() && URLUtil.isValidUrl(url)) {
                doFetchFeed(url);
            } else {
                Toast.makeText(getActivity(), "Invalid url", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doFetchFeed(String url) {
        showLoadingView();
        FetchFeedTask task = new FetchFeedTask(url);
        task.setCallback(this);
        task.execute((Void) null);
    }

    private void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.VISIBLE);
        loadingView.setOnClickListener(view -> {
        });
    }

    private void hideLoadingView() {
        loadingView.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpToolbarTitle();
    }

    private void setUpToolbarTitle() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle("Home");
        }
    }

    @Override
    public void onFetchFeedComplete(List<Feed> data) {
        listFeeds.addAll(data);
        rvFeeds.setAdapter(adapter);
        adapter.setListFeed(data);
        hideLoadingView();
    }

    @Override
    public void onFetchFeedError() {
        hideLoadingView();
        Toast.makeText(getActivity(),
                "Something went wrong !",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickFeedItem(Feed feed) {
        FragmentNewsDetails fragmentFeedDetails = (FragmentNewsDetails) this.getChildFragmentManager().findFragmentById(R.id.fragmentDetails);
        boolean isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandScape && fragmentFeedDetails != null && fragmentFeedDetails.isVisible()) {
            //case landscape
            fragmentFeedDetails.setFeed(feed);

        } else {
            //case normal
            Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
            intent.putExtra(FEED_EXTRA_NAME, (Serializable) feed);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(listFeeds.size() > 0){
            outState.putParcelableArrayList(LIST_FEEDS_EXTRA_NAME, listFeeds);
        }
        super.onSaveInstanceState(outState);
    }
}
