package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.model.CardInfo;
import com.ybxiang.driver.activity.CarInfoDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/30.
 */
public class CheckCardListAdapter extends BaseListAdapter<CardInfo> {

    public CheckCardListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_check_card, parent, false);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            holder.region = (TextView) convertView.findViewById(R.id.region);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final CardInfo cardInfo = mList.get(position);
        holder.name.setText(cardInfo.getId_name());
        holder.number.setText(cardInfo.getId_card());
        holder.region.setText(cardInfo.getRegioninfo());
        holder.time.setText(cardInfo.getCreate_time());
        switch (cardInfo.getVerifyresult()) {
            case 1:
                holder.result.setText("一致");
                break;
            case 2:
                holder.result.setText("不一致");
                holder.result.setTextColor(0xffff0000);
                break;
            case 3:
                holder.result.setText("无此号码");
                holder.result.setTextColor(0xffff0000);
                break;
            case 4:
                holder.result.setText("一致,无照片");
                break;
        }
        convertView.setTag(holder);
        return convertView;
    }

    class Holder {
        private TextView name;
        private TextView time;
        private TextView number;
        private TextView result;
        private TextView region;
    }
}
