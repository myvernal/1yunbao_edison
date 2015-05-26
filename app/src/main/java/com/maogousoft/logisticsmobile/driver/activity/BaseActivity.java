package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ybxiang.driver.activity.AnonymousActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.NewSourceActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.im.KBBinder;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.ybxiang.driver.activity.CarsListActivity;
import com.ybxiang.driver.activity.SearchDPListActivity;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends FragmentActivity implements OnClickListener {

	private View mShare;

	protected SQLiteDatabase sdb;

	public MGApplication application;

	public Context mContext;

	protected ImageLoader imageLoader;

	protected KBBinder mBinder;

	private MyProgressDialog progressDialog;

	private Toast toast;

	private View toastView;

	private boolean isShown = false;

	private Resources resources;

	// 设置分享内容
	public String content = null;

	public InputMethodManager imm = null;
    public DisplayImageOptions options;
	private boolean isRightKeyIntoShare = true;// 顶部条右键是否进入分享
    private boolean isShowAnonymousActivity = true;
    private int isFirstResume = 0;
    public static final String TAG = "BaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MGApplication) getApplication();
		imageLoader = application.getImageLoader();
		sdb = application.getDB();
		mContext = this;
		mBinder = application.getBinder();
		application.addTask(this);
		init();
	}

    public void setIsShowAnonymousActivity(boolean value) {
        isShowAnonymousActivity = value;
    }

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mShare = findViewById(R.id.titlebar_id_more);
	}

	private void init() {
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		progressDialog = new MyProgressDialog(mContext);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		resources = getResources();
		MobclickAgent.onError(this);

        options = new DisplayImageOptions.Builder().resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageForEmptyUri(R.drawable.ic_img_loading)
                .displayer(new FadeInBitmapDisplayer(300)).build();
	}

	/** 设置分享内容 **/
	public void setShareContent(String content) {
		this.content = content;
	}

	/** 获取分享内容 **/
	public String getShareContent() {
		return this.content;
	}

	public void showProgress(String message) {
        if(isShowAnonymousActivity) {
            return;
        }
		progressDialog.setMessage(message);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

    public void showSpecialProgress(String message) {
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void showSpecialProgress() {
        progressDialog.setMessage(getString(R.string.progress_loading_2));
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

	public void showDefaultProgress() {
        if(isShowAnonymousActivity) {
            return;
        }
		progressDialog.setMessage(resources
				.getString(R.string.progress_loading));
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void dismissProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void showMsg(String msg) {
		if (TextUtils.isEmpty(msg) || !isShown) {
			return;
		}
		if (null == toast) {
			toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		if (null == toastView) {
			toastView = View.inflate(mContext, R.layout.view_toast, null);
		}
		((TextView) toastView).setText(msg);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
				toast.getYOffset() / 2);
		toast.setView(toastView);
		toast.show();
	}

	public void showMsg(int resId) {
		showMsg(getResources().getString(resId));
	}

	@Override
	protected void onPause() {
		super.onPause();
		isShown = false;
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.finishActivity(this);
		isShown = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isShown = true;
		MobclickAgent.onResume(this);
        //如果是匿名登陆,并且不是登陆或者注册页面,需要显示遮罩层
        if(application.isAnonymous() && isShowAnonymousActivity && isFirstResume == 0) {
            isFirstResume++;//显示过一次就不再显示
            startActivity(new Intent(mContext, AnonymousActivity.class));
        } else if(isShowAnonymousActivity && isFirstResume > 0) {
            finish();
        }
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Constants.ANONYMOUS_RESULT_CODE) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setIsRightKeyIntoShare(boolean isRightKeyIntoShare) {
		this.isRightKeyIntoShare = isRightKeyIntoShare;
	}

	@Override
	public void onClick(View v) {
//        else if (v.getId() == R.id.titlebar_id_more) {
//			if (isRightKeyIntoShare) {
//                LogUtil.e(TAG, "titlebar_id_more");
//				startActivity(new Intent(mContext, ShareActivity.class)
//						.putExtra("share", content));
//			}
//		}
	}

	protected void exitAppHint() {

		final MyAlertDialog dialog = new MyAlertDialog(mContext);
		dialog.show();
		dialog.setTitle("提示");
		dialog.setMessage("您确定要退出么？");
		dialog.setLeftButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
                application.finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		dialog.setRightButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

    public void share(String value) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if(TextUtils.isEmpty(value)) {
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.string_share_tips));
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, value);
        }
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    public void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.string_share_tips));
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    public void shareCard(String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/png");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.common_share_info));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, ""));
    }

    // 快速搜索新货源
    public void fastSearchSource(final FocusLineInfo focusLineInfo) {
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
            jsonObject.put(Constants.TOKEN, application.getToken());
            params.put("start_province", focusLineInfo.getStart_province());
            params.put("start_city", focusLineInfo.getStart_city());
            params.put("start_district", focusLineInfo.getStart_district());
            params.put("end_province", focusLineInfo.getEnd_province());
            params.put("end_city", focusLineInfo.getEnd_city());
            params.put("end_district", focusLineInfo.getEnd_district());
            params.put("device_type", Constants.DEVICE_TYPE);
            jsonObject.put(Constants.JSON, params);
            showSpecialProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<NewSourceInfo> mList = (ArrayList<NewSourceInfo>) result;

                                        if (mList.size() != 0) {
                                            Intent intent = new Intent(mContext, NewSourceActivity.class);
                                            intent.putExtra("isFromHomeActivity", true);
                                            intent.putExtra("NewSourceInfos", (Serializable) mList);
                                            intent.putExtra("focusLineInfo", focusLineInfo);
                                            intent.putExtra("params", params.toString());
                                            mContext.startActivity(intent);
                                        } else {
                                            showMsg("暂无满足条件的信息，请扩大搜索范围再试。");
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null) {
                                        // 您当月的免费搜索次数已经用完
                                        // if (result.equals("您当月的免费搜索次数已经用完")) {
                                        final MyAlertDialog dialog = new MyAlertDialog(
                                                mContext);
                                        dialog.show();
                                        dialog.setTitle("提示");
                                        // 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
                                        dialog.setMessage(result.toString());
                                        dialog.setLeftButton("确定",
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();

                                                        String content = null;
                                                        startActivity(new Intent(
                                                                mContext,
                                                                ShareActivity.class)
                                                                .putExtra("share",
                                                                        content));
                                                        finish();
                                                    }
                                                });

                                        // }
                                    }
                                    // showMsg(result.toString());
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

    // 快速查找车源
    public void fastSearchCar(final FocusLineInfo focusLineInfo) {
        JSONObject params = new JSONObject();
        try {
            params.put("start_province", focusLineInfo.getStart_province());
            params.put("start_city", focusLineInfo.getStart_city());
            params.put("start_district", focusLineInfo.getStart_district());
            params.put("end_province", focusLineInfo.getEnd_province());
            params.put("end_city", focusLineInfo.getEnd_city());
            params.put("end_district", focusLineInfo.getEnd_district());
            params.put("device_type", Constants.DEVICE_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext, CarsListActivity.class);
        intent.putExtra(Constants.COMMON_KEY, params.toString());
        intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_CAR_SOURCE);
        //将搜索条件传下去
        mContext.startActivity(intent);
    }

    // 快速查找车源
    public void fastSearchSpecialLine(final FocusLineInfo focusLineInfo) {
        JSONObject params = new JSONObject();
        try {
            params.put("city_start", focusLineInfo.getStart_city_str());
            params.put("city_end", focusLineInfo.getEnd_city_str());
            params.put("area_start", focusLineInfo.getStart_area_str());
            params.put("area_end", focusLineInfo.getEnd_area_str());
            params.put("device_type", Constants.DEVICE_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(mContext, SearchDPListActivity.class);
        intent.putExtra(Constants.COMMON_KEY, params.toString());
        intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_SPECIAL_LINE);
        //将搜索条件传下去
        mContext.startActivity(intent);
    }
}
