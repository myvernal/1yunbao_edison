package com.maogousoft.logisticsmobile.driver.activity.home;
// PR111 个人中心【我的易运宝】

import android.app.AlertDialog;
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
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.activity.CarsListActivity;
import com.ybxiang.driver.activity.CheckCardActivity;
import com.ybxiang.driver.activity.MySourceActivity;
import com.ybxiang.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的ABC
 * 
 * @author lenovo
 */
public class MyabcActivityShipper extends BaseActivity {

	// 返回,完善资料
	private Button mComplete, mContactKeFu;
	private TextView mName, mCompanyName, mPhone;
    private ImageView mPhoto;
	// 个人abc信息
	private ShipperInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_myabc_user);
        if(application.getUserType() == Constants.USER_DRIVER) {
            //如果当前是车主,则跳转到车主信息页面
            startActivity(new Intent(mContext, MyabcActivityDriver.class));
            finish();
        }
		initViews();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_myabc_title);

		setIsRightKeyIntoShare(false);
		mContactKeFu = (Button) findViewById(R.id.titlebar_id_more);
		mContactKeFu.setText("联系客服");

        mComplete = (Button) findViewById(R.id.myabc_id_complete);
		mName = (TextView) findViewById(R.id.myabc_id_name);
		mCompanyName = (TextView) findViewById(R.id.myabc_id_company_name);
		mPhone = (TextView) findViewById(R.id.myabc_id_phone);
        mPhoto = (ImageView) findViewById(R.id.account_photo);
		mContactKeFu.setOnClickListener(this);
        mComplete.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		/*if (v == mUpdatePwd) {
			startActivity(new Intent(mContext, UpdatePwdActivity.class));
		} else if (v == mComplete) {
			application.logout();
			startActivity(new Intent(mContext, LoginActivity.class));
		} else if (v == mCharge) {
			startActivity(new Intent(mContext, ChargeActivity.class));
		} else if (v == mAccountRecord) {
			startActivity(new Intent(mContext, AccountRecordActivity.class));
		} else if (v == mUpdate) {
			startActivity(new Intent(mContext, OptionalActivity.class).putExtra("info", userInfo));
		} else if (v == mHistory) {
			startActivity(new Intent(mContext, MySourceActivity.class));
		} else */
        if (v == mComplete) {
            application.logout();
            startActivity(new Intent(mContext, LoginActivity.class));
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

    // 我的车队
    public void onMyCars(View view) {
        Intent intent = new Intent(mContext, CarsListActivity.class);
        intent.putExtra(Constants.MY_CARS_SEARCH, true);
        startActivity(intent);
    }

    // 验证证件
    public void onCheckCard(View view) {
        startActivity(new Intent(mContext, CheckCardActivity.class));
    }

    // 历史货源
    public void onOldSource(View view) {
        startActivity(new Intent(mContext, MySourceActivity.class));
    }

    /**
     * 我的账号
     *
     * @param view
     */
    public void onMyAccountInfo(View view) {
        startActivity(new Intent(mContext, MyabcAccountInfoActivity.class));
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

	// 获取我的abc信息
	private void getABCInfo() {
		// if (mAbcInfo != null) {
		// return;
		// }
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    ShipperInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (ShipperInfo) result;

                                        if (!TextUtils.isEmpty(userInfo.getName())) {
                                            mName.setText(userInfo.getName());
                                        }
                                        if (!TextUtils.isEmpty(userInfo.getCompany_name())) {
                                            mCompanyName.setText(userInfo.getCompany_name());
                                        }
                                        mPhone.setText(userInfo.getPhone());
                                        ImageLoader.getInstance().displayImage(userInfo.getCompany_logo(), mPhoto, options,
                                                new Utils.MyImageLoadingListener(mContext, mPhoto));
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
