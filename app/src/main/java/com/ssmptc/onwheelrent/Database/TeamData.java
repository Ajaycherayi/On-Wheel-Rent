package com.ssmptc.onwheelrent.Database;

public class TeamData {

    String name,email,phoneNumber,insta,imgUrl;

    public TeamData() {

    }

    public TeamData(String name, String email, String phoneNumber,String insta, String imgUrl) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.insta = insta;
        this.imgUrl = imgUrl;
    }

    public String getInsta() {
        return insta;
    }

    public void setInsta(String insta) {
        this.insta = insta;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
