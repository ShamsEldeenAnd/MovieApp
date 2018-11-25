package com.example.android.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.TrailerAdapter;
import com.example.android.movieapp.DataBase.AppDatabase;
import com.example.android.movieapp.DataBase.MovieEntry;
import com.example.android.movieapp.Executers.AppExecutors;
import com.example.android.movieapp.Model.Movie;

import com.example.android.movieapp.Model.Trailer;
import com.example.android.movieapp.Utiles.JsonUtiles;
import com.example.android.movieapp.Utiles.NetworkUtiles;
import com.example.android.movieapp.ViewModel.MovieByidFactory;
import com.example.android.movieapp.ViewModel.MovieByidModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import java.util.List;

public class MovieDetails extends AppCompatActivity implements TrailerAdapter.OnClickItemListner {

    private TextView original_title_tv, overview_tv, rating_tv, release_date_tv, trailer_tv;
    private ImageView movieImg;
    private Button fav_btn;

    private static final String img_url_w500 = "http://image.tmdb.org/t/p/w185/";
    private Movie movie;

    private RecyclerView trailerList;

    private RecyclerView.LayoutManager manager;
    private TrailerAdapter trailerAdapter;

    private List<Trailer> trailers;


    //data base
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setTitle("MovieDetail");

        //init db
        mDb = AppDatabase.getInstance(getApplicationContext());

        // recovering the instance state
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable("Movie");
        }

        original_title_tv = findViewById(R.id.org_title);
        overview_tv = findViewById(R.id.overview);
        rating_tv = findViewById(R.id.vote_average);
        release_date_tv = findViewById(R.id.release_date);
        movieImg = findViewById(R.id.movie_img);
        trailer_tv = findViewById(R.id.trailers);
        trailerList = findViewById(R.id.trailer_list);
        fav_btn = findViewById(R.id.fav_btn);

        initRecyclerView();


        Intent intent = getIntent();
        if (NetworkUtiles.CheckNetwork(this)) {
            if (intent != null) {
                if (intent.hasExtra("Movie")) {
                    movie = intent.getParcelableExtra("Movie");
                    loadTrailers();
                    populateUI();
                    favouritesCheck();
                } else {
                    closeError();
                }

            } else {
                closeError();
            }
        } else
            closeError();


    }

    private void favouritesCheck() {
        MovieByidFactory factory = new MovieByidFactory(mDb, movie.getId());
        MovieByidModel viewModel = ViewModelProviders.of(this, factory).get(MovieByidModel.class);
        viewModel.getMovieEntryLiveData().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable MovieEntry entry) {
                if (entry != null) {
                    btnChangeState();
                }
            }
        });
    }


    //handle the ui of the btn when clicked
    private void btnChangeState() {
        fav_btn.setEnabled(false);
        fav_btn.setText("Favourites");
        fav_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
    }

    private void initRecyclerView() {
        trailerList.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        trailerList.setLayoutManager(manager);

    }

    //error message
    private void closeError() {
        finish();
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }


    private void populateUI() {
        original_title_tv.setText(movie.getOriginal_title());
        release_date_tv.setText(movie.getRelease_date());
        rating_tv.setText(movie.getVote_average());
        overview_tv.setText(movie.getOverview());
        Picasso.get()
                .load(img_url_w500 + movie.getImage_url())
                .into(movieImg);
    }


    private void loadTrailers() {
        URL trailerUrl = NetworkUtiles.buildSortUrl(movie.getId() + "/videos");
        new GetTrailers().execute(trailerUrl);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        movie = savedInstanceState.getParcelable("Movie");
        populateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("Movie", movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void OnClickItem(int index) {
        openYoutube(trailers.get(index).getKey());
    }

    public void openYoutube(String key) {
        Uri youtube_page = NetworkUtiles.buildYoutubeUrl(key);
        Intent intent = new Intent(Intent.ACTION_VIEW, youtube_page);

        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_app));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selection = item.getItemId();
        if (selection == R.id.review_page) {
            Intent review = new Intent(this, ReviewActivity.class);
            review.putExtra("id", movie.getId());
            startActivity(review);
        }
        return super.onOptionsItemSelected(item);
    }

    //operation save movie
    public void saveMovie(View view) {

        final MovieEntry entry = new MovieEntry(movie.getOriginal_title(), movie.getId());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertMovie(entry);

            }
        });
    }

    class GetTrailers extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;

            try {
                result = NetworkUtiles.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.isEmpty()) {
                trailers = JsonUtiles.parseTrailersJson(result);
                trailerAdapter = new TrailerAdapter(trailers, MovieDetails.this);
                trailerList.setAdapter(trailerAdapter);
            }
        }
    }


}
