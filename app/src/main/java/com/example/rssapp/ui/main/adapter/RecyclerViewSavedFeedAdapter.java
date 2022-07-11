package com.example.rssapp.ui.main.adapter;

import static com.example.rssapp.helper.Constant.VIEW_HOLDER_TYPE_FEED;

import android.annotation.SuppressLint;
import android.util.Log;
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
import com.example.rssapp.data.model.CombineDataFeed;
import com.example.rssapp.data.model.Feed;
import com.example.rssapp.helper.Constant;
import com.example.rssapp.helper.ViewHolderType;

import java.util.List;

public class RecyclerViewSavedFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CombineDataFeed> listCombineData = null;
    private IAdapterCallback callback = null;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_HOLDER_TYPE_FEED:
                return new FeedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false));
            default:
                return new FeedWithSectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CombineDataFeed currentCombineData = listCombineData.get(position);
        switch (holder.getItemViewType()){
            case VIEW_HOLDER_TYPE_FEED:
                ((FeedViewHolder) holder).bindData(currentCombineData);
                break;
            default:
                ((FeedWithSectionViewHolder) holder).bindData(currentCombineData);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listCombineData.get(position).getType().getValue();
    }

    @Override
    public int getItemCount() {
        return listCombineData.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFeed(List<CombineDataFeed> listCombineData){
        this.listCombineData = listCombineData;
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
        public void bindData(CombineDataFeed currentCombineData){
            Glide.with(itemView.getContext())
                    .load(currentCombineData.getImgUrl())
                    .into(ivImage);
            tvChannel.setText(currentCombineData.getChannel());
            tvTitle.setText(currentCombineData.getTitle());
            tvDescription.setText(currentCombineData.getDescription());

            containerLayout.setOnClickListener(view -> {
                if(callback != null){
                    callback.onClickFeedItem(currentCombineData);
                }
            });

            containerLayout.setOnLongClickListener(view -> {
                if(callback != null){
                    callback.onLongClickFeedItem(currentCombineData);
                }
                return false;
            });
        }
    }

    public class FeedWithSectionViewHolder extends RecyclerView.ViewHolder{
        TextView tvChannelSection;
        public FeedWithSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChannelSection = itemView.findViewById(R.id.tvChannelSection);
        }

        public void bindData(CombineDataFeed currentFeed) {
            tvChannelSection.setText(currentFeed.getChannel());
        }
    }

    public interface IAdapterCallback{
        void onClickFeedItem(CombineDataFeed feed);
        void onLongClickFeedItem(CombineDataFeed feed);
    }

    public void setCallback(IAdapterCallback callback) {
        this.callback = callback;
    }
}
