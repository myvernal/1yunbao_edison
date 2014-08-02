package com.maogousoft.logisticsmobile.driver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.im.KBBinder;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity implements OnClickListener {

	private Button mShare;

	protected SQLiteDatabase sdb;

	public MGApplication application;

	public Context context;

	protected ImageLoader imageLoader;

	protected KBBinder mBinder;

	private MyProgressDialog progressDialog;

	private Toast toast;

	private View toastView;

	private boolean isShown = false;

	private Resources resources;

	// 设置分享内容
	private String content = null;

	public InputMethodManager imm = null;

	private boolean isRightKeyIntoShare = true;// 顶部条右键是否进入分享

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.i("wst", "baseactivity-onCreate");

		application = (MGApplication) getApplication();
		imageLoader = application.getImageLoader();
		sdb = application.getDB();
		context = this;
		mBinder = application.getBinder();
		application.addTask(this);
		init();

	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mShare = (Button) findViewById(R.id.titlebar_id_more);
		if (mShare != null) {
			mShare.setOnClickListener(this);
		}
	}

	private void init() {

		LogUtil.i("wst", "baseactivity-init");

		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		progressDialog = new MyProgressDialog(context);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		resources = getResources();
		MobclickAgent.onError(this);
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
		progressDialog.setMessage(message);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void showDefaultProgress() {
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
			toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		if (null == toastView) {
			toastView = View.inflate(context, R.layout.view_toast, null);
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
	}

	public void setIsRightKeyIntoShare(boolean isRightKeyIntoShare) {
		this.isRightKeyIntoShare = isRightKeyIntoShare;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.titlebar_id_back) {
			onBackPressed();
		} else if (v.getId() == R.id.titlebar_id_more) {
			if (isRightKeyIntoShare) {
				startActivity(new Intent(context, ShareActivity.class)
						.putExtra("share", content));
			}
		}
	}

	protected void exitAppHint() {

		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.show();
		dialog.setTitle("提示");
		dialog.setMessage("您确定要退出么？");
		dialog.setLeftButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		dialog.setRightButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}
}
