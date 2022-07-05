package com.example.rssapp.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssapp.R;
import com.example.rssapp.data.model.Feed;
import com.example.rssapp.helper.FetchFeedTask;
import com.example.rssapp.ui.main.MainActivity;
import com.example.rssapp.ui.newsDetails.NewsDetailsActivity;

import java.util.List;

public class FragmentHome extends Fragment implements FetchFeedTask.IFetchFeedCallback {

    private EditText edtUrl;
    private ImageButton btnFetchData;
    private RecyclerView rvData;
    private FrameLayout loadingView;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mappingView(view);
        doFetchFeed();
    }

    private void mappingView(View view) {
        edtUrl = view.findViewById(R.id.edtUrl);
        btnFetchData = view.findViewById(R.id.btnFetchData);
        rvData = view.findViewById(R.id.rvData);
        loadingView = view.findViewById(R.id.loadingView);
        pbLoading = view.findViewById(R.id.pbLoading);
    }

    private void doFetchFeed() {
        showLoadingView();
        FetchFeedTask task = new FetchFeedTask("");
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
        Log.d("Fetch task", "onFetchFeedComplete: " + data);
        hideLoadingView();
    }

    @Override
    public void onFetchFeedError() {
        hideLoadingView();
        Toast.makeText(getActivity(),
                "Something went wrong !",
                Toast.LENGTH_LONG).show();
    }
}
