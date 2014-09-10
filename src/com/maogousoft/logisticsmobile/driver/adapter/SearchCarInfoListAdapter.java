package com.maogousoft.logisticsmobile.driver.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.other.MapActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.MyCarsDetailActivity;
import com.ybxiang.driver.model.LocationInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/30.
 */
public class SearchCarInfoListAdapter extends BaseListAdapter<CarInfo> implements View.OnClickListener {

    private Context mContext;

    public SearchCarInfoListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarInfo carInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_mycarinfo, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.nameId)).setText(carInfo.getOwer_name());
        ((TextView) convertView.findViewById(R.id.plate_numberId)).setText(carInfo.getPlate_number());
        ((TextView) convertView.findViewById(R.id.phone)).setText(carInfo.getOwer_phone());
        if(!TextUtils.isEmpty(carInfo.getLast_position_time())) {
            ((TextView) convertView.findViewById(R.id.location_time)).setText(carInfo.getLast_position_time());
        } else if(carInfo.getPulish_date() > 0) {
            Date date = new Date(Long.valueOf(carInfo.getPulish_date()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            String locationTime = simpleDateFormat.format(date);
            ((TextView) convertView.findViewById(R.id.location_time)).setText(locationTime);
        }
        ((TextView) convertView.findViewById(R.id.locationId)).setText(carInfo.getAddress());
        convertView.setTag(carInfo);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        CarInfo carInfo = (CarInfo) view.getTag();
        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, carInfo.getId());
        intent.putExtra(Constants.COMMON_OBJECT_KEY, carInfo);
        mContext.startActivity(intent);
    }
}
