package com.example.rssapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssapp.R;
import com.example.rssapp.data.db.AppDatabase;
import com.example.rssapp.data.model.Feed;
import com.example.rssapp.ui.main.MainActivity;
import com.example.rssapp.ui.main.adapter.RecyclerViewFeedAdapter;

import java.util.List;

public class FragmentSavedNews extends Fragment {
    private EditText edtSearch;
    private ImageButton btnSearch;
    private ImageView imgEmpty;
    private RecyclerView rvSavedFeeds;
    private List<Feed> listSavedFeeds = null;
    private RecyclerViewFeedAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mappingView(view);
        handleActionClick();
    }

    private void mappingView(View view) {
        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        imgEmpty = view.findViewById(R.id.imgEmpty);
        rvSavedFeeds = view.findViewById(R.id.rvSavedFeeds);
    }

    private void getSavedFeedsFromDB() {
        listSavedFeeds = AppDatabase.getInstance(getActivity()).getAllFeeds();
        if(listSavedFeeds != null && listSavedFeeds.size() > 0){
            hideEmptyView();
            adapter = new RecyclerViewFeedAdapter();
            adapter.setListFeed(listSavedFeeds);
            rvSavedFeeds.setAdapter(adapter);
        }else {
            showEmptyView(false);
        }
    }

    private void handleActionClick() {
        btnSearch.setOnClickListener(view -> {
            String searchText = edtSearch.getText().toString().trim();
            if(searchText.isEmpty()){
                getSavedFeedsFromDB();
            }else{
                List<Feed> listFilterFeed = AppDatabase.getInstance(getActivity()).searchFeedWithTitle(searchText);
                if(listFilterFeed != null && listFilterFeed.size() > 0){
                    hideEmptyView();
                    adapter.setListFeed(listFilterFeed);
                }else {
                    showEmptyView(true);
                }
            }
        });
    }

    private void showEmptyView(boolean isSearching){
        rvSavedFeeds.setVisibility(View.GONE);
        if(!isSearching){
            edtSearch.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
        }
        imgEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView(){
        rvSavedFeeds.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        imgEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpToolbarTitle();
        getSavedFeedsFromDB();
    }

    private void setUpToolbarTitle(){
        if(getActivity() instanceof MainActivity){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle("Saved News");
        }
    }
}
