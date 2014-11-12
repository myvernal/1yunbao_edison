package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarrierInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aliang on 2014/11/13.
 */
public class AgreementCreateStep3Activity extends BaseActivity {

    private int orderId;
    private EditText payPassword;
    private int agreementType;
    private int driverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_step3);
        HeaderView mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.agreement_edit_tip);

        initView();
        initData();
    }

    private void initView() {
        payPassword = (EditText) findViewById(R.id.pay_password);
    }

    private void initData() {
        orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        agreementType = getIntent().getIntExtra(Constants.AGREEMENT_TYPE, -1);
        driverId = getIntent().getIntExtra(Constants.USER_ID, -1);
        if (orderId == -1 || agreementType == -1 || driverId == -1) {
            finish();
        }
    }

    public void onNext(View view) {
        if(payPassword.length() == 0) {
            showMsg("请输入支付密码！");
            return;
        }
        getAgreement();
    }

    // 进入合同制作页面
    private void getAgreement() {
        try {
            showSpecialProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.CONTRACT_IMPORT_PREVIEW);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("order_id", orderId)
                    .put("contract_type", agreementType)
                    .put("pay_password", payPassword.getText())
                    .put("carrier_driverId", driverId).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarrierInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof CarrierInfo) {
                                        CarrierInfo carrierInfo = (CarrierInfo) result;
                                        if(!TextUtils.isEmpty(carrierInfo.getPath())) {
                                            final Intent intent = new Intent(mContext, AgreementPreviewActivity.class);
                                            intent.putExtra(Constants.COMMON_KEY, Constants.BASE_URL + carrierInfo.getPath());
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
}
