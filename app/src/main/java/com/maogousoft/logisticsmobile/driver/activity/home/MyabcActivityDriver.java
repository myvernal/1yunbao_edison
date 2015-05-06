package com.maogousoft.logisticsmobile.driver.activity.home;

// PR111 个人中心【我的易运宝】
import java.util.List;

import com.maogousoft.logisticsmobile.driver.activity.info.MoneyManagerActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.ybxiang.driver.activity.SearchShopActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.LoginActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 我的ABC
 * 
 * @author lenovo
 */
public class MyabcActivityDriver extends BaseActivity {

	private Button mComplete, mUpdate; // 退出登录
	private Button mContactKeFu; // 联系客服
	private TextView mName, mRecommender, mPhone;// 姓名，手机号，推荐人账号
	private TextView mIsRing; // 铃声的有，无
	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_myabc_driver);
		initViews();
	}

	/**
	 * 主营路线
	 * 
	 * @param view
	 */
	public void onMainLine(View view) {
		startActivity(new Intent(mContext, MainLineActivity.class));
	}

	/**
	 * 车辆信息
	 * 
	 * @param view
	 */
	public void onCarInfo(View view) {
		startActivity(new Intent(mContext, MyabcCarInfoActivity.class));
	}

	/**
	 * 我的账号
	 * 
	 * @param view
	 */
	public void onMyAccountInfo(View view) {
		startActivity(new Intent(mContext, MyabcAccountInfoActivity.class));
	}

    // 好友货源
    public void onFriendsSource(View view) {
        Intent intentNewSource = new Intent(mContext, NewSourceActivity.class);
        intentNewSource.putExtra("getFriendOrderList", true);
        startActivity(intentNewSource);
    }

    // 关注货源
    public void onFocusSource(View view) {
        Intent intentNewSource = new Intent(mContext, NewSourceActivity.class);
        intentNewSource.putExtra("QUERY_MAIN_LINE_ORDER", true);
        startActivity(intentNewSource);
    }

    //货运名片
    public void onCard(View view) {
        startActivity(new Intent(mContext, MyBusinessCard.class));
    }

    // 物流园区
    public void onVIP(View view) {
        startActivity(new Intent(mContext, SearchShopActivity.class));
    }

    // 实用工具
    public void onInteraction(View view) {
        startActivity(new Intent(mContext, OthersActivity.class));
    }

    // 财务管理
    public void onMoneyManager(View view) {
        startActivity(new Intent(mContext, MoneyManagerActivity.class));
    }

	/**
	 * 我的信誉
	 * 
	 * @param view
	 */
	public void onMyReputation(View view) {
		startActivity(new Intent(mContext, MyReputationActivity.class));
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_myabc_title);
		mContactKeFu = (Button) findViewById(R.id.titlebar_id_more);
		mContactKeFu.setText("联系客服");

		mComplete = (Button) findViewById(R.id.myabc_id_complete);
        mUpdate = (Button) findViewById(R.id.myabc_id_update);
        mName = (TextView) findViewById(R.id.myabc_id_name);
		mRecommender = (TextView) findViewById(R.id.myabc_id_recommender);
		mPhone = (TextView) findViewById(R.id.myabc_id_phone);

		mIsRing = (TextView) findViewById(R.id.myabc_is_ring);

		mContactKeFu.setOnClickListener(this);
		mComplete.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	@Override
	public void onClick(View v) {
		((BaseActivity) mContext).setIsRightKeyIntoShare(false);
		super.onClick(v);
		if (v == mComplete) {
			application.logout();
			startActivity(new Intent(mContext, LoginActivity.class));
		} else if(v == mUpdate) {
            if(mAbcInfo == null) {
                Toast.makeText(mContext, "正在获取账号资料,请稍后", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(mContext, OptionalActivity.class).putExtra("info", mAbcInfo));
        } else if (v == mContactKeFu) {
			// PR111 begin
			// Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
			// + "4008765156"));
			// startActivity(intent);
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
					.setTitle(R.string.contact_kehu).setItems(
							R.array.contact_kehu_items,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									// 打电话
									case 0:
										Intent intent = new Intent(
												Intent.ACTION_CALL, Uri
														.parse("tel:"
																+ "4008765156"));
										startActivity(intent);
										break;
									// 发QQ
									case 1:
										Toast.makeText(mContext,
												R.string.contact_kehu_qq,
												Toast.LENGTH_LONG).show();
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

	/**
	 * 给QQ好友发送消息
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
			intent.putExtra(Intent.EXTRA_TEXT, "你好吗？少年");
			intent.putExtra(Intent.EXTRA_CC, "1026947987");
			// 进行发送
			startActivity(intent);
		} else {
			System.out.println("QQ没有安装");
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

									if (application.checkIsRingNewSource()) {
										mIsRing.setText(getString(R.string.string_is_ring, "是"));
									} else {
										mIsRing.setText(getString(R.string.string_is_ring, "否"));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			getABCInfo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 历史订单
	 * 
	 * @param view
	 */
	public void onMyHistoryOrder(View view) {
		startActivity(new Intent(mContext, HistroyOrderActivity.class).putExtra("info", mAbcInfo));
	}

	/**
	 * 是否有声音
	 * 
	 * @param view
	 */
	public void onSound(View view) {

		final MyAlertDialog dialog = new MyAlertDialog(mContext);
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

	}

	@Override
	public void onBackPressed() {
		sendBroadcast(new Intent(MainActivity.ACTION_SWITCH_MAINACTIVITY));
	}
}
