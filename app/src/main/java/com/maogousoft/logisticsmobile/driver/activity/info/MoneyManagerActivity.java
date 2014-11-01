package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.HistroyOrderActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2015/5/6.
 */
public class MoneyManagerActivity extends BaseActivity {

    private TextView mBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_manager);
        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_money_manager);
        findViewById(R.id.titlebar_id_more).setVisibility(View.VISIBLE);

        mBalance = (TextView) findViewById(R.id.myabc_id_balance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAbcInfo();
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
                                    mBalance.setText(String.format(getString(R.string.string_home_myabc_balance), object.optDouble("gold", 0d)));
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

    // 获取我的abc信息
    private void getAbcInfo() {
        final JSONObject jsonObject = new JSONObject();
        try {
            showDefaultProgress();
            JSONObject params = new JSONObject();
            if(application.getUserType() == Constants.USER_DRIVER) {
                jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
            } else {
                jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
                params.put("user_id", application.getUserId());
            }
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    application.getUserType() == Constants.USER_DRIVER ? DriverInfo.class : ShipperInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        if (result instanceof ShipperInfo) {
                                            ShipperInfo userInfo = (ShipperInfo) result;
                                            if (TextUtils.isEmpty(userInfo.getYunbao_pay())) {
                                                //支付密码为空, 设置完成后返回本页面
                                                startActivity(new Intent(mContext, SettingPayPasswordActivity.class).putExtra(Constants.COMMON_KEY, true));
                                                finish();
                                            } else {
                                                // 获取账户余额
                                                getBalance();
                                            }
                                        } else if(result instanceof DriverInfo) {
                                            DriverInfo userInfo = (DriverInfo) result;
                                            if (TextUtils.isEmpty(userInfo.getYunbao_pay())) {
                                                //支付密码为空, 设置完成后返回本页面
                                                startActivity(new Intent(mContext, SettingPayPasswordActivity.class).putExtra(Constants.COMMON_KEY, true));
                                                finish();
                                            } else {
                                                // 获取账户余额
                                                getBalance();
                                            }
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
     * 修改支付密码
     *
     * @param view
     */
    public void onChangePayPwd(View view) {
        startActivity(new Intent(mContext, SettingPayPasswordActivity.class));
    }

    /**
     * 历史订单
     *
     * @param view
     */
    public void onMyHistoryOrder(View view) {
        startActivity(new Intent(mContext, HistroyOrderActivity.class));
    }

    /**
     * 帐号充值/转账/提现
     *
     * @param view
     */
    public void onCharge(View view) {
        startActivity(new Intent(mContext, ChargeActivity.class));
    }

    /**
     * 账户记录
     *
     * @param view
     */
    public void onAccountRecord(View view) {
        startActivity(new Intent(mContext, AccountRecordActivity.class));
    }
}
