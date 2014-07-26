package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.ShopEvaluate;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

public class ShopDetailListAdapter extends BaseListAdapter<ShopEvaluate> {

	public ShopDetailListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.activity_vip_shopdetail_item, parent, false);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_hint = (TextView) convertView.findViewById(R.id.tv_hint);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.ratingbar_score = (RatingBar) convertView.findViewById(R.id.ratingbar_score);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShopEvaluate shopEvaluate = mList.get(position);

		String name = shopEvaluate.getName();
		int Is_true = shopEvaluate.getIs_true();
		String content = shopEvaluate.getReply_content();
		float score1 = shopEvaluate.getScore1();
		float score2 = shopEvaluate.getScore2();
		float score3 = shopEvaluate.getScore3();

		holder.tv_name.setText(CheckUtils.checkIsNull(name));
		if (Is_true == 1) {
			holder.tv_hint.setText("商家已如实提供会员优惠");
		} else {
			holder.tv_hint.setText("");
		}
		holder.tv_content.setText(CheckUtils.checkIsNull(content));
		holder.ratingbar_score.setRating((score1 + score2 + score3) / 3);

		return convertView;
	}

	final static class ViewHolder {

		TextView tv_name, tv_hint, tv_content;
		RatingBar ratingbar_score;
	}

}
