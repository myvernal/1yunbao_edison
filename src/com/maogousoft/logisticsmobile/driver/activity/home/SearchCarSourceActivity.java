package com.maogousoft.logisticsmobile.driver.activity.home;
// PR203 找车【三方】
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;
import com.ybxiang.driver.activity.NearbyCarSourceActivity;


/**
 * 找车
 * 
 * @author ybxiang
 */
public class SearchCarSourceActivity extends BaseActivity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(SearchCarSourceActivity.class);

	private Button mBack;

	private Button mSubmit;
	private Button mRightButton;

	private CitySelectView cityselectStart, cityselectEnd;

	private Spinner carTypeSpinner;
	private EditText edtCarLength;

	private ArrayList<HashMap<String, Object>> listCarTypes = new ArrayList<HashMap<String, Object>>();// 车辆类型集合

	private int selectedCarType = 0;// 选择的车辆类型
	private Context mContext; //PR1.3
	private int selectedCarWay = 0;// 选择的车辆方式，PR1.3

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_search_car_source);
		mContext = SearchCarSourceActivity.this;//PR1.3
		initViews();
		initUtils();
		initDatas();

	}

	// 初始化视图
	private void initViews() {
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("查找车源");
		mRightButton = ((Button)findViewById(R.id.titlebar_id_more));
		mRightButton.setText("附近车源");
		carTypeSpinner = (Spinner) findViewById(R.id.search_car_type);
		edtCarLength = (EditText) findViewById(R.id.edt_search_source_carlength);

		mSubmit = (Button) findViewById(R.id.search_source__submit);

		cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
		cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);

		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mRightButton.setOnClickListener(this);
	}

	private void initUtils() {

		getCarTypes();

	}

	// 初始化数据，获取车型
	private void initDatas() {
	    // PR1.3 change simple_spinner_item to simple_spinner_item_car_style
		carTypeSpinner.setAdapter(new SimpleAdapter(context, listCarTypes,
				R.layout.simple_spinner_item_car_style, new String[] { "name" },
				new int[] { android.R.id.text1 }));

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
		final int id = v.getId();
		switch (id) {

		case R.id.search_source__submit:
			// 先检测是否已经完善了资料
			if (application.checkIsRegOptional()) {
				submit();
			} else {
				final MyAlertDialog dialog = new MyAlertDialog(context);
				dialog.show();
				dialog.setTitle("提示");
				dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
				dialog.setLeftButton("完善资料", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent = new Intent(context,
								OptionalActivity.class);
						intent.putExtra("isFormRegisterActivity", false);
						startActivity(intent);
						finish();
					}
				});
				dialog.setRightButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

			break;
		// PR206 附近车源 begin
		case R.id.titlebar_id_more:
			Intent nearbyCarSourceIntent = new Intent();
			nearbyCarSourceIntent.setClass(mContext, NearbyCarSourceActivity.class);
			startActivity(nearbyCarSourceIntent);
			break;
		// PR206 附近车源 end
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

			if (selectedCarType != 0) {
				params.put("car_type", selectedCarType);
			}
			if (!TextUtils.isEmpty(edtCarLength.getText().toString())) {
				params.put("car_length", edtCarLength.getText().toString());
			}
            params.put("car_way", selectedCarWay);  // add PR1.3
			params.put("device_type", Constants.DEVICE_TYPE);
			jsonObject.put(Constants.JSON, params);
			showDefaultProgress();
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
	// add for PR1.3  begin
	/**
	 * 运输方式：零担或者整车，二者必选其一
	 * @param view
	 */
    public void onChooseCarWay(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运输方式【零担，整车】");
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
        }
    }
	/**
	 * 关注线路
	 * 保持搜索的条件，保存为一个名字（本地）
	 * @param view
	 */
    public void onFocusLine(View view){
	    AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle(R.string.focus_line).setMessage(R.string.focus_line_dialog_msg).setCancelable(true);
	    builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 保存此条线路信息
                // saveFocusLine();
                Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
            }
        });
	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
	    builder.create().show();
	}
	/**
	 * 保存关注路线的信息，存储到本地还是什么的？
	 */
    private void saveFocusLine() {
        
    }
	/**
	 * 快速设置
	 * 从关注的线路列表选择进行快速填充文本
	 * @param view
	 */
	public void onFastSetting(View view){
	    Toast.makeText(mContext, "快速设置生效", Toast.LENGTH_SHORT).show();
	}
	// add for PR1.3  end
}
