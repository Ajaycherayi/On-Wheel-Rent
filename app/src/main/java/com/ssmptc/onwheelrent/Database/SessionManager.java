package com.ssmptc.onwheelrent.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Initialize Variables
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("CustomerAppKey",0);
        editor = sharedPreferences.edit();
        editor.apply();
    }


    // Set Login
    public void setUserLogin(boolean login){
        editor.putBoolean("KEY_LOGIN",login);
        editor.commit();
    }

    public boolean getUserLogin(){

        return sharedPreferences.getBoolean("KEY_LOGIN",false);
    }
    public void setDetails(String phoneNo,String password){
        editor.putString("KEY_PHONE",phoneNo);
        editor.putString("KEY_PASSWORD",password);

        editor.commit();

    }


    public String getPhone(){
        return sharedPreferences.getString("KEY_PHONE","");
    }




    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

}
