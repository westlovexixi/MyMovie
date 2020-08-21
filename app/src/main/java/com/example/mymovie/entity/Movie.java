package com.example.mymovie.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "ID")
    public String movieID;

    @ColumnInfo(name = "Title")
    public String movieTitle;

    @ColumnInfo(name = "Release Date")
    public String releaseDate;

    @ColumnInfo(name = "Add Date")
    public String addDate;

    public Movie(String movieID, String movieTitle, String releaseDate, String addDate){
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.addDate = addDate;
    }

    public void setMovieID(String movieID){
        this.movieID = movieID;
    }

    public String getMovieID(){
        return movieID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setAddDate(String addDate){
        this.addDate = addDate;
    }
    public String getAddDate(){
        return addDate;
    }
}
