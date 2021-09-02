package com.ssmptc.onwheelrent.Database;

public class VehicleData {
    String id,vehicleNumber,vehicleName,category,amount,ownerName,phone,place,imgUrl;

    public VehicleData() {
    }

    public VehicleData(String id, String vehicleNumber, String vehicleName,String category, String amount, String ownerName, String phone, String place, String imgUrl) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.vehicleName = vehicleName;
        this.amount = amount;
        this.ownerName = ownerName;
        this.phone = phone;
        this.place = place;
        this.imgUrl = imgUrl;
        this.category = category;

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vNumber) {
        this.vehicleNumber = vNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vName) {
        this.vehicleName = vName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
