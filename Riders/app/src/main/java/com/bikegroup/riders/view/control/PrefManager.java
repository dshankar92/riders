package com.bikegroup.riders.view.control;

import android.content.Context;
import android.content.SharedPreferences;

import com.bikegroup.riders.view.appController.AppController;


/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "sidekick";

    /* app is launched for the first time*/
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager() {

        pref = AppController.getInstance().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    /*get string value from pref*/
    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    /*save strinf value in pref*/
    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }
}
