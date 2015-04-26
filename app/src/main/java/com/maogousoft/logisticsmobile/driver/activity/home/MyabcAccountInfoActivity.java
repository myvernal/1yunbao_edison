package com.maogousoft.logisticsmobile.driver.activity.home;

import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.AccountRecordActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.UpdatePwdActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;

/**
 * Myabc 我的账户
 * 
 * @author ybxiang
 */
public class MyabcAccountInfoActivity extends BaseActivity {
	private TextView mBalance;
    private View myabc_id_charge, myabc_id_account_record, myabc_id_updatepwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myabc_account);
		initViews();
		initData(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getBalance();// 获取账户余额
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("我的账户");


		mBalance = (TextView) findViewById(R.id.myabc_id_balance);
        myabc_id_charge = findViewById(R.id.myabc_id_charge);
        myabc_id_charge.setOnClickListener(this);
        myabc_id_account_record = findViewById(R.id.myabc_id_account_record);
        myabc_id_account_record.setOnClickListener(this);
        myabc_id_updatepwd = findViewById(R.id.myabc_id_updatepwd);
        myabc_id_updatepwd.setOnClickListener(this);
	}

	private void initData(Bundle savedInstanceState) {

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

	@Override
	public void onClick(View v) {
		super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case R.id.myabc_id_charge:
                onCharge(v);
                break;
            case R.id.myabc_id_account_record:
                onAccountRecord(v);
                break;
            case R.id.myabc_id_updatepwd:
                onChangePasswd(v);
                break;
        }
	}

	/**
	 * 修改密码
	 * 
	 * @param view
	 */
	public void onChangePasswd(View view) {
		startActivity(new Intent(mContext, UpdatePwdActivity.class));
	}

	/**
	 * 账户记录
	 * 
	 * @param view
	 */
	public void onAccountRecord(View view) {
		startActivity(new Intent(mContext, AccountRecordActivity.class));
	}

	/**
	 * 帐号充值
	 * 
	 * @param view
	 */
	public void onCharge(View view) {
		startActivity(new Intent(mContext, ChargeActivity.class));
	}
}
