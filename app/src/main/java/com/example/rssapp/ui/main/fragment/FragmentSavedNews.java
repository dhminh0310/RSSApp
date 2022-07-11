package com.example.rssapp.ui.main.fragment;

import static com.example.rssapp.helper.Constant.FEED_EXTRA_NAME;
import static com.example.rssapp.helper.Constant.IS_SAVED_FEED_EXTRA_NAME;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssapp.R;
import com.example.rssapp.data.db.AppDatabase;
import com.example.rssapp.data.model.CombineDataFeed;
import com.example.rssapp.data.model.Feed;
import com.example.rssapp.helper.ViewHolderType;
import com.example.rssapp.ui.main.MainActivity;
import com.example.rssapp.ui.main.adapter.RecyclerViewSavedFeedAdapter;
import com.example.rssapp.ui.newsDetails.NewsDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentSavedNews extends Fragment implements RecyclerViewSavedFeedAdapter.IAdapterCallback {
    private EditText edtSearch;
    private ImageButton btnSearch;
    private ImageView imgEmpty;
    private RecyclerView rvSavedFeeds;
    private RecyclerViewSavedFeedAdapter adapter = null;

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
        List<Feed> listSavedFeeds = AppDatabase.getInstance(getActivity()).getFeedsSortedByChannel();
        if (listSavedFeeds != null && listSavedFeeds.size() > 0) {
            hideEmptyView();
            adapter = new RecyclerViewSavedFeedAdapter();
            adapter.setListFeed(getListCombineData(listSavedFeeds));
            adapter.setCallback(this);
            rvSavedFeeds.setAdapter(adapter);
        } else {
            showEmptyView(false);
        }
    }

    private List<CombineDataFeed> getListCombineData(List<Feed> listFeed) {
        List<CombineDataFeed> listCombineData = new ArrayList<>();
        String currentChannel = "";

        for (int i = 0; i < listFeed.size(); i++) {
            if (!listFeed.get(i).getChannel().equalsIgnoreCase(currentChannel)) {
                listCombineData.add(new CombineDataFeed(
                        ViewHolderType.SECTION,
                        listFeed.get(i).getChannel()
                ));
            }
            listCombineData.add(new CombineDataFeed(
                    ViewHolderType.FEED,
                    listFeed.get(i).getId(),
                    listFeed.get(i).getTitle(),
                    listFeed.get(i).getUrl(),
                    listFeed.get(i).getDescription(),
                    listFeed.get(i).getChannel(),
                    listFeed.get(i).getImgUrl()
            ));
            currentChannel = listFeed.get(i).getChannel();
        }
        return listCombineData;
    }

    private void handleActionClick() {
        btnSearch.setOnClickListener(view -> {
            String searchText = edtSearch.getText().toString().trim();
            if (searchText.isEmpty()) {
                getSavedFeedsFromDB();
            } else {
                List<Feed> listFilterFeed = AppDatabase.getInstance(getActivity()).searchFeedWithTitle(searchText);
                if (listFilterFeed != null && listFilterFeed.size() > 0) {
                    hideEmptyView();
                    adapter.setListFeed(getListCombineData(listFilterFeed));
                } else {
                    showEmptyView(true);
                }
            }
        });
    }

    private void showEmptyView(boolean isSearching) {
        rvSavedFeeds.setVisibility(View.GONE);
        if (!isSearching) {
            edtSearch.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
        }
        imgEmpty.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
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

    private void setUpToolbarTitle() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle("Saved News");
        }
    }

    @Override
    public void onClickFeedItem(CombineDataFeed combineDataFeed) {
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra(FEED_EXTRA_NAME, combineDataFeed);
        intent.putExtra(IS_SAVED_FEED_EXTRA_NAME, true);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLongClickFeedItem(CombineDataFeed combineDataFeed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete feed");
        builder.setMessage("Are you sure ?");

        builder.setPositiveButton("Ok", (dialog, i) -> {
            boolean isSuccess = AppDatabase.getInstance(getActivity()).deleteFeed(combineDataFeed.getId());
            if(!isSuccess){
                Toast.makeText(getActivity(),
                        "Something went wrong when delete feed",
                        Toast.LENGTH_SHORT).show();
            }else {
                List<Feed> listSavedFeeds = AppDatabase.getInstance(getActivity()).getFeedsSortedByChannel();
                if (listSavedFeeds != null && listSavedFeeds.size() > 0) {
                    adapter.setListFeed(getListCombineData(listSavedFeeds));
                } else {
                    showEmptyView(false);
                }
            }
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
