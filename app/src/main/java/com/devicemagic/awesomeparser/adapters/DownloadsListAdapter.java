package com.devicemagic.awesomeparser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devicemagic.awesomeparser.R;
import com.devicemagic.awesomeparser.models.Download;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadsListAdapter extends RecyclerView.Adapter<DownloadsListAdapter.ViewHolder> {

    private ArrayList<Download> itemList;

    public DownloadsListAdapter(ArrayList<Download> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (itemList.get(position) != null && itemList.get(position).getItem() != null) {
            holder.titleTextView.setText(itemList.get(position).getItem().getValue());
        } else {
            holder.titleTextView.setText(null);
        }
    }

    public void addItem(Download item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public void reset() {
        itemList = new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

