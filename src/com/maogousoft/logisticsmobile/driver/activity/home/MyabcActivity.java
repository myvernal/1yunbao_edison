package com.maogousoft.logisticsmobile.driver.activity.home;
// PR111 个人中心【我的易运宝】
import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.AccountRecordActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChangePathActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.LoginActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.UpdatePwdActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 我的ABC
 * 
 * @author lenovo
 */
public class MyabcActivity extends BaseActivity {

    private Context mContext; // PR111
	// 返回,完善资料
	private Button mBack, mComplete, mUpdate;

	private Button mContactKeFu;

	private TextView mName, mRecommender, mPhone, mUpdatePwd, mBalance,
			mCharge, mAccountRecord, mCredit, mOnlineTime, mOnlineTimeRank,
			mRecommonedCount, mRecommonedCountRank, mClinch, mClinchRank;

	private RelativeLayout mCreditContainer;

	private RelativeLayout mHistory;

	private RatingBar mCreditRatingbar;

	private LinearLayout mLayoutIsRing;

	private Button mChangePath;
	private TextView mPath1, mPath2, mPath3, mCarNum, mCarlength, mCartype,
			mCarzhaizhong, mIsRing;

	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_myabc);
		mContext = MyabcActivity.this; // PR111
		initViews();
		getBalance();
		// 隐藏我的易运宝左边的返回按钮
		((Button)findViewById(R.id.titlebar_id_back)).setVisibility(View.GONE);
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
		mCredit = (TextView) findViewById(R.id.myabc_id_mycredit);
		mOnlineTime = (TextView) findViewById(R.id.myabc_id_onlinetime);
		mOnlineTimeRank = (TextView) findViewById(R.id.myabc_id_onlinetime_rank);
		mRecommonedCount = (TextView) findViewById(R.id.myabc_id_recommendcount);
		mRecommonedCountRank = (TextView) findViewById(R.id.myabc_id_recommendcount_rank);
		mClinch = (TextView) findViewById(R.id.myabc_id_clinch);
		mClinchRank = (TextView) findViewById(R.id.myabc_id_clinch_rank);
		mCreditContainer = (RelativeLayout) findViewById(R.id.myabc_id_mycreditcontainer);
		mCreditContainer.setClickable(true);
		mHistory = (RelativeLayout) findViewById(R.id.myabc_id_history);
		mHistory.setClickable(true);
		mCreditRatingbar = (RatingBar) findViewById(R.id.myabc_id_ratingbar);
		mCreditRatingbar.setIsIndicator(true);
		mLayoutIsRing = (LinearLayout) findViewById(R.id.layout_is_ring);
		mLayoutIsRing.setClickable(true);

		mChangePath = (Button) findViewById(R.id.myabc_id_change_path);
		mPath1 = (TextView) findViewById(R.id.myabc_id_path1);
		mPath2 = (TextView) findViewById(R.id.myabc_id_path2);
		mPath3 = (TextView) findViewById(R.id.myabc_id_path3);
		mCarNum = (TextView) findViewById(R.id.myabc_id_car_num);
		mCarlength = (TextView) findViewById(R.id.myabc_id_car_length111);
		mCartype = (TextView) findViewById(R.id.myabc_id_car_type);
		mCarzhaizhong = (TextView) findViewById(R.id.myabc_id_car_zhaizhong);
		mIsRing = (TextView) findViewById(R.id.myabc_is_ring);

		mContactKeFu.setOnClickListener(this);
		mComplete.setOnClickListener(this);
		mCreditContainer.setOnClickListener(this);
		mHistory.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mUpdatePwd.setOnClickListener(this);
		mCharge.setOnClickListener(this);
		mAccountRecord.setOnClickListener(this);
		mUpdate.setOnClickListener(this);
		mChangePath.setOnClickListener(this);
		mLayoutIsRing.setOnClickListener(this);
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
		} else if (v == mCreditContainer) {
			startActivity(new Intent(context, MyCreditActivity.class).putExtra(
					"info", mAbcInfo));
		} else if (v == mComplete) {
			application.logout();
			startActivity(new Intent(context, LoginActivity.class));

		} else if (v == mCharge) {
			startActivity(new Intent(context, ChargeActivity.class));
		} else if (v == mAccountRecord) {
			startActivity(new Intent(context, AccountRecordActivity.class));
		} else if (v == mUpdate) {
			startActivity(new Intent(context, OptionalActivity.class).putExtra(
					"info", mAbcInfo));
		} else if (v == mHistory) {
			startActivity(new Intent(context, HistroyOrderActivity.class)
					.putExtra("info", mAbcInfo));
		} else if (v == mChangePath) {
			// if (mAbcInfo == null) {
			// showMsg("请等待获取线路");
			// } else {

			String[] array = new String[] { "线路1", "线路2", "线路3" };
			new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
					context).setTitle("选择需要修改的路线")
					.setItems(array, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Intent intent = new Intent(context,
									ChangePathActivity.class);
							// intent.putExtra("info", mAbcInfo);
							intent.putExtra("path", which);
							startActivityForResult(intent, 1);
						}
					}).show();

			// }
		} else if (mLayoutIsRing == v) {

			final MyAlertDialog dialog = new MyAlertDialog(context);
			dialog.show();
			dialog.setTitle("提示");
			dialog.setMessage("收到新货源，需要提示音么？");
			dialog.setLeftButton("是", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mIsRing.setText("是");
					application.writeIsRingNewSource(true);
				}
			});
			dialog.setRightButton("否", new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					mIsRing.setText("否");
					application.writeIsRingNewSource(false);
				}
			});
		} else if (v == mContactKeFu) {
		    // PR111 begin
//			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//					+ "4008765156"));
//			startActivity(intent);
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
						// sendImageToQQ("hello");
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

	// 向QQ发送消息
	/**
	 * 给QQ好友发送消息
	 * 
	 * @param imagePath
	 */
	public void sendImageToQQ(String text) {
		// 创建查询条件的intent，用于查询哪些软件的有activity支持发送
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/*");
		// 查询所有支持的信息
		List<ResolveInfo> infos = getPackageManager().queryIntentActivities(
				intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		// QQ的支持发送的activity信息
		ActivityInfo qqInfo = null;
		// 循环找到QQ的
		for (ResolveInfo info : infos) {
			ActivityInfo in = info.activityInfo;
			if (in.packageName.equals("com.tencent.mobileqq")) { // 如果存在QQ的包名
				qqInfo = in;
				break;
			}
		}
		if (qqInfo != null) {
			// 再次设置intent，进行发送,指向QQ的这个可以发送的页面
			intent.setComponent(new ComponentName(qqInfo.packageName,
					qqInfo.name));
			// 设置需要发送的数据
			intent.putExtra(Intent.EXTRA_TEXT,
					"你好吗？少年");
			intent.putExtra(Intent.EXTRA_CC, "1026947987");
			// 进行发送
			startActivity(intent);
		}else{
			System.out.println("QQ没有安装");
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
										.format(getString(R.string.string_home_myabc_balance),
												object.optDouble("gold")));
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
			jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, "");
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					AbcInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result != null) {
									mAbcInfo = (AbcInfo) result;

									if (!TextUtils.isEmpty(mAbcInfo.getName())) {
										mName.setText(mAbcInfo.getName());
									}
									if (!TextUtils.isEmpty(mAbcInfo
											.getRecommender())) {
										mRecommender.setText(mAbcInfo
												.getRecommender());
									}
									mPhone.setText(mAbcInfo.getPhone());
									mOnlineTime.setText(mAbcInfo
											.getOnline_time() + "天");
									mOnlineTimeRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getOnline_time_rank()));
									mRecommonedCount.setText(mAbcInfo
											.getRecommender_count() + "人");
									mRecommonedCountRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getRecommender_count_rank()));
									mClinch.setText(mAbcInfo.getOrder_count()
											+ "单");
									mClinchRank.setText(String
											.format(getString(R.string.string_home_myabc_rank),
													mAbcInfo.getOrder_count_rank()));

									CityDBUtils mDBUtils = new CityDBUtils(
											application.getCitySDB());
									String path1Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province(),
											mAbcInfo.getStart_city(),
											mAbcInfo.getEnd_province(),
											mAbcInfo.getEnd_city());
									String path2Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province2(),
											mAbcInfo.getStart_city2(),
											mAbcInfo.getEnd_province2(),
											mAbcInfo.getEnd_city2());
									String path3Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province3(),
											mAbcInfo.getStart_city3(),
											mAbcInfo.getEnd_province3(),
											mAbcInfo.getEnd_city3());

									mPath1.setText(path1Str);
									mPath2.setText(path2Str);
									mPath3.setText(path3Str);

									if (!TextUtils.isEmpty(mAbcInfo
											.getPlate_number())) {
										mCarNum.setText(mAbcInfo
												.getPlate_number());
									}

									mCarlength.setText(mAbcInfo.getCar_length()
											+ "米");
									if (!TextUtils.isEmpty(mAbcInfo
											.getCar_type_str())) {
										mCartype.setText(mAbcInfo
												.getCar_type_str());
									}
									mCarzhaizhong.setText(mAbcInfo
											.getCar_weight() + "吨");

									if (application.checkIsRingNewSource()) {
										mIsRing.setText("是");
									} else {
										mIsRing.setText("否");
									}

									float score = mAbcInfo.getScore();
									if (score == 0) {
										score = 5;
									}
									mCreditRatingbar.setRating(score);

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
