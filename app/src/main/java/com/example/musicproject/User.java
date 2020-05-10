package com.example.musicproject;

public class User {

    String Email,Password,UserName;

    String Phone;

    String UserId;


    //Default constructor
    public User(){

    }


    public User( String ID,String email, String password, String username, String phone){

        this.UserId = ID;
        this.Email = email;
        this.Password = password;
        this.UserName = username;
        this.Phone = phone;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
