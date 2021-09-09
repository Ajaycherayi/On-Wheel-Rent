package com.ssmptc.onwheelrent.Database;

public class TeamData {

    String name,email,phoneNumber,insta,imgUrl,descTitle,desc;

    public TeamData() {

    }

    public TeamData(String name,String descTitle,String desc, String email, String phoneNumber,String insta, String imgUrl) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.insta = insta;
        this.imgUrl = imgUrl;
        this.descTitle = descTitle;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescTitle() {
        return descTitle;
    }

    public String getInsta() {
        return insta;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInsta(String insta) {
        this.insta = insta;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDescTitle(String descTitle) {
        this.descTitle = descTitle;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
