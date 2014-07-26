package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.model.FocuseLineInfo;
import com.ybxiang.driver.model.FocusLineInfo;

/**
 * 关注路线的的adapter 省市区 
 * 
 * @author ybxiang
 * 
 */
public class FocusLineInfoListAdapter extends BaseListAdapter<FocuseLineInfo> {

	public FocusLineInfoListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_focusline_info, parent,
					false);
		}
		((TextView) convertView.findViewById(R.id.startID)).setText(""+mList.get(position).getStart_province());
		((TextView) convertView.findViewById(R.id.endID)).setText(""+mList.get(position).getEnd_province());
		return convertView;
	}
}
