package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AgreementInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

import java.util.Date;

/**
 * Created by aliang on 2015/4/26.
 */
public class AgreementAdapter extends BaseListAdapter<AgreementInfo> {

    private CityDBUtils dbUtils;
    private int userType;

    public AgreementAdapter(Context context, int userType) {
        super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
        this.userType = userType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_agreement_adapter_layout, parent, false);
            holder = new ViewHolder();
            holder.source_detail_phone = convertView.findViewById(R.id.source_detail_phone);
            holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
            holder.order_line = (TextView) convertView.findViewById(R.id.order_line);
            holder.order_state = (TextView) convertView.findViewById(R.id.order_state);

            if(userType == Constants.USER_SHIPPER) {
                holder.source_detail_phone.setVisibility(View.VISIBLE);
            } else {
                holder.source_detail_phone.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AgreementInfo agreementInfo = (AgreementInfo) getItem(position);

        /*String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        if (sourceInfo.getEnd_province() > 0 || sourceInfo.getEnd_city() > 0 || sourceInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
            title.append(wayStart).append("--").append(wayEnd);
        }

        detail.append(sourceInfo.getCargo_type_str());

        if (sourceInfo.getCargo_number() != null && sourceInfo.getCargo_number() != 0) {
            detail.append("/")
                    .append(sourceInfo.getCargo_desc())
                    .append(sourceInfo.getCargo_number())
                    .append(CheckUtils.getCargoUnitName(mContext, sourceInfo.getCargo_unit()));
        }

        if (!TextUtils.isEmpty(sourceInfo.getCar_type_str())) {
            detail.append("/");
            detail.append(sourceInfo.getCar_type_str());

        }*/
        holder.order_line.setText(agreementInfo.getLoading_address().replaceAll("-", "") + agreementInfo.getUnload_address().replaceAll("-", ""));

        Date date = new Date();
        String betweenTime = "刚发布";
        if (date.getTime() > agreementInfo.getCreate_time()) {
            long time = date.getTime() - agreementInfo.getCreate_time();
            long hour = time / (60 * 60 * 1000);
            long minites = (time % (60 * 60 * 1000)) / (60 * 1000);
            betweenTime = hour + "时" + minites + "分";
        }
        holder.order_time.setText(betweenTime + "前");
        if(agreementInfo.getStatus() == 0) {
            holder.order_state.setText(R.string.car_info_wait);
        } else {
            holder.order_state.setText(R.string.car_info_doing);
        }

        //电话号码
        holder.source_detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = agreementInfo.getDriver_phone();
                if (!TextUtils.isEmpty(phoneStr)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView order_time, order_line, order_state;
        View source_detail_phone;
    }
}
