package com.maogousoft.logisticsmobile.driver.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;

/**
 * 我的信誉
 * 
 * @author ybxiang
 */
public class MyReputationActivity extends BaseActivity {
	private RatingBar mCreditRatingbar;
	private TextView mOnlineTime, mOnlineTimeRank, mRecommonedCount,
			mRecommonedCountRank, mClinch, mClinchRank;
	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myabc_reputation);
		initViews();
		initData(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();// 获取ABCInfo
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("我的信誉");
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		// 信誉记录
		mCreditRatingbar = (RatingBar) findViewById(R.id.myabc_id_ratingbar);
		mCreditRatingbar.setIsIndicator(true);
		// 在线天数
		mOnlineTime = (TextView) findViewById(R.id.myabc_id_onlinetime);
		mOnlineTimeRank = (TextView) findViewById(R.id.myabc_id_onlinetime_rank);
		// 成交单数
		mClinch = (TextView) findViewById(R.id.myabc_id_clinch);
		mClinchRank = (TextView) findViewById(R.id.myabc_id_clinch_rank);
		// 推荐人数
		mRecommonedCount = (TextView) findViewById(R.id.myabc_id_recommendcount);
		mRecommonedCountRank = (TextView) findViewById(R.id.myabc_id_recommendcount_rank);
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

									mOnlineTime.setText(mAbcInfo
											.getOnline_time() + "天");
									mOnlineTimeRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getOnline_time_rank()));
									mRecommonedCount.setText(mAbcInfo
											.getRecommender_count() + "人");
									mRecommonedCountRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getRecommender_count_rank()));
									mClinch.setText(mAbcInfo.getOrder_count()
											+ "单");
									mClinchRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getOrder_count_rank()));

									float score = mAbcInfo.getScore();
									if (score == 0) {
										score = 5;
									}
									mCreditRatingbar.setRating(score);

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
	 * 信誉记录
	 * 
	 * @param view
	 */
	public void onMyCreditContainer(View view) {
		startActivity(new Intent(context, MyCreditActivity.class).putExtra(
				"info", mAbcInfo));
	}
}