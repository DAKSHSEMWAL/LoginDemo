package com.daksh.kuro.logindemo.User;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String Email;
    private String Photo;
    private String Gender;
    private String Loyality_point;
    private String DOB;


    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public User(String name, String password, String phone, String email, String photo, String gender,String dob) {
        Name = name;
        Password = password;
        Phone = phone;
        Email = email;
        Gender = gender;
        Photo = photo;
        DOB = dob;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public User() {

    }

    public String getName() {

        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLoyality_point() {
        return Loyality_point;
    }

    public void setLoyality_point(String loyality_point) {
        Loyality_point = "0";
    }
    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }
}
