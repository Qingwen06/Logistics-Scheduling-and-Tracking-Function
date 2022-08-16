package com.example.driver;

public class Students2 {

    String phone,name,email,purl,status,statuscombine,deliveryman,deliverymanid,latitude,longitude,imageurl,receiverName,receiverPhone,item,weight,date,deliverydate,completedate;

    Students2()
    {

    }

    public Students2( String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliverymanid, String latitude, String longitude, String imageurl, String receiverName, String receiverPhone, String item, String weight, String date, String deliverydate, String completedate) {
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.purl = purl;
        this.status = status;
        this.statuscombine = statuscombine;
        this.deliveryman = deliveryman;
        this.deliverymanid = deliverymanid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageurl = imageurl;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.item = item;
        this.weight = weight;
        this.date = date;
        this.deliverydate = deliverydate;
        this.completedate = completedate;
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

    public String getDeliveryid() { return deliverymanid; }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

    public String getImageurl() { return imageurl; }

    public String getReceiverName() { return receiverName; }

    public String getReceiverPhone() { return receiverPhone; }

    public String getItem() { return item; }

    public String getWeight() { return weight; }

    public String getDate() { return date; }

    public String getDeliverydate() { return deliverydate; }

    public String getCompletedate() { return completedate; }

}
