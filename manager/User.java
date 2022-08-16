package com.example.manager;

public class User {

    String FullName,PhoneNumber,UserEmail;

    User()
    {

    }

    public User(String FullName, String PhoneNumber, String UserEmail) {
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;
        this.UserEmail = UserEmail;
    }

    public String getFullName() { return FullName; }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getUserEmail() { return UserEmail; }


}
