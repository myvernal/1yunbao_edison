package com.maogousoft.logisticsmobile.driver.activity.other;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

/**
 * 地图
 * 
 * @author ashuang
 */
public class MapActivity extends BaseActivity implements BDLocationListener {

	private MapView mMapView;

	/** 地图控制器(移动，放大，缩小，旋转，俯视角度) */
	private MapController mMapController = null;

	/** 定位相关 控制器 */
	private LocationClient mLocClient;

	/** 一个显示用户当前位置的Overlay */
	private MyLocationOverlay myLocationOverlay = null;

	private MKSearch mSearch = null;

	private String keyWord = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_other_map);
		initData();
		initView();
		initMap();
	}

	private void initView() {
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_map_round);
		mMapView = (MapView) findViewById(R.id.motorcade_map);
	}

	private void initData() {
		if (getIntent().hasExtra("keyWord"))
			keyWord = getIntent().getStringExtra("keyWord");
	}

	private void initMap() {
		if (application.getBMapManager() == null) {
			application.initBMapManager();
		}
		mMapController = mMapView.getController();
		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.enableCompass(); // 打开指南针
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(200); // 定位时间，毫秒 小于1秒则一次定位;大于等于1秒则定时定位
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(this);
		mLocClient.setLocOption(option);
		mLocClient.start();

		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setLongClickable(true);
		mMapView.getOverlays().add(myLocationOverlay);

		mSearch = new MKSearch();
		mSearch.init(application.getBMapManager(), new MKSearchListener() {

			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
				// 返回步行路线搜索结果
			}

			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
				// 返回公交搜索结果
			}

			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// 返回联想词信息搜索结果
			}

			public void onGetPoiResult(MKPoiResult result, int arg1, int arg2) {
				/**
				 * * POI搜索结果（范围检索、城市POI检索、周边检索） * * @param result 搜索结果 * @param
				 * type 返回结果类型（11,12,21:poi列表 7:城市列表） * @param iError
				 * 错误号（0表示正确返回）
				 */
				if (result == null) {
					return;
				}
				// PoiOverlay是baidu map api提供的用于显示POI的Overlay
				PoiOverlay poioverlay = new PoiOverlay(MapActivity.this, mMapView);
				// 设置搜索到的POI数据
				poioverlay.setData(result.getAllPoi());
				// 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）
				mMapView.getOverlays().add(poioverlay);
				mMapView.refresh();
			}

			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// 返回poi相信信息搜索的结果
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
				/** * 驾车路线搜索结果 * * @param result 搜索结果 * @param iError 错误号 */

			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// 返回公交车详情信息搜索结果
			}

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
				/**
				 * * 根据经纬度搜索地址信息结果 * * @param result 搜索结果 * @param iError 错误号
				 * （0表示正确返回）
				 */
			}
		});
	}

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
	public void onClick(View v) {
		super.onClick(v);
		if (R.id.titlebar_id_more == v.getId()) {
			mLocClient.requestLocation();
		}
	}

	@Override
	protected void onDestroy() {
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 保存地图状态
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 监听函数，有新位置的时候, 更新位置标志，移动到地图中心点
	 */
	public void onReceiveLocation(BDLocation location) {
		if (location != null) {

			try {
				// 位置信息
				LocationData locData = new LocationData();
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				GeoPoint geoPoint = new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6));
				// 更新位置标志
				myLocationOverlay.setData(locData);

				if (!keyWord.equals(""))
					mSearch.poiSearchNearBy(keyWord, geoPoint, Constants.DISTANCE);
				mMapView.refresh();
				// 移动到地图中心点
				mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {

	}
}
