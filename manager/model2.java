package com.example.manager;

public class model2
{
    String name,email,phone,status,purl,deliveryid,reason,receiverName,receiverIC,imageurl,receiverPhone;
    String item,weight,date,deliverydate,completedate,canceldate;

    model2()
    {

    }

    public model2(String name, String email, String phone, String status, String purl, String deliveryid, String reason, String receiverName, String receiverIC, String imageurl, String receiverPhone, String item, String weight, String date, String deliverydate, String completedate, String canceldate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.purl = purl;
        this.deliveryid = deliveryid;
        this.reason = reason;
        this.receiverName = receiverName;
        this.receiverIC = receiverIC;
        this.imageurl = imageurl;
        this.receiverPhone = receiverPhone;
        this.item = item;
        this.weight = weight;
        this.date = date;
        this.deliverydate = deliverydate;
        this.completedate = completedate;
        this.canceldate = canceldate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDeliveryid() {
        return deliveryid;
    }

    public void setDeliveryid(String deliveryid) { this.deliveryid = deliveryid; }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) { this.reason = reason; }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverIC() {
        return receiverIC;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getReceiverPhone() { return receiverPhone; }

    public String getItem() { return item; }

    public String getWeight() { return weight; }

    public String getDate() { return date; }

    public String getDeliverydate() { return deliverydate; }

    public String getCompletedate() { return completedate; }

    public String getCanceldate() { return canceldate; }

}