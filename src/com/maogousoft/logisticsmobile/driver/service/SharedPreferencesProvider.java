package com.maogousoft.logisticsmobile.driver.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.maogousoft.logisticsmobile.driver.Constants;

/**
 * Created by aliang on 2015/4/14.
 */
public class SharedPreferencesProvider {

    private static SharedPreferencesProvider sharedPreferencesProvider;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String sp_name = "configs";

    private SharedPreferencesProvider (){}

    public static SharedPreferencesProvider getInstance(Context context) {
        if(sharedPreferencesProvider == null) {
            sharedPreferencesProvider = new SharedPreferencesProvider();
            sharedPreferences = context.getSharedPreferences(sp_name, 0);
            editor = sharedPreferences.edit();
        }
        return sharedPreferencesProvider;
    }

    public void saveOldPhoneState(int state) {
        editor.putInt(Constants.PREFERENCE_NAME_PHONE_STATE, state);
        editor.commit();
    }

    public int getOldPhoneState() {
        return sharedPreferences.getInt(Constants.PREFERENCE_NAME_PHONE_STATE, -1);
    }
}
