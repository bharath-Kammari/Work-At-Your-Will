package com.example.workatyourwill2;

import java.util.Date;

public class RequestModel {
    String requestFrom;
    String requestTo;
    String serviceId;
    Date date;
    String s_name;
    String s_cost;
    String s_desc;
    String s_rating;
    String bookings_month;
    String business_name;
    boolean accepted;

    public String getRequestFrom() {
        return requestFrom;
    }

    public String getRequestTo() {
        return requestTo;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Date getDate() {
        return date;
    }

    public String getS_name() {
        return s_name;
    }

    public String getS_cost() {
        return s_cost;
    }

    public String getS_desc() {
        return s_desc;
    }

    public String getS_rating() {
        return s_rating;
    }

    public String getBookings_month() {
        return bookings_month;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public boolean isAccepted() {
        return accepted;
    }
    public RequestModel(){

    }
    public RequestModel(String requestFrom, String requestTo, String serviceId, Date date,  boolean accepted,String s_name, String s_cost, String s_desc, String s_rating, String bookings_month, String business_name) {
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.serviceId = serviceId;
        this.date = date;
        this.s_name = s_name;
        this.s_cost = s_cost;
        this.s_desc = s_desc;
        this.s_rating = s_rating;
        this.bookings_month = bookings_month;
        this.business_name = business_name;
        this.accepted = accepted;
    }
}
