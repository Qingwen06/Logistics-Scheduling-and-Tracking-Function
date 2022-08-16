package com.example.manager;

public class model
{
    String name,email,phone,status,purl,latitude,longitude,key,item,weight,date;
    model()
    {

    }
    public model(String name, String email, String phone, String status, String purl, String latitude, String longitude, String key, String item, String weight, String date) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.purl = purl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
        this.item = item;
        this.weight = weight;
        this.date = date;
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

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getItem() { return item; }

    public void setItem(String item) { this.item = item; }

    public String getWeight() { return weight; }

    public void setWeight(String weight) { this.weight = weight; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

}