package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.SafePinanInfo;
import com.ybxiang.driver.util.Utils;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/27.
 */
public class SafePinanActivity extends BaseActivity {
    private Button mTitleBarBack, mTitleBarMore;
    private EditText safe_money, safe_all_money;
    private TextView user_money;
    private CheckBox safe_check_box;
    private double userGold = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_pingan_layout);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("平安保险");
        // 返回按钮生效
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarBack.setOnClickListener(this);
        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("保险记录");
        mTitleBarMore.setOnClickListener(this);

        safe_check_box = (CheckBox) findViewById(R.id.safe_check_box);
        user_money = (TextView) findViewById(R.id.user_money);
        safe_all_money = (EditText) findViewById(R.id.safe_all_money);
        safe_money = (EditText) findViewById(R.id.safe_money);

        //保费由费率和保额相乘
        safe_all_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    float allMoney = Float.valueOf(safe_all_money.getText().toString()) * 10000 * 0.0003f;
                    safe_money.setText(allMoney + "");
                } else {
                    safe_money.setText("0");
                }
            }
        });
    }

    private void initData() {
        getBalance();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                startActivity(new Intent(context, SafeListActivity.class));
                break;
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
                                    String gold = String.format(getString(R.string.safe_user_money), userGold);
                                    user_money.setText(Html.fromHtml(Utils.textFormatRed(gold)));
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

    /**
     * 返回
     *
     * @param view
     */
    public void onClickBack(View view) {
        onBackPressed();
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void onClickNext(View view) {
        if(safe_all_money.getText().length() <= 0) {
            Toast.makeText(context, "请填写保险金额!", Toast.LENGTH_SHORT).show();
        } else if (!safe_check_box.isChecked()) {
            Toast.makeText(context, "请勾选同意保险协议", Toast.LENGTH_SHORT).show();
        } else if(userGold == -1) {
            Toast.makeText(context, "正在获取账户余额,请稍后...", Toast.LENGTH_SHORT).show();
        } else if(userGold <= Double.valueOf(safe_money.getText().toString())) {
            Toast.makeText(context, "账户余额不足,请充值...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, ChargeActivity.class));
        } else {
            Intent intent = new Intent(context, SafePinanEditActivity.class);
            SafePinanInfo safePinanInfo = new SafePinanInfo();
            safePinanInfo.setAmount_covered(Double.valueOf(safe_all_money.getText().toString()));//保险金额
            safePinanInfo.setInsurance_charge(Double.valueOf(safe_money.getText().toString()));//保险费用
            intent.putExtra(Constants.COMMON_KEY, safePinanInfo);
            startActivity(intent);
        }
    }
}
