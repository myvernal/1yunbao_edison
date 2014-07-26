package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;

public class EvaluateListAdapter extends BaseListAdapter<EvaluateInfo> {

	private CityDBUtils dbUtils;

	public EvaluateListAdapter(Context context) {
		super(context);
		dbUtils = new CityDBUtils(application.getCitySDB());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_evaluate, parent, false);
			holder = new ViewHolder();
			holder.orderNum = (TextView) convertView.findViewById(R.id.evaluate_number);
			holder.orderLine = (TextView) convertView.findViewById(R.id.evaluate_line);
			holder.orderName = (TextView) convertView.findViewById(R.id.evaluate_name);
			holder.detail = (TextView) convertView.findViewById(R.id.evaluate_detail);
			holder.score1 = (RatingBar) convertView.findViewById(R.id.evaluate_score1);
			holder.score2 = (RatingBar) convertView.findViewById(R.id.evaluate_score2);
			holder.score3 = (RatingBar) convertView.findViewById(R.id.evaluate_score3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final EvaluateInfo evaluateInfo = mList.get(position);
		holder.orderNum.setText(String.format(mResources.getString(R.string.string_evaluate_order), evaluateInfo.getOrder_id()));
		holder.orderLine.setText(String.format(mResources.getString(R.string.string_evaluate_line),
				dbUtils.getCityInfo(evaluateInfo.getStart_province(), evaluateInfo.getStart_city(), evaluateInfo.getStart_district()),
				dbUtils.getCityInfo(evaluateInfo.getEnd_province(), evaluateInfo.getEnd_city(), evaluateInfo.getEnd_district())));
		holder.orderName.setText(String.format(mResources.getString(R.string.string_evaluate_name), evaluateInfo.getCargo_desc()));
		holder.detail.setText(evaluateInfo.getReply_content());
		holder.score1.setRating(evaluateInfo.getScore1());
		holder.score2.setRating(evaluateInfo.getScore2());
		holder.score3.setRating(evaluateInfo.getScore3());
		return convertView;
	}

	final static class ViewHolder {
		TextView orderNum, orderName, orderLine, detail;
		RatingBar score1, score2, score3;
	}
}
