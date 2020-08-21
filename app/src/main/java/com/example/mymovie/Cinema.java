package com.example.mymovie;

public class Cinema {
    private int cid;
    private String cname;
    private String cpostcode;

    public Cinema(Integer id, String name, String code){
        cid = id;
        cname = name;
        cpostcode = code;
    }

    public Cinema(int cid){
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public void setPid(int pid) {
        this.cid = pid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCpostcode() {
        return cpostcode;
    }

    public void setCpostcode(String cpostcode) {
        this.cpostcode = cpostcode;
    }
}

