package com.example.mymovie.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymovie.entity.Movie;
import com.example.mymovie.repository.WatchlistRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private WatchlistRepository wRepository;
    private MutableLiveData<List<Movie>> allMovies;

    public MovieViewModel(){
        allMovies = new MutableLiveData<>();
    }

    public void initalizeVars(Application application){
        wRepository = new WatchlistRepository(application);
    }

    public void setMovies(List<Movie> movies){
        allMovies.setValue(movies);
    }

    public LiveData<List<Movie>> getAllMovies(){
        return wRepository.getAllWatchlist();
    }

    public void insert(Movie movie){
        wRepository.insert(movie);
    }

    public void delete(Movie movie){
        wRepository.delete(movie);
    }

    public void  updateMovies(Movie... movies){
        wRepository.updateMovie(movies);
    }

    public Movie findById(String id){
        return wRepository.findByID(id);
    }
}
