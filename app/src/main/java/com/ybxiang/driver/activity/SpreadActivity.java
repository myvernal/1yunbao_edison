package com.ybxiang.driver.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

public class SpreadActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	private Context mContext;
	private Button mContactKeFu;
	private TextView mPubishGoodsText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = SpreadActivity.this;
		setContentView(R.layout.spread_goods_source);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("我要推广");
		mPubishGoodsText = (TextView) findViewById(R.id.publish_goods_succeed);
		mPubishGoodsText.setVisibility(View.VISIBLE);
		mContactKeFu = (Button) findViewById(R.id.titlebar_id_more);
		mContactKeFu.setText("客服");
		mContactKeFu.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		((BaseActivity) mContext).setIsRightKeyIntoShare(false);
		super.onClick(v);
		switch (v.getId()) {
		// 客服
		case R.id.titlebar_id_more:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle(R.string.contact_kehu).setItems(R.array.contact_kehu_items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					// 打电话
					case 0:
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"4008765156"));
						startActivity(intent);
						break;
				    // 发QQ
					case 1:
						Toast.makeText(mContext, R.string.contact_kehu_qq, Toast.LENGTH_LONG).show();
						// sendImageToQQ("hello");
						break;

					default:
						break;
					}
					
				}
			});
			builder.create().show();
			break;

		default:
			break;
		}
	}
	// 资费说明
	public void onDesc(View view){
		
	}
	// 我的推广
	public void onMySpread(View view){
		
	}
	
	// 我要推广
	public void onSpread(View view) {
		final MyAlertDialog dialog = new MyAlertDialog(context);
		dialog.show();
		dialog.setTitle("提示");
		dialog.setMessage("使用此功能会扣除X个物流币");
		dialog.setLeftButton("确定", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Toast.makeText(mContext, "推广成功", Toast.LENGTH_SHORT).show();
				finish();
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
