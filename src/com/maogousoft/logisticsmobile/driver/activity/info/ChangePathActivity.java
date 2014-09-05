package com.maogousoft.logisticsmobile.driver.activity.info;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;

/**
 * 改变线路
 * 
 * @author lenovo
 */
public class ChangePathActivity extends BaseActivity {

	private Button mBack, mSubmit;

	private TextView tvPath1, tvPath2, tvPath3;

	private ImageView ivJiantou1, ivJiantou2, ivJiantou3;

	private Button mSelectProvinceFirst, mSelectCityFirst,
			mSelectEndProvinceFirst, mSelectEndCityFirst;

	private Button mSelectProvinceSecond, mSelectCitySecond,
			mSelectEndProvinceSecond, mSelectEndCitySecond;

	private Button mSelectProvinceThird, mSelectCityThird,
			mSelectEndProvinceThird, mSelectEndCityThird;

	private MyGridView mGridViewFirst, mGridViewFirst2;

	private MyGridView mGridViewSecond, mGridViewSecond2;

	private MyGridView mGridViewThird, mGridViewThird2;

	private CityDBUtils mDBUtils;

	private CityListAdapter mAdapterFirst, mAdapterFirst2;

	private CityListAdapter mAdapterSecond, mAdapterSecond2;

	private CityListAdapter mAdapterThird, mAdapterThird2;

	// 当前选中线路1的一级城市，二级城市
	private CityInfo currentProvinceFirst, currentCityFirst,
			currentEndProvinceFirst, currentEndCityFirst;
	// 当前选中线路2的一级城市，二级城市
	private CityInfo currentProvinceSecond, currentCitySecond,
			currentEndProvinceSecond, currentEndCitySecond;
	// 当前选中的线路3一级城市，二级城市
	private CityInfo currentProvinceThird, currentCityThird,
			currentEndProvinceThird, currentEndCityThird;

