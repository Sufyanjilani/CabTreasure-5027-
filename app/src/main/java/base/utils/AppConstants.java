package base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import base.activities.Activity_Splash;

public class AppConstants {

    private static AppConstants appConstants = null;
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private String tokenKey = "token";
    private AppConstants( ){

    }

    public static AppConstants getAppConstants( ){
        if(appConstants == null){
            appConstants = new AppConstants();
        }
        return appConstants;
    }

    public void applicationContext(Context context){
        try{
            this.context = context;
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            edit = sp.edit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void putToken(String token){
        try{
            edit.putString(tokenKey,token);
            edit.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getToken(){
        try{
            return  sp.getString(tokenKey, "");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public void removeToken(){
        try{
            edit.remove(tokenKey).commit();
        }catch (Exception e){
            e.printStackTrace();

        }
    }



}
