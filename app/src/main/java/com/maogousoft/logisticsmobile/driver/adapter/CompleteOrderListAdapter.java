package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.HistoryOrder;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 已完成订单数据适配器
 * 
 * @author admin
 * 
 */
public class CompleteOrderListAdapter extends BaseListAdapter<HistoryOrder> {

	private CityDBUtils mDBUtils;

	public CompleteOrderListAdapter(Context context) {
		super(context);
		mDBUtils = new CityDBUtils(application.getCitySDB());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_complete, parent, false);
			holder = new ViewHolder();
			holder.line = (TextView) convertView.findViewById(R.id.source_id_order_info);
			holder.type = (TextView) convertView.findViewById(R.id.source_id_order_type);
			holder.price = (TextView) convertView.findViewById(R.id.source_id_order_nums);
			holder.time = (TextView) convertView.findViewById(R.id.source_id_order_time);
			holder.number = (TextView) convertView.findViewById(R.id.source_id_order_number);
			holder.img = (ImageView) convertView.findViewById(R.id.source_id_order_image);
			holder.imgTips = convertView.findViewById(R.id.source_id_order_imagetips);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HistoryOrder sourceInfo = mList.get(position);
		holder.number.setText(String.format(mResources.getString(R.string.sourcedetail_number), sourceInfo.getId()));

		String path1Str = mDBUtils.getStartEndStr(sourceInfo.getStart_province(), sourceInfo.getStart_city(),
				sourceInfo.getEnd_province(), sourceInfo.getEnd_city());

		holder.line.setText(path1Str);

		// title.append(CheckUtils.checkIsNull(sourceInfo.getStart_province_str())).append(CheckUtils.checkIsNull(sourceInfo.getStart_city_str())).append(CheckUtils.checkIsNull(sourceInfo.getStart_district_str()));
		// title.append("-").append(CheckUtils.checkIsNull(sourceInfo.getEnd_province_str())).append(CheckUtils.checkIsNull(sourceInfo.getEnd_city_str())).append(CheckUtils.checkIsNull(sourceInfo.getEnd_district_str()));
		// holder.line.setText(title.toString());
		holder.type.setText(sourceInfo.getCargo_type_str());
		holder.price.setText("价格：" + sourceInfo.getPrice() + "元");
		holder.time.setText(TimeUtils.getDetailTime(sourceInfo.getValidate_time()));
		mImageLoader.displayImage(sourceInfo.getCargo_photo1(), holder.img);
		holder.imgTips.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				if (!TextUtils.isEmpty(sourceInfo.getCargo_photo1())) {
					list.add(sourceInfo.getCargo_photo1());
				}
				if (!TextUtils.isEmpty(sourceInfo.getCargo_photo2())) {
					list.add(sourceInfo.getCargo_photo2());
				}
				if (!TextUtils.isEmpty(sourceInfo.getCargo_photo3())) {
					list.add(sourceInfo.getCargo_photo3());
				}
				mContext.startActivity(new Intent(mContext, ImagePagerActivity.class).putStringArrayListExtra("images",
						list));
			}
		});
		return convertView;
	}

	final static class ViewHolder {

		TextView line, type, price, time, number;
		ImageView img;
		View imgTips;
	}
}
