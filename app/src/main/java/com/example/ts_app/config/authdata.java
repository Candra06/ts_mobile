package com.example.ts_app.config;

import android.content.Context;
import android.content.SharedPreferences;

public class authdata {
    private static authdata mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String auth = "auth_key";
    private static final String kd_user = "kd_akses";
    private static final String level = "level";
    private static final String exp_date = "exp_date";
    private static final String nama = "nama";
    private static final String kd_outlet = "kd_outlet";

    public boolean setdatauser(String xkode_auth, String xkode_user,String xnama_user,String xlevel, String xexp_data, String xkd_outlet){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(auth, xkode_auth);
        editor.putString(kd_user, xkode_user);
        editor.putString(level, xlevel);
        editor.putString(exp_date, xexp_data);
        editor.putString(nama, xnama_user);
        editor.putString(kd_outlet, xkd_outlet);

        editor.apply();

        return true;
    }

    private authdata(Context context){ mCtx = context;}
    public static synchronized authdata getInstance(Context context){
        if (mInstance == null){
            mInstance = new authdata(context);
        }
        return mInstance;
    }

    public String getAuth(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(auth, null);
    }

    public String getKd_user(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(kd_user, null);
    }

    public String getKd_outlet(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(kd_outlet, null);
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(kd_user, null) != null) {
            return true;
        }
        return false;
    }

    public String getLevel(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(level, null);
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

}
