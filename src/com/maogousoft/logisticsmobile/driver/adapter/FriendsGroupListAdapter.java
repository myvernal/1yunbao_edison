package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.FriendsGroup;

/**
 * 我的好友类组的adapter
 * @author ybxiang
 *
 */
public class FriendsGroupListAdapter extends BaseListAdapter<FriendsGroup> {

	public FriendsGroupListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.listitem_friends_group, parent,false);
		}
		TextView mFriendsTypeTextView=(TextView)convertView.findViewById(R.id.friends_type);
		mFriendsTypeTextView.setText("Edison");
		TextView mFriendsNumberTextView=(TextView)convertView.findViewById(R.id.friends_number);
		mFriendsNumberTextView.setText("15982034811");
		return convertView;
	}
}
