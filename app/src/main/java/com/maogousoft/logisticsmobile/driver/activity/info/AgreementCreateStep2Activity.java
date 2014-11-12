package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarrierInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2014/11/11.
 */
public class AgreementCreateStep2Activity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AgreementCreateStep2Activity";
    private LinearLayout qiandanLayout, priceLayout, phoneLayout;
    private RadioButton preCheckedRadio;
    private RadioButton currentCheckedRadio;
    private int orderId;
    private int agreementType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_step2);
        HeaderView mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.agreement_edit_tip);

        initView();
        initData();
    }

    private void initView() {
        qiandanLayout = (LinearLayout) findViewById(R.id.qiangdan_list_container);
        priceLayout = (LinearLayout) findViewById(R.id.price_list_container);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_list_container);
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
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_CARRIER_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("order_id", orderId).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarrierInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
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
                button.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t" + getString(R.string.agreement_carrier_price, carrierInfo.getDriver_price()));
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
                button.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t" + getString(R.string.agreement_carrier_price, carrierInfo.getDriver_price()));
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
        } else {
            currentCheckedRadio = null;
        }
    }

    public void onNext(View view) {
        if(currentCheckedRadio == null) {
            showMsg("请选择承运人");
            return;
        }
        CarrierInfo carrierInfo = (CarrierInfo) currentCheckedRadio.getTag();
        startActivity(new Intent(mContext, AgreementCreateStep3Activity.class)
                .putExtra(Constants.ORDER_ID, orderId)
                .putExtra(Constants.AGREEMENT_TYPE, agreementType)
                .putExtra(Constants.USER_ID, carrierInfo.getDriver_id()));
        finish();
    }
}
