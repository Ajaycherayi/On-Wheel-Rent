package com.ssmptc.onwheelrent.Database;

import com.google.firebase.database.Exclude;

public class VehicleImageUrl {

    private String ImageUrl;
    private String mKey;
    public VehicleImageUrl(){

    }
    public VehicleImageUrl(String imageUrl){
        this.ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    @Exclude
    public String getKey(){
        return mKey;
    }
    @Exclude
    public void setKey(String key){
        mKey = key;
    }

}
