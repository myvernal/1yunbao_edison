package com.maogousoft.logisticsmobile.driver;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;

public class CitySelectView extends LinearLayout {

	/**
	 * 有省市区
	 */
	public static final int TYPE_ONE = 1;
	/**
	 * 有省市
	 */
	public static final int TYPE_TWO = 2;

	/**
	 * 无
	 */
	public static final int GRIDVIEW_TYPE_NULL = 0;
	/**
	 * 省
	 */
	public static final int GRIDVIEW_TYPE_PROVINCE = 1;
	/**
	 * 市
	 */
	public static final int GRIDVIEW_TYPE_CITY = 2;
	/**
	 * 区县
	 */
	public static final int GRIDVIEW_TYPE_TOWNS = 3;

	/**
	 * 当前view类型
	 */
	private int currentViewType;

	/**
	 * 当前gridview 显示的类型
	 */
	private int currentGridViewType;

	private Context ctx;

	private Button btnProvince, btnCity, btnTowns;
	private GridView gridview;
	private RelativeLayout layoutControlPanel;
	private Button btnClose, btnClear;

	private CityListAdapter cityAdapter;
	private CityInfo selectedProvince, selectedCity, selectedTowns;
	private CityDBUtils mDBUtils;

	private boolean isAutoShowNext = false;// 是否在选择省之后，自动显示市。是否在选择市之后，自动显示区县

	public CitySelectView(final Context context, AttributeSet attrs) {
		super(context, attrs);

		ctx = context;
		View view = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.view_city_select, this, true);

		btnProvince = (Button) view.findViewById(R.id.btn_city_select_province);
		btnCity = (Button) view.findViewById(R.id.btn_city_select_city);
		btnTowns = (Button) view.findViewById(R.id.btn_city_select_towns);
		gridview = (GridView) view.findViewById(R.id.gridview_city_select);

		layoutControlPanel = (RelativeLayout) view
				.findViewById(R.id.layout_control_panel_city_select);

		btnClose = (Button) view.findViewById(R.id.btn_city_select_close);
		btnClear = (Button) view.findViewById(R.id.btn_city_select_clear);

		initListener();
		initData();

	}

	private void initListener() {

		btnProvince.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				clickBtnProvince();

			}
		});
		btnCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickBtnCity();
			}
		});
		btnTowns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickBtnTowns();
			}
		});

		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btnProvince.setText("省份");
				btnCity.setText("城市");
				btnTowns.setText("区县");
				selectedProvince = null;
				selectedCity = null;
				selectedTowns = null;

				if (gridview.isShown()) {
					hideView();
				}

			}
		});

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gridview.isShown()) {
					hideView();
				}
			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (!isAutoShowNext) {
					hideView();
				}
				final CityInfo mCityInfo = (CityInfo) cityAdapter
						.getItem(position);
				final int mDeep = mCityInfo.getDeep();
				switch (mDeep) {
				case 1:
					selectedCity = null;
					selectedTowns = null;
					btnCity.setText("城市");
					btnTowns.setText("区县");

					selectedProvince = mCityInfo;
					btnProvince.setText(mCityInfo.getName());

					if (isAutoShowNext) {
						clickBtnCity();
					}

					break;
				case 2:

					selectedTowns = null;
					btnTowns.setText("区县");

					selectedCity = mCityInfo;
					btnCity.setText(mCityInfo.getName());

					if (isAutoShowNext) {
						clickBtnTowns();
					}

					break;

				case 3:
					selectedTowns = mCityInfo;
					btnTowns.setText(mCityInfo.getName());

					if (isAutoShowNext) {
						hideView();
					}
					break;

				}
			}
		});

	}

	private void initData() {

		mDBUtils = new CityDBUtils(
				((MGApplication) ctx.getApplicationContext()).getCitySDB());
		cityAdapter = new CityListAdapter(ctx);
		gridview.setAdapter(cityAdapter);
	}

	// 点击 省份 button
	private void clickBtnProvince() {
		// 如果当前有gridview显示
		if (gridview.isShown()) {
			// 这里要判断，上一次是否是点击的 省份。如果是，就消失
			if (currentGridViewType == GRIDVIEW_TYPE_PROVINCE) {

				hideView();

			} else {

				// 如果不是，那么就显示省份列表

				currentGridViewType = GRIDVIEW_TYPE_PROVINCE;
				List<CityInfo> mList = mDBUtils.getFirstCity();
				cityAdapter.setList(mList);

			}
		} else {

			// 如果当前没有gridview显示，那么直接显示 省份列表
			currentGridViewType = GRIDVIEW_TYPE_PROVINCE;
			gridview.setVisibility(View.VISIBLE);
			layoutControlPanel.setVisibility(View.VISIBLE);
			List<CityInfo> mList = mDBUtils.getFirstCity();
			cityAdapter.setList(mList);
		}
	}

	// 点击 城市 button
	private void clickBtnCity() {
		if (selectedProvince == null) {
			Toast.makeText(ctx, "请先选择省份", Toast.LENGTH_SHORT).show();
			return;
		}
		if (gridview.isShown()) {

			if (currentGridViewType == GRIDVIEW_TYPE_CITY) {

				hideView();

			} else {

				currentGridViewType = GRIDVIEW_TYPE_CITY;
				List<CityInfo> mList = mDBUtils.getSecondCity(selectedProvince
						.getId());
				cityAdapter.setList(mList);
			}

		} else {
			currentGridViewType = GRIDVIEW_TYPE_CITY;
			gridview.setVisibility(View.VISIBLE);
			layoutControlPanel.setVisibility(View.VISIBLE);
			List<CityInfo> mList = mDBUtils.getSecondCity(selectedProvince
					.getId());
			cityAdapter.setList(mList);
		}
	}

	// 点击 区县 button
	private void clickBtnTowns() {
		if (selectedProvince == null) {
			Toast.makeText(ctx, "请先选择省份", Toast.LENGTH_SHORT).show();
			return;
		}
		if (selectedCity == null) {
			Toast.makeText(ctx, "请先选择城市", Toast.LENGTH_SHORT).show();
			return;
		}

		if (gridview.isShown()) {
			if (currentGridViewType == GRIDVIEW_TYPE_TOWNS) {

				hideView();
			} else {

				currentGridViewType = GRIDVIEW_TYPE_TOWNS;
				List<CityInfo> mList = mDBUtils.getThridCity(selectedCity
						.getId());
				cityAdapter.setList(mList);
			}
		} else {
			currentGridViewType = GRIDVIEW_TYPE_TOWNS;
			gridview.setVisibility(View.VISIBLE);
			layoutControlPanel.setVisibility(View.VISIBLE);
			List<CityInfo> mList = mDBUtils.getThridCity(selectedCity.getId());
			cityAdapter.setList(mList);
		}
	}

	// 隐藏gridview和控制面板
	private void hideView() {
		currentGridViewType = GRIDVIEW_TYPE_NULL;
		gridview.setVisibility(View.GONE);
		layoutControlPanel.setVisibility(View.GONE);
	}

	public CityInfo getSelectedProvince() {

		return selectedProvince;
	}

	public CityInfo getSelectedCity() {

		return selectedCity;
	}

	public CityInfo getSelectedTowns() {

		return selectedTowns;

	}

}
