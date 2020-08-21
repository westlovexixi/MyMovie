package com.example.mymovie.model;

public class ListResult {
    private String id, post_path, title, release_date;

    public ListResult(String id, String post_path, String title, String release_date){
        this.id = id;
        this.post_path = post_path;
        this.title = title;
        this.release_date = release_date;
    }

    public String getId(){
        return id;
    }

    public String getPost_path(){
        return post_path;
    }

    public String getTitle(){
        return title;
    }

    public String getRelease_date(){
        return release_date;
    }




}
