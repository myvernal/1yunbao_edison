package com.ybxiang.driver.activity;

// 发布车源

import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class PublishCarSourceActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;
	private TextView mTitleBarContent;
	private int car_status = 0;// 车辆状态:0 空车 1 在途
	private EditText mCar_locationView, mPriceView, mCarNumView,
			mCarlengthView, mCarzhaizhongView, mDescriptionView, mNameView,
			mPhoneView, mDayView, mHourView;
	// 当前位置，报价,
	// 车牌号，车长，载重，补充说明,姓名，手机号码,有效时间（天，小时）
	private double price; // 报价
	private int unit = 2;// 报价单位 1车：2吨：3方
	private Spinner mCartypeSpinner; // 车型
	private CitySelectView cityselectStart, cityselectEnd; // 出发地，目的地
	private Button mTitleBarBack;
	private Button mTitleBarMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_home_publish_car_source);
		mContext = PublishCarSourceActivity.this;
		initViews();
	}

	// 初始化视图
	private void initViews() {
		mTitleBarContent = (TextView) findViewById(R.id.titlebar_id_content);
		mTitleBarContent.setText(R.string.menu_publish_car_source);

		mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
		mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
		mTitleBarMore.setText("已发布");
		mCar_locationView = (EditText) findViewById(R.id.car_location);

		mTitleBarMore.setOnClickListener(this);
		mTitleBarBack.setOnClickListener(this);
		cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
		cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);
	}

	/**
	 * 管理发布过的车源
	 */
	@Override
	public void onClick(View v) {
		((BaseActivity) mContext).setIsRightKeyIntoShare(false);
		super.onClick(v);
		switch (v.getId()) {
		// 管理已发布的车源
		case R.id.titlebar_id_more:
			Intent intent = new Intent();
			intent.setClass(mContext, ManagerCarSourceActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 车辆状态：空车或在途
	 * 
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
			car_status = 0;
			break;
		case R.id.car_status_running:
			car_status = 1;
			// 整车
			break;
		}
	}

	/**
	 * 点击定位
	 * 
	 * @param view
	 */
	public void onGetLocation(View view) {

	}

	/**
	 * 发布车源
	 * 
	 * @param view
	 */
	public void onPublishCarSource(View view) {

	}
}
