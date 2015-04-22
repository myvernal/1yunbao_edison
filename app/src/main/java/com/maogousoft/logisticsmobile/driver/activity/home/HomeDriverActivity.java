package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseHomeActivity;
import com.maogousoft.logisticsmobile.driver.activity.ShakeActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.ybxiang.driver.activity.*;

/**
 * 司机首页
 * 
 * @author ybxiang
 */
public class HomeDriverActivity extends BaseHomeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home_new_driver1);
        findViewById(R.id.titlebar_id_back).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.titlebar_id_content)).setText("易运宝");
		super.onCreate(savedInstanceState);
        setIsShowAnonymousActivity(false);
	}

	// 查找货源
	public void onSearchSource(View view) {
		Intent intent = new Intent(context, NewSourceActivity.class);
		intent.putExtra(Constants.SEARCH_SOURCE, true);
		startActivity(intent);
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

	// 好友货源
	public void onFriendsSource(View view) {
		Intent intentNewSource = new Intent(context, NewSourceActivity.class);
		intentNewSource.putExtra("getFriendOrderList", true);
		startActivity(intentNewSource);
	}

    //摇一摇
    public void onShakeSearch(View view) {
        startActivity(new Intent(context, ShakeActivity.class));
    }

    // 关注货源
    public void onFocusSource(View view) {
        Intent intentNewSource = new Intent(context, NewSourceActivity.class);
        intentNewSource.putExtra("QUERY_MAIN_LINE_ORDER", true);
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

	@Override
	public void onBackPressed() {
		exitAppHint();
	}

}