	private int changePath = 0;// 需要修改的线路

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_changepath);
		initViews();
		initUtils();
		initDatas();
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("修改线路");
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mSubmit = (Button) findViewById(R.id.info_id_register_submit);

		tvPath1 = (TextView) findViewById(R.id.tv_path1);
		tvPath2 = (TextView) findViewById(R.id.tv_path2);
		tvPath3 = (TextView) findViewById(R.id.tv_path3);

		ivJiantou1 = (ImageView) findViewById(R.id.iv_jiantou_path1);
		ivJiantou2 = (ImageView) findViewById(R.id.iv_jiantou_path2);
		ivJiantou3 = (ImageView) findViewById(R.id.iv_jiantou_path3);

		mSelectProvinceFirst = (Button) findViewById(R.id.info_id_register_select_province);
		mSelectCityFirst = (Button) findViewById(R.id.info_id_register_select_city);
		mSelectEndProvinceFirst = (Button) findViewById(R.id.info_id_register_select_end_province);
		mSelectEndCityFirst = (Button) findViewById(R.id.info_id_register_select_end_city);

		mSelectProvinceSecond = (Button) findViewById(R.id.info_id_register_select_province_second);
		mSelectCitySecond = (Button) findViewById(R.id.info_id_register_select_city_second);
		mSelectEndProvinceSecond = (Button) findViewById(R.id.info_id_register_select_end_province_second);
		mSelectEndCitySecond = (Button) findViewById(R.id.info_id_register_select_end_city_second);

		mSelectProvinceThird = (Button) findViewById(R.id.info_id_register_select_province_third);
		mSelectCityThird = (Button) findViewById(R.id.info_id_register_select_city_third);
		mSelectEndProvinceThird = (Button) findViewById(R.id.info_id_register_select_end_province_third);
		mSelectEndCityThird = (Button) findViewById(R.id.info_id_register_select_end_city_third);

		mGridViewFirst = (MyGridView) findViewById(R.id.info_id_register_city);
		mGridViewFirst2 = (MyGridView) findViewById(R.id.info_id_register_endcity);

		mGridViewSecond = (MyGridView) findViewById(R.id.info_id_register_city_second);
		mGridViewSecond2 = (MyGridView) findViewById(R.id.info_id_register_endcity_second);

		mGridViewThird = (MyGridView) findViewById(R.id.info_id_register_city_third);
		mGridViewThird2 = (MyGridView) findViewById(R.id.info_id_register_endcity_third);

		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mSelectProvinceFirst.setOnClickListener(this);
		mSelectCityFirst.setOnClickListener(this);
		mSelectEndProvinceFirst.setOnClickListener(this);
		mSelectEndCityFirst.setOnClickListener(this);

		mSelectProvinceSecond.setOnClickListener(this);
		mSelectCitySecond.setOnClickListener(this);
		mSelectEndProvinceSecond.setOnClickListener(this);
		mSelectEndCitySecond.setOnClickListener(this);

		mSelectProvinceThird.setOnClickListener(this);
		mSelectCityThird.setOnClickListener(this);
		mSelectEndProvinceThird.setOnClickListener(this);
		mSelectEndCityThird.setOnClickListener(this);

	}

	/** 初始化工具类 **/
	private void initUtils() {
		mDBUtils = new CityDBUtils(application.getCitySDB());
		mAdapterFirst = new CityListAdapter(context);
		mAdapterFirst2 = new CityListAdapter(context);

		mGridViewFirst.setAdapter(mAdapterFirst);
		mGridViewFirst2.setAdapter(mAdapterFirst2);
		mGridViewFirst.setOnItemClickListener(mOnItemClickListenerFirst);
		mGridViewFirst2.setOnItemClickListener(mOnItemClickListenerFirst2);

		mAdapterSecond = new CityListAdapter(context);
		mAdapterSecond2 = new CityListAdapter(context);

		mGridViewSecond.setAdapter(mAdapterSecond);
		mGridViewSecond2.setAdapter(mAdapterSecond2);
		mGridViewSecond.setOnItemClickListener(mOnItemClickListenerSecond);
		mGridViewSecond2.setOnItemClickListener(mOnItemClickListenerSecond2);

		mAdapterThird = new CityListAdapter(context);
		mAdapterThird2 = new CityListAdapter(context);

		mGridViewThird.setAdapter(mAdapterThird);
		mGridViewThird2.setAdapter(mAdapterThird2);
		mGridViewThird.setOnItemClickListener(mOnItemClickListenerThird);
		mGridViewThird2.setOnItemClickListener(mOnItemClickListenerThird2);
	}

	// 初始化数据，获取车型
	private void initDatas() {

		changePath = getIntent().getIntExtra("path", 0);

		if (changePath == 0) {

			tvPath1.setVisibility(View.VISIBLE);
			ivJiantou1.setVisibility(View.VISIBLE);

			mSelectProvinceFirst.setVisibility(View.VISIBLE);
			mSelectCityFirst.setVisibility(View.VISIBLE);
			mSelectEndProvinceFirst.setVisibility(View.VISIBLE);
			mSelectEndCityFirst.setVisibility(View.VISIBLE);

			// mGridViewFirst.setVisibility(View.VISIBLE);
			// mGridViewFirst2.setVisibility(View.VISIBLE);

		} else if (changePath == 1) {

			tvPath2.setVisibility(View.VISIBLE);
			ivJiantou2.setVisibility(View.VISIBLE);

			mSelectProvinceSecond.setVisibility(View.VISIBLE);
			mSelectCitySecond.setVisibility(View.VISIBLE);
			mSelectEndProvinceSecond.setVisibility(View.VISIBLE);
			mSelectEndCitySecond.setVisibility(View.VISIBLE);

			// mGridViewSecond.setVisibility(View.VISIBLE);
			// mGridViewSecond2.setVisibility(View.VISIBLE);

		} else if (changePath == 2) {

			tvPath3.setVisibility(View.VISIBLE);
			ivJiantou3.setVisibility(View.VISIBLE);

			mSelectProvinceThird.setVisibility(View.VISIBLE);
			mSelectCityThird.setVisibility(View.VISIBLE);
			mSelectEndProvinceThird.setVisibility(View.VISIBLE);
			mSelectEndCityThird.setVisibility(View.VISIBLE);

			// mGridViewThird.setVisibility(View.VISIBLE);
			// mGridViewThird2.setVisibility(View.VISIBLE);
		}

	}

	private OnItemClickListener mOnItemClickListenerFirst2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewFirst.isShown()) {
				mGridViewFirst.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterFirst2
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentEndCityFirst = null;
				currentEndProvinceFirst = mCityInfo;
				mSelectEndProvinceFirst.setText(mCityInfo.getName());
				mSelectEndCityFirst.setText(R.string.string_city);
				List<CityInfo> mList4 = mDBUtils
						.getSecondCity(currentEndProvinceFirst.getId());
				mAdapterFirst2.setList(mList4);
				break;
			case 2:
				currentEndCityFirst = mCityInfo;
				mSelectEndCityFirst.setText(mCityInfo.getName());
				mGridViewFirst2.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	private OnItemClickListener mOnItemClickListenerFirst = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewFirst2.isShown()) {
				mGridViewFirst2.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterFirst
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentCityFirst = null;
				currentProvinceFirst = mCityInfo;
				mSelectProvinceFirst.setText(mCityInfo.getName());
				mSelectCityFirst.setText(R.string.string_city);
				List<CityInfo> mList2 = mDBUtils
						.getSecondCity(currentProvinceFirst.getId());
				mAdapterFirst.setList(mList2);
				break;
			case 2:
				currentCityFirst = mCityInfo;
				mSelectCityFirst.setText(mCityInfo.getName());
				mGridViewFirst.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	private OnItemClickListener mOnItemClickListenerSecond2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewSecond.isShown()) {
				mGridViewSecond.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterSecond2
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentEndCitySecond = null;
				currentEndProvinceSecond = mCityInfo;
				mSelectEndProvinceSecond.setText(mCityInfo.getName());
				mSelectEndCitySecond.setText(R.string.string_city);
				List<CityInfo> mList4 = mDBUtils
						.getSecondCity(currentEndProvinceSecond.getId());
				mAdapterSecond2.setList(mList4);
				break;
			case 2:
				currentEndCitySecond = mCityInfo;
				mSelectEndCitySecond.setText(mCityInfo.getName());
				mGridViewSecond2.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	private OnItemClickListener mOnItemClickListenerSecond = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewSecond2.isShown()) {
				mGridViewSecond2.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterSecond
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentCitySecond = null;
				currentProvinceSecond = mCityInfo;
				mSelectProvinceSecond.setText(mCityInfo.getName());
				mSelectCitySecond.setText(R.string.string_city);
				List<CityInfo> mList2 = mDBUtils
						.getSecondCity(currentProvinceSecond.getId());
				mAdapterSecond.setList(mList2);
				break;
			case 2:
				currentCitySecond = mCityInfo;
				mSelectCitySecond.setText(mCityInfo.getName());
				mGridViewSecond.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	private OnItemClickListener mOnItemClickListenerThird2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewThird.isShown()) {
				mGridViewThird.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterThird2
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentEndCityThird = null;
				currentEndProvinceThird = mCityInfo;
				mSelectEndProvinceThird.setText(mCityInfo.getName());
				mSelectEndCityThird.setText(R.string.string_city);
				List<CityInfo> mList4 = mDBUtils
						.getSecondCity(currentEndProvinceThird.getId());
				mAdapterThird2.setList(mList4);
				break;
			case 2:
				currentEndCityThird = mCityInfo;
				mSelectEndCityThird.setText(mCityInfo.getName());
				mGridViewThird2.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	private OnItemClickListener mOnItemClickListenerThird = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			if (mGridViewThird2.isShown()) {
				mGridViewThird2.setVisibility(View.GONE);
			}
			final CityInfo mCityInfo = (CityInfo) mAdapterThird
					.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
			case 1:
				currentCityThird = null;
				currentProvinceThird = mCityInfo;
				mSelectProvinceThird.setText(mCityInfo.getName());
				mSelectCityThird.setText(R.string.string_city);
				List<CityInfo> mList2 = mDBUtils
						.getSecondCity(currentProvinceThird.getId());
				mAdapterThird.setList(mList2);
				break;
			case 2:
				currentCityThird = mCityInfo;
				mSelectCityThird.setText(mCityInfo.getName());
				mGridViewThird.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {
		case R.id.info_id_register_select_province:
			if (mGridViewFirst.isShown()) {
				mGridViewFirst.setVisibility(View.GONE);
				return;
			}
			if (mGridViewFirst2.isShown()) {
				mGridViewFirst2.setVisibility(View.GONE);
			}
			mGridViewFirst.setVisibility(View.VISIBLE);
			List<CityInfo> mList = mDBUtils.getFirstCity();
			mAdapterFirst.setList(mList);
			break;
		case R.id.info_id_register_select_city:
			if (currentProvinceFirst == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewFirst2.isShown()) {
				mGridViewFirst2.setVisibility(View.GONE);
			}
			if (mGridViewFirst.isShown()) {
				mGridViewFirst.setVisibility(View.GONE);
				return;
			}
			mGridViewFirst.setVisibility(View.VISIBLE);
			List<CityInfo> mList2 = mDBUtils.getSecondCity(currentProvinceFirst
					.getId());
			mAdapterFirst.setList(mList2);
			break;
		case R.id.info_id_register_submit:
			submit();
			break;
		case R.id.info_id_register_select_end_province:
			if (mGridViewFirst2.isShown()) {
				mGridViewFirst2.setVisibility(View.GONE);
				return;
			}
			if (mGridViewFirst.isShown()) {
				mGridViewFirst.setVisibility(View.GONE);
			}
			mGridViewFirst2.setVisibility(View.VISIBLE);
			List<CityInfo> mList3 = mDBUtils.getFirstCity();
			mAdapterFirst2.setList(mList3);
			break;
		case R.id.info_id_register_select_end_city:
			if (currentEndProvinceFirst == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewFirst.isShown()) {
				mGridViewFirst.setVisibility(View.GONE);
			}
			if (mGridViewFirst2.isShown()) {
				mGridViewFirst2.setVisibility(View.GONE);
				return;
			}
			mGridViewFirst2.setVisibility(View.VISIBLE);
			List<CityInfo> mList4 = mDBUtils
					.getSecondCity(currentEndProvinceFirst.getId());
			mAdapterFirst2.setList(mList4);
			break;

		case R.id.info_id_register_select_province_second:
			if (mGridViewSecond.isShown()) {
				mGridViewSecond.setVisibility(View.GONE);
				return;
			}
			if (mGridViewSecond2.isShown()) {
				mGridViewSecond2.setVisibility(View.GONE);
			}
			mGridViewSecond.setVisibility(View.VISIBLE);
			List<CityInfo> mList5 = mDBUtils.getFirstCity();
			mAdapterSecond.setList(mList5);
			break;
		case R.id.info_id_register_select_city_second:
			if (currentProvinceSecond == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewSecond2.isShown()) {
				mGridViewSecond2.setVisibility(View.GONE);
			}
			if (mGridViewSecond.isShown()) {
				mGridViewSecond.setVisibility(View.GONE);
				return;
			}
			mGridViewSecond.setVisibility(View.VISIBLE);
			List<CityInfo> mList6 = mDBUtils
					.getSecondCity(currentProvinceSecond.getId());
			mAdapterSecond.setList(mList6);
			break;
		case R.id.info_id_register_select_end_province_second:
			if (mGridViewSecond2.isShown()) {
				mGridViewSecond2.setVisibility(View.GONE);
				return;
			}
			if (mGridViewSecond.isShown()) {
				mGridViewSecond.setVisibility(View.GONE);
			}
			mGridViewSecond2.setVisibility(View.VISIBLE);
			List<CityInfo> mList7 = mDBUtils.getFirstCity();
			mAdapterSecond2.setList(mList7);
			break;
		case R.id.info_id_register_select_end_city_second:
			if (currentEndProvinceSecond == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewSecond.isShown()) {
				mGridViewSecond.setVisibility(View.GONE);
			}
			if (mGridViewSecond2.isShown()) {
				mGridViewSecond2.setVisibility(View.GONE);
				return;
			}
			mGridViewSecond2.setVisibility(View.VISIBLE);
			List<CityInfo> mList8 = mDBUtils
					.getSecondCity(currentEndProvinceSecond.getId());
			mAdapterSecond2.setList(mList8);
			break;

		case R.id.info_id_register_select_province_third:
			if (mGridViewThird.isShown()) {
				mGridViewThird.setVisibility(View.GONE);
				return;
			}
			if (mGridViewThird2.isShown()) {
				mGridViewThird2.setVisibility(View.GONE);
			}
			mGridViewThird.setVisibility(View.VISIBLE);
			List<CityInfo> mList9 = mDBUtils.getFirstCity();
			mAdapterThird.setList(mList9);
			break;
		case R.id.info_id_register_select_city_third:
			if (currentProvinceThird == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewThird2.isShown()) {
				mGridViewThird2.setVisibility(View.GONE);
			}
			if (mGridViewThird.isShown()) {
				mGridViewThird.setVisibility(View.GONE);
				return;
			}
			mGridViewThird.setVisibility(View.VISIBLE);
			List<CityInfo> mList10 = mDBUtils
					.getSecondCity(currentProvinceThird.getId());
			mAdapterThird.setList(mList10);
			break;
		case R.id.info_id_register_select_end_province_third:
			if (mGridViewThird2.isShown()) {
				mGridViewThird2.setVisibility(View.GONE);
				return;
			}
			if (mGridViewThird.isShown()) {
				mGridViewThird.setVisibility(View.GONE);
			}
			mGridViewThird2.setVisibility(View.VISIBLE);
			List<CityInfo> mList11 = mDBUtils.getFirstCity();
			mAdapterThird2.setList(mList11);
			break;
		case R.id.info_id_register_select_end_city_third:
			if (currentEndProvinceThird == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridViewThird.isShown()) {
				mGridViewThird.setVisibility(View.GONE);
			}
			if (mGridViewThird2.isShown()) {
				mGridViewThird2.setVisibility(View.GONE);
				return;
			}
			mGridViewThird2.setVisibility(View.VISIBLE);
			List<CityInfo> mList12 = mDBUtils
					.getSecondCity(currentEndProvinceThird.getId());
			mAdapterThird2.setList(mList12);
			break;

		default:
			break;
		}
	}

	// 输入框失去焦点事件监听
	private final OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

		}
	};

	// 提交
	private void submit() {

		if (changePath == 0) {

			if (currentProvinceFirst == null || currentCityFirst == null
					|| currentEndProvinceFirst == null
					|| currentEndCityFirst == null) {
				showMsg("请选择省市");
				return;
			}

		} else if (changePath == 1) {

			if (currentProvinceSecond == null || currentCitySecond == null
					|| currentEndProvinceSecond == null
					|| currentEndCitySecond == null) {
				showMsg("请选择省市");
				return;
			}

		} else if (changePath == 2) {
			if (currentProvinceThird == null || currentCityThird == null
					|| currentEndProvinceThird == null
					|| currentEndCityThird == null) {
				showMsg("请选择省市");
				return;
			}
		}

		final JSONObject jsonObject = new JSONObject();
		final JSONObject params = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_UPDATE_LINE);
			jsonObject.put(Constants.TOKEN, application.getToken());

			if (currentProvinceFirst != null && currentCityFirst != null
					&& currentEndProvinceFirst != null
					&& currentEndCityFirst != null) {
				params.put("start_province", currentProvinceFirst.getId());
				params.put("start_city", currentCityFirst.getId());
				params.put("end_province", currentEndProvinceFirst.getId());
				params.put("end_city", currentEndCityFirst.getId());
			}

			if (currentProvinceSecond != null && currentCitySecond != null
					&& currentEndProvinceSecond != null
					&& currentEndCitySecond != null) {
				params.put("start_province2", currentProvinceSecond.getId());
				params.put("start_city2", currentCitySecond.getId());
				params.put("end_province2", currentEndProvinceSecond.getId());
				params.put("end_city2", currentEndCitySecond.getId());
			}

			if (currentProvinceThird != null && currentCityThird != null
					&& currentEndProvinceThird != null
					&& currentEndCityThird != null) {
				params.put("start_province3", currentProvinceThird.getId());
				params.put("start_city3", currentCityThird.getId());
				params.put("end_province3", currentEndProvinceThird.getId());
				params.put("end_city3", currentEndCityThird.getId());
			}

			params.put("device_type", Constants.DEVICE_TYPE);
			jsonObject.put(Constants.JSON, params);
			showSpecialProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					UserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								setResult(RESULT_OK);
								showMsg("线路修改成功.");
								finish();
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

}
