package com.maogousoft.logisticsmobile.driver.activity.other;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

/**
 * 地图
 *
 * @author aliang
 */
public class MapActivity extends BaseActivity implements BDLocationListener, OnGetPoiSearchResultListener {
    // 定位相关
    private LocationClient mLocClient;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;// 是否首次定位
    private View mMore;
    private TextView mTitle;
    private String mMapType;
    private BDLocation location;
    private PoiSearch mPoiSearch = null;
    private String poiName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initViews();
        initData();
    }

    private void initViews() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mTitle = (TextView)findViewById(R.id.titlebar_id_content);
        mMore = findViewById(R.id.titlebar_id_more);
        mMore.setVisibility(View.GONE);

    }

    private void initData() {
        mLocClient = new LocationClient(this);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mMapType = getIntent().getStringExtra(Constants.MAP_TYPE);
        if(Constants.MAP_TYPE_DEFAULT.equals(mMapType)) {
            locationAction();
        } else if(Constants.MAP_TYPE_SHOP.equals(mMapType)) {
            mTitle.setText("商户位置");
            ShopInfo shopInfo = (ShopInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
            location = new BDLocation();
            location.setAddrStr(shopInfo.getVender_address());
            //在服务器上存的反的
            location.setLongitude(shopInfo.getLatitude());
            location.setLatitude(shopInfo.getLongitude());
            location.setRadius(3000);
            showLocation(location);
        } else if (Constants.MAP_TYPE_HOTEL.equals(mMapType) || Constants.MAP_TYPE_GAS.equals(mMapType)
                || Constants.MAP_TYPE_BANK.equals(mMapType)){
            mTitle.setText("附近" + mMapType);
            // 初始化搜索模块，注册搜索事件监听
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(this);
            poiName = mMapType;
            locationAction();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 显示位置
     * @param location
     */
    private void showLocation(BDLocation location) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }
    }

    // 定位初始化
    private void locationAction() {
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(500);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null || !isFirstLoc) {
            return;
        }
        LogUtil.e(TAG, "city:" + location.getCity());
        if(TextUtils.isEmpty(poiName)) {
            showLocation(location);
            mLocClient.stop();
        } else {
            PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            poiNearbySearchOption.location(latLng)
                    .radius(3000)
                    .keyword(poiName);//城市
            mPoiSearch.searchNearby(poiNearbySearchOption);
            location.setRadius(3000);
            showLocation(location);
            mLocClient.stop();
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }

    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            // }
            return true;
        }
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
    protected void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mLocClient.stop();
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
