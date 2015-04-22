package com.maogousoft.logisticsmobile.driver.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * LOG工具
 * 
 * @author lenovo
 */
public class LogUtil {

	private static boolean isDebug = true;

	private static final String TAG = "wuliu";

	public static String makeLogTag(Class<?> cls) {
		return TAG + cls.getSimpleName();
	}

	public static int d(String subTAG, String msg) {
		if (isDebug && !TextUtils.isEmpty(msg)) {
			return Log.d(subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int i(String subTAG, String msg) {
		if (isDebug && !TextUtils.isEmpty(msg)) {
			return Log.i(subTAG, msg);
		} else {
			return 0;
		}
	}

	public static int e(String subTAG, String msg) {
		if (isDebug && !TextUtils.isEmpty(msg)) {
			return Log.e(subTAG, msg);
		} else {
			return 0;
		}
	}
}
