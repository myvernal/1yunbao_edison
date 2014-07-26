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
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.HistoryOrder;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 主营路线
 * 
 * @author ybxiang
 */
public class MainLineActivity extends BaseActivity {
	private Button mChangePath;
	private TextView mPath1, mPath2, mPath3;
	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_line);
		initViews();
		initData(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("主营路线");
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);

		mChangePath = (Button) findViewById(R.id.myabc_id_change_path);
		mPath1 = (TextView) findViewById(R.id.myabc_id_path1);
		mPath2 = (TextView) findViewById(R.id.myabc_id_path2);
		mPath3 = (TextView) findViewById(R.id.myabc_id_path3);
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
									CityDBUtils mDBUtils = new CityDBUtils(
											application.getCitySDB());
									String path1Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province(),
											mAbcInfo.getStart_city(),
											mAbcInfo.getEnd_province(),
											mAbcInfo.getEnd_city());
									String path2Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province2(),
											mAbcInfo.getStart_city2(),
											mAbcInfo.getEnd_province2(),
											mAbcInfo.getEnd_city2());
									String path3Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province3(),
											mAbcInfo.getStart_city3(),
											mAbcInfo.getEnd_province3(),
											mAbcInfo.getEnd_city3());

									mPath1.setText(path1Str);
									mPath2.setText(path2Str);
									mPath3.setText(path3Str);

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
	 * 修改路线
	 * 
	 * @param view
	 */
	public void onChangePath(View view) {
		String[] array = new String[] { "线路1", "线路2", "线路3" };
		new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
				context).setTitle("选择需要修改的路线")
				.setItems(array, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(context,
								ChangePathActivity.class);
						// intent.putExtra("info", mAbcInfo);
						intent.putExtra("path", which);
						startActivityForResult(intent, 1);
					}
				}).show();
	}
}
