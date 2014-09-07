package com.maogousoft.logisticsmobile.driver.activity.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CarTypeListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.MD5;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;

/**
 * 完善货主资料
 * 
 * @author ybxiang
 */
public class OptionalShipperActivity extends BaseActivity {

	private Button mBack, mLogin, mRegister;

	private EditText mName, mChezhuPhone;

	private boolean isFormRegisterActivity;

	private boolean isFormPushSourceDetail;// 是否从推送进入，并且没有完善资料，然后进入的本页面

	private AbcInfo abcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_optional_shipper);
		initViews();
        setIsShowAnonymousActivity(false);
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_register_complete_title);
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mLogin = (Button) findViewById(R.id.titlebar_id_more);
		mRegister = (Button) findViewById(R.id.info_id_register_submit);

		mChezhuPhone = (EditText) findViewById(R.id.info_id_register_chezhu_phone);
		mName = (EditText) findViewById(R.id.info_id_register_name);

		mBack.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mRegister.setOnClickListener(this);

		mChezhuPhone.setOnFocusChangeListener(mOnFocusChangeListener);
		mName.setOnFocusChangeListener(mOnFocusChangeListener);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {

		case R.id.info_id_register_submit:
			submit();
			break;

		default:
			break;
		}
	}

	// 输入框失去焦点事件监听
	private final OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == mName && !hasFocus) {

			} else if (v == mChezhuPhone && !hasFocus) {

			}

		}
	};

	// 提交注册
	private void submit() {
		if (!CheckUtils.checkIsEmpty(mName)) {
			showMsg("联系人不能为空");
			mName.requestFocus();
			return;
		}

		if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
			if (!CheckUtils.checkIsEmpty(mChezhuPhone)) {
				showMsg("座机号码不能为空");
				mChezhuPhone.requestFocus();
				mChezhuPhone.selectAll();
				return;
			}
		}

		final JSONObject jsonObject = new JSONObject();
		final JSONObject params = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.SHIPPER_REG_OPTIONAL2);
			jsonObject.put(Constants.TOKEN, null);

			if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
				params.put("telcom", mChezhuPhone.getText().toString());
			}
			params.put("contact", mName.getText().toString());
			params.put("device_type", Constants.DEVICE_TYPE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, params);
            showSpecialProgress();
			ApiClient.doWithObject(Constants.SHIPPER_SERVER_URL, jsonObject,
					UserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:

								if (isFormRegisterActivity) {
									startActivity(new Intent(context,
											MainActivity.class));
									// 第一次进入，获取 所有 新货源
									// getNewSourceData(1);
								}

								onBackPressed();

								showMsg("完善资料成功.");

								application.writeIsRegOptional(true);

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

	// 获取所有新货源推送
	private void getNewSourceData(int page) {
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
					.toString());

			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NewSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									List<NewSourceInfo> mList = (List<NewSourceInfo>) result;

									if (mList != null) {
										NotificationManager mNotificationManager = (NotificationManager) context
												.getSystemService(Context.NOTIFICATION_SERVICE);

										for (int i = 0; i < mList.size(); i++) {
											NewSourceInfo newSourceInfo = mList
													.get(i);

											StringBuffer sb = new StringBuffer();
											sb.append("有新的货源,");
											sb.append(newSourceInfo
													.getCargo_desc());
											sb.append(",");
											sb.append(newSourceInfo
													.getCargo_number());

											switch (newSourceInfo
													.getCargo_unit()) {
											case 1:
												sb.append("车");
												break;
											case 2:
												sb.append("吨");
												break;
											case 3:
												sb.append("方");
												break;
											}

											mNotificationManager.notify(
													i,
													getOrderNotification(
															context,
															sb.toString(),
															newSourceInfo, i));
										}
									}
								}
								break;
							}

						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 跳入新货源详情
	private Notification getOrderNotification(Context context, String title,
			NewSourceInfo sourceInfo, int num) {
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());
		final Intent intent = new Intent(context, SourceDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(SourceDetailActivity.ORDER_INFO, sourceInfo);
		intent.putExtra("type", "MessageBroadCastReceiver");
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, num,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, "点击查看订单详情",
				pendingIntent);

		if (application.checkIsRingNewSource()) {
			notification.sound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + R.raw.mm);
		} else {
			notification.sound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + R.raw.silence);
		}

		// notification.sound = Uri.parse("android.resource://"
		// + context.getPackageName() + "/" + R.raw.mm);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		return notification;
	}

	@Override
	public void onBackPressed() {
		if (isFormPushSourceDetail) {
			application.finishAllActivity();
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);

		} else {
			super.onBackPressed();
		}
	}

}
