package com.example.mymovie.model;

public class WatchList {
    private String wTitle;
    private String wReleaseDate;
    private String wAddDate;

    public WatchList(String wTitle, String wReleaseDate, String wAddDate){
        this.wTitle = wTitle;
        this.wReleaseDate = wReleaseDate;
        this.wAddDate = wAddDate;
    }

    public String getwTitle(){
        return wTitle;
    }

    public String getwReleaseDate(){
        return wReleaseDate;
    }

    public String getwAddDate(){
        return wAddDate;
    }
}
