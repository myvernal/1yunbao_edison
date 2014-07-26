package com.maogousoft.logisticsmobile.driver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**将时间戳转换成详细时间**/
	public static String getDetailTime(long time) {
		return sdf.format(new Date(time));
	}
}
