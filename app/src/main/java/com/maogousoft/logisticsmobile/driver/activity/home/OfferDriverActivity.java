package com.maogousoft.logisticsmobile.driver.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aliang on 2015/4/27.
 */
public class OfferDriverActivity extends BaseActivity {

    private HeaderView mHeaderView;
    private EditText offerValue;
    private View offer_type_car, offer_type_weight;
    private int offerType;
    private NewSourceInfo sourceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_layout);
        mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle("报价");
        initView();
        initData();
    }

    private void initView() {
        offerValue = (EditText) findViewById(R.id.offer_value);
        offer_type_car = findViewById(R.id.offer_type_car);
        offer_type_weight = findViewById(R.id.offer_type_weight);
        offer_type_car.setOnClickListener(this);
        offer_type_weight.setOnClickListener(this);
        offer_type_car.setSelected(true);
    }

    private void initData() {
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if (serializable instanceof NewSourceInfo) {
            sourceInfo = (NewSourceInfo) serializable;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.offer_type_car:
                // 元/车
                offerType = 0;
                offer_type_car.setSelected(true);
                offer_type_weight.setSelected(false);
                break;
            case R.id.offer_type_weight:
                offerType = 1;
                offer_type_car.setSelected(false);
                offer_type_weight.setSelected(true);
                // 元/吨
                break;
        }
    }

    /**
     * 报价
     *
     * @param view
     */
    public void onOffer(View view) {
        if (offerValue.getText().toString().length() <= 0) {
            Toast.makeText(mContext, "请输入报价", Toast.LENGTH_SHORT).show();
            return;
        }
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER_COUNTSSS);
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject params = new JSONObject();
            params.put("order_id", sourceInfo.getId());
            params.put("driver_price", offerValue.getText());
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AbcInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg("报价已发送");
                                    finish();
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
