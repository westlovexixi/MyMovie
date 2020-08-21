package com.example.mymovie;


import java.sql.Date;

public class Person {
    private int pid;
    private String pfname;
    private String plname;
    private String pgender;
    private String pdob;
    private String pstreetno;
    private String pstreetname;
    private String pstate;
    private String ppostcode;

    public Person(int pid){
        this.pid = pid;
    }

    public Person(int pid, String pfname, String plname, String pgender, String pdob, String pstreetno, String pstreetname, String pstate, String ppostcode) {
        this.pid = pid;
        this.pfname = pfname;
        this.plname = plname;
        this.pgender = pgender;
        this.pdob = pdob;
        this.pstreetno = pstreetno;
        this.pstreetname = pstreetname;
        this.pstate = pstate;
        this.ppostcode = ppostcode;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPfname() {
        return pfname;
    }

    public void setPfname(String pfname) {
        this.pfname = pfname;
    }

    public String getPlname() {
        return plname;
    }

    public void setPlname(String plname) {
        this.plname = plname;
    }

    public String getPgender() {
        return pgender;
    }

    public void setPgender(String pgender) {
        this.pgender = pgender;
    }

    public String getPdob() {
        return pdob;
    }

    public void setPdob(String pdob) {
        this.pdob = pdob;
    }

    public String getPstreetno() {
        return pstreetno;
    }

    public void setPstreetno(String pstreetno) {
        this.pstreetno = pstreetno;
    }

    public String getPstreetname() {
        return pstreetname;
    }

    public void setPstreetname(String pstreetname) {
        this.pstreetname = pstreetname;
    }

    public String getPstate() {
        return pstate;
    }

    public void setPstate(String pstate) {
        this.pstate = pstate;
    }

    public String getPpostcode() {
        return ppostcode;
    }

    public void setPpostcode(String ppostcode) {
        this.ppostcode = ppostcode;
    }
}
