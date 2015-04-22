package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.CarInfoDetailActivity;
import com.ybxiang.driver.activity.MyCarsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 我的车队的adapter 1：姓名，车牌号，车型，车长 2：路线，位置，定位时间
 * 
 * @author ybxiang
 * 
 */
public class MyCarInfoListAdapter extends BaseListAdapter<CarInfo> implements View.OnClickListener {

    private Context mContext;

	public MyCarInfoListAdapter(Context context) {
		super(context);
        mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CarInfo carInfo = mList.get(position);
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.listitem_mycarinfo, parent, false);
            holderView.nameId = ((TextView) convertView.findViewById(R.id.nameId));
            holderView.plate_numberId = ((TextView) convertView.findViewById(R.id.plate_numberId));
            holderView.phone = ((TextView) convertView.findViewById(R.id.phone));
            holderView.location_time = ((TextView) convertView.findViewById(R.id.location_time));
            holderView.locationId = ((TextView) convertView.findViewById(R.id.locationId));
        } else {
            holderView = (HolderView) convertView.getTag(R.id.common_key);
        }
        holderView.nameId.setText(carInfo.getDriver_name());
        holderView.plate_numberId.setText(carInfo.getPlate_number());
        holderView.phone.setText(carInfo.getPhone());
        if(!TextUtils.isEmpty(carInfo.getLocation_time()) && Long.valueOf(carInfo.getLocation_time()) > 0) {
            Date date = new Date(Long.valueOf(carInfo.getLocation_time()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String locationTime = simpleDateFormat.format(date);
            holderView.location_time.setText(locationTime);
        } else if(carInfo.getPulish_date() > 0) {
            Date date = new Date(Long.valueOf(carInfo.getPulish_date()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String locationTime = simpleDateFormat.format(date);
            holderView.location_time.setText(locationTime);
        }
        holderView.locationId.setText(carInfo.getLocation());
        convertView.setTag(carInfo);
        convertView.setTag(R.id.common_key, holderView);
        convertView.setOnClickListener(this);
        return convertView;
    }

    class HolderView {
        public TextView nameId;
        public TextView plate_numberId;
        public TextView phone;
        public TextView location_time;
        public TextView locationId;
    }

    @Override
    public void onClick(View view) {
        CarInfo carInfo = (CarInfo) view.getTag();
        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, carInfo.getId());
        mContext.startActivity(intent);
    }
}
