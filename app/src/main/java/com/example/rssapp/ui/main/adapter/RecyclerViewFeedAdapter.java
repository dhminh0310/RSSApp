package com.example.rssapp.ui.main.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rssapp.R;
import com.example.rssapp.data.model.Feed;

import java.util.List;

public class RecyclerViewFeedAdapter extends RecyclerView.Adapter<RecyclerViewFeedAdapter.FeedViewHolder> {

    private List<Feed> listFeeds = null;
    private IAdapterCallback callback = null;

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed currentFeed = listFeeds.get(position);

        Glide.with(holder.itemView.getContext())
                .load(currentFeed.getImgUrl())
                .into(holder.ivImage);
        holder.tvChannel.setText(currentFeed.getChannel());
        holder.tvTitle.setText(currentFeed.getTitle());
        holder.tvDescription.setText(currentFeed.getDescription());
        holder.containerLayout.setOnClickListener(view -> {
            if(callback != null){
                callback.onClickFeedItem(currentFeed);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFeeds.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFeed(List<Feed> listFeeds){
        this.listFeeds = listFeeds;
        notifyDataSetChanged();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout containerLayout;
        ImageView ivImage;
        TextView tvChannel;
        TextView tvTitle;
        TextView tvDescription;
        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            containerLayout = itemView.findViewById(R.id.containerLayout);
            ivImage = itemView.findViewById(R.id.ivFeedImage);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    public interface IAdapterCallback{
        void onClickFeedItem(Feed feed);
    }

    public void setCallback(IAdapterCallback callback) {
        this.callback = callback;
    }
}
