package com.example.mymovie.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymovie.entity.Movie;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();

    @Query(("SELECT * FROM Movie WHERE ID = :movieid LIMIT 1"))
    Movie findByID(String movieid);

    @Insert
    long insert(Movie movie);

    @Delete
    void delete(Movie movie);


    @Update(onConflict = REPLACE)
    void updateMovie(Movie... movies);
}
