package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseHomeActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.*;

/**
 * 三方首页
 * 
 * @author ybxiang
 */
public class HomeShipperActivity extends BaseHomeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.i("wst", "HomeActivity Shipper -onCreate");
		setContentView(R.layout.activity_home_new_shipper);
        super.onCreate(savedInstanceState);
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

	// 查找车源
	public void onFindCar(View view) {
		startActivity(new Intent(context, FindCarActivity.class));
	}

	// 验证保险
	public void onCheckSafe(View view) {
		startActivity(new Intent(context, SafeActivity.class));
	}

    // 验证证件
    public void onCheckCard(View view) {
        startActivity(new Intent(context, CheckCardActivity.class));
    }

	// 会员特权
	public void onVIP(View view) {
		startActivity(new Intent(context, ShopListActivity.class));
	}

	// 物流点评
	public void onInteraction(View view) {
		startActivity(new Intent(context, ShipDPActivity.class));
	}

    // 查找货源
    public void onSearchSource(View view) {
        startActivity(new Intent(context, SearchSourceActivity.class));
    }

	@Override
	public void onBackPressed() {
		exitAppHint();
	}
}
