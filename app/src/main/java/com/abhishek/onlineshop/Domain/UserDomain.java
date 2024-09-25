package com.abhishek.onlineshop.Domain;

public class UserDomain {
    String name,email,password, mobileNumber;
    public UserDomain(){

    }

    public UserDomain(String name, String email, String password, String mob_number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNumber = mob_number;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
