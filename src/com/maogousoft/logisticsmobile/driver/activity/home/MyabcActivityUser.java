package com.maogousoft.logisticsmobile.driver.activity.home;
// PR111 个人中心【我的易运宝】

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.*;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的ABC
 * 
 * @author lenovo
 */
public class MyabcActivityUser extends BaseActivity {

    private Context mContext; // PR111
	// 返回,完善资料
	private Button mBack, mComplete, mUpdate, mContactKeFu;
	private TextView mName, mRecommender, mPhone, mUpdatePwd, mBalance,
			mCharge, mAccountRecord, mOnlineTime;
	private RelativeLayout mHistory;
	// 个人abc信息
	private HuoZhuUserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_myabc_user);
		mContext = MyabcActivityUser.this; // PR111
		initViews();
		getBalance();
		// 隐藏我的易运宝左边的返回按钮
		findViewById(R.id.titlebar_id_back).setVisibility(View.GONE);
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_home_myabc_title);

		setIsRightKeyIntoShare(false);
		mContactKeFu = (Button) findViewById(R.id.titlebar_id_more);
		mContactKeFu.setText("联系客服");

		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mComplete = (Button) findViewById(R.id.myabc_id_complete);
		mUpdate = (Button) findViewById(R.id.myabc_id_update);
		mName = (TextView) findViewById(R.id.myabc_id_name);
		mRecommender = (TextView) findViewById(R.id.myabc_id_recommender);
		mPhone = (TextView) findViewById(R.id.myabc_id_phone);
		mUpdatePwd = (TextView) findViewById(R.id.myabc_id_updatepwd);
		mBalance = (TextView) findViewById(R.id.myabc_id_balance);
		mCharge = (TextView) findViewById(R.id.myabc_id_charge);
		mAccountRecord = (TextView) findViewById(R.id.myabc_id_account_record);
		mOnlineTime = (TextView) findViewById(R.id.myabc_id_onlinetime);
		mHistory = (RelativeLayout) findViewById(R.id.myabc_id_history);
		mHistory.setClickable(true);
		mContactKeFu.setOnClickListener(this);
		mComplete.setOnClickListener(this);
		mHistory.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mUpdatePwd.setOnClickListener(this);
		mCharge.setOnClickListener(this);
		mAccountRecord.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	@Override
	public void onClick(View v) {

		super.onClick(v);
		if (v == mUpdatePwd) {
			startActivity(new Intent(context, UpdatePwdActivity.class));
		} else if (v == mComplete) {
			application.logout();
			startActivity(new Intent(context, LoginActivity.class));
		} else if (v == mCharge) {
			startActivity(new Intent(context, ChargeActivity.class));
		} else if (v == mAccountRecord) {
			startActivity(new Intent(context, AccountRecordActivity.class));
		} else if (v == mUpdate) {
			startActivity(new Intent(context, OptionalActivity.class).putExtra("info", userInfo));
		} else if (v == mHistory) {
			startActivity(new Intent(context, HistroyOrderActivity.class).putExtra("info", userInfo));
		} else if (v == mContactKeFu) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle(R.string.contact_kehu).setItems(R.array.contact_kehu_items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					// 打电话
					case 0:
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"4008765156"));
						startActivity(intent);
						break;
				    // 发QQ
					case 1:
						Toast.makeText(mContext, R.string.contact_kehu_qq, Toast.LENGTH_LONG).show();
						break;

					default:
						break;
					}
					
				}
			});
			builder.create().show();
			// PR111 end
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
								mBalance.setText(String
										.format(getString(R.string.string_home_myabc_balance), object.optDouble("gold")));
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
	private void getABCInfo() {
		// if (mAbcInfo != null) {
		// return;
		// }
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, "");
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    HuoZhuUserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (HuoZhuUserInfo) result;

                                        if (!TextUtils.isEmpty(userInfo.getName())) {
                                            mName.setText(userInfo.getName());
                                        }
                                        if (!TextUtils.isEmpty(userInfo.getCompany_name())) {
                                            mRecommender.setText(userInfo.getCompany_name());
                                        }
                                        mPhone.setText(userInfo.getPhone());
                                        mOnlineTime.setText(userInfo.getAddress());
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			getABCInfo();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onBackPressed() {
		sendBroadcast(new Intent(MainActivity.ACTION_SWITCH_MAINACTIVITY));
	}
}
