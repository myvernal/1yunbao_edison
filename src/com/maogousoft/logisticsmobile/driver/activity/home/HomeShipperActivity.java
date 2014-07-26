package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.CheckSafeActivity;
import com.ybxiang.driver.activity.FindCarActivity;
import com.ybxiang.driver.activity.MyCarsActivity;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.activity.PublishGoodsSourceActivity;
import com.ybxiang.driver.activity.SpreadActivity;

/**
 * 三方首页
 * 
 * @author ybxiang
 */
public class HomeShipperActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogUtil.i("wst", "HomeActivity Shipper -onCreate");

		setContentView(R.layout.activity_home_new_shipper);
		// initViews();
	}

	// 我的车队
	public void onMyCars(View view) {
		startActivity(new Intent(context, MyCarsActivity.class));
	}

	// 我要推广
	public void onSpread(View view) {
		startActivity(new Intent(context, SpreadActivity.class));
	}

	// 我的好友
	public void onMyFriends(View view) {
		startActivity(new Intent(context, MyFriendsActivity.class));
	}

	// 发布货源
	public void onPublishGoods(View view) {
		startActivity(new Intent(context, PublishGoodsSourceActivity.class));
	}

	// 找车
	public void onFindCar(View view) {
		startActivity(new Intent(context, FindCarActivity.class));
	}

	// 验证保险
	public void onCheckSafe(View view) {
		startActivity(new Intent(context, CheckSafeActivity.class));
	}

	// 会员特权
	public void onVIP(View view) {
		startActivity(new Intent(context, ShopListActivity.class));
	}

	// 互动
	public void onInteraction(View view) {
		startActivity(new Intent(context, OthersActivity.class));
	}

	@Override
	public void onBackPressed() {
		exitAppHint();
	}
}
