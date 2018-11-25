package com.example.android.movieapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.movieapp.DataBase.AppDatabase;
import com.example.android.movieapp.DataBase.MovieEntry;

public class MovieByidModel extends ViewModel {
    private LiveData<MovieEntry> movieEntryLiveData;

    public MovieByidModel(AppDatabase db, String movie_id) {
        this.movieEntryLiveData = db.taskDao().loadMoviebyid(movie_id);
    }

    public LiveData<MovieEntry> getMovieEntryLiveData() {
        return movieEntryLiveData;
    }


}
