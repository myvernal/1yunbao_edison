package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
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
import com.maogousoft.logisticsmobile.driver.activity.home.SearchCarSourceActivity;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.CloudSearchAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.utils.HttpUtils;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.MyCarsDetailActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarCloudSearchActivity extends BaseActivity implements BDLocationListener, CloudListener {

    private static final String LTAG = "CarCloudSearchActivity";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LinearLayout markerView;
    private InfoWindow mInfoWindow;
    private LocationClient mLocClient;
    private boolean isFirstLoc = true;// 是否首次定位
    private Button mBack, mMore;
    private TextView mTitle, mNext;
    private int currentModel = CURRENT_MODEL_MAP;
    private static final int CURRENT_MODEL_MAP = 0;
    private static final int CURRENT_MODEL_LIST = 1;
    private View listContainer, mapContainer;
    private BaseListAdapter mAdapter;
    private ListView mListView;
    private View mEmpty;
    private boolean isMyCarLocation = false;
    private LocalSearchInfo info;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_lbssearch);
        initViews();
        initData();
    }

    private void initViews() {
        // 地图初始化
        findViewById(R.id.search).setOnClickListener(this);
        mNext = (TextView) findViewById(R.id.next);
        mNext.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mTitle = (TextView) findViewById(R.id.titlebar_id_content);
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
        mapContainer = findViewById(R.id.mapContainer);
        mListView = (ListView) findViewById(android.R.id.list);
        mEmpty = findViewById(android.R.id.empty);
        //图层点击事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                queryCarInfo(marker);
                return true;
            }
        });
        //设定中心点坐标

        LatLng cenpt = new LatLng(0, 0);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private LinearLayout getMarkerView(Marker marker, String carDescription) {
        markerView = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_marker_detail_layout, null);
        TextView description = (TextView) markerView.findViewById(R.id.description);
        description.setText(carDescription);
        TextView line = (TextView) markerView.findViewById(R.id.line);
        String lineStr = marker.getExtraInfo().getString("line");
        line.setText(TextUtils.isEmpty(lineStr) || lineStr.length() < 3 ? "暂无线路信息" : lineStr);
        return markerView;
    }

    //查询车辆详细信息
    private void queryCarInfo(final Marker marker) {
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            // 搜索新货源
            jsonObject.put(Constants.ACTION, Constants.GET_MY_FLEET_DETAIL);
            jsonObject.put(Constants.TOKEN, application.getToken());
            params.put("id", marker.getExtraInfo().getString("carID"));
            jsonObject.put(Constants.JSON, params);
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    CarInfo carInfo = (CarInfo) result;
                                    final StringBuilder carInfoDescription = new StringBuilder();
                                    carInfoDescription.append(carInfo.getPlate_number())
                                            .append("\r\r")
                                            .append(carInfo.getCar_length() + "米");
                                    //车型
                                    int carTypeValue = carInfo.getCar_type();
                                    String[] carTypeStr = context.getResources().getStringArray(R.array.car_types_name);
                                    for (int i = 0; i < Constants.carTypeValues.length; i++) {
                                        if (Constants.carTypeValues[i] == carTypeValue) {
                                            carInfoDescription.append(carTypeStr[i]);
                                            break;
                                        }
                                    }
                                    carInfoDescription.append("\r\r").append("载" + carInfo.getCar_weight() + "吨");

                                    //显示车源详细信息
                                    final LatLng ll = marker.getPosition();
                                    Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                                    p.y -= 40;
                                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                                    InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                        public void onInfoWindowClick() {
                                            LatLng llNew = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
                                            marker.setPosition(llNew);
                                            mBaiduMap.hideInfoWindow();
                                            //点击弹出的信息框,进入车辆详情
                                            Intent intent = new Intent(context, MyCarsDetailActivity.class);
                                            intent.putExtra(Constants.COMMON_KEY, Integer.parseInt(marker.getExtraInfo().getString("carID")));
                                            intent.putExtra(Constants.QUERY_CAR_INFO_FROM_MAP, true);
                                            startActivity(intent);
                                        }
                                    };
                                    View popupView = getMarkerView(marker, carInfoDescription.toString());
                                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(popupView);
                                    if (bitmapDescriptor != null) {
                                        mInfoWindow = new InfoWindow(bitmapDescriptor, llInfo, -40, listener);
                                        mBaiduMap.showInfoWindow(mInfoWindow);
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
                if (currentModel == CURRENT_MODEL_MAP) {
                    currentModel = CURRENT_MODEL_LIST;
                    listContainer.setVisibility(View.VISIBLE);
                    mapContainer.setVisibility(View.GONE);
                    //列表数据处理
                    if (mAdapter.getCount() <= 0) {
                        mEmpty.setVisibility(View.VISIBLE);
                    }
                    mMore.setText("查看地图");
                } else {
                    currentModel = CURRENT_MODEL_MAP;
                    listContainer.setVisibility(View.GONE);
                    mapContainer.setVisibility(View.VISIBLE);
                    mMore.setText("查看列表");
                }
                break;
            case R.id.search:
                Intent intent = new Intent(context, SearchCarSourceActivity.class);
                intent.putExtra(Constants.CLOUD_CARS_SEARCH, true);
                startActivity(intent);
                break;
            case R.id.next:
                searchLbsDataMore();
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

    public void locationMyCars(List<CarInfo> list) {
        //添加列表数据
        if (list != null && !list.isEmpty()) {
            Log.d(LTAG, "locationMyCars, result length: " + list.size());
            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_text);
            LatLng ll;
            LatLngBounds.Builder builder = new Builder();
            for (CarInfo carInfo : list) {
                if (TextUtils.isEmpty(carInfo.getLatitude()) || TextUtils.isEmpty(carInfo.getLongitude())) {
                    continue;
                }
                ll = new LatLng(Double.valueOf(carInfo.getLatitude()), Double.valueOf(carInfo.getLongitude()));
                Bundle bundle = new Bundle();
                bundle.putString("name", carInfo.getOwer_name());
                bundle.putString("address", carInfo.getLocation());
                if (null != carInfo.getLocation_time()) {
                    bundle.putString("locationTime", carInfo.getLocation_time());
                }
                if (null != carInfo.getPhone()) {
                    bundle.putString("phone", carInfo.getPhone());
                }
                //车源ID
                if (0 != carInfo.getId()) {
                    bundle.putString("carID", carInfo.getId() + "");
                }
                //车源线路
                CityDBUtils dbUtils = new CityDBUtils(application.getCitySDB());
                StringBuffer sb = new StringBuffer();
                if (carInfo.getStart_province() > 0) {
                    String wayStart = dbUtils.getCityInfo(carInfo.getStart_province(), carInfo.getStart_city(), carInfo.getStart_district());
                    if (carInfo.getEnd_province1() > 0 || carInfo.getEnd_city1() > 0 || carInfo.getEnd_district1() > 0) {
                        String wayEnd1 = dbUtils.getCityInfo(carInfo.getEnd_province1(), carInfo.getEnd_city1(), carInfo.getEnd_district1());
                        sb.append(wayStart + "--" + wayEnd1 + "\n");
                    }
                    if (carInfo.getEnd_province2() > 0 || carInfo.getEnd_city2() > 0 || carInfo.getEnd_district2() > 0) {
                        String wayEnd2 = dbUtils.getCityInfo(carInfo.getEnd_province2(), carInfo.getEnd_city2(), carInfo.getEnd_district2());
                        sb.append(wayStart + "--" + wayEnd2 + "\n");
                    }
                    if (carInfo.getEnd_province3() > 0 || carInfo.getEnd_city3() > 0 || carInfo.getEnd_district3() > 0) {
                        String wayEnd3 = dbUtils.getCityInfo(carInfo.getEnd_province3(), carInfo.getEnd_city3(), carInfo.getEnd_district3());
                        sb.append(wayStart + "--" + wayEnd3);
                    }
                    bundle.putString("line", sb.toString());
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
        }
        mAdapter.notifyDataSetChanged();
    }

    public void onGetSearchResult(CloudSearchResult result, int error) {
        //添加列表数据
        final List<CarInfo> mList = new ArrayList<CarInfo>();
        if (result != null && result.poiList != null && result.poiList.size() > 0) {
            Log.d(LTAG, "onGetSearchResult, result length: " + result.poiList.size());
            if (result.poiList.size() < 50) {
                //加载了全部,继续循环从第一页开始
                pageIndex = 1;
            } else {
                pageIndex++;
            }
            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_text);
            LatLng ll;
            LatLngBounds.Builder builder = new Builder();
            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                Bundle bundle = new Bundle();
                bundle.putString("name", info.title);
                bundle.putString("address", info.address);
                if (null != info.extras.get("locationTime")) {
                    bundle.putString("locationTime", info.extras.get("locationTime").toString());
                }
                if (null != info.extras.get("phone")) {
                    bundle.putString("phone", info.extras.get("phone").toString());
                }
                //车源ID
                if (null != info.extras.get("carID")) {
                    bundle.putString("carID", info.extras.get("carID").toString());
                }
                //车源线路
                if (null != info.extras.get("line")) {
                    bundle.putString("line", info.extras.get("line").toString());
                }
                //tags:车牌号
                //title:姓名
                //封装数据
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).extraInfo(bundle);
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
            }
            mLocClient.stop();
            LatLngBounds bounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);
            for (CloudPoiInfo info : result.poiList) {
                CarInfo carInfo = new CarInfo();
                if (info.extras.get("plateNumber") != null) {
                    carInfo.setPlate_number(info.extras.get("plateNumber").toString());
                }
                if (info.extras.get("phone") != null) {
                    carInfo.setPhone(info.extras.get("phone").toString());
                }
                if (info.extras.get("locationTime") != null) {
                    carInfo.setLast_position_time(info.extras.get("locationTime").toString());
                }
                if (info.extras.get("carID") != null) {
                    carInfo.setId(Integer.parseInt(info.extras.get("carID").toString()));
                }
                carInfo.setLocation(info.address);
                carInfo.setDriver_name(info.title);
                mList.add(carInfo);
            }
            LogUtil.e(TAG, "mList.size:" + mList.size());
        } else {
            //加载了全部,继续循环从第一页开始
            pageIndex = 1;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeAll();
                mAdapter.addAll(mList);
                mAdapter.notifyDataSetChanged();
                mNext.setVisibility(View.VISIBLE);
            }
        });
    }

    // 定位初始化
    private void locationAction() {
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 显示我的位置
     *
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
    public void onReceiveLocation(final BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        LogUtil.e(TAG, "onReceiveLocation");
        if (location == null) {
            //定位失败重新定位一次
            mLocClient.requestLocation();
            LogUtil.e(TAG, "location:" + location);
            return;
        }
        /*else if(TextUtils.isEmpty(location.getCity())) {
            getAddress(location.getLatitude(), location.getLongitude(), new LocHelper.LocCallback() {
                @Override
                public void onRecivedLoc(double lat, double lng, String addr) {
                    //获取到文字地址
                    //TODO
                }
            });
        }*/
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        isMyCarLocation = getIntent().getBooleanExtra(Constants.MY_CARS_SEARCH, false);
        if (serializable instanceof ArrayList && isMyCarLocation) {
            List<CarInfo> list = (List<CarInfo>) serializable;
            mAdapter.addAll(list);
            showLocation(location);
            locationMyCars(list);
        } else {
            if (TextUtils.isEmpty(location.getCity())) {
                getAddress(location.getLatitude(), location.getLongitude(), new LocHelper.LocCallback() {
                    @Override
                    public void onRecivedLoc(double lat, double lng, String addr) {
                        //获取到城市地名
                        searchLbsData(location, addr);
                    }
                });
            } else {
                searchLbsData(location, location.getCity());
            }
        }
        mLocClient.stop();
    }

    private void searchLbsData(BDLocation location, String region) {
        if (info == null) {
            info = new LocalSearchInfo();
        }
        info.ak = Constants.BAIDU_CLOUD_SEARCH_Key;
        info.geoTableId = Constants.BAIDU_LBS_TABLE_ID;
        info.pageIndex = pageIndex;
        info.region = region;
        info.pageSize = 50;
        info.sortby = "sort_num:-1";
        showLocation(location);
        CloudManager.getInstance().localSearch(info);
    }

    private void searchLbsDataMore() {
        if (info != null) {
            info.pageIndex = pageIndex;
            CloudManager.getInstance().localSearch(info);
        }
    }

    /**
     * 通过经纬度,查询地址
     *
     * @param lat
     * @param lng
     */
    public void getAddress(final double lat, final double lng, final LocHelper.LocCallback resultCallback) {
        new AsyncTask<BDLocation, Object, HashMap<String, Object>>() {

            @Override
            protected void onPostExecute(HashMap<String, Object> result) {
                if (result != null) {
                    String addr = (String) result.get("addr");
                    String city = (String) result.get("city");
                    LogUtil.e(TAG, "geocoder，获取到的城市:" + city);

                    if (resultCallback != null) {
                        resultCallback.onRecivedLoc(lat, lng, city);
                    }
                } else {
                    if (resultCallback != null) {
                        resultCallback.onRecivedLoc(0, 0, null);
                    }
                }
                super.onPostExecute(result);
            }

            @Override
            protected HashMap<String, Object> doInBackground(BDLocation... params) {

                String url = "http://api.map.baidu.com/geocoder?" + "location=" + lat + "," + lng + "&output=json&key="
                        + Constants.strKey;

                // 时效宝 百度key b66fbb5a289082fa86ef1a7df81ab57f

                String returnStr = HttpUtils.getURLData(url);

                HashMap<String, Object> hmResult = null;

                JSONObject jObject;
                try {
                    jObject = new JSONObject(returnStr);

                    if (!jObject.getString("status").equalsIgnoreCase("OK")) {
                        LogUtil.e(TAG, "geocoder，没有获取到数据");
                        return null;
                    }

                    hmResult = new HashMap<String, Object>();
                    String addr = jObject.getJSONObject("result").getString("formatted_address");
                    String city = jObject.getJSONObject("result").getJSONObject("addressComponent").getString("city");
                    hmResult.put("addr", addr);
                    hmResult.put("city", city);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return hmResult;
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        CloudManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        mLocClient.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
}

