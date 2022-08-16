package com.example.driver;

public class Studentscus {

    String phone,name,email,purl,status,statuscombine,deliveryman,deliveryid,latitude,longitude,key;
    String receiverIC, receiverName, receiverPhone, imageurl, reason;

    Studentscus()
    {

    }

    public Studentscus( String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliveryid, String latitude, String longitude, String key, String receiverIC, String receiverName, String receiverPhone, String imageurl, String reason) {
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
        this.receiverIC = receiverIC;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.imageurl = imageurl;
        this.reason = reason;
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

    public String getReceiverIC() { return receiverIC; }

    public String getReceiverName() { return receiverName; }

    public String getReceiverPhone() { return receiverPhone; }

    public String getImageurl() { return imageurl; }

    public String getReason() { return reason; }

}
