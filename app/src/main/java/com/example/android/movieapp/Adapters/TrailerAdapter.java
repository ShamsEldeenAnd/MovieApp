package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.Model.Trailer;

import com.example.android.movieapp.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;

    private OnClickItemListner monClickItemListner;


    public interface OnClickItemListner {
        void OnClickItem(int index);
    }


    public TrailerAdapter(List<Trailer> trailers, OnClickItemListner monClickItemListner) {
        this.trailers = trailers;
        this.monClickItemListner = monClickItemListner;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_row, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.name.setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            monClickItemListner.OnClickItem(position);
        }
    }
}
