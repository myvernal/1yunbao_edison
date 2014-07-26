package com.maogousoft.logisticsmobile.driver.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChangePathActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.HistoryOrder;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * Myabc 车辆信息
 * 
 * @author ybxiang
 */
public class MyabcCarInfoActivity extends BaseActivity {
	// 返回,完善资料
	private Button mBack, mUpdate;
	private TextView mCarNum, mCarlength, mCartype, mCarzhaizhong;
	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_info);
		initViews();
		initData(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("车辆信息");
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);

		mUpdate = (Button) findViewById(R.id.myabc_id_update);

		mCarNum = (TextView) findViewById(R.id.myabc_id_car_num);
		mCarlength = (TextView) findViewById(R.id.myabc_id_car_length111);
		mCartype = (TextView) findViewById(R.id.myabc_id_car_type);
		mCarzhaizhong = (TextView) findViewById(R.id.myabc_id_car_zhaizhong);
	}

	private void initData(Bundle savedInstanceState) {

	}

	// 获取我的abc信息
	private void getABCInfo() {
		// if (mAbcInfo != null) {
		// return;
		// }
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, "");
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					AbcInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result != null) {
									mAbcInfo = (AbcInfo) result;
									if (!TextUtils.isEmpty(mAbcInfo
											.getPlate_number())) {
										mCarNum.setText(mAbcInfo
												.getPlate_number());
									}

									mCarlength.setText(mAbcInfo.getCar_length()
											+ "米");
									if (!TextUtils.isEmpty(mAbcInfo
											.getCar_type_str())) {
										mCartype.setText(mAbcInfo
												.getCar_type_str());
									}
									mCarzhaizhong.setText(mAbcInfo
											.getCar_weight() + "吨");

								}
								break;
							case ResultCode.RESULT_ERROR:
								if (result != null)
									showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								if (result != null)
									showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	/**
	 * 完善信息
	 * 
	 * @param view
	 */
	public void onComplementCarInfo(View view) {
		startActivity(new Intent(context, OptionalActivity.class).putExtra(
				"info", mAbcInfo));
	}
}
