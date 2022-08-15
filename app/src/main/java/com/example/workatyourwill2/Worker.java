package com.example.workatyourwill2;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class Worker {
    private String worker_id;
    private String name;
    private String business_name;
    private String Ph_No;
    private GeoPoint latLng;
    private String profession;


    public Worker(String worker_id,String name, String business_name,String ph_No, GeoPoint latLng, String profession) {
        this.worker_id=worker_id;
        this.name = name;
        this.business_name=business_name;
        Ph_No = ph_No;
        this.latLng = latLng;
        this.profession = profession;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPh_No() {
        return Ph_No;
    }

    public void setPh_No(String ph_No) {
        Ph_No = ph_No;
    }

    public GeoPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(GeoPoint latLng) {
        this.latLng = latLng;
    }

    public String getProfession() {
        return profession;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
