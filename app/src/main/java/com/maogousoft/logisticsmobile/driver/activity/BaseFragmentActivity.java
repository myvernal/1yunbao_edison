package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity implements
		OnClickListener {

	protected SQLiteDatabase sdb;

	protected MGApplication application;

	protected Context context;

	protected ImageLoader imageLoader;

	private MyProgressDialog progressDialog;

	private Toast toast;

	private View toastView;

	private boolean isShown = false;

	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MGApplication) getApplication();
		imageLoader = application.getImageLoader();
		sdb = application.getDB();
		context = this;
		init();
	}

	private void init() {
		progressDialog = new MyProgressDialog(context);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		resources = getResources();
		MobclickAgent.onError(this);
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

		LogUtil.i("wst", "basefragmentactivity");

		super.onDestroy();
		isShown = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isShown = true;
		MobclickAgent.onResume(this);
	}

	@Override
	public void onClick(View v) {

	}

	protected void exitAppHint() {

		final MyAlertDialog dialog = new MyAlertDialog(context, R.style.DialogTheme);
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
