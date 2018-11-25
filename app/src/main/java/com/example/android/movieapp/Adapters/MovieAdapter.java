package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieapp.Model.Movie;
import com.example.android.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private List<Movie> movies;
    private OnClickItemListner monClickItemListner;


    public interface OnClickItemListner {
        void OnClickItem(int index);
    }

    public MovieAdapter(List<Movie> movies, OnClickItemListner monClickItemListner) {
        this.movies = movies;
        this.monClickItemListner = monClickItemListner;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_row, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //populate the data on the view
        final String img_url = "http://image.tmdb.org/t/p/w185/";
        Picasso.get()
                .load(img_url + movies.get(position).getImage_url())
                .into(holder.movie_img);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movie_img;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movie_img = itemView.findViewById(R.id.movie_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            monClickItemListner.OnClickItem(position);
        }
    }
}
