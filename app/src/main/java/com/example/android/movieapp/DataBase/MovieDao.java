package com.example.android.movieapp.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("select * from movie")
    LiveData<List<MovieEntry>> loadAllmovies();

    @Insert
    void insertMovie(MovieEntry entry);

    @Delete
    void deleteMovie(MovieEntry taskEntry);

    @Query("SELECT * FROM movie where movie_id=:id")
    LiveData<MovieEntry> loadMoviebyid(String id);
}
