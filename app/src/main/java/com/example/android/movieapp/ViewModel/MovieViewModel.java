package com.example.android.movieapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.movieapp.DataBase.AppDatabase;
import com.example.android.movieapp.DataBase.MovieEntry;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private LiveData<List<MovieEntry>> entryLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        entryLiveData = AppDatabase.getInstance(this.getApplication()).taskDao().loadAllmovies();
    }

    public LiveData<List<MovieEntry>> getEntryLiveData() {
        return entryLiveData;
    }
}
