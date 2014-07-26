package com.maogousoft.logisticsmobile.driver.activity.info;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * 忘记密码
 * 
 * @author lenovo
 */
public class ForgetActivity extends BaseActivity {

	private Button mBack, mSubmit, mAuth;

	private EditText phone, authentication, password1, password2;

	private Handler handler;

	private static int TIME = 60;

	private int time;

	private Timer timer;

	private TimerTask timerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_forget);
		initViews();
		initHandler();
	}

	private void initViews() {
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_info_forget_title);
		phone = (EditText) findViewById(R.id.forget_phone);
		authentication = (EditText) findViewById(R.id.forget_authentication);
		password1 = (EditText) findViewById(R.id.forget_password1);
		password2 = (EditText) findViewById(R.id.forget_password2);
		mSubmit = (Button) findViewById(R.id.forget_submitbtn);
		mAuth = (Button) findViewById(R.id.forget_authentication_bty);

		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mAuth.setOnClickListener(this);
	}

	private void initHandler() {
		handler = new Handler() {

			public void handleMessage(Message msg) {
				if (msg.what <= 0) {
					mAuth.setText(R.string.string_register_getverifycode);
					mAuth.setClickable(true);
				} else {
					mAuth.setClickable(false);
					mAuth.setText(String.format(getString(R.string.string_register_wait), msg.what));
				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.forget_authentication_bty:
			authentication();
			break;
		case R.id.forget_submitbtn:
			submit();
			break;

		default:
			break;
		}
	}

	private void submit() {
		if (!check(2)) {
			return;
		}
		try {
			showDefaultProgress();
			JSONObject json = new JSONObject();
			json.put("phone", phone.getText().toString().trim());
			json.put("vcode", authentication.getText().toString().trim());
			json.put("password", MD5.encode(password1.getText().toString().trim()));
			json.put("user_type", 1);
			JSONObject params = new JSONObject();
			params.put(Constants.ACTION, Constants.GET_PASSWORD);
			params.put(Constants.JSON, json.toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, params, null, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					dismissProgress();
					switch (code) {
					case ResultCode.RESULT_OK:
						showMsg("找回密码成功，请登陆");
						finish();
						break;
					case ResultCode.RESULT_ERROR:
						showMsg(result.toString());
						break;
					case ResultCode.RESULT_FAILED:
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

	private boolean check(int num) {
		if (num == 1) {
			if (TextUtils.isEmpty(phone.getText().toString().trim())) {
				showMsg(R.string.string_login_phone);
				phone.requestFocus();
				return false;
			}
		} else if (num == 2) {
			if (TextUtils.isEmpty(phone.getText().toString().trim())) {
				showMsg(R.string.string_login_phone);
				phone.requestFocus();
				return false;
			}

			if (!CheckUtils.checkPhone(phone.getText().toString().trim())) {
				showMsg("手机号码格式不正确");
				phone.requestFocus();
				return false;
			}

			if (TextUtils.isEmpty(authentication.getText().toString().trim())) {
				showMsg("请输入验证码");
				authentication.requestFocus();
				return false;
			}

			if (TextUtils.isEmpty(password1.getText().toString().trim())) {
				showMsg(R.string.string_register_new_password);
				password1.requestFocus();
				return false;
			}

			if (TextUtils.isEmpty(password2.getText().toString().trim())) {
				showMsg(R.string.string_register_new_password2);
				password2.requestFocus();
				return false;
			}

			if (!password1.getText().toString().trim().equals(password2.getText().toString().trim())) {
				showMsg("密码不一致");
				password2.requestFocus();
				password2.setText("");
				return false;
			}
		}
		return true;
	}

	/** 发送验证短信 */
	private void authentication() {
		if (!check(1)) {
			return;
		}
		time = TIME;
		timer = new Timer();
		timerTask = new TimerTask() {

			public void run() {
				handler.sendEmptyMessage(time--);
			}
		};
		JSONObject json = new JSONObject();
		try {
			json.put(Constants.ACTION, Constants.GET_PASSWORD_VCODE);
			json.put(Constants.JSON, new JSONObject().put("phone", phone.getText().toString().trim()));
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, json, null, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					switch (code) {
					case ResultCode.RESULT_OK:
						timer.schedule(timerTask, 0, 1000);
						mAuth.setClickable(false);
						break;
					case ResultCode.RESULT_ERROR:
						showMsg(result.toString());
						break;
					case ResultCode.RESULT_FAILED:
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
