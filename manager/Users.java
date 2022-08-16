package com.example.manager;

public class Users {

    String usersID;
    String FullName,PhoneNumber,UserEmail;

    Users()
    {

    }

    public Users(String FullName, String PhoneNumber, String UserEmail) {
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
