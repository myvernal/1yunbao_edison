package com.maogousoft.logisticsmobile.driver.activity.other;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;

/**
 * 里程计算器
 * 
 * @author lenovo
 */
public class DistanceCalcActivity extends BaseActivity implements OnItemClickListener {

	private MKSearch mkSearch = null;

	private MapView mMapView;

	private MyGridView gridView1;

	private CityDBUtils mDBUtils;

	private CityListAdapter mAdapter1;

	private TextView line;

	private ImageView calc_change;

	private Button bty1, bty2, bty3, bty4,search;

	private int city1 = 0, city2 = 0, city3 = 0, city4 = 0, id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_calc);
		initViews();
		initMap();
		initData();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_calc_title);
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		search=(Button)findViewById(R.id.titlebar_id_more);
		search.setText("搜索");
		search.setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.calc_map);

		line = (TextView) findViewById(R.id.calc_line);
		line.setText(R.string.string_other_calc_line);
		bty1 = (Button) findViewById(R.id.calc_bty1);
		bty2 = (Button) findViewById(R.id.calc_bty2);
		bty3 = (Button) findViewById(R.id.calc_bty3);
		bty4 = (Button) findViewById(R.id.calc_bty4);
		bty1.setOnClickListener(this);
		bty2.setOnClickListener(this);
		bty3.setOnClickListener(this);
		bty4.setOnClickListener(this);
		calc_change = (ImageView) findViewById(R.id.calc_change);
		calc_change.setOnClickListener(this);

		gridView1 = (MyGridView) findViewById(R.id.calc_gridview1);
	}

	private void initMap() {
		if (application.getBMapManager() == null) {
			application.initBMapManager();
		}
		mMapView.getController().setZoom(18);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setLongClickable(true);

		mkSearch = new MKSearch();
		mkSearch.init(application.getBMapManager(), searchListener);
	}

	private void initData() {
		mAdapter1 = new CityListAdapter(context);

		gridView1.setOnItemClickListener(this);
		gridView1.setAdapter(mAdapter1);

		mDBUtils = new CityDBUtils(application.getCitySDB());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_id_more:
			check();
			break;
		case R.id.calc_bty1:
			click(1);
			break;
		case R.id.calc_bty2:
			click(2);
			break;
		case R.id.calc_bty3:
			click(3);
			break;
		case R.id.calc_bty4:
			click(4);
			break;
		case R.id.calc_change:
			change();
			break;
		case R.id.titlebar_id_back:
			finish();
			break;
		}
	}

	private void change() {
		Animation a = AnimationUtils.loadAnimation(context, R.anim.anim_view_rotate);
		a.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				int num = city1;
				city1 = city3;
				city3 = num;
				num = city2;
				city2 = city4;
				city4 = num;

				String str = bty1.getText().toString();
				bty1.setText(bty3.getText().toString());
				bty3.setText(str);
				str = bty2.getText().toString();
				bty2.setText(bty4.getText().toString());
				bty4.setText(str);
			}
		});
		calc_change.startAnimation(a);
		a.start();
	}

	private void check() {
		settextt();
		if (city1 == 0 || city2 == 0 || city3 == 0 || city4 == 0) {
			showMsg("请选择省份和城市");
			return;
		}
		if (city1 == city2 && city3 == city4) {
			showMsg("同一个城市不用计算吧");
			return;
		}
		mMapView.setVisibility(View.VISIBLE);
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = bty2.getText().toString();
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = bty4.getText().toString();
		mkSearch.drivingSearch(bty1.getText().toString(), stNode, bty3.getText().toString(), enNode);
		showProgress("计算中...");
	}

	private void click(int position) {
		List<CityInfo> mList = null;
		id = position;
		switch (position) {
		case 1:
			mList = mDBUtils.getFirstCity();
			if (mList != null) {
				mAdapter1.setList(mList);
			}
			break;
		case 2:
			if (city1 == 0) {
				click(1);
			} else {
				mList = mDBUtils.getSecondCity(city1);
				if (mList != null) {
					mAdapter1.setList(mList);
				}
			}
			break;
		case 3:
			mList = mDBUtils.getFirstCity();
			if (mList != null) {
				mAdapter1.setList(mList);
			}
			break;
		case 4:
			if (city3 == 0) {
				click(3);
			} else {
				mList = mDBUtils.getSecondCity(city3);
				if (mList != null) {
					mAdapter1.setList(mList);
				}
			}
			break;
		}
	}

	private void calc(int mm) {
		if (mm != 0) {
			int distance = (int) mm / 1000;
			line.setText(line.getText().toString() + " 里程(" + distance + "Km)");
		}
	}

	private void settextt() {
		StringBuffer sb = new StringBuffer(getResources().getString(R.string.string_other_calc_line));
		if (city2 != 0)
			sb.append(bty2.getText().toString());
		sb.append("—");
		if (city4 != 0)
			sb.append(bty4.getText().toString());
		line.setText(sb);
	}

	private MKSearchListener searchListener = new MKSearchListener() {

		/** 返回驾乘路线搜索结果 */
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int arg1) {
			dismissProgress();
			if (arg1 != 0 || res == null) {
				showMsg("没有找到线路");
				return;
			}
			RouteOverlay ro = new RouteOverlay(DistanceCalcActivity.this, mMapView);
			ro.setData(res.getPlan(0).getRoute(0));
			calc(res.getPlan(0).getDistance());
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(ro);
			mMapView.refresh();
			mMapView.getController().zoomToSpan(ro.getLatSpanE6(), ro.getLonSpanE6());
			mMapView.getController().animateTo(res.getStart().pt);
		}

		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		}

		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
		}

		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		}

		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}

		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		}

		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
		}
	};

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
		final CityInfo mCityInfo = (CityInfo) mAdapter1.getItem(position);
		switch (id) {
		case 1:
			bty1.setText(mCityInfo.getName());
			bty2.setText(R.string.string_home_publish_title_second);
			city1 = mCityInfo.getId();
			city2 = 0;
			mAdapter1.removeAll();
			click(2);
			break;
		case 2:
			bty2.setText(mCityInfo.getName());
			city2 = mCityInfo.getId();
			mAdapter1.removeAll();
			if (city3 == 0)
				click(3);
			break;
		case 3:
			bty3.setText(mCityInfo.getName());
			bty4.setText(R.string.string_home_publish_title_second);
			city3 = mCityInfo.getId();
			city4 = 0;
			mAdapter1.removeAll();
			click(4);
			break;
		case 4:
			bty4.setText(mCityInfo.getName());
			city4 = mCityInfo.getId();
			mAdapter1.removeAll();
			break;
		}
		settextt();
	}
}
