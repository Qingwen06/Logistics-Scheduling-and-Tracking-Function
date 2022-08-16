package com.example.manager;

public class Students {

    String phone,name,email,purl,status,statuscombine,deliveryman,deliveryid,latitude,longitude;
    String receiverName,receiverPhone,receiverIC,imageurl,reason,item,weight,date,deliverydate,completedate,canceldate;

    Students()
    {

    }

    public Students( String phone, String name, String email, String purl, String status, String statuscombine, String deliveryman, String deliveryid, String latitude, String longitude, String receiverIC, String receiverName, String receiverPhone, String imageurl, String reason, String item, String weight, String date, String deliverydate, String completedate, String canceldate) {
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
        this.receiverIC = receiverIC;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.imageurl = imageurl;
        this.reason = reason;
        this.item = item;
        this.weight = weight;
        this.date = date;
        this.deliverydate = deliverydate;
        this.completedate = completedate;
        this.canceldate = canceldate;
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

    public String getReceiverIC() { return receiverIC; }

    public String getReceiverName() { return receiverName; }

    public String getReceiverPhone() { return receiverPhone; }

    public String getImageurl() { return imageurl; }

    public String getReason() { return reason; }

    public String getItem() { return item; }

    public String getWeight() { return weight; }

    public String getDate() { return date; }

    public String getDeliverydate() { return deliverydate; }

    public String getCompletedate() { return completedate; }

    public String getCanceldate() { return canceldate; }

}
