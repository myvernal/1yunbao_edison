package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;

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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_mycarinfo, parent,
					false);
		}
		((TextView) convertView.findViewById(R.id.nameId)).setText(mList.get(position).getOwer_name());
		((TextView) convertView.findViewById(R.id.plate_numberId)).setText(mList.get(position).getPlate_number());
		((TextView) convertView.findViewById(R.id.car_type_strId)).setText(mList.get(position).getCar_type());
		((TextView) convertView.findViewById(R.id.car_lengthId)).setText(String.valueOf(mList.get(position).getCar_length()));
		((TextView) convertView.findViewById(R.id.locationId)).setText(mList.get(position).getLocation());
		((TextView) convertView.findViewById(R.id.location_timeId)).setText(mList.get(position).getLocation_time());
		return convertView;
	}
}
