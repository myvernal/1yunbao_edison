package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.*;

/**
 * 专线首页 special
 * 
 * @author ybxiang
 */
public class HomeSpecialActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.i("wst", "HomeActivity Special -onCreate");
		setContentView(R.layout.activity_home_new_special);
	}

	// 找货
	public void onSearchSource(View view) {
		startActivity(new Intent(mContext, SearchSourceActivity.class));
	}

	// 找车
	public void onFindCar(View view) {
		startActivity(new Intent(mContext, SearchCarSourceActivity.class));
	}

	// 我的好友
	public void onMyFriends(View view) {
		startActivity(new Intent(mContext, MyFriendsActivity.class));
	}

	// 发货
	public void onPublishGoods(View view) {
		startActivity(new Intent(mContext, PublishGoodsSourceActivity.class));
	}

	// 发车
	public void onPublishCar(View view) {
		startActivity(new Intent(mContext, PublishCarSourceActivity.class));
	}

	// 新货源
	public void onNewSource(View view) {
		Intent intentNewSource = new Intent(mContext, NewSourceActivity.class);
		intentNewSource.putExtra("isFromHomeActivity", true);
		startActivity(intentNewSource);
	}

	// 验证保险
	public void onCheckSafe(View view) {
		startActivity(new Intent(mContext, CheckSafeActivity.class));
	}

	// 互动
	public void onInteraction(View view) {
		startActivity(new Intent(mContext, OthersActivity.class));
	}

	// 会员特权
	public void onVIP(View view) {
		startActivity(new Intent(mContext, SearchShopActivity.class));
	}

	// 我的推广
	public void onSpread(View view) {
		startActivity(new Intent(mContext, SpreadActivity.class));
	}

	@Override
	public void onBackPressed() {
		exitAppHint();
	}

}
