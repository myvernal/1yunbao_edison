package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListMapActivity;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;

public class ShopListAdapter extends BaseListAdapter<ShopInfo> {

	private Context context;

	public ShopListAdapter(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_vip_shoplist_item1, parent, false);
			holder = new ViewHolder();
			
			holder.location_address = (TextView)convertView.findViewById(R.id.location_address);
			holder.parking_num = (TextView) convertView.findViewById(R.id.parking_spaces_num);
			holder.normal_price = (TextView) convertView.findViewById(R.id.normal_price);
			holder.member_price = (TextView) convertView.findViewById(R.id.vip_content);
			holder.campus_activities = (TextView) convertView.findViewById(R.id.campus_activities);
			holder.vender_phone = (TextView) convertView.findViewById(R.id.vender_phone);
			holder.park_name = (TextView) convertView.findViewById(R.id.park_name);
            holder.locationImg = convertView.findViewById(R.id.location_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ShopInfo shopInfo = mList.get(position);
        holder.park_name.setText(shopInfo.getVender_name());
        holder.location_address.setText(shopInfo.getVender_address());
        holder.parking_num.setText(shopInfo.getParking_spaces_num() + "个");
        holder.normal_price.setText(shopInfo.getNormal_price() + "/天");
        holder.member_price.setText(shopInfo.getMember_price());
        holder.campus_activities.setText(shopInfo.getCampus_activities());
        holder.vender_phone.setText(shopInfo.getVender_phone());
        holder.locationImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, ShopListMapActivity.class);
				intent.putExtra("shopInfo", shopInfo);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	final static class ViewHolder {

		TextView location_address;
		TextView parking_num;
		TextView normal_price;
		TextView member_price;
		TextView campus_activities;
		TextView vender_phone;
		TextView park_name;
		View locationImg;
	}

}
