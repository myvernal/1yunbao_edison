package com.maogousoft.logisticsmobile.driver.adapter;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.NoticeInfo;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoListAdapter extends BaseListAdapter<NoticeInfo> {

	public InfoListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_info, parent, false);
			holder=new ViewHolder();
			holder.type=(TextView)convertView.findViewById(R.id.notify_type);
			holder.time=(TextView)convertView.findViewById(R.id.notify_time);
			holder.theme=(TextView)convertView.findViewById(R.id.notify_theme);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder)convertView.getTag();
		}
		final NoticeInfo noticeInfo=mList.get(position);
		holder.type.setText(noticeInfo.getCategory()==0?"全站公告":"系统私人消息");
		holder.time.setText(TimeUtils.getDetailTime(noticeInfo.getCreate_time()));
		holder.theme.setText(noticeInfo.getTitle());
		return convertView;
	}
	final static class ViewHolder{
		TextView type,time,theme;
	}
}
