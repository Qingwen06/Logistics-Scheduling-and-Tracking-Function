package com.example.driver;

public class Students {

    String phone,name,email,purl,status,statuscombine,deliveryman,deliveryid,latitude,longitude,key,item,weight,date,deliverydate;

    Students()
    {

    }

    public Students( String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliveryid, String latitude, String longitude, String key, String item, String weight, String date, String deliverydate) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.purl = purl;
        this.status = status;
        this.statuscombine = statuscombine;
        this.deliveryman = deliveryman;
        this.deliveryid = deliveryid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
        this.item = item;
        this.weight = weight;
        this.date = date;
        this.deliverydate = deliverydate;
    }

    public String getPhone() { return phone; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPurl() {
        return purl;
    }

    public String getStatus() {
        return status;
    }

    public String getStatuscombine() { return statuscombine; }

    public String getDeliveryman() { return deliveryman; }

    public String getDeliveryid() { return deliveryid; }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

    public String getKey() { return key; }

    public String getItem() { return item; }

    public String getWeight() { return weight; }

    public String getDate() { return date; }

    public String getDeliverydate() { return deliverydate; }

}
