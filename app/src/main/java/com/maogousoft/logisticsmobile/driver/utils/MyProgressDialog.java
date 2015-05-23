package com.maogousoft.logisticsmobile.driver.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;

/**
 * 自定义 加载框
 * 
 * @author wst
 * @date 2013年12月13日
 */
public class MyProgressDialog extends AlertDialog {

	// @Override
	// public void dismiss() {
	// super.dismiss();
	// animationDrawable.stop();
	// }
	//
	// @Override
	// public void show() {
	// super.show();
	// animationDrawable.start();
	// }

	private TextView mMessageView;

	private CharSequence mMessage;

	private Context mContext;

	// private static AnimationDrawable animationDrawable;

	public MyProgressDialog(Context context) {
		super(context, R.style.DialogTheme);
		this.mContext = context;
	}

	public void setMyTouchOutside(boolean touchOutside) {
		this.setCanceledOnTouchOutside(touchOutside);
	}

	public void setMyCancelable(boolean cancelable) {
		this.setCancelable(cancelable);
	}

	public void setMyMessage(String message) {
		this.setMessage(message);
	}

	public MyProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
	}

	protected MyProgressDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message) {

		return show(context, title, message, null);
	}

	public static MyProgressDialog show(Context context, CharSequence title,
			CharSequence message, OnCancelListener cancleListener) {

		MyProgressDialog dialog = new MyProgressDialog(context);
		dialog.setMessage(message);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(cancleListener);
		dialog.show();

		return dialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(mContext).inflate(R.layout.view_progress, null);
		// ImageView ivProgress = (ImageView)
		// view.findViewById(R.id.iv_progress);
		// ivProgress.setBackgroundResource(R.drawable.animation_progress);
		// animationDrawable = (AnimationDrawable) ivProgress.getBackground();

		mMessageView = (TextView) view.findViewById(R.id.tv_progress);
		setContentView(view);
		if (mMessage != null) {
			setMessage(mMessage);
		}

	}

	public void mydismiss() {
		dismiss();
	}

	public void myShow() {
		this.show();
	}

	@Override
	public void setMessage(CharSequence message) {
		if (mMessageView != null) {
			mMessageView.setText(message);
		} else {
			mMessage = message;
		}
	}
}
