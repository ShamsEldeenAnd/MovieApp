package com.example.android.movieapp.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.movieapp.DataBase.AppDatabase;

public class MovieByidFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase database;
    private final String movie_id;

    public MovieByidFactory(AppDatabase database, String movie_id) {
        this.database = database;
        this.movie_id = movie_id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MovieByidModel(database, movie_id);
    }
}
