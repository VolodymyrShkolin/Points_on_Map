package com.bignerdranch.android.points_on_map.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bignerdranch.android.points_on_map.R;
import com.bignerdranch.android.points_on_map.data.PlacesItem;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<PlacesItem> list_;


    public RecyclerViewAdapter(List<PlacesItem> list_)
    {
        this.list_ = list_;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemPlace.setText(list_.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list_.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemPlace;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPlace =itemView.findViewById(R.id.itemPlace);
        }
    }
}
