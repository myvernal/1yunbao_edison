package com.ybxiang.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;

/**
 * 车源详细界面
 *
 * @author ybxiang
 */
public class CarInfoDetailActivity extends BaseActivity {
    private Button mTitleBarMore;
    private CarInfo carInfo;
    private TextView car_info_detail_way, car_info_detail_number, car_info_detail_car_length,
            car_info_detail_car_type, car_info_detail_weight, car_info_detail_price, car_info_detail_price_unit,
            car_info_detail_address, car_info_detail_contact_name, car_info_detail_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_info_detail);
        initViews();
        initData();
    }

    private void initData() {
        carInfo = (CarInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        car_info_detail_way.setText(carInfo.getWayStartStr() + "->" + carInfo.getWayEndStr());
        car_info_detail_number.setText(carInfo.getPlate_number());
        car_info_detail_car_length.setText(carInfo.getCar_length() + "米");
        //车型
        int carTypeValue = carInfo.getCar_type();
        String[] carTypeStr = mContext.getResources().getStringArray(R.array.car_types_name);
        for(int i=0;i<Constants.carTypeValues.length;i++) {
            if(Constants.carTypeValues[i] == carTypeValue){
                car_info_detail_car_type.setText(carTypeStr[i]);
            }
        }
        car_info_detail_weight.setText(carInfo.getCar_weight() + "吨");
        //报价
        int priceUnit = carInfo.getUnits();
        String[] unitStr = mContext.getResources().getStringArray(R.array.car_price_unit);
        for(int i=0;i<Constants.unitTypeValues.length;i++) {
            if(Constants.unitTypeValues[i] == priceUnit){
                car_info_detail_price.setText(carInfo.getPrice() + "元/" + unitStr[i]);
            }
        }
        car_info_detail_address.setText(carInfo.getLocation());
        car_info_detail_contact_name.setText(carInfo.getOwer_name());
        car_info_detail_phone.setText(carInfo.getOwer_phone());
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("车源详情");
        // 返回按钮生效

        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setVisibility(View.GONE);

        car_info_detail_way = (TextView) findViewById(R.id.car_info_detail_way);
        car_info_detail_number = (TextView) findViewById(R.id.car_info_detail_number);
        car_info_detail_car_length = (TextView) findViewById(R.id.car_info_detail_car_length);
        car_info_detail_car_type = (TextView) findViewById(R.id.car_info_detail_car_type);
        car_info_detail_weight = (TextView) findViewById(R.id.car_info_detail_weight);
        car_info_detail_price = (TextView) findViewById(R.id.car_info_detail_price);
        car_info_detail_price_unit = (TextView) findViewById(R.id.car_info_detail_price_unit);
        car_info_detail_address = (TextView) findViewById(R.id.car_info_detail_address);
        car_info_detail_contact_name = (TextView) findViewById(R.id.car_info_detail_contact_name);
        car_info_detail_phone = (TextView) findViewById(R.id.car_info_detail_phone);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

}
