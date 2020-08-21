package com.example.mymovie;

import java.sql.Date;
import java.sql.Time;

public class Memoir {
    private int mid;
    private String moviename;
    private String releasedate;
    private String watchtime;
    private String watchdate;
    private String comment;
    private float pstar;
    private Person pid;
    private Cinema cid;

    public Memoir(int mid, String moviename, String releasedate, String watchtime, String watchdate, String comment, float pstar) {
        this.mid = mid;
        this.moviename = moviename;
        this.releasedate = releasedate;
        this.watchtime = watchtime;
        this.watchdate = watchdate;
        this.comment = comment;
        this.pstar = pstar;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWatchtime() {
        return watchtime;
    }

    public void setWatchtime(String watchtime) {
        this.watchtime = watchtime;
    }

    public String getWatchdate() {
        return watchdate;
    }

    public void setWatchdate(String watchdate) {
        this.watchdate = watchdate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getPstar() {
        return pstar;
    }

    public void setPstar(float pstar) {
        this.pstar = pstar;
    }


    public void setPid(int pid){
        this.pid = new Person(pid);
    }

    public int getPid(){
        return pid.getPid();
    }

    public void setCid(int cid){
        this.cid = new Cinema(cid);
    }

    public int getCid(){
        return cid.getCid();
    }
}
