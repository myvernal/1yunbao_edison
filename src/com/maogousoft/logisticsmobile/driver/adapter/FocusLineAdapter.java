package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.ybxiang.driver.model.FocusLineInfo;

/**
 * Created by aliang on 2014/8/13.
 */
public class FocusLineAdapter extends BaseListAdapter<FocusLineInfo> {

    public FocusLineAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.focus_line_item, parent, false);
            holder.wayBegin = (TextView) convertView.findViewById(R.id.wayBegin);
            holder.wayEnd = (TextView) convertView.findViewById(R.id.wayEnd);
            holder.countView = (TextView) convertView.findViewById(R.id.source_count);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final FocusLineInfo focusLineInfo = mList.get(position);

        holder.wayBegin.setText(focusLineInfo.getStart_str());
        holder.wayEnd.setText(focusLineInfo.getEnd_str());
        if(focusLineInfo.getCount() > 0) {
            holder.countView.setText(mContext.getString(R.string.focus_line_count, focusLineInfo.getCount()));
        } else {
            holder.countView.setVisibility(View.GONE);
        }
        convertView.setTag(holder);
        convertView.setTag(R.id.focus_line_key, focusLineInfo);
        return convertView;
    }

    class Holder {
        private TextView wayBegin;
        private TextView wayEnd;
        private TextView countView;
    }
}
