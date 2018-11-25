package com.example.android.movieapp.DataBase;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "movie")
public class MovieEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String original_title;
    private String movie_id;


    public MovieEntry(int id, String original_title, String movie_id) {
        this.id = id;
        this.original_title = original_title;
        this.movie_id = movie_id;
    }

    @Ignore
    public MovieEntry(String original_title, String movie_id) {
        this.original_title = original_title;
        this.movie_id = movie_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}
