package com.example.workatyourwill2;

import java.util.ArrayList;
import java.util.Date;

public class FirebaseModel {
    public Date date_added;
    public String s_name;
    public String s_desc;
    public int s_cost;
    public double s_rating;
    public String worker_id;
    public int s_bookings_per_month;
    public ArrayList<Review> s_reviews;

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }
    public FirebaseModel(){

    }
    public FirebaseModel(Date date_added, String s_name, String s_desc, int s_cost, String worker_id) {
        this.date_added=date_added;
        this.s_name = s_name;
        this.s_desc = s_desc;
        this.s_cost = s_cost;

        this.worker_id = worker_id;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_desc() {
        return s_desc;
    }

    public void setS_desc(String s_desc) {
        this.s_desc = s_desc;
    }

    public double getS_cost() {
        return s_cost;
    }

    public void setS_cost(int s_cost) {
        this.s_cost = s_cost;
    }

    public double getS_rating() {
        return s_rating;
    }

    public void setS_rating(double s_rating) {
        this.s_rating = s_rating;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public int getS_bookings_per_month() {
        return s_bookings_per_month;
    }

    public void setS_bookings_per_month(int s_bookings_per_month) {
        this.s_bookings_per_month = s_bookings_per_month;
    }

    public ArrayList<Review> getS_reviews() {
        return s_reviews;
    }

    public void setS_reviews(ArrayList<Review> s_reviews) {
        this.s_reviews = s_reviews;
    }
}
