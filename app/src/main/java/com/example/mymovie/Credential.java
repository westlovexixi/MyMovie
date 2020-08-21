package com.example.mymovie;

import java.sql.Date;

public class Credential {
    private int creid;
    private String username;
    private String passwordhash;
    private String signupdate;
    private Person pid;

    public Credential(int creid, String username, String passwordhash, String signupdate) {
        this.creid = creid;
        this.username = username;
        this.passwordhash = passwordhash;
        this.signupdate = signupdate;
    }

    public void setPid(int id){
        pid =new Person(id);
    }

    public int getPid(){
        return pid.getPid();
    }

    public int getCreid() {
        return creid;
    }

    public void setCreid(int creid) {
        this.creid = creid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(String signupdate) {
        this.signupdate = signupdate;
    }
}
