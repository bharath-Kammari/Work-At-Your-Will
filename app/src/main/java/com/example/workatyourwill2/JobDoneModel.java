package com.example.workatyourwill2;

import java.util.Date;

public class JobDoneModel {
    boolean accepted;
    String bookings_month;
    String business_name;
    Date date;
    String estimateDelivery;
    String orderStatus;
    String requestFrom;
    String requestTo;
    String s_cost;
    String s_desc;
    String s_name;
    String s_rating;
    String serviceId;
    Date delivered_on;
    public JobDoneModel(){

    }

    public JobDoneModel(boolean accepted, String bookings_month, String business_name, Date date, String estimateDelivery, String orderStatus, String requestFrom, String requestTo, String s_cost, String s_desc, String s_name, String s_rating, String serviceId, Date delivered_on) {
        this.accepted = accepted;
        this.bookings_month = bookings_month;
        this.business_name = business_name;
        this.date = date;
        this.estimateDelivery = estimateDelivery;
        this.orderStatus = orderStatus;
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.s_cost = s_cost;
        this.s_desc = s_desc;
        this.s_name = s_name;
        this.s_rating = s_rating;
        this.serviceId = serviceId;
        this.delivered_on = delivered_on;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getBookings_month() {
        return bookings_month;
    }

    public void setBookings_month(String bookings_month) {
        this.bookings_month = bookings_month;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public String getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(String requestTo) {
        this.requestTo = requestTo;
    }

    public String getS_cost() {
        return s_cost;
    }

    public void setS_cost(String s_cost) {
        this.s_cost = s_cost;
    }

    public String getS_desc() {
        return s_desc;
    }

    public void setS_desc(String s_desc) {
        this.s_desc = s_desc;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_rating() {
        return s_rating;
    }

    public void setS_rating(String s_rating) {
        this.s_rating = s_rating;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDelivered_on() {
        return delivered_on;
    }

    public void setDelivered_on(Date delivered_on) {
        this.delivered_on = delivered_on;
    }
}
