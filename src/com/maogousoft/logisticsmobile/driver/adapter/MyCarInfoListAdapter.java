package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.CarInfoDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 我的车队的adapter 1：姓名，车牌号，车型，车长 2：路线，位置，定位时间
 * 
 * @author ybxiang
 * 
 */
public class MyCarInfoListAdapter extends BaseListAdapter<CarInfo> {

	public MyCarInfoListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CarInfo carInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_mycarinfo, parent,
                    false);
        }
        ((TextView) convertView.findViewById(R.id.nameId)).setText(carInfo.getOwer_name());
        ((TextView) convertView.findViewById(R.id.plate_numberId)).setText(carInfo.getPlate_number());
        //车型
        int carTypeValue = carInfo.getCar_type();
        String[] carTypeStr = mContext.getResources().getStringArray(R.array.car_types_name);
        for (int i = 0; i < Constants.carTypeValues.length; i++) {
            if (Constants.carTypeValues[i] == carTypeValue) {
                ((TextView) convertView.findViewById(R.id.car_type_strId)).setText(mContext.getString(R.string.car_info_car_type, carTypeStr[i]));
            }
        }
        ((TextView) convertView.findViewById(R.id.car_lengthId)).setText(String.valueOf(carInfo.getCar_length()));
        ((TextView) convertView.findViewById(R.id.location_timeId)).setText(carInfo.getLocation_time());
        return convertView;
    }
}
