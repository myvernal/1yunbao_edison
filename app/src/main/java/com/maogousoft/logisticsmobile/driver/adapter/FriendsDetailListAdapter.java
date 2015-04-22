package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.Friends;

/**
 * 我的好友列表的adapter
 * @author ybxiang
 *
 */
public class FriendsDetailListAdapter extends BaseListAdapter<Friends> {

	public FriendsDetailListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=mInflater.inflate(R.layout.listitem_friends_child, parent,false);
		}
		TextView name=(TextView)convertView.findViewById(R.id.name);
		name.setText(mList.get(position).getName());
		TextView phone=(TextView)convertView.findViewById(R.id.phone);
		phone.setText(mList.get(position).getPhone());
		return convertView;
	}
}
