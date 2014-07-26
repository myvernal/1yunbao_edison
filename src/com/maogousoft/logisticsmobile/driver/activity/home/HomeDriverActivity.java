package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.HelpFindGoodsSourceActivity;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.activity.PublishCarSourceActivity;
import com.ybxiang.driver.activity.SpreadActivity;

/**
 * 司机首页
 * 
 * @author ybxiang
 */
public class HomeDriverActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.i("wst", "HomeActivity driver -onCreate");
		setContentView(R.layout.activity_home_new_driver1);
	}

	// 查找货源
	public void onSearchSource(View view) {
		startActivity(new Intent(context, SearchSourceActivity.class));
	}

	// 帮我找货
	public void onHelpFindGoods(View view) {
		startActivity(new Intent(context, HelpFindGoodsSourceActivity.class));
	}

	// 我的好友
	public void onMyFriends(View view) {
		startActivity(new Intent(context, MyFriendsActivity.class));
	}

	// 新货源
	public void onNewSource(View view) {
		Intent intentNewSource = new Intent(context, NewSourceActivity.class);
		intentNewSource.putExtra("isFromHomeActivity", true);
		startActivity(intentNewSource);
	}

	// 发布车源
	public void onPublishCar(View view) {
		startActivity(new Intent(context, PublishCarSourceActivity.class));
	}

	// 我的推广
	public void onSpread(View view) {
		startActivity(new Intent(context, SpreadActivity.class));
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
