package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarrierInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2014/11/11.
 */
public class AgreementCreateStep2Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, BDLocationListener {

    private static final String TAG = "AgreementCreateStep2Activity";
    private LinearLayout qiandanLayout, priceLayout, phoneLayout;
    private RadioButton preCheckedRadio;
    private RadioButton currentCheckedRadio;
    private EditText other_driver;
    private int orderId;
    private int agreementType;
    private LocationClient mLocClient;
    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_step2);
        HeaderView mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.agreement_edit_tip);

        initView();
        initData();
        locationAction();
    }

    private void initView() {
        qiandanLayout = (LinearLayout) findViewById(R.id.qiangdan_list_container);
        priceLayout = (LinearLayout) findViewById(R.id.price_list_container);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_list_container);
        other_driver = (EditText) findViewById(R.id.other_driver);
        other_driver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && currentCheckedRadio != null) {
                    currentCheckedRadio.setChecked(false);
                }
            }
        });
    }

    private void initData() {
        orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        agreementType = getIntent().getIntExtra(Constants.AGREEMENT_TYPE, -1);
        if (orderId > 0 && agreementType > 0) {
            getData(orderId);
        } else {
            finish();
        }
    }

    // 根据条件请求指定页数的数据
    private void getData(int orderId) {
        try {
            showSpecialProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_CARRIER_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("order_id", orderId).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarrierInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<CarrierInfo> mList = (List<CarrierInfo>) result;
                                        LayoutInflater inflater = LayoutInflater.from(mContext);
                                        displayData(mList, inflater);
                                    }
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

    private void displayData(List<CarrierInfo> mList, LayoutInflater inflater) {
        //遍历数据
        for (CarrierInfo carrierInfo : mList) {
            if (TextUtils.equals("Y", carrierInfo.getIs_grab_single_car())) {
                RadioButton button = new RadioButton(mContext);
                button.setTextAppearance(mContext, R.style.CarrierItemStyle);
                button.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone());
                button.setOnCheckedChangeListener(this);
                button.setTag(carrierInfo);
                qiandanLayout.addView(button);
            }
            if (TextUtils.equals("Y", carrierInfo.getIs_price_car())) {
                RadioButton button = new RadioButton(mContext);
                button.setTextAppearance(mContext, R.style.CarrierItemStyle);
                button.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t" + getString(R.string.agreement_carrier_price, carrierInfo.getDriver_price()));
                button.setOnCheckedChangeListener(this);
                button.setTag(carrierInfo);
                priceLayout.addView(button);
            }
            if (TextUtils.equals("Y", carrierInfo.getIs_phone_car())) {
                RadioButton button = new RadioButton(mContext);
                button.setTextAppearance(mContext, R.style.CarrierItemStyle);
                button.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone());
                button.setOnCheckedChangeListener(this);
                button.setTag(carrierInfo);
                phoneLayout.addView(button);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            if(preCheckedRadio != null) {
                preCheckedRadio.setChecked(false);
            }
            currentCheckedRadio = (RadioButton) compoundButton;
            preCheckedRadio = (RadioButton) compoundButton;
            currentCheckedRadio.setChecked(true);
            other_driver.setText("");//选择承运人后,清空手动输入的账号
        } else {
            currentCheckedRadio = null;
        }
    }

    public void onNext(View view) {
        if(currentCheckedRadio == null && other_driver.length() == 0) {
            showMsg("请选择承运人");
            return;
        }
        if(currentCheckedRadio != null) {
            CarrierInfo carrierInfo = (CarrierInfo) currentCheckedRadio.getTag();
            getAgreement(carrierInfo);
        } else {
            getAgreement(null);
        }
    }

    // 进入合同制作页面
    private void getAgreement(CarrierInfo carrierInfo) {
        try {
            showSpecialProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.CONTRACT_IMPORT_PREVIEW);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("order_id", orderId)
                    .put("contract_type", agreementType)
                    .put("driver_phone", other_driver.getText())
                    .put("carrier_driverId", carrierInfo == null ? "" : carrierInfo.getDriver_id()).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarrierInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof CarrierInfo) {
                                        CarrierInfo carrierInfo = (CarrierInfo) result;
                                        if (!TextUtils.isEmpty(carrierInfo.getPath())) {
                                            final Intent intent = new Intent(mContext, AgreementCreateStep3Activity.class);
                                            intent.putExtra(Constants.COMMON_KEY, Constants.BASE_URL + carrierInfo.getPath() + "&latitude=" + latitude + "&longitude=" + longitude);
                                            mContext.startActivity(intent);
                                            finish();
                                        }
                                    }
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

    // 开始定位
    private void locationAction() {
        showDefaultProgress();
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        LogUtil.e(TAG, "onReceiveLocation");
        dismissProgress();
        if (location == null) {
            //定位失败重新定位一次
            mLocClient.requestLocation();
            LogUtil.e(TAG, "location:" + location);
            return;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LogUtil.e(TAG, "location:" + location.getLatitude() + "," + location.getLongitude());
            mLocClient.stop();
        }
    }
}
