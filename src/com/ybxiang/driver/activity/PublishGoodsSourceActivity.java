package com.ybxiang.driver.activity;

// add this file for 发布货源

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class PublishGoodsSourceActivity extends BaseActivity implements OnClickListener{
    private Context mContext;
    private TextView mTitleBarContent;
    private Button mTitleBarBack,mTitleBarMore;
    private int selectedCarWay = 0;// 选择的车辆方式
    private int selectedCarStatus = 0;// 选择的车辆状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_goods_source);
        mContext = PublishGoodsSourceActivity.this;
        mTitleBarContent = (TextView) findViewById(R.id.titlebar_id_content);
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarContent.setText("发布货源");
        mTitleBarMore.setText("管理");
        mTitleBarBack.setOnClickListener(this);
        mTitleBarMore.setOnClickListener(this);
    }
    /**
     * 管理发布过的车源
     */
    @Override
    public void onClick(View v) {
    	((BaseActivity) mContext).setIsRightKeyIntoShare(false);
    	super.onClick(v);
        switch (v.getId()) {
        case R.id.titlebar_id_more:
            Intent intent = new Intent();
            intent.setClass(mContext, ManagerGoodsSourceActivity.class);
            startActivity(intent);
            break;

        default:
            break;
        }
    }
    /**
     * 车辆状态：空车或在途
     * @param view
     */
    public void onCarStatus(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运车辆状态【空车，在途】");
            return;
        }
        switch (view.getId()) {
        case R.id.car_status_idle:
            // 空车
            selectedCarStatus = 0;
            break;
        case R.id.car_status_running:
            selectedCarStatus = 1;
            // 整车
            break;
        }
    }
    /**
     * 运输方式：零担，整车，零整不限，三者必选其一
     * @param view
     */
    public void onChooseCarWay(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运输方式【零担，整车，零整不限】");
            return;
        }
        switch (view.getId()) {
        case R.id.car_way_part:
            // 零担
            selectedCarWay = 0;
            break;
        case R.id.car_way_whole:
            selectedCarWay = 1;
            // 整车
            break;
        case R.id.car_way_all:
            selectedCarWay = 2;
            // 零整不限
            break;
        }
    }
    /**
     * 发布车源
     * @param view
     */
    public void onPublishCarSource(View view){
        
    }
}
