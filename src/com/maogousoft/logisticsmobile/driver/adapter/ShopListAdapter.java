package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
			
//			holder.image = (ImageView)convertView.findViewById(R.id.source_id_order_image);
//			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//			holder.ratingbar = (RatingBar) convertView.findViewById(R.id.ratingbar);
//			holder.tv_huowu_name = (TextView) convertView.findViewById(R.id.tv_huowu_name);
//			holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
//			holder.tv_vip_price = (TextView) convertView.findViewById(R.id.tv_vip_price);
//			holder.tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
//			holder.rlayout_loction= (RelativeLayout) convertView.findViewById(R.id.rlayout_loction);
//			holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.locationImg = convertView.findViewById(R.id.location_img);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ShopInfo shopInfo = mList.get(position);

//		String tv_name = shopInfo.getVender_name();
//		String goods_name = shopInfo.getGoods_name();
//		Double ratingbar = (Double) shopInfo.getScore();
//		String tv_price = shopInfo.getNormal_price();
//		String tv_vip_price = shopInfo.getMember_price().toString();
//		String tv_mark = shopInfo.getOther();
//		String tv_addr = shopInfo.getVender_address();
//
//		holder.tv_name.setText(CheckUtils.checkIsNull(tv_name));
//		float socre = Float.parseFloat(ratingbar + "");
//		if (socre == 0) {
//			holder.ratingbar.setRating(5);
//		} else {
//			holder.ratingbar.setRating(Float.parseFloat(ratingbar + ""));
//		}
//
//		holder.tv_huowu_name.setText(CheckUtils.checkIsNull(goods_name) + "：");
//		holder.tv_price.setText(CheckUtils.checkIsNull(tv_price));
//		holder.tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
//
//		holder.tv_vip_price.setText("会员：" + CheckUtils.checkIsNull(tv_vip_price));
//		holder.tv_mark.setText(CheckUtils.checkIsNull(tv_mark));
//		holder.tv_addr.setText(CheckUtils.checkIsNull(tv_addr));
//		mImageLoader.displayImage(shopInfo.getPhoto1(), holder.image);

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

		TextView tv_name;
		TextView tv_huowu_name;
		TextView tv_price;
		TextView tv_vip_price;
		TextView tv_mark;
		View locationImg;

		RatingBar ratingbar;
		ImageView image;
		RelativeLayout rlayout_loction;
	}

}
