package com.example.android.movieapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.android.movieapp.Adapters.MovieEntryAdapter;
import com.example.android.movieapp.DataBase.AppDatabase;
import com.example.android.movieapp.DataBase.MovieEntry;
import com.example.android.movieapp.Executers.AppExecutors;
import com.example.android.movieapp.ViewModel.MovieViewModel;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class FavouritesActivity extends AppCompatActivity {

    private RecyclerView favouritesList;
    private RecyclerView.LayoutManager manager;
    private MovieEntryAdapter movieEntryAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        mDb = AppDatabase.getInstance(getApplicationContext());

        setTitle("My Favourites");
        favouritesList = findViewById(R.id.favourites_list);

        initRecyclerView();
        retrieveMovies();

    }

    private void retrieveMovies() {
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getEntryLiveData().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                movieEntryAdapter = new MovieEntryAdapter(movieEntries);
                favouritesList.setAdapter(movieEntryAdapter);
            }
        });
    }

    private void initRecyclerView() {
        favouritesList.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        favouritesList.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        favouritesList.addItemDecoration(decoration);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int pos = viewHolder.getAdapterPosition();
                        List<MovieEntry> entries = movieEntryAdapter.getEntries();
                        mDb.taskDao().deleteMovie(entries.get(pos));
                    }
                });

            }
        }).attachToRecyclerView(favouritesList);
    }
}
