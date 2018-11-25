package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.DataBase.MovieEntry;
import com.example.android.movieapp.R;

import java.util.List;

public class MovieEntryAdapter extends RecyclerView.Adapter<MovieEntryAdapter.MovieEntryAdapterViewHolder> {

    List<MovieEntry> entries;

    public List<MovieEntry> getEntries() {
        return entries;
    }

    public MovieEntryAdapter(List<MovieEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public MovieEntryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favourites_row, parent, false);
        return new MovieEntryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieEntryAdapterViewHolder holder, int position) {
        holder.fav_title.setText(entries.get(position).getOriginal_title());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    class MovieEntryAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView fav_title;

        public MovieEntryAdapterViewHolder(View itemView) {
            super(itemView);
            fav_title = itemView.findViewById(R.id.fav_title);
        }
    }
}
