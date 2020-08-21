package com.example.mymovie.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mymovie.dao.MovieDAO;
import com.example.mymovie.database.MovieDatabase;
import com.example.mymovie.entity.Movie;

import java.util.List;

public class WatchlistRepository {

    private MovieDAO dao;
    private LiveData<List<Movie>> allWatchlist;
    private Movie movie;

    public WatchlistRepository(Application application){
        MovieDatabase db = MovieDatabase.getInstance(application);
        dao = db.movieDAO();
    }

    public LiveData<List<Movie>> getAllWatchlist(){
        allWatchlist = dao.getAll();
        return allWatchlist;
    }

    public void  insert(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(movie);
            }
        });
    }

    public void delete(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(movie);
            }
        });
    }

    public void updateMovie(final Movie... movies){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMovie(movies);
            }
        });
    }

    public Movie findByID(final String movieID){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Movie movie = dao.findByID(movieID);
                setMovie(movie);
            }
        });
        return movie;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }

    
}
