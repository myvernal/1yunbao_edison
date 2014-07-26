package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.IndexGridActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 首页
 * 
 * @author lenovo
 */
public class HomeActivity extends BaseActivity {

	private Button mSearchSource;
	private Button mOnlineSource;
	private Button mUnMatchedSource;
	private Button mNewSource;
	private Button mMyAbc;
	private Button mVip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.i("wst", "HomeActivity-onCreate");

		setContentView(R.layout.activity_home_new_driver);
		//initViews();
	}

	// 初始化视图
	private void initViews() {
		mSearchSource = (Button) findViewById(R.id.home_id_search_source);
		mOnlineSource = (Button) findViewById(R.id.home_id_online_source);
		mUnMatchedSource = (Button) findViewById(R.id.home_id_unmatched_source);
		mNewSource = (Button) findViewById(R.id.home_id_new_source);
		mMyAbc = (Button) findViewById(R.id.home_id_myabc);
		mVip = (Button) findViewById(R.id.home_id_vip);

		mOnlineSource.setOnClickListener(this);
		mUnMatchedSource.setOnClickListener(this);
		mNewSource.setOnClickListener(this);
		mMyAbc.setOnClickListener(this);
		mVip.setOnClickListener(this);
		mSearchSource.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {
		case R.id.home_id_search_source:
			startActivity(new Intent(context, SearchSourceActivity.class));
			break;
		case R.id.home_id_online_source:
			startActivity(new Intent(context, OnLineSourceActivity.class));
			break;
		case R.id.home_id_unmatched_source:
			startActivity(new Intent(context, UnMatchedSourceActivity.class));
			break;
		case R.id.home_id_new_source:
			Intent intentNewSource = new Intent(context,
					NewSourceActivity.class);
			intentNewSource.putExtra("isFromHomeActivity", true);
			startActivity(intentNewSource);
			break;
		case R.id.home_id_myabc:
			startActivity(new Intent(context, MyabcActivity.class));
			break;
		case R.id.home_id_vip:
			// showMsg(R.string.tips_home_vipmessage);
			startActivity(new Intent(context, ShopListActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		exitAppHint();
	}

}
