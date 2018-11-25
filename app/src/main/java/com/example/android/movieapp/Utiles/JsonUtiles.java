package com.example.android.movieapp.Utiles;

import com.example.android.movieapp.Model.Movie;
import com.example.android.movieapp.Model.Review;
import com.example.android.movieapp.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtiles {

    //return list of movies
    public static List<Movie> parseMovieJson(String json) {
        List<Movie> results = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(json);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject movie = result.getJSONObject(i);
                String original_title = movie.getString("original_title");
                String overview = movie.getString("overview");
                String vote_average = movie.getString("vote_average");
                String release_date = movie.getString("release_date");
                String image_url = movie.getString("poster_path");
                String id = movie.getString("id");
                results.add(new Movie(original_title, overview, vote_average, release_date, image_url, id));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }


    public static List<Trailer> parseTrailersJson(String json) {
        List<Trailer> results = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(json);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject trailer = result.getJSONObject(i);
                String key = trailer.getString("key");
                String name = trailer.getString("name");
                String site = trailer.getString("site");
                results.add(new Trailer(key, name, site));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }


    public static List<Review> parseReviewJson(String json) {
        List<Review> results = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(json);
            JSONArray result = object.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject review = result.getJSONObject(i);
                String author = review.getString("author");
                String content = review.getString("content");

                results.add(new Review(author, content));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}
