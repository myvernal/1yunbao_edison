package com.maogousoft.logisticsmobile.driver.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.CloudSearchAdapter;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CarCloudSearchActivity extends BaseActivity implements BDLocationListener, CloudListener {

	private static final String LTAG = "CloudSearchActivity";
	private MapView mMapView;
	private BaiduMap mBaiduMap;
    private RelativeLayout markerView;
    private InfoWindow mInfoWindow;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;// 是否首次定位
    private Button mBack, mMore;
    private TextView mTitle;
    private int currentModel = CURRENT_MODEL_MAP;
    private static final int CURRENT_MODEL_MAP = 0;
    private static final int CURRENT_MODEL_LIST = 1;
    private View listContainer;
    private BaseListAdapter mAdapter;
    private ListView mListView;
    private View mEmpty;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_lbssearch);
        initViews();
        initData();
	}

    private void initViews() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mTitle = (TextView)findViewById(R.id.titlebar_id_content);
        mTitle.setText("附近车源");
        mMore = (Button) findViewById(R.id.titlebar_id_more);
        mMore.setText("查看列表");
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mBack.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 在没有获取到位置的时候默认不显示定位图层
        mBaiduMap.setMyLocationEnabled(true);
        listContainer = findViewById(R.id.listContainer);
        mListView = (ListView) findViewById(android.R.id.list);
        mEmpty = findViewById(android.R.id.empty);
        //图层点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                p.y -= 75;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        LatLng llNew = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
                        marker.setPosition(llNew);
                        mBaiduMap.hideInfoWindow();
                    }
                };
                //RelativeLayout popupView = getMarkerView(marker);
                TextView textView = new TextView(context);
                String nameStr = marker.getExtraInfo().getString("name");
                String addressStr = marker.getExtraInfo().getString("address");
                String phoneStr = marker.getExtraInfo().getString("phone");
                String locationTime = marker.getExtraInfo().getString("locationTime");
                textView.setPadding(40,40,40,40);
                textView.setText(nameStr + "\n\n" + "地址:" + addressStr +
                        "\n\n" + "时间:" + locationTime +
                        "\n\n" + "联系电话:" + phoneStr);
                textView.setBackgroundResource(R.drawable.popup);
                mInfoWindow = new InfoWindow(textView, llInfo, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    private RelativeLayout getMarkerView(Marker marker) {
        if(markerView == null) {
            markerView = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker_detail_layout, null);
        }
        String nameStr = marker.getExtraInfo().getString("name");
        String addressStr = marker.getExtraInfo().getString("address");
        String phoneStr = marker.getExtraInfo().getString("phone");

        TextView name = (TextView) markerView.findViewById(R.id.name);
        TextView address = (TextView) markerView.findViewById(R.id.address);
        TextView phone = (TextView) markerView.findViewById(R.id.phone);

        name.setText(nameStr);
        address.setText("地址:" + addressStr);
        phone.setText("联系电话:" + phoneStr);
        return markerView;
    }

    private void initData() {
        CloudManager.getInstance().init(this);
        mLocClient = new LocationClient(this);
        // 初始化adapter
        mAdapter = new CloudSearchAdapter(context);
        mListView.setAdapter(mAdapter);
        //开始定位
        locationAction();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                if(currentModel == CURRENT_MODEL_MAP) {
                    currentModel = CURRENT_MODEL_LIST;
                    listContainer.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                    //列表数据处理
                    if (mAdapter.getCount()  <= 0) {
                        mEmpty.setVisibility(View.VISIBLE);
                    }
                    mMore.setText("查看地图");
                } else {
                    currentModel = CURRENT_MODEL_MAP;
                    listContainer.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);
                    mMore.setText("查看列表");
                }
                break;
        }
    }

    public void onGetDetailSearchResult(DetailSearchResult result, int error) {
		if (result != null) {
			if (result.poiInfo != null) {
				Toast.makeText(context, result.poiInfo.title, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CarCloudSearchActivity.this, "status:" + result.status, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void onGetSearchResult(CloudSearchResult result, int error) {
        //添加列表数据
        final List<CarInfo> mList = new ArrayList<CarInfo>();
		if (result != null && result.poiList != null && result.poiList.size() > 0) {
			Log.d(LTAG, "onGetSearchResult, result length: " + result.poiList.size());
			mBaiduMap.clear();
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			LatLng ll;
			LatLngBounds.Builder builder = new Builder();
			for (CloudPoiInfo info : result.poiList) {
				ll = new LatLng(info.latitude, info.longitude);
                Bundle bundle = new Bundle();
                bundle.putString("name", info.title);
                bundle.putString("address", info.address);
                if(null != info.extras.get("locationTime")) {
                    bundle.putString("locationTime", info.extras.get("locationTime").toString());
                }
                if(null != info.extras.get("phone")) {
                    bundle.putString("phone", info.extras.get("phone").toString());
                }
                //tags:车牌号
                //title:姓名
                //封装数据
				OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).extraInfo(bundle);
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
			}
			LatLngBounds bounds = builder.build();
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
			mBaiduMap.animateMapStatus(u);
            for(CloudPoiInfo info : result.poiList) {
                CarInfo carInfo = new CarInfo();
                if(info.extras.get("plateNumber") != null) {
                    carInfo.setPlate_number(info.extras.get("plateNumber").toString());
                }
                if(info.extras.get("phone") != null) {
                    carInfo.setPhone(info.extras.get("phone").toString());
                }
                if(info.extras.get("locationTime") != null) {
                    carInfo.setLast_position_time(info.extras.get("locationTime").toString());
                }
                if(info.extras.get("carID") != null) {
                    carInfo.setId(Integer.parseInt(info.extras.get("carID").toString()));
                }
                carInfo.setLocation(info.address);
                carInfo.setDriver_name(info.title);
                mList.add(carInfo);
            }
            LogUtil.e(TAG, "mList.size:" + mList.size());
		}
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(mList);
                mAdapter.notifyDataSetChanged();
            }
        });
	}

    // 定位初始化
    private void locationAction() {
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        option.disableCache(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 显示我的位置
     * @param location
     */
    private void showLocation(BDLocation location) {
        location.setRadius(10000);
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

    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        LogUtil.e(TAG, "onReceiveLocation");
        if(location == null || TextUtils.isEmpty(location.getCity())) {
            //定位失败重新定位一次
            mLocClient.requestLocation();
            return;
        }
//        NearbySearchInfo info = new NearbySearchInfo();
//        info.ak = Constants.BAIDU_APP_Key;
//        info.geoTableId = Constants.BAIDU_LBS_TABLE_ID;
//        info.radius = 10000;
//        info.location = location.getLatitude() + "," + location.getLongitude();
//        showLocation(location);
//        CloudManager.getInstance().nearbySearch(info);
        LocalSearchInfo info = new LocalSearchInfo();
        info.ak = Constants.BAIDU_CLOUD_SEARCH_Key;
        info.geoTableId = Constants.BAIDU_LBS_TABLE_ID;
        info.region = location.getCity();
        info.pageSize = 50;
        showLocation(location);
        CloudManager.getInstance().localSearch(info);
        mLocClient.stop();
    }

    @Override
    public void onReceivePoi(BDLocation location) {

    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mLocClient.stop();
        CloudManager.getInstance().destroy();
        super.onDestroy();
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
}

