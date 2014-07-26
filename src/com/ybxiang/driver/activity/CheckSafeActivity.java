package com.ybxiang.driver.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

public class CheckSafeActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	private RadioButton mCheckButton, mSafeButton;
	private LinearLayout mCheckIDCardLayout;
	private LinearLayout mCheckIDCardResultLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = CheckSafeActivity.this;
		setContentView(R.layout.check_safe);
		((Button) findViewById(R.id.titlebar_id_more)).setText("客服");
		((Button) findViewById(R.id.titlebar_id_back)).setOnClickListener(this);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("验证保险");

		mCheckButton = (RadioButton) findViewById(R.id.checkButton);
		mSafeButton = (RadioButton) findViewById(R.id.safeButton);
		mCheckIDCardLayout = (LinearLayout) findViewById(R.id.check_IDCard);
		mCheckIDCardResultLayout = (LinearLayout) findViewById(R.id.check_IDCard_result);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	// 验证
	public void onCheck(View view) {
		Toast.makeText(mContext, "验证", Toast.LENGTH_SHORT).show();
	}

	// 保险
	public void onSafe(View view) {
		Toast.makeText(mContext, "保险", Toast.LENGTH_SHORT).show();
	}

	// 验证身份证
	public void onChechIDCard(View view) {
		mCheckIDCardLayout.setVisibility(View.GONE);
		mCheckIDCardResultLayout.setVisibility(View.VISIBLE);
	}
	// 验证身份证结果
	public void onChechIDCardResult(View view) {
		mCheckIDCardLayout.setVisibility(View.VISIBLE);
		mCheckIDCardResultLayout.setVisibility(View.GONE);
	}

	/**
	 * 默认选中验证项
	 */
	@Override
	protected void onResume() {
		mCheckButton.setChecked(true);
		super.onResume();
	}
}
