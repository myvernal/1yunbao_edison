package com.maogousoft.logisticsmobile.driver.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.util.Common;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.info.LoginActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * loading界面
 * 
 * @author lenovo
 */
public class LoadingActivity extends BaseActivity {

	private ImageView logo;

	private AnimationDrawable ad = null;

	private Timer mTimer;

	private TimerTask mTimerTask;

	private WindowManager.LayoutParams attrs;

	// 是否正在执行登录操作
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initViews();

	}

	// 初始化视图控件
	private void initViews() {

		logo = (ImageView) findViewById(R.id.loading_logo);
		logo.setBackgroundResource(R.drawable.animation_loading);
		ad = (AnimationDrawable) logo.getBackground();

		mTimer = new Timer();
		mTimerTask = new TimerTask() {

			public void run() {
				startActivity(new Intent(context, LoginActivity.class));
				finish();
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoginTask().execute();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (ad != null) {
			ad.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ad != null) {
			ad.stop();
		}
		if (mTimer != null) {
			mTimer.cancel();
		}
		mTimer = null;
		mTimerTask = null;
	}

	private class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// 复制数据库
			copyDB();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// 判断当前网络是否可用
			if (!application.checkNetWork()) {
				MyAlertDialog dialog = new MyAlertDialog(context);
				dialog.show();
				dialog.setTitle("提示");
				dialog.setMessage("网络错误,请检查网络!");
				dialog.setLeftButton("网络设置", new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intentToNetwork = new Intent(
								"android.settings.WIRELESS_SETTINGS");
						startActivity(intentToNetwork);
					}
				});
				dialog.setRightButton("退出", new OnClickListener() {

					@Override
					public void onClick(View v) {
						application.finishAllActivity();
					}
				});
				return;
			}
			if (isLogin) {
				return;
			}
			final String phone = application.getUserName();
			final String password = application.getPassword();
            final String userType = application.getUserType() == Constants.USER_DRIVER ? Constants.DRIVER_LOGIN : Constants.USER_LOGIN;
			// 如果保存了用户名和密码，且自动登录
			if (application.checkAutoLogin() && !TextUtils.isEmpty(phone)
					&& !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userType)) {
				final JSONObject jsonObject = new JSONObject();
				try {
					isLogin = true;
					jsonObject.put(Constants.ACTION, userType);
					jsonObject.put(Constants.TOKEN, null);
					jsonObject.put(
							Constants.JSON,
							new JSONObject().put("phone", phone)
									.put("password", MD5.encode(password))
									.put("device_type", Constants.DEVICE_TYPE)
									.toString());
					ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
							jsonObject, UserInfo.class, new AjaxCallBack() {

								@Override
								public void receive(int code, Object result) {
									isLogin = false;
									switch (code) {
									case ResultCode.RESULT_OK:
										UserInfo userInfo = (UserInfo) result;
										application.writeUserInfo(phone,
												password,
												userInfo.getDriver_id());
										application.setToken(userInfo
												.getToken());
										application.writeInfo("name",
												userInfo.getName());
										application.startXMPPService();
										startActivity(new Intent(context,
												MainActivity.class));

										// startActivity(new Intent(context,
										// .class));

										finish();
										break;
									case ResultCode.RESULT_ERROR:
										mTimer.schedule(mTimerTask, 2000);
										break;
									case ResultCode.RESULT_FAILED:
										mTimer.schedule(mTimerTask, 2000);
										break;

									default:
										break;
									}
								}
							});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else
				mTimer.schedule(mTimerTask, 2000);
		}
	}

	// 复制城市数据库
	private void copyDB() {

		String dbPath = new StringBuilder().append("/data/data/")
				.append(getPackageName()).append("/databases/").toString();
		String dbUrl = dbPath + "place.db";
		File dbFile = new File(dbPath);
		if (!dbFile.exists()) {
			dbFile.mkdirs();
		}
		if (!new File(dbUrl).exists()) {
			InputStream is = getResources().openRawResource(R.raw.place);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(dbUrl);
				byte[] b = new byte[1024];
				int count = 0;
				while ((count = is.read(b)) > 0) {
					fos.write(b, 0, count);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
