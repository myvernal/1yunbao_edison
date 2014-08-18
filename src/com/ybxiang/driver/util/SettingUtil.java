package com.ybxiang.driver.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aliang on 2014/8/19.
 */
public class SettingUtil {

    private static final String TAG = "SettingUtil";
    private static final String FILE_NAME = "MAIN_LINE_NOTIFY";
    public static final String MAIN_LINE_1 = "MAIN_LINE_1";
    public static final String MAIN_LINE_2 = "MAIN_LINE_2";
    public static final String MAIN_LINE_3 = "MAIN_LINE_3";
    private static SettingUtil settingUtil;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SettingUtil(){}

    public static SettingUtil getInstance(Context context) {
        if(settingUtil == null) {
            settingUtil = new SettingUtil();
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return settingUtil;
    }

    /**
     * 设置值:true打开,false关闭
     * @param value
     */
    public void setMainLineNotifyStatus(String name, boolean value) {
        editor.putBoolean(name, value);
        editor.commit();
    }

    /**
     * 获取线路通知状态,true打开,false关闭
     * @param name
     * @return
     */
    public boolean getMainLineNotifyStatus(String name) {
        return sharedPreferences.getBoolean(name, true);
    }
}
