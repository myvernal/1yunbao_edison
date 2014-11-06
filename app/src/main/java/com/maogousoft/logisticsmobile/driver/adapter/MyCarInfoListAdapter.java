package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.MyCarsDetailActivity;

/**
 * 我的车队的adapter 1：姓名，车牌号，车型，车长 2：路线，位置，定位时间
 * 
 * @author ybxiang
 * 
 */
public class MyCarInfoListAdapter extends BaseListAdapter<CarInfo> implements View.OnClickListener {

    private Context mContext;
    private CityDBUtils dbUtils;
    public MyCarInfoListAdapter(Context context) {
        super(context);
        mContext = context;
        dbUtils = new CityDBUtils(application.getCitySDB());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_mycarinfo, parent, false);
            viewHolder.car_info_line = (TextView) convertView.findViewById(R.id.car_info_line);
            viewHolder.car_info_desc = (TextView) convertView.findViewById(R.id.car_info_desc);
            viewHolder.car_info_state = (TextView) convertView.findViewById(R.id.car_info_state);
            viewHolder.car_info_money = (TextView) convertView.findViewById(R.id.car_info_money);
            viewHolder.car_info_phone = convertView.findViewById(R.id.car_info_phone);
            convertView.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CarInfo carInfo = mList.get(position);
        final StringBuilder line = new StringBuilder();
        final StringBuilder detail = new StringBuilder();

        String wayStart = dbUtils.getCityInfo(carInfo.getStart_province(), carInfo.getStart_city(), carInfo.getStart_district());
        if (carInfo.getEnd_province() > 0 || carInfo.getEnd_city() > 0 || carInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(carInfo.getEnd_province(), carInfo.getEnd_city(), carInfo.getEnd_district());
            line.append(wayStart + "--" + wayEnd);
        } else {
            if (carInfo.getEnd_province1() > 0 || carInfo.getEnd_city1() > 0 || carInfo.getEnd_district1() > 0) {
                String wayEnd1 = dbUtils.getCityInfo(carInfo.getEnd_province1(), carInfo.getEnd_city1(), carInfo.getEnd_district1());
                line.append(wayStart + "--" + wayEnd1);
            }
            if (carInfo.getEnd_province2() > 0 || carInfo.getEnd_city2() > 0 || carInfo.getEnd_district2() > 0) {
                String wayEnd2 = dbUtils.getCityInfo(carInfo.getEnd_province2(), carInfo.getEnd_city2(), carInfo.getEnd_district2());
                line.append("\n" + wayStart + "--" + wayEnd2);
            }
            if (carInfo.getEnd_province3() > 0 || carInfo.getEnd_city3() > 0 || carInfo.getEnd_district3() > 0) {
                String wayEnd3 = dbUtils.getCityInfo(carInfo.getEnd_province3(), carInfo.getEnd_city3(), carInfo.getEnd_district3());
                line.append("\n" + wayStart + "--" + wayEnd3);
            }
        }

        //车型
        int carTypeValue = carInfo.getCar_type();
        String[] carTypeStr = mContext.getResources().getStringArray(R.array.car_types_name);
        for (int i = 0; i < Constants.carTypeValues.length; i++) {
            if (Constants.carTypeValues[i] == carTypeValue) {
                detail.append(carTypeStr[i]);
            }
        }

        if(carInfo.getCar_weight() > 0) {
            detail.append("/" + carInfo.getCar_weight() + "吨");
        }

        if (carInfo.getCar_length() != null && !TextUtils.isEmpty(carInfo.getCar_length())) {
            detail.append("/" + carInfo.getCar_length() + "米");
        }

        viewHolder.car_info_line.setText(line.toString());
        viewHolder.car_info_desc.setText(detail.toString());
        viewHolder.car_info_money.setText((TextUtils.isEmpty(carInfo.getBond()) ? 0 : carInfo.getBond()) + "元");
        if(carInfo.getStatus() == 0) {
            viewHolder.car_info_state.setText(R.string.car_info_wait);
        } else {
            viewHolder.car_info_state.setText(R.string.car_info_doing);
        }
        //电话号码
        viewHolder.car_info_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = carInfo.getPhone();
                if(TextUtils.isEmpty(phoneStr)) {
                    phoneStr = carInfo.getOwer_phone();
                }
                if (!TextUtils.isEmpty(phoneStr)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    mContext.startActivity(intent);
                }
            }
        });
        convertView.setOnClickListener(this);
        convertView.setTag(R.id.common_key, carInfo);
        return convertView;
    }

    class ViewHolder {
        TextView car_info_line, car_info_desc, car_info_state, car_info_money;
        View car_info_phone;
    }

    @Override
    public void onClick(View view) {
        CarInfo carInfo = (CarInfo) view.getTag(R.id.common_key);
        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, carInfo.getId());
        mContext.startActivity(intent);
    }
}
