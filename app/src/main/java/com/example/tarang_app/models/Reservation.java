package com.example.tarang_app.models;

import com.google.gson.annotations.SerializedName;

public class Reservation {
    private int id;
    private venue myvenue;
    private String date;
    private String start_time;
    private String end_time;

    public venue getMyvenue() {
        return myvenue;
    }

    public void setMyvenue(venue myvenue) {
        this.myvenue = myvenue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

}

