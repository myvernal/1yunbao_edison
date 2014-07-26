package com.ybxiang.driver.activity;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

public class HelpFindGoodsSourceActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = HelpFindGoodsSourceActivity.this;
		setContentView(R.layout.activity_home_help_find_goods_source);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("帮我找货");
		// 隐藏右边"礼品"标题
		((Button) findViewById(R.id.titlebar_id_more)).setVisibility(View.GONE);
		((Button) findViewById(R.id.titlebar_id_back)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	public void onHelpFindGoods(View view) {
		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.show();
		dialog.setTitle("提示");
		dialog.setMessage("使用此功能将会扣除您X个物流币");
		dialog.setLeftButton("确定", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// 拨通帮我找货热线
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ "4008765156"));
				startActivity(intent);
				// TODO 扣除物流币的操作
				// doCostLogisticsCoins();
			}
		});
		dialog.setRightButton("取消", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}
}
