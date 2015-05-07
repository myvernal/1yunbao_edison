package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.HistroyOrderActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;

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
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_money_manager);
        findViewById(R.id.titlebar_id_more).setVisibility(View.VISIBLE);

        mBalance = (TextView) findViewById(R.id.myabc_id_balance);
    }

    private void initData() {
        getBalance();// 获取账户余额
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
