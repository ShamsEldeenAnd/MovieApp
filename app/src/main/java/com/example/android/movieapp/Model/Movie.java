package com.example.android.movieapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String original_title;
    private String overview;
    private String vote_average;
    private String release_date;
    private String image_url;
    private String id;

    public Movie(String original_title, String overview, String vote_average, String release_date, String image_url, String id) {
        this.original_title = original_title;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.image_url = image_url;
        this.id = id;
    }

    protected Movie(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        image_url = in.readString();
        id = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeString(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(image_url);
        parcel.writeString(id);
    }
}
