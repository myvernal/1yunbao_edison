package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.model.CardInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/10.
 */
public class CheckCardActivity extends BaseActivity {

    private Button mTitleBarBack, mTitleBarMore;
    private EditText check_name, check_number;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_card_layout);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("证件验证");
        check_name = (EditText) findViewById(R.id.check_name);
        check_number = (EditText) findViewById(R.id.check_number);
        // 返回按钮生效
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarBack.setOnClickListener(this);
        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("验证记录");
        mTitleBarMore.setOnClickListener(this);
        //验证按钮
        findViewById(R.id.check).setOnClickListener(this);
        //同意协议选择框
        checkBox = (CheckBox) findViewById(R.id.checkBox);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                Toast.makeText(context, "验证记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check:
                if (TextUtils.isEmpty(check_name.getText())) {
                    Toast.makeText(context, "请填写姓名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(check_number.getText())) {
                    Toast.makeText(context, "请填写身份证号码.", Toast.LENGTH_SHORT).show();
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(context, "请勾选同意服务协议.", Toast.LENGTH_SHORT).show();
                } else {
                    getData();
                }
                break;
        }
    }

    private void getData() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.CHECK_CARD);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("id_name", check_name.getText())
                    .put("id_card", check_number.getText()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CardInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof CarInfo) {
                                        CardInfo cardInfo = (CardInfo) result;
                                        LogUtil.d(TAG, cardInfo.getMsg());
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

    /**
     * 帐号充值
     *
     * @param view
     */
    public void onCharge(View view) {
        startActivity(new Intent(context, ChargeActivity.class));
    }
}
