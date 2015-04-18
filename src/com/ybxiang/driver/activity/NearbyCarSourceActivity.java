package com.ybxiang.driver.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.NewSourceActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SearchSourceActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 查找车源
 * 
 * @author ybxiang
 */
public class NearbyCarSourceActivity extends BaseActivity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(SearchSourceActivity.class);
	private Button mMore;

	private Button mSubmit;

	private CitySelectView cityselectStart, cityselectEnd;

	private Spinner carTypeSpinner, carStatusSpinner, carLocationStatus;
	private EditText edtCarLength;

	private ArrayList<HashMap<String, Object>> listCarTypes = new ArrayList<HashMap<String, Object>>();// 车辆类型集合

	private int selectedCarStatus = 0;// 选择的车辆状态 不限，空车，在途
	private int selectedCarLocationStatus = 0;// 选择车辆定位有效事件状态 今天，两小时内，六小时内
	private int selectedCarType = 0;// 选择的车辆类型
	private Context mContext; // PR1.3
	private int selectedCarWay = 0;// 选择的车辆方式，PR1.3

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_car_source);
		mContext = NearbyCarSourceActivity.this;// PR1.3
		initViews();
		initUtils();
		initDatas();

	}

	// 初始化视图
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText("附件车源");
		mMore = (Button) findViewById(R.id.titlebar_id_more);

		carTypeSpinner = (Spinner) findViewById(R.id.search_car_type);
		carLocationStatus = (Spinner) findViewById(R.id.search_car_location_status);
		carStatusSpinner = (Spinner) findViewById(R.id.search_car_status);
		edtCarLength = (EditText) findViewById(R.id.edt_search_source_carlength);

		mSubmit = (Button) findViewById(R.id.search_source__submit);

		cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
		cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);
		mMore.setOnClickListener(this);
		mSubmit.setOnClickListener(this);

	}

	private void initUtils() {

		getCarTypes();

	}

	// 初始化数据，获取车型
	private void initDatas() {
		// 车辆状态
		// PR1.3 change simple_spinner_item to simple_spinner_item_car_style
		ArrayAdapter<CharSequence> carStatusAdapter = ArrayAdapter
				.createFromResource(mContext, R.array.car_status,
						R.layout.simple_spinner_item_car_style);
		carStatusSpinner.setAdapter(carStatusAdapter);
		carStatusSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectedCarStatus = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		// 车辆定位时间有效区间
		ArrayAdapter<CharSequence> carLocationStatusAdapter = ArrayAdapter
				.createFromResource(mContext, R.array.car_location_time,
						R.layout.simple_spinner_item_car_style);
		carLocationStatus.setAdapter(carLocationStatusAdapter);
		carStatusSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectedCarLocationStatus = arg2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		// 车型
		carTypeSpinner.setAdapter(new SimpleAdapter(context, listCarTypes,
				R.layout.simple_spinner_item_car_style,
				new String[] { "name" }, new int[] { android.R.id.text1 }));

		carTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				selectedCarType = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// ybxiang 显示搜索结果
		case R.id.search_source__submit:
			Toast.makeText(mContext, "显示结果正在开发中", Toast.LENGTH_SHORT).show();
//			// 先检测是否已经完善了资料
//			if (application.checkIsRegOptional()) {
//				submit();
//			} else {
//				final MyAlertDialog dialog = new MyAlertDialog(context);
//				dialog.show();
//				dialog.setTitle("提示");
//				dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
//				dialog.setLeftButton("完善资料", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//						Intent intent = new Intent(context,
//								OptionalActivity.class);
//						intent.putExtra("isFormRegisterActivity", false);
//						startActivity(intent);
//						finish();
//					}
//				});
//				dialog.setRightButton("取消", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//					}
//				});
//			}
//
//			break;

		default:
			break;
		}
	}

	private void submit() {

		if (cityselectStart.getSelectedProvince() == null
				| cityselectEnd.getSelectedProvince() == null) {

			showMsg("请选择出发地，目的地。");

			return;

		}

		final JSONObject jsonObject = new JSONObject();
		final JSONObject params = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.SEARCH_SOURCE_ORDER);
			jsonObject.put(Constants.TOKEN, application.getToken());

			params.put("start_province", cityselectStart.getSelectedProvince()
					.getId());
			params.put("end_province", cityselectEnd.getSelectedProvince()
					.getId());

			if (cityselectStart.getSelectedCity() != null) {
				params.put("start_city", cityselectStart.getSelectedCity()
						.getId());
			}

			if (cityselectEnd.getSelectedCity() != null) {
				params.put("end_city", cityselectEnd.getSelectedCity().getId());
			}

			if (cityselectStart.getSelectedTowns() != null) {
				params.put("start_district", cityselectStart.getSelectedTowns()
						.getId());
			}

			if (cityselectEnd.getSelectedTowns() != null) {
				params.put("end_district", cityselectEnd.getSelectedTowns()
						.getId());
			}
			// add ybxiang begin
			if (selectedCarStatus != 0) {
				params.put("car_status", selectedCarStatus);
			}
			if (selectedCarLocationStatus != 0) {
				params.put("car_location_status", selectedCarLocationStatus);
			}
			// add ybxiang end
			if (selectedCarType != 0) {
				params.put("car_type", selectedCarType);
			}
			if (!TextUtils.isEmpty(edtCarLength.getText().toString())) {
				params.put("car_length", edtCarLength.getText().toString());
			}
			params.put("car_way", selectedCarWay); // add PR1.3
			params.put("device_type", Constants.DEVICE_TYPE);
			jsonObject.put(Constants.JSON, params);
            showSpecialProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NewSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									List<NewSourceInfo> mList = (ArrayList<NewSourceInfo>) result;

									if (mList.size() != 0) {
										Intent intent = new Intent(context,
												NewSourceActivity.class);
										intent.putExtra("isFromHomeActivity",
												true);
										intent.putExtra("NewSourceInfos",
												(Serializable) mList);
										context.startActivity(intent);
									} else {
										showMsg("暂无满足条件的信息，请扩大搜索范围再试。");
									}
								}
								break;
							case ResultCode.RESULT_ERROR:
								if (result != null)
									showMsg(result.toString());

								break;
							case ResultCode.RESULT_FAILED:
								if (result != null) {
									// 您当月的免费搜索次数已经用完

									// if (result.equals("您当月的免费搜索次数已经用完")) {
									final MyAlertDialog dialog = new MyAlertDialog(
											context);
									dialog.show();
									dialog.setTitle("提示");
									// 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
									dialog.setMessage(result.toString());
									dialog.setLeftButton("确定",
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													dialog.dismiss();

													String content = null;
													startActivity(new Intent(
															context,
															ShareActivity.class)
															.putExtra("share",
																	content));
													finish();
												}
											});

									// }
								}
								// showMsg(result.toString());
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

	public void getCarTypes() {

		HashMap<String, Object> hm = null;

		hm = new HashMap<String, Object>();
		hm.put("id", 43);
		hm.put("name", "不限");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 44);
		hm.put("name", "平板");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 45);
		hm.put("name", "高栏");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 46);
		hm.put("name", "厢式");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 47);
		hm.put("name", "冷藏");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 48);
		hm.put("name", "集装箱");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 49);
		hm.put("name", "全封闭");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 50);
		hm.put("name", "特种");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 51);
		hm.put("name", "危险");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 52);
		hm.put("name", "自卸");
		listCarTypes.add(hm);

		hm = new HashMap<String, Object>();
		hm.put("id", 53);
		hm.put("name", "其他");
		listCarTypes.add(hm);
	}
}
