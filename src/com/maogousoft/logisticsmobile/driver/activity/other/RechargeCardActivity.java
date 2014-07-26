package com.maogousoft.logisticsmobile.driver.activity.other;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

/**
 * 卡号充值
 * 
 */
public class RechargeCardActivity extends BaseActivity {

	private EditText muser, mpwd;

	private Button mSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_recharge_card);
		initViews();
	}

	// 初始化视图
	private void initViews() {
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_other_recharge_card);
		muser = (EditText) findViewById(R.id.other_recharge_card_user);
		mpwd = (EditText) findViewById(R.id.other_recharge_card_pwd);

		mSubmit = (Button) findViewById(R.id.other_recharge_card_submit);
		mSubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mSubmit) {
			if (!CheckUtils.checkIsEmpty(muser)) {
				showMsg("请输入卡号");
				return;
			}

			if (!CheckUtils.checkIsEmpty(mpwd)) {
				showMsg("请输入卡号");
				return;
			}

			if (muser.getText().toString().trim().length() < 9) {
				showMsg("请输入有效的卡号");
				return;
			}

			if (mpwd.getText().toString().trim().length() < 9) {
				showMsg("请输入有效的卡密");
				return;
			}
			submit();
		}
	};

	// 提交修改密码的请求
	private void submit() {
		final JSONObject jsonObject = new JSONObject();
		try {
			showDefaultProgress();
			jsonObject.put(Constants.ACTION, Constants.DRIVER_COUPON);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(
					Constants.JSON,
					new JSONObject()
							.put("card_no", muser.getText().toString().trim())
							.put("card_pwd", mpwd.getText().toString().trim())
							.toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								showMsg("充值成功");
								muser.setText("");
								mpwd.setText("");
								break;
							case ResultCode.RESULT_ERROR:
								if (result instanceof String)
									showMsg(result.toString());
								else
									showMsg("充值失败");
								break;
							case ResultCode.RESULT_FAILED:
								if (result instanceof String)
									showMsg(result.toString());
								else
									showMsg("充值失败");
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
