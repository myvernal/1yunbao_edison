package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.EvaluateListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;

/**
 * 我的信誉详情
 * 
 * @author lenovo
 */
public class MyCreditActivity extends BaseActivity {

	private ListView mListView;

	private TextView mName, mPhone, mCarNumber;
	private RatingBar mScore, mScore1, mScore2, mScore3;

	private EvaluateListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_driver_credit);
		initViews();
		initData();
		queryData();
	}

	// 初始化视图
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_driver_credit_title);
		mListView = (ListView) findViewById(android.R.id.list);
		mName = (TextView) findViewById(R.id.credit_name);
		mPhone = (TextView) findViewById(R.id.credit_phone);
		mCarNumber = (TextView) findViewById(R.id.credit_carnum);
		mScore = (RatingBar) findViewById(R.id.credit_score);
		mScore1 = (RatingBar) findViewById(R.id.credit_score1);
		mScore2 = (RatingBar) findViewById(R.id.credit_score2);
		mScore3 = (RatingBar) findViewById(R.id.credit_score3);

		mScore.setIsIndicator(true);
		mScore1.setIsIndicator(true);
		mScore2.setIsIndicator(true);
		mScore3.setIsIndicator(true);
	}

	// 初始化数据
	private void initData() {
		if (getIntent().hasExtra("info")) {
			DriverInfo driverInfo = (DriverInfo) getIntent()
					.getSerializableExtra("info");
			if (driverInfo != null) {
				mName.setText(driverInfo.getName());
				mPhone.setText(driverInfo.getPhone());
				mCarNumber.setText(driverInfo.getPlate_number());
				float score = driverInfo.getScore();
				if (score == 0) {
					score = 5;
				}
				mScore.setRating(score);
				float score1 = driverInfo.getScroe1();
				if (score1 == 0) {
					score1 = 5;
				}
				mScore1.setRating(score1);
				float score2 = driverInfo.getScroe2();
				if (score2 == 0) {
					score2 = 5;
				}
				mScore2.setRating(score2);
				float score3 = driverInfo.getScroe3();
				if (score3 == 0) {
					score3 = 5;
				}
				mScore3.setRating(score3);
			}
		}
		mAdapter = new EvaluateListAdapter(mContext, application.getUserType(), true);
		mListView.setAdapter(mAdapter);
	}

	// 获取评价列表
	private void queryData() {
        showSpecialProgress();
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.GET_DRIVER_REPLY);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(
					Constants.JSON,
					new JSONObject().put("driver_id",
							application.getDriverId().replace("d", ""))
							.toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
					EvaluateInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								mAdapter.setList((List<EvaluateInfo>) result);
								break;
							case ResultCode.RESULT_ERROR:
								showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
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
}
