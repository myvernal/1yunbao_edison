package com.maogousoft.logisticsmobile.driver.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

/**
 * Myabc 车辆信息
 * 
 * @author ybxiang
 */
public class MyabcCarInfoActivity extends BaseActivity {
	private TextView mName, mPhone, mCarNum, mCarlength, mCartype, mCarzhaizhong,
            myabc_id_car_contact, myabc_id_car_card_number;
    private ImageView mPhoto;
	// 个人abc信息
	private DriverInfo mDriverInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_info);
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("车辆信息");
        findViewById(R.id.titlebar_id_more).setVisibility(View.VISIBLE);

        mPhoto = (ImageView) findViewById(R.id.account_photo);
		mCarNum = (TextView) findViewById(R.id.myabc_id_car_num);
		mCarlength = (TextView) findViewById(R.id.myabc_id_car_length111);
		mCartype = (TextView) findViewById(R.id.myabc_id_car_type);
		mCarzhaizhong = (TextView) findViewById(R.id.myabc_id_car_zhaizhong);
        mName = (TextView) findViewById(R.id.myabc_id_name);
        mPhone = (TextView) findViewById(R.id.myabc_id_phone);

        myabc_id_car_contact = (TextView) findViewById(R.id.myabc_id_car_contact);
        myabc_id_car_card_number = (TextView) findViewById(R.id.myabc_id_car_card_number);
    }

	// 获取我的abc信息
	private void getABCInfo() {
		// if (mAbcInfo != null) {
		// return;
		// }
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.GET_DRIVER_INFO);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					DriverInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result != null) {
									mDriverInfo = (DriverInfo) result;
									if (!TextUtils.isEmpty(mDriverInfo.getPlate_number())) {
										mCarNum.setText(getString(R.string.string_car_detail_number, mDriverInfo.getPlate_number()));
									}
									mCarlength.setText(getString(R.string.string_car_detail_car_length, mDriverInfo.getCar_length()));
									mCarzhaizhong.setText(getString(R.string.string_car_detail_car_weight, mDriverInfo.getCar_weight()));
                                    ImageLoader.getInstance().displayImage(mDriverInfo.getId_card_photo(), mPhoto, options,
                                            new Utils.MyImageLoadingListener(mContext, mPhoto));
                                    mName.setText(mDriverInfo.getName());
                                    mPhone.setText(mDriverInfo.getPhone());

                                    myabc_id_car_contact.setText(getString(R.string.string_car_detail_contact, mDriverInfo.getLinkman()));
                                    myabc_id_car_card_number.setText(getString(R.string.string_car_detail_card_num, mDriverInfo.getFrame_number()));
                                    //车型
                                    int carTypeValue = mDriverInfo.getCar_type();
                                    String[] carTypeStr = getResources().getStringArray(R.array.car_types_name);
                                    for (int i = 0; i < Constants.carTypeValues.length; i++) {
                                        if (Constants.carTypeValues[i] == carTypeValue) {
                                            mCartype.setText(getString(R.string.string_car_detail_car_type, carTypeStr[i]));
                                            break;
                                        }
                                    }
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
		startActivity(new Intent(mContext, OptionalActivity.class).putExtra("info", mDriverInfo));
	}
}
