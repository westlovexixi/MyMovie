package com.example.mymovie.model;

public class MemoirList {
    private String post_path, title, releaseDate,watchDate, code, comment, rate;

    public MemoirList(String post_path, String title, String releaseDate, String watchDate, String code, String comment, String rate) {
        this.post_path = post_path;
        this.title = title;
        this.releaseDate = releaseDate;
        this.watchDate = watchDate;
        this.code = code;
        this.comment = comment;
        this.rate = rate;
    }

    public String getPost_path() {
        return post_path;
    }

    public void setPost_path(String post_path) {
        this.post_path = post_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
