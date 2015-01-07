package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(cardInfo.getCreate_time());
        String time = simpleDateFormat.format(date);
        holder.time.setText(time);
        if (TextUtils.equals("0", cardInfo.getStatus())) {
            holder.result.setText("验证失败");
            holder.result.setTextColor(0xffff0000);
        } else if (TextUtils.equals("1", cardInfo.getStatus())) {
            holder.result.setText("一致");
            holder.result.setTextColor(0xff00dd00);
        } else if (TextUtils.equals("2", cardInfo.getStatus())) {
//                holder.result.setText("身份验证不一致");
            holder.result.setText("不一致");
            holder.result.setTextColor(0xffff0000);
        } else if (TextUtils.equals("3", cardInfo.getStatus())) {
//                holder.result.setText("库中无此号码");
            holder.result.setText("不一致");
            holder.result.setTextColor(0xffff0000);
        } else if (TextUtils.equals("4", cardInfo.getStatus())) {
//            holder.result.setText("身份验证一致，无照片");
            holder.result.setText("一致");
            holder.result.setTextColor(0xff00dd00);
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
