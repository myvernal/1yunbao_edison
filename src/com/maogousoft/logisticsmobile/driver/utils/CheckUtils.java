package com.maogousoft.logisticsmobile.driver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.ybxiang.driver.util.Utils;

public class CheckUtils {

	public static boolean checkIsEmpty(EditText text) {
		if (!TextUtils.isEmpty(text.getText().toString())) {
			return true;
		}
		return false;
	}

	public static String checkIsNull(String value) {
		if (!TextUtils.isEmpty(value) && !value.equalsIgnoreCase("null")) {
			return value;
		}
		return "";
	}

	/** 验证手机号码是否符合 **/
	public static boolean checkPhone(String phone) {
		if (phone != null && !phone.equals("")) {
			if (phone.trim().length() == 11) {
				return true;
			}
		}
		return false;
		// Pattern pattern = Pattern
		// .compile("(130|131|132|145|155|156|185|186|134|135|136|137|138|139|147|150|151|152|157|158|159|182|183|187|188|133|153|189|180)\\d{8}");
		// Matcher matcher = pattern.matcher(phone);
		// return matcher.matches();
	}

	/**
	 * 获取货物单位
	 * 
	 * @param unitType
	 * @return
	 */
	public static String getCargoUnitName(Context context, Integer unitType) {
        String unitStr = "";
        String[] priceUnit = context.getResources().getStringArray(R.array.car_price_unit);
        for (int i = 0; i < Constants.unitTypeValues.length; i++) {
            if (Constants.unitTypeValues[i] == unitType) {
                unitStr =  priceUnit[i];
                break;
            }
        }
        return unitStr;
	}

	/** 屏蔽 **/
	public static String pbContent(String content) {
		if (TextUtils.isEmpty(content) || content.equalsIgnoreCase("null")) {
			return "";
		}

		int length = content.length() / 2;

		String temp1 = content.substring(0, length);

		StringBuffer sb = new StringBuffer();
		sb.append(temp1);
		for (int i = 0; i < (content.length() - length); i++) {
			sb.append("*");
		}

		return sb.toString();

	}
}
