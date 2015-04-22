package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.OnlineSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper.LocCallback;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

/**
 * 在途货源
 * 
 * @author lenovo
 */
public class OnLineSourceActivity extends BaseActivity {

	private TextView mNumber, mFrom, mSend, mLine;
	private TextView mHuoZhuName, mHuoZhuTelcom, mHuoZhuPhone;
    private View mBack;
	// 返回,联系人,聊天,评价
	private Button mTouShu, mChat, mLocation;

	private Button btn_huozhu_telcom, btn_huozhu_phone;

	private OnlineSourceInfo mSourceInfo;
	private View mListContainer;
	// private TextView mText;
	private Button mRetry;

	private CheckBox chk1, chk2, chk3, chk4, chk5, chk6;
	/** 定位相关 控制器 */

	private CheckBox[] chks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_online_source);
		initViews();
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_title_online_source);


		btn_huozhu_telcom = (Button) findViewById(R.id.btn_huozhu_telcom);
		btn_huozhu_phone = (Button) findViewById(R.id.btn_huozhu_phone);
		mTouShu = (Button) findViewById(R.id.home_id_onlinesource_toushu);
		mChat = (Button) findViewById(R.id.home_id_onlinesource_chat);
		mLocation = (Button) findViewById(R.id.home_id_onlinesource_location);

		mNumber = (TextView) findViewById(R.id.home_id_onlinesource_number);
		mLine = (TextView) findViewById(R.id.home_id_onlinesource_line);
		mFrom = (TextView) findViewById(R.id.home_id_onlinesource_from);
		mSend = (TextView) findViewById(R.id.home_id_onlinesource_send);

		mHuoZhuName = (TextView) findViewById(R.id.home_id_onlinesource_huozhu_name);
		mHuoZhuTelcom = (TextView) findViewById(R.id.home_id_onlinesource_huozhu_telcom);
		mHuoZhuPhone = (TextView) findViewById(R.id.home_id_onlinesource_huozhu_phone);

		// mProgressContainer = findViewById(R.id.progressContainer);
		mListContainer = findViewById(R.id.listContainer);
		// mProgressBar=(ProgressBar)findViewById(android.R.id.progress);
		// mText = (TextView) findViewById(android.R.id.text1);
		mRetry = (Button) findViewById(android.R.id.button1);

		chk1 = (CheckBox) findViewById(R.id.chk1);
		chk2 = (CheckBox) findViewById(R.id.chk2);
		chk3 = (CheckBox) findViewById(R.id.chk3);
		chk4 = (CheckBox) findViewById(R.id.chk4);
		chk5 = (CheckBox) findViewById(R.id.chk5);
		chk6 = (CheckBox) findViewById(R.id.chk6);

		btn_huozhu_telcom.setOnClickListener(this);
		btn_huozhu_phone.setOnClickListener(this);
		mTouShu.setOnClickListener(this);
		mChat.setOnClickListener(this);
		mLocation.setOnClickListener(this);
		mRetry.setOnClickListener(this);
		chk1.setOnClickListener(this);
		chk2.setOnClickListener(this);
		chk4.setOnClickListener(this);
		chk5.setOnClickListener(this);
		chk6.setOnClickListener(this);

		chks = new CheckBox[] { chk1, chk2, chk3, chk4, chk5, chk6 };
	}

	// 获取在途货源
	private void getData() {
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION,
					Constants.QUERY_PENDING_SOURCE_ORDER_IN_SHIPPING);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, null);
			mRetry.setVisibility(View.GONE);
            showSpecialProgress();
			// mText.setVisibility(View.VISIBLE);
			// mText.setText(R.string.progress_loading);
			// mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					OnlineSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result != null) {
									List<OnlineSourceInfo> mList = (List<OnlineSourceInfo>) result;
									if (!mList.isEmpty()) {
										mSourceInfo = mList.get(0);
										showOnlineSource(mSourceInfo);
										// mProgressContainer.setVisibility(View.GONE);
										mListContainer
												.setVisibility(View.VISIBLE);
										dismissProgress();

									} else {
										dismissProgress();
										// mText.setText(R.string.tips_onlinesource_empty);
									}
								} else {
									dismissProgress();
									// mText.setText(R.string.tips_onlinesource_empty);
								}
								break;
							case ResultCode.RESULT_ERROR:
								if (result != null) {
									showMsg(result.toString());
									dismissProgress();
									// mText.setVisibility(View.GONE);
									mRetry.setVisibility(View.VISIBLE);
								}
								break;
							case ResultCode.RESULT_FAILED:
								if (result != null) {
									showMsg(result.toString());
									dismissProgress();
									// mText.setVisibility(View.GONE);
									mRetry.setVisibility(View.VISIBLE);
								}
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

	// 根据状态，设置checkbox
	private void setState(int state) {
		if (state == -1) {
			chk1.setChecked(false);
			chk2.setChecked(false);
			chk3.setChecked(false);
			chk4.setChecked(false);
			chk5.setChecked(false);
			chk6.setChecked(false);
			chk1.setEnabled(true);
			chk2.setEnabled(true);
			chk3.setEnabled(true);
			chk4.setEnabled(true);
			chk5.setEnabled(true);
			chk6.setEnabled(true);
		}
		mSourceInfo.setExecute_status(state);
		final int length = chks.length;
		for (int i = 0; i < length; i++) {
			if (i + 1 > state) {
				chks[i].setEnabled(true);
				chks[i].setChecked(false);
			} else if (i + 1 == state) {
				chks[i].setEnabled(false);
				chks[i].setChecked(true);
			} else {
				chks[i].setChecked(true);
				chks[i].setEnabled(false);
			}
		}
	}

	private void showOnlineSource(OnlineSourceInfo mOnlineSourceInfo) {
		mNumber.setText(String.format(getString(R.string.sourcedetail_number),
				mOnlineSourceInfo.getId()));
		mLine.setText(new StringBuilder()
				.append(mOnlineSourceInfo.getStart_province_str())
				.append(CheckUtils.checkIsNull(mOnlineSourceInfo
						.getStart_city_str()))
				.append(CheckUtils.checkIsNull(mOnlineSourceInfo
						.getStart_district_str()))
				.append("-")
				.append(mOnlineSourceInfo.getEnd_province_str())
				.append(CheckUtils.checkIsNull(mOnlineSourceInfo
						.getEnd_city_str()))
				.append(CheckUtils.checkIsNull(mOnlineSourceInfo
						.getEnd_district_str())));
		mFrom.setText(String.format(
				getString(R.string.string_onlinesource_from),
				mOnlineSourceInfo.getCompany_name()));
		mSend.setText(String.format(
				getString(R.string.string_onlinesource_send),
				application.getDriverName()));

		mHuoZhuName.setText("联系人："
				+ CheckUtils.checkIsNull(mOnlineSourceInfo.getUser_name()));
		mHuoZhuTelcom.setText("座机："
				+ CheckUtils.checkIsNull(mOnlineSourceInfo.getUser_telcom()));
		mHuoZhuPhone.setText("手机："
				+ CheckUtils.checkIsNull(mOnlineSourceInfo.getUser_phone()));

		// -1：未开始。1-到达装货，2-启程，3-在途，4-到达目的地，5-卸货,6-回单密码完成
		setState(mOnlineSourceInfo.getExecute_status());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mSourceInfo == null) {
			getData();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {
		case R.id.home_id_onlinesource_chat: {
			Intent intent = new Intent(context, ChatListActivity.class);
			intent.putExtra("user_id", mSourceInfo.getUser_id() + "");
			intent.putExtra("user_name", mSourceInfo.getUser_name());
			startActivity(intent);
		}
			break;
		case R.id.home_id_onlinesource_toushu:
			Uri uri = Uri.parse("tel:" + "4008765156");
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(intent);
			break;
		case R.id.home_id_onlinesource_evaluate:
			startActivity(new Intent(context, EvaluateActivity.class).putExtra(
					"onlineSourceInfo", mSourceInfo));
			break;
		case android.R.id.button1:
			getData();
			break;
		case R.id.home_id_onlinesource_location:

			showDefaultProgress();
			LocHelper.getInstance(context).getResult(new LocCallback() {

				@Override
				public void onRecivedLoc(double lat, double lng, String addr) {
					dismissProgress();

					if (lat == 0 || lng == 0 || TextUtils.isEmpty(addr)) {
						return;
					}

					JSONObject jsonObject = new JSONObject();
					try {

						String submitJson = new JSONObject()
								.put("order_id", mSourceInfo.getId())
								.put("location", addr).put("longitude", lng)
								.put("latitude", lng).toString();

						//jsonObject.put(Constants.ACTION, Constants.SHIPPING_ORDER_UPDATE_LOCATION);
						jsonObject.put(Constants.TOKEN, application.getToken());
						jsonObject.put(Constants.JSON, submitJson);
						ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
								jsonObject, null, new AjaxCallBack() {

									@Override
									public void receive(int code, Object result) {
										switch (code) {
										case ResultCode.RESULT_OK:
											showMsg("成功上报位置");
											break;
										case ResultCode.RESULT_ERROR:
											if (result instanceof String)
												showMsg(result.toString());
											break;
										case ResultCode.RESULT_FAILED:
											if (result instanceof String)
												showMsg(result.toString());
											break;

										}
									}
								});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			break;
		case R.id.chk1:
			changeState(mSourceInfo.getId(), 1);
			break;
		case R.id.chk2:
			changeState(mSourceInfo.getId(), 2);
			break;

		case R.id.chk4:
			changeState(mSourceInfo.getId(), 4);
			break;
		case R.id.chk5:
			changeState(mSourceInfo.getId(), 5);
			break;
		case R.id.chk6:
			chk6.setChecked(false);
			returnPassword();
			break;

		case R.id.btn_huozhu_telcom:
			if (!TextUtils.isEmpty(mSourceInfo.getUser_telcom())) {
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ mSourceInfo.getUser_telcom())));
			}
			break;
		case R.id.btn_huozhu_phone:
			if (!TextUtils.isEmpty(mSourceInfo.getUser_phone())) {
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ mSourceInfo.getUser_phone())));
			}
			break;

		default:
			break;
		}
	}

	// 回单密码
	private void returnPassword() {
		final GrabDialog dialog = new GrabDialog(context);
		dialog.show();
		final EditText mInput = (EditText) dialog
				.findViewById(android.R.id.text1);
		mInput.setInputType(InputType.TYPE_CLASS_TEXT);
		dialog.setTitle("提示");
		dialog.setMessage("请输入回单密码：");
		dialog.setLeftButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (TextUtils.isEmpty(mInput.getText())) {
						showMsg("回单密码不能为空!");
					} else {
						dialog.dismiss();
						final JSONObject params = new JSONObject();
						try {
							params.put(Constants.ACTION,
									Constants.VALIDATE_RECEIPT_PASSWORD);
							params.put(Constants.TOKEN, application.getToken());
							params.put(
									Constants.JSON,
									new JSONObject()
											.put("order_id",
													mSourceInfo.getId())
											.put("receipt_password",
													mInput.getText().toString())
											.toString());
							showDefaultProgress();
							ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
									params, null, new AjaxCallBack() {

										@Override
										public void receive(int code,
												Object result) {
											dismissProgress();
											switch (code) {
											case ResultCode.RESULT_OK:
												showMsg("订单已完成");
												finish();
												startActivity(new Intent(
														context,
														EvaluateActivity.class)
														.putExtra(
																"onlineSourceInfo",
																mSourceInfo));
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
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		});
		dialog.setRightButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	// 更新状态
	private void changeState(int order_id, final int state) {
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION,
					Constants.SHIPPING_ORDER_UPDATE_STATUS);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(
					Constants.JSON,
					new JSONObject().put("order_id", order_id)
							.put("status", state).toString());
			showProgress("正在更新状态");
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								showMsg("状态更新成功");
								setState(state);
								break;
							case ResultCode.RESULT_ERROR:
								setState(mSourceInfo.getExecute_status());
								if (result != null)
									showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								setState(mSourceInfo.getExecute_status());
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

}
