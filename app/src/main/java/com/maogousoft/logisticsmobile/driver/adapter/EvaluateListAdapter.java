package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EvaluateListAdapter extends BaseListAdapter<EvaluateInfo> {

	public EvaluateListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_evaluate, parent, false);
			holder = new ViewHolder();
			holder.commentName = (TextView) convertView.findViewById(R.id.comment_name);
			holder.commentPhoto = (ImageView) convertView.findViewById(R.id.comment_photo);
			holder.commentTime = (TextView) convertView.findViewById(R.id.comment_time);
			holder.commentContent = (TextView) convertView.findViewById(R.id.comment_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final EvaluateInfo evaluateInfo = mList.get(position);
		holder.commentName.setText(mContext.getString(R.string.comment_name, evaluateInfo.getDriver_name(), evaluateInfo.getDriver_name()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String replyTime = simpleDateFormat.format(new Date(evaluateInfo.getReply_time()));
        holder.commentTime.setText(replyTime);
        holder.commentContent.setText(evaluateInfo.getReply_content());
        return convertView;
	}

	final static class ViewHolder {
		TextView commentName, commentTime, commentContent;
		ImageView commentPhoto;
	}
}
