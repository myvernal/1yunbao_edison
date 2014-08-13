package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.CarInfoDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 车源管理的adapter 路线，载重，车型
 * 
 * @author ybxiang
 * 
 */
public class CarInfoListAdapter extends BaseListAdapter<CarInfo> {

    private Context mContext;
    private CityDBUtils dbUtils;

	public CarInfoListAdapter(Context context) {
		super(context);
        mContext = context;
        dbUtils = new CityDBUtils(application.getCitySDB());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_carinfo, parent, false);
            holder = new Holder();
            holder.weightView = (TextView) convertView.findViewById(R.id.weightId);
            holder.carTypeView = (TextView) convertView.findViewById(R.id.typeId);
            holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            holder.wayView = (TextView) convertView.findViewById(R.id.wayId);
		} else {
            holder = (Holder) convertView.getTag();
        }
        final CarInfo carInfo = mList.get(position);
        //载重
		holder.weightView.setText(mContext.getString(R.string.car_info_weight, carInfo.getCar_weight() + "吨"));
        //车型
        int carTypeValue = carInfo.getCar_type();
        String[] carTypeStr = mContext.getResources().getStringArray(R.array.car_types_name);
        for(int i=0;i<Constants.carTypeValues.length;i++) {
            if(Constants.carTypeValues[i] == carTypeValue){
                holder.carTypeView.setText(mContext.getString(R.string.car_info_car_type,carTypeStr[i]));
            }
        }
        //发布日期
        Date date = new Date(carInfo.getPulish_date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.createTime.setText(mContext.getString(R.string.car_info_publish_date, simpleDateFormat.format(date)));
        //线路
        convertView.setTag(holder);
        new LoadContentTask(convertView, carInfo).execute("");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CarInfoDetailActivity.class);
                intent.putExtra(Constants.COMMON_KEY, carInfo);
                mContext.startActivity(intent);
            }
        });
		return convertView;
	}

    class Holder {
        private TextView wayView;
        private TextView weightView;
        private TextView carTypeView;
        private TextView createTime;
    }

    protected class LoadContentTask extends AsyncTask<String, Void, Boolean> {

        private CarInfo carInfo;
        private Holder holder;
        private String wayStart;
        private String wayEnd;

        public LoadContentTask(View view, CarInfo carInfo) {
            this.holder = (Holder) view.getTag();
            this.carInfo = carInfo;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            wayStart = dbUtils.getCityInfo(carInfo.getStart_province(), carInfo.getStart_city(), carInfo.getStart_district());
            wayEnd = dbUtils.getCityInfo(carInfo.getEnd_province(), carInfo.getEnd_city(), carInfo.getEnd_district());
            carInfo.setWayStartStr(wayStart);
            carInfo.setWayEndStr(wayEnd);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            holder.wayView.setText(mContext.getString(R.string.car_info_way, wayStart, wayEnd));
        }
    }
}
