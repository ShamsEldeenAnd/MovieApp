package com.example.android.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.MovieAdapter;
import com.example.android.movieapp.Model.Movie;
import com.example.android.movieapp.Utiles.JsonUtiles;
import com.example.android.movieapp.Utiles.NetworkUtiles;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Poster extends AppCompatActivity implements MovieAdapter.OnClickItemListner {

    private ProgressBar progressBar;
    private RecyclerView mMovielist;
    private List<Movie> movies;
    private RecyclerView.LayoutManager manager;
    private MovieAdapter mMovieAdapter;
    private String defaultQuery = "popular";
    private String savedQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        progressBar = findViewById(R.id.progress_bar);
        mMovielist = findViewById(R.id.movie_list);

        if (savedInstanceState != null) {
            savedQuery = savedInstanceState.getString("query");
        }
        //init recycler view
        manager = new GridLayoutManager(this, 2);
        mMovielist.setHasFixedSize(true);
        mMovielist.setLayoutManager(manager);
        if (NetworkUtiles.CheckNetwork(this)) {
            if (savedQuery == null)
                LoadMovies(defaultQuery);
            else
                LoadMovies(savedQuery);
        } else
            closeError();
    }

    private void LoadMovies(String query) {
        URL url = NetworkUtiles.buildSortUrl(query);
        new GetMovieResults().execute(url);
    }

    //error message
    private void closeError() {
        finish();
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void OnClickItem(int index) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("Movie", movies.get(index));
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("query", savedQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (NetworkUtiles.CheckNetwork(this)) {
            int selection = item.getItemId();
            if (selection == R.id.sort_action_pop) {
                savedQuery = "popular";
                LoadMovies(savedQuery);
            } else if (selection == R.id.sort_action_pop_avg) {
                savedQuery= "top_rated";
                LoadMovies(savedQuery);
            } else if (selection == R.id.my_favourites) {
                Intent fav_page = new Intent(Poster.this, FavouritesActivity.class);
                startActivity(fav_page);
            }
        } else {
            closeError();
        }
        return super.onOptionsItemSelected(item);
    }

    class GetMovieResults extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String results = null;
            try {
                results = NetworkUtiles.getResponseFromHttpUrl(url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String json) {
            progressBar.setVisibility(View.INVISIBLE);
            if (json != null && !json.isEmpty()) {
                movies = JsonUtiles.parseMovieJson(json);
                mMovieAdapter = new MovieAdapter(movies, Poster.this);
                mMovielist.setAdapter(mMovieAdapter);
            }
        }
    }


}
