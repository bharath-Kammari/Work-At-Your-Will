package com.example.workatyourwill2;

import java.util.Date;

public class Review {
    double rating;
    Date date;
    String user_id;

    public Review(double rating, Date date, String user_id) {
        this.rating = rating;
        this.date = date;
        this.user_id = user_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
