package com.example.android.movieapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.ReviewAdapter;
import com.example.android.movieapp.Utiles.JsonUtiles;
import com.example.android.movieapp.Utiles.NetworkUtiles;
import com.example.android.movieapp.Model.Review;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private String id;
    private List<Review> reviews;
    private ReviewAdapter reviewAdapter;

    private RecyclerView.LayoutManager manager;
    private RecyclerView reviewList;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        setTitle("Reviews");
        reviewList = findViewById(R.id.review_list);
        progressBar = findViewById(R.id.progress_bar);

        initRecyclerView();

        Intent intent = getIntent();
        if (NetworkUtiles.CheckNetwork(this)) {
            if (intent != null) {
                if (intent.hasExtra("id")) {
                    id = intent.getStringExtra("id");
                    loadReviews();
                } else {
                    closeError();
                }

            } else {
                closeError();
            }
        } else
            closeError();

    }

    private void initRecyclerView() {
        reviewList.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        reviewList.setLayoutManager(manager);

    }

    private void loadReviews() {
        URL reviewUrl = NetworkUtiles.buildSortUrl(id + "/reviews");
        new GetReviews().execute(reviewUrl);
    }


    //error message
    private void closeError() {
        finish();
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
    }


    class GetReviews extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

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
            progressBar.setVisibility(View.INVISIBLE);
            if (result != null && !result.isEmpty()) {
                reviews = JsonUtiles.parseReviewJson(result);
                reviewAdapter = new ReviewAdapter(reviews);
                reviewList.setAdapter(reviewAdapter);
            }
        }
    }
}
