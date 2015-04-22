package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;

/**
 * 九宫格城市适配器
 * 
 * @author lenovo
 */
public class CityListAdapter extends BaseListAdapter<CityInfo> {

	public CityListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.griditem_city, parent, false);
		}
		CityInfo cityInfo = mList.get(position);
		((TextView) convertView).setText(cityInfo.getName());
		return convertView;
	}
}
