package com.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.model.confirmclaim.MdModel;
import com.model.confirmclaim.Model;

public class SessionManager {

    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String intro = "intro";
    public static String login = "login";
    public static String isopen = "isopen";
    public static String user = "modellist";
    public static String dcharge = "dcharge";
    public static String address = "address";


    public static String currency = "currency";
    public static String pincode = "pincode";
    public static String pincoded = "pincoded";
    public static String coupon = "coupon";
    public static String couponid = "couponid";
    public static String storename = "storename";
    public static String storeid = "storeid";


    public static String terms = "terms";
    public static String contact = "contact";
    public static String about = "about";
    public static String policy = "policy";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setFloatData(String key, float val) {
        mEditor.putFloat(key, val);
        mEditor.commit();
    }

    public float getFloatData(String key) {
        return mPrefs.getFloat(key, 0);
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setModel(MdModel val) {
        mEditor.putString(user, new Gson().toJson(val));
        mEditor.commit();
    }

    public MdModel getModel() {
        return new Gson().fromJson(mPrefs.getString(user, ""), MdModel.class);
    }

    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
