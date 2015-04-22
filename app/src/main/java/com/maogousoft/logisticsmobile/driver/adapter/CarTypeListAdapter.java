package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;

/**
 * 汽车类型的adapter
 * @author lenovo
 *
 */
public class CarTypeListAdapter extends BaseListAdapter<DictInfo> {

	public CarTypeListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.listitem_cartype, parent,false);
		}
		TextView text=(TextView)convertView.findViewById(android.R.id.text1);
		text.setText(mList.get(position).getName());
		return convertView;
	}
}
