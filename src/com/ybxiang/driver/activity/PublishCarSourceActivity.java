package com.ybxiang.driver.activity;

// 发布车源

import android.text.TextUtils;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;
import org.json.JSONException;
import org.json.JSONObject;

public class PublishCarSourceActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "PublishCarSourceActivity";
    private Context mContext;
    private TextView mTitleBarContent;
    private EditText price, mCarNum, mCarlength, car_weight, description, ower_name, dayId, hourId;
    // 当前位置，报价,
    // 车牌号，车长，载重，补充说明,姓名，手机号码,有效时间（天，小时）
    private TextView ower_phone;
    private Spinner search_car_type, car_price_unit;
    private CitySelectView cityselectStart, cityselectEnd; // 出发地，目的地
    private Button mTitleBarBack;
    private Button mTitleBarMore;
    private double longitude;//经度
    private double latitude;//纬度
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_home_publish_car_source);
        mContext = PublishCarSourceActivity.this;
        initViews();
        onGetLocation();
    }

    // 初始化视图
    private void initViews() {
        mTitleBarContent = (TextView) findViewById(R.id.titlebar_id_content);
        mTitleBarContent.setText(R.string.menu_publish_car_source);

        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("已发布");

        mTitleBarMore.setOnClickListener(this);
        mTitleBarBack.setOnClickListener(this);
        cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);

        price = (EditText) findViewById(R.id.price); //报价
        mCarNum = (EditText) findViewById(R.id.mCarNum); //车牌号
        mCarlength = (EditText) findViewById(R.id.mCarlength); //车长
        car_weight = (EditText) findViewById(R.id.car_weight); //车重
        ower_name = (EditText) findViewById(R.id.ower_name); //联系人
        ower_phone = (TextView) findViewById(R.id.ower_phone); //联系人手机号码
        dayId = (EditText) findViewById(R.id.dayId); //有效日期,日
        hourId = (EditText) findViewById(R.id.hourId); //有效日期,小时
        description = (EditText) findViewById(R.id.description); //补充说明

        car_price_unit = (Spinner) findViewById(R.id.car_price_unit);//报价单位
        search_car_type = (Spinner) findViewById(R.id.search_car_type);//车型

        //填充
        if (application.getAbcInfo() == null) {
            getABCInfo();
        } else {
            initData();
        }
    }

    private void initData() {
        mCarNum.setText(application.getAbcInfo().getPlate_number() + "");
        mCarlength.setText(application.getAbcInfo().getCar_length() + "");
        car_weight.setText(application.getAbcInfo().getCar_weight() + "");
        ower_name.setText(application.getAbcInfo().getName() + "");
        ower_phone.setText(application.getAbcInfo().getPhone() + "");
        int value = application.getAbcInfo().getCar_type();
        for (int i = 0; i < Constants.carTypeValues.length; i++) {
            if (value == Constants.carTypeValues[i]) {
                search_car_type.setSelection(i);
            }
        }
    }

    /**
     * 管理发布过的车源
     */
    @Override
    public void onClick(View v) {
        ((BaseActivity) mContext).setIsRightKeyIntoShare(false);
        super.onClick(v);
        switch (v.getId()) {
            // 管理已发布的车源
            case R.id.titlebar_id_more:
                Intent intent = new Intent();
                intent.setClass(mContext, ManagerCarSourceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 坐标定位
     */
    public void onGetLocation() {
        LocHelper.getInstance(context).getResult(new LocHelper.LocCallback() {

            @Override
            public void onRecivedLoc(double lat, double lng, String addr) {

                if (lat != 0 && lng != 0 && !TextUtils.isEmpty(addr)) {
                    longitude = lng;
                    latitude = lat;
                    address = addr;
                    LogUtil.d(TAG, addr);
                }
            }
        });
    }

    /**
     * 发布车源
     *
     * @param view
     */
    public void onPublishCarSource(View view) {
        final JSONObject jsonObject = new JSONObject();
        try {
            showProgress("正在发布...");
            jsonObject.put(Constants.ACTION, Constants.PUBLISH_DRIVER_CAR_INFO);
            jsonObject.put(Constants.TOKEN, application.getToken());

            int carType = Constants.getCarTypeValues(search_car_type.getSelectedItemPosition());
            int unitType = Constants.getUnitTypeValues(car_price_unit.getSelectedItemPosition());//单位
            JSONObject dataJson = new JSONObject();
            dataJson.put("start_province",
                    cityselectStart.getSelectedProvince() == null ? -1 : cityselectStart.getSelectedProvince().getId()); //出发省
            dataJson.put("start_city",
                    cityselectStart.getSelectedCity() == null ? -1 : cityselectStart.getSelectedCity().getId());//出发地市
            dataJson.put("start_district",
                    cityselectStart.getSelectedTowns() == null ? -1 : cityselectStart.getSelectedTowns().getId());//出发区县
            dataJson.put("end_province",
                    cityselectEnd.getSelectedProvince() == null ? -1 : cityselectEnd.getSelectedProvince().getId());//目的省
            dataJson.put("end_city",
                    cityselectEnd.getSelectedCity() == null ? -1 : cityselectEnd.getSelectedCity().getId());//目的地市
            dataJson.put("end_district",
                    cityselectEnd.getSelectedTowns() == null ? -1 : cityselectEnd.getSelectedTowns().getId());//目的区县
            dataJson.put("price", price.getText());//报价 1 多少每吨  2 多少每公里
            dataJson.put("plate_number", mCarNum.getText());//车牌号
            dataJson.put("car_length", mCarlength.getText());//车长
            dataJson.put("car_type", carType);//车型
            dataJson.put("car_weight", car_weight.getText());//载重
            dataJson.put("ower_name", ower_name.getText());//联系人
            dataJson.put("ower_phone", ower_phone.getText());//手机号码
            dataJson.put("user_date", Integer.valueOf(dayId.getText().toString()) * 24 * 60 * 60 * 1000
                    + Integer.valueOf(hourId.getText().toString()) * 60 * 60 * 1000);//有效期
            dataJson.put("description", description.getText());//补充说明
            dataJson.put("longitude", longitude);//经度
            dataJson.put("latitude", latitude);//纬度
            dataJson.put("address", address);//位置
            dataJson.put("pulish_date", System.currentTimeMillis());//发布日期
            dataJson.put("units", unitType);
            jsonObject.put(Constants.JSON, dataJson.toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    UserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
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

    // 获取我的abc信息
    private void getABCInfo() {
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
                                        AbcInfo mAbcInfo = (AbcInfo) result;
                                        application.setAbcInfo(mAbcInfo);
                                        initData();
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
}
