package com.maogousoft.logisticsmobile.driver.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.HistoryOrder;
import com.maogousoft.logisticsmobile.driver.model.OnlineSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/****
 * 评价货主
 * 
 * @author lenovo
 */
public class EvaluateActivity extends BaseActivity {

	// 订单号码，订单路线，订单货物名称，装车时间，所用时间
	private TextView mOrderNumber, mOrderLine, mOrderName, mOrderLoadingTime;

	// 详细评价
	private EditText mRemark;

	// 信誉评价，发货信息准确度，履约诚信度，付款及时率
	private RatingBar mScore, mScore1, mScore2, mScore3;

	private Button mSubmit;

	private String id = "", name = "", line = "", zhuangCheTime = "";
	private float scoreFloat;

	private CityDBUtils mDBUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_evaluate);
		initViews();
		initData();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_onlinesource_evaluate);
		mOrderNumber = (TextView) findViewById(R.id.source_id_order_number);
		mOrderLine = (TextView) findViewById(R.id.source_id_detail_line);
		mOrderName = (TextView) findViewById(R.id.source_id_detail_name);
		mOrderLoadingTime = (TextView) findViewById(R.id.source_id_detail_uploadtime);

		mRemark = (EditText) findViewById(R.id.evaluate_id_remark);
		mScore = (RatingBar) findViewById(R.id.evaluate_id_score);
		mScore1 = (RatingBar) findViewById(R.id.evaluate_id_score_true);
		mScore2 = (RatingBar) findViewById(R.id.evaluate_id_score_integrity);
		mScore3 = (RatingBar) findViewById(R.id.evaluate_id_score_timely);

		mSubmit = (Button) findViewById(R.id.evaluate_id_submit);
		mSubmit.setOnClickListener(this);
	}

	private void initData() {
		mDBUtils = new CityDBUtils(application.getCitySDB());

		if (getIntent().hasExtra("historyOrder")) {
			HistoryOrder mSourceInfo = (HistoryOrder) getIntent()
					.getSerializableExtra("historyOrder");
			if (mSourceInfo != null) {

				id = mSourceInfo.getId() + "";
				name = mSourceInfo.getCargo_desc();
				line = mDBUtils.getStartEndStr(mSourceInfo.getStart_province(),
						mSourceInfo.getStart_city(),
						mSourceInfo.getEnd_province(),
						mSourceInfo.getEnd_city());
				zhuangCheTime = TimeUtils.getDetailTime(mSourceInfo
						.getLoading_time());

			}
		} else if (getIntent().hasExtra("onlineSourceInfo")) {
			OnlineSourceInfo mSourceInfo = (OnlineSourceInfo) getIntent()
					.getSerializableExtra("onlineSourceInfo");
			if (mSourceInfo != null) {

				id = mSourceInfo.getId() + "";
				name = mSourceInfo.getCargo_desc();
				line = mDBUtils.getStartEndStr(mSourceInfo.getStart_province(),
						mSourceInfo.getStart_city(),
						mSourceInfo.getEnd_province(),
						mSourceInfo.getEnd_city());
				zhuangCheTime = TimeUtils.getDetailTime(mSourceInfo
						.getLoading_time());

			}
		}

		mOrderNumber.setText(getString(R.string.sourcedetail_number).replace(
				"%s", id + ""));
		mOrderName.setText(name);
		mOrderLine.setText(line);
		mOrderLoadingTime.setText(zhuangCheTime);
		if (scoreFloat == 0) {
			scoreFloat = 5;
		}
		mScore.setRating(scoreFloat);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mSubmit) {
			JSONObject jsonObject = new JSONObject();
			try {
				JSONObject params = new JSONObject();
				params.put("order_id", id);
				params.put("score1", mScore1.getRating());
				params.put("score2", mScore2.getRating());
				params.put("score3", mScore3.getRating());
				params.put("reply_content", mRemark.getText().toString());
				jsonObject.put(Constants.TOKEN, application.getToken());
				jsonObject.put(Constants.ACTION, Constants.RATING_TO_USER);
				jsonObject.put(Constants.JSON, params.toString());
				showProgress("正在提交评论");
				ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
						null, new AjaxCallBack() {

							@Override
							public void receive(int code, Object result) {
								dismissProgress();
								switch (code) {
								case ResultCode.RESULT_OK:
									showMsg("评价成功!");
									finish();
									break;
								case ResultCode.RESULT_FAILED:
									showMsg(result.toString());
									break;
								case ResultCode.RESULT_ERROR:
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
}
