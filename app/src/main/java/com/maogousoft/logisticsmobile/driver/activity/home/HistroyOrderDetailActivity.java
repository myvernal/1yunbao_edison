package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.HistoryOrder;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 已完成订单详情
 * 
 * @author lenovo
 */
public class HistroyOrderDetailActivity extends BaseActivity {

	// 线路，货源名称，货源类型，货源重量,货源价格,装车时间
	private TextView mLine, mName, mType, mWeight, mPrice, mTime, mNumber,
			mCompanyName, mNum, mMark;
	// 评价
	private Button mEvaluate;

	private HistoryOrder mSourceInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_orderdetail);
		initViews();
		initData(savedInstanceState);
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("历史订单详情");


		mCompanyName = (TextView) findViewById(R.id.source_id_detail_company_name);
		mNum = (TextView) findViewById(R.id.source_id_detail_num);
		mMark = (TextView) findViewById(R.id.source_id_detail_mark);

		mNumber = (TextView) findViewById(R.id.source_id_order_number);
		mLine = (TextView) findViewById(R.id.source_id_detail_line);
		mName = (TextView) findViewById(R.id.source_id_detail_name);
		mType = (TextView) findViewById(R.id.source_id_detail_type);
		mWeight = (TextView) findViewById(R.id.source_id_detail_space);
		mPrice = (TextView) findViewById(R.id.source_id_detail_price);
		mTime = (TextView) findViewById(R.id.source_id_detail_uploadtime);
		mEvaluate = (Button) findViewById(R.id.source_id_detail_place);

		mEvaluate.setOnClickListener(this);
	}

	private void initData(Bundle savedInstanceState) {
		if (getIntent().hasExtra("sourceInfo")) {
			mSourceInfo = (HistoryOrder) getIntent().getSerializableExtra(
					"sourceInfo");
			if (mSourceInfo == null) {
				return;
			}
			mNumber.setText(String.format(
					getString(R.string.sourcedetail_number),
					mSourceInfo.getId()));
			// mLine.setText(String.format("%s%s%s-%s%s%s",
			// mSourceInfo.getStart_province(),
			// CheckUtils.checkIsNull(mSourceInfo.getStart_city_str()),
			// CheckUtils.checkIsNull(mSourceInfo.getStart_district_str()),
			// CheckUtils.checkIsNull(mSourceInfo.getEnd_province_str()),
			// CheckUtils.checkIsNull(mSourceInfo.getEnd_city_str()),
			// CheckUtils.checkIsNull(mSourceInfo.getEnd_district_str())));

			CityDBUtils mDBUtils = new CityDBUtils(application.getCitySDB());
			String path1Str = mDBUtils.getStartEndStr(
					mSourceInfo.getStart_province(),
					mSourceInfo.getStart_city(), mSourceInfo.getEnd_province(),
					mSourceInfo.getEnd_city());

			mLine.setText(path1Str);

			mName.setText(mSourceInfo.getCargo_desc());
			mType.setText(mSourceInfo.getCargo_type_str());
			mWeight.setText(mSourceInfo.getCargo_number()
					+ CheckUtils.getCargoUnitName(context, mSourceInfo.getCargo_unit()));
			mPrice.setText(String.format(
					getString(R.string.sourcedetail_price),
					mSourceInfo.getPrice()));
			mTime.setText(TimeUtils.getDetailTime(mSourceInfo.getLoading_time()));

			mCompanyName.setText(mSourceInfo.getCompany_name());
			mNum.setText(mSourceInfo.getCargo_unit() + "");
			mMark.setText(mSourceInfo.getCargo_remark());

		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mEvaluate) {
			if (mSourceInfo != null) {
				startActivity(new Intent(context, EvaluateActivity.class)
						.putExtra("historyOrder", mSourceInfo));
			}
		}
	}
}
