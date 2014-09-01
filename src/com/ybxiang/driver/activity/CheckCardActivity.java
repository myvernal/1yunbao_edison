package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.maogousoft.logisticsmobile.driver.model.CardInfo;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/10.
 */
public class CheckCardActivity extends BaseActivity {

    private Button mTitleBarBack, mTitleBarMore;
    private EditText check_name, check_number;
    private CheckBox checkBox;
    private double userGold = -1;
    private TextView user_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_card_layout);
        initViews();
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("证件验证");
        check_name = (EditText) findViewById(R.id.check_name);
        check_number = (EditText) findViewById(R.id.check_number);
        user_money = (TextView) findViewById(R.id.user_money);
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

    private void initData() {
        getBalance();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                startActivity(new Intent(context, CheckCardListActivity.class));
                break;
            case R.id.check:
                if (TextUtils.isEmpty(check_name.getText())) {
                    Toast.makeText(context, "请填写姓名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(check_number.getText())) {
                    Toast.makeText(context, "请填写身份证号码.", Toast.LENGTH_SHORT).show();
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(context, "请勾选同意服务协议.", Toast.LENGTH_SHORT).show();
                } else {
                    if(userGold == -1) {
                        Toast.makeText(context, "正在获取账户余额,请稍后...", Toast.LENGTH_SHORT).show();
                    } else if(userGold < 2) {
                        Toast.makeText(context, "账户余额不足,请充值...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, ChargeActivity.class));
                    } else {
                        checkData();
                    }
                }
                break;
        }
    }

    /**
     * 开始验证
     */
    private void checkData() {
        try {
            showSpecialProgress("正在验证,请稍后");
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
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof CardInfo) {
                                        CardInfo cardInfo = (CardInfo) result;
                                        Intent intent = new Intent(context, CheckCardResultActivity.class);
                                        intent.putExtra(Constants.COMMON_KEY, cardInfo);
                                        startActivity(intent);
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

    // 获取账户余额
    private void getBalance() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_ACCOUNT_GOLD);
            jsonObject.put(Constants.TOKEN, application.getToken());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    JSONObject object = (JSONObject) result;
                                    userGold = object.optDouble("gold");
                                    String gold = String.format(getString(R.string.check_card_money), userGold);
                                    user_money.setText(gold);
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
        } catch (Exception e) {
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
