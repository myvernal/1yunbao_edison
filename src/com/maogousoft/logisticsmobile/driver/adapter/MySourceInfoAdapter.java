package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.ybxiang.driver.activity.MySourceDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/24.
 */
public class MySourceInfoAdapter extends BaseListAdapter<NewSourceInfo> implements View.OnClickListener {
    private Context mContext;
    private CityDBUtils dbUtils;

    public MySourceInfoAdapter(Context context) {
        super(context);
        mContext = context;
        dbUtils = new CityDBUtils(application.getCitySDB());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewSourceInfo sourceInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_mysourceinfo, parent, false);
        }
        String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
        ((TextView) convertView.findViewById(R.id.source_way)).setText(wayStart +  "-->" + wayEnd);
        ((TextView) convertView.findViewById(R.id.source_name)).setText(sourceInfo.getCargo_desc());
        if(sourceInfo.getCreate_time() > 0) {
            Date date = new Date(Long.valueOf(sourceInfo.getCreate_time()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String locationTime = simpleDateFormat.format(date);
            ((TextView) convertView.findViewById(R.id.source_create_time)).setText(locationTime);
        }
        convertView.setTag(sourceInfo);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        NewSourceInfo sourceInfo = (NewSourceInfo) view.getTag();
        Intent intent = new Intent(mContext, MySourceDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, sourceInfo);
        mContext.startActivity(intent);
    }
}
