package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
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
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.maogousoft.logisticsmobile.driver.model.SafePinanInfo;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/27.
 */
public class SafePinanActivity extends BaseActivity {
    private Button mTitleBarBack, mTitleBarMore;
    private EditText safe_percent, safe_money, safe_all_money;
    private TextView user_money, desc;
    private CheckBox safe_check_box;
    private double userGold = -1;
    private HuoZhuUserInfo userInfo;

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

        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("保险记录");
        mTitleBarMore.setOnClickListener(this);

        safe_check_box = (CheckBox) findViewById(R.id.safe_check_box);
        safe_percent = (EditText) findViewById(R.id.safe_percent);
        user_money = (TextView) findViewById(R.id.user_money);
        safe_all_money = (EditText) findViewById(R.id.safe_all_money);
        safe_money = (EditText) findViewById(R.id.safe_money);
        desc = (TextView) findViewById(R.id.desc);
        desc.setOnClickListener(this);

        //保费由费率和保额相乘
        safe_all_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    double allMoney = (Float.valueOf(editable.toString()) * 10000) * userInfo.getPa_1() / 100f;
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
                Intent intent = new Intent(mContext, SafeListActivity.class);
                intent.putExtra(Constants.COMMON_KEY, Constants.SAFE_PINGAN);
                startActivity(intent);
                break;
            case R.id.desc:
                Intent intentDesc = new Intent(mContext, BaoxianDescActivity.class);
                intentDesc.putExtra(Constants.COMMON_KEY, Constants.SAFE_PINGAN);
                startActivity(intentDesc);
                break;
        }
    }

    // 获取账户余额
    private void getBalance() {
        showSpecialProgress("正在获取用户余额,请稍后");
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
                            //取回用户余额后,再去费率
                            getUserInfo();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取用户信息中的费率
    private void getUserInfo() {
        showSpecialProgress("正在获取费率,请稍后...");
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    HuoZhuUserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (HuoZhuUserInfo) result;
                                        if(null == userInfo.getPa_1()) {
                                            userInfo.setPa_1(0.03);
                                        }
                                        if(userInfo.getPa_1() > 0) {
                                            safe_percent.setText(userInfo.getPa_1() + "");
                                            if(!TextUtils.isEmpty(safe_all_money.getText())) {
                                                double allMoney = (Float.valueOf(safe_all_money.getText().toString()) * 10000) * userInfo.getPa_1() / 100f;
                                                safe_money.setText(allMoney + "");
                                            }
                                        } else {
                                            Toast.makeText(mContext, "获取保险费率出错,请联系客服...", Toast.LENGTH_SHORT).show();
                                        }
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

    /**
     * 帐号充值
     *
     * @param view
     */
    public void onCharge(View view) {
        startActivity(new Intent(mContext, ChargeActivity.class));
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
        if(Float.valueOf(safe_percent.getText().toString()) <= 0) {
            Toast.makeText(mContext, "正在获取保险费率,请稍后...", Toast.LENGTH_SHORT).show();
        } else if(safe_all_money.getText().length() <= 0) {
            Toast.makeText(mContext, "请填写保险金额!", Toast.LENGTH_SHORT).show();
        } else if (!safe_check_box.isChecked()) {
            Toast.makeText(mContext, "请勾选同意保险协议", Toast.LENGTH_SHORT).show();
        } else if(userGold == -1) {
            Toast.makeText(mContext, "正在获取账户余额,请稍后...", Toast.LENGTH_SHORT).show();
        } else if(userGold <= Double.valueOf(safe_money.getText().toString())) {
            Toast.makeText(mContext, "账户余额不足,请充值...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext, ChargeActivity.class));
        } else {
            Intent intent = new Intent(mContext, SafePinanEditActivity.class);
            SafePinanInfo safePinanInfo = new SafePinanInfo();
            safePinanInfo.setType(Constants.SAFE_PINGAN);
            safePinanInfo.setAmount_covered(Double.valueOf(safe_all_money.getText().toString()));//保险金额
            safePinanInfo.setInsurance_charge(Double.valueOf(safe_money.getText().toString()));//保险费用
            intent.putExtra(Constants.COMMON_KEY, safePinanInfo);
            startActivity(intent);
        }
    }
}
