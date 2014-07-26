package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;

/**
 * 车源管理的adapter 路线，载重，车型
 * 
 * @author ybxiang
 * 
 */
public class CarInfoListAdapter extends BaseListAdapter<CarInfo> {

	public CarInfoListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_carinfo, parent,
					false);
		}
		((TextView) convertView.findViewById(R.id.wayId)).setText(mList.get(
				position).getWay());
		((TextView) convertView.findViewById(R.id.weightId)).setText(mList.get(
				position).getCar_weight_str());
		((TextView) convertView.findViewById(R.id.typeId)).setText(mList.get(
				position).getCar_type_str());
		return convertView;
	}
}
