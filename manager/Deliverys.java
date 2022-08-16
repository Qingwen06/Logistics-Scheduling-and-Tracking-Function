package com.example.manager;

public class Deliverys {

    double latitude, longitude;

    Deliverys(){

    }

    Deliverys(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

}
