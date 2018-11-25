package com.example.android.movieapp.Utiles;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.example.android.movieapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtiles {

    private static final String STATIC_Movie_URL = "https://api.themoviedb.org/3/movie/";
    private static final String STATIC_Youtube_URL = "https://www.youtube.com/watch";
    private static final String API_KEY_PARAM = "api_key";
    private static final String Youtube_KEY_PARAM = "v";

    private static final String API_KEY_Query = BuildConfig.API_KEY;


    //getting sorting url
    public static URL buildSortUrl(String sortQuery) {
        Uri builtUri = Uri.parse(STATIC_Movie_URL + sortQuery).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY_Query)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildYoutubeUrl(String key) {
        Uri builtUri = Uri.parse(STATIC_Youtube_URL).buildUpon()
                .appendQueryParameter(Youtube_KEY_PARAM, key)
                .build();

        return builtUri;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //check  the connection
    public static boolean CheckNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}