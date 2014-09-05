package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

// 货主详细评价
public class UserCreditActivity extends BaseActivity {

	private ListView mListView;
	private RatingBar mScore, credit_score1, credit_score2, credit_score3;
	private TextView creditPhone;

	private EvaluateListAdapter mAdapter;
	private int user_id;
	private String user_phone;
	private float user_score;

	private TextView tvName, tvCompanyName, tvAddr, tvTelcom;

	private HuoZhuUserInfo hzui = new HuoZhuUserInfo();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_user_credit);
		initViews();
		initData();
		queryData();
	}

	// 初始化视图
	private void initViews() {
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("货主信誉");
		mListView = (ListView) findViewById(android.R.id.list);
		mScore = (RatingBar) findViewById(R.id.credit_score);
		credit_score1 = (RatingBar) findViewById(R.id.credit_score1);
		credit_score2 = (RatingBar) findViewById(R.id.credit_score2);
		credit_score3 = (RatingBar) findViewById(R.id.credit_score3);
		mScore.setIsIndicator(true);
		credit_score1.setIsIndicator(true);
		credit_score2.setIsIndicator(true);
		credit_score3.setIsIndicator(true);
		creditPhone = (TextView) findViewById(R.id.credit_phone);

		tvName = (TextView) findViewById(R.id.tv_name);
		tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
		tvAddr = (TextView) findViewById(R.id.tv_addr);
		tvTelcom = (TextView) findViewById(R.id.tv_telcom);
	}

	// 初始化数据
	private void initData() {
		user_id = getIntent().getIntExtra("user_id", 0);
		user_phone = getIntent().getStringExtra("user_phone");
		user_score = getIntent().getFloatExtra("user_score", 0);
		if (user_score == 0) {
			user_score = 5;
		}
		mScore.setRating(user_score);

		float user_score1 = getIntent().getIntExtra("user_score1", 0);
		if (user_score1 == 0) {
			user_score1 = 5;
		}
		credit_score1.setRating(user_score1);

		float user_score2 = getIntent().getIntExtra("user_score2", 0);
		if (user_score2 == 0) {
			user_score2 = 5;
		}
		credit_score2.setRating(user_score2);

		float user_score3 = getIntent().getIntExtra("user_score3", 0);
		if (user_score3 == 0) {
			user_score3 = 5;
		}
		credit_score3.setRating(user_score3);

		creditPhone.setText(CheckUtils.pbContent(user_phone));
		mAdapter = new EvaluateListAdapter(context);
		mListView.setAdapter(mAdapter);
	}

	// 获取评价列表
	private void queryData() {
        showSpecialProgress();
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.GET_USER_REPLY);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON,
					new JSONObject().put("user_id", user_id).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
					EvaluateInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								mAdapter.setList((List<EvaluateInfo>) result);
								break;
							// case ResultCode.RESULT_ERROR:
							// showMsg(result.toString());
							// break;
							// case ResultCode.RESULT_FAILED:
							// showMsg(result.toString());
							// break;
							//
							// default:
							// break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}

		final JSONObject jsonObject1 = new JSONObject();
		try {
			jsonObject1.put(Constants.ACTION, Constants.GET_USER_INFO);
			jsonObject1.put(Constants.TOKEN, application.getToken());
			jsonObject1.put(Constants.JSON,
					new JSONObject().put("user_id", user_id).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject1,
					HuoZhuUserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:

								hzui = (HuoZhuUserInfo) result;

								tvName.setText(CheckUtils.pbContent(hzui
										.getName()));

								tvCompanyName.setText(CheckUtils.pbContent(hzui
										.getCompany_name()));

								tvAddr.setText(CheckUtils.pbContent(hzui
										.getAddress()));

								tvTelcom.setText(CheckUtils.pbContent(hzui
										.getTelcom()));

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
