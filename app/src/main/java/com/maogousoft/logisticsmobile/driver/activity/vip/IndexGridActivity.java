package com.maogousoft.logisticsmobile.driver.activity.vip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * 会员特权，首页
 * 
 * @author lenovo
 */
public class IndexGridActivity extends BaseActivity implements OnItemClickListener {

	private GridView mGridView;
	private Button titlebar_id_more;

	private SimpleAdapter mAdapter;

	private RadioButton radioNear, radioCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_index_grid);
		initViews();
		initListener();

	}

	// 初始化视图
	private void initViews() {

		radioNear = (RadioButton) findViewById(R.id.radio_near);
		radioCategory = (RadioButton) findViewById(R.id.radio_category);


		((TextView) findViewById(R.id.titlebar_id_content)).setText("会员特权");

		titlebar_id_more = (Button) findViewById(R.id.titlebar_id_more);
		titlebar_id_more.setText("添加商户");

		mGridView = (GridView) findViewById(R.id.other_id_gridview);

		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		String[] arrays = getResources().getStringArray(R.array.vip);
		int[] icons = { R.drawable.selector_others_stay, R.drawable.selector_others_oil, R.drawable.selector_vip_cy,
				R.drawable.selector_vip_xx, R.drawable.selector_vip_fix, R.drawable.selector_vip_other };
		final int lenght = arrays.length;
		for (int i = 0; i < lenght; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("icon", icons[i]);
			map.put("title", arrays[i]);
			mList.add(map);
		}
		mAdapter = new SimpleAdapter(mContext, mList, R.layout.griditem_others, new String[] { "icon", "title" },
				new int[] { R.id.other_id_item_icon, R.id.other_id_item_title });
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}

	public void initListener() {
		titlebar_id_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final GrabDialog dialog = new GrabDialog(mContext);
				dialog.show();
				final EditText mInput = (EditText) dialog.findViewById(android.R.id.text1);
				dialog.setTitle("提示");
				dialog.setMessage("请先输入权限密码");
				dialog.setLeftButton("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						dialog.dismiss();

						if (TextUtils.isEmpty(mInput.getText().toString())) {
							Toast.makeText(IndexGridActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
							return;
						}

						// mInput
						// 登录
						final String username = "11111111111";
						final String password = mInput.getText().toString();

						final JSONObject jsonObject = new JSONObject();
						try {
                            showSpecialProgress();
							jsonObject.put(Constants.ACTION, Constants.DRIVER_LOGIN);
							jsonObject.put(Constants.TOKEN, null);
							jsonObject.put(
									Constants.JSON,
									new JSONObject().put("phone", username).put("password", MD5.encode(password))
											.put("device_type", Constants.DEVICE_TYPE).toString());
							ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, UserInfo.class,
									new AjaxCallBack() {

										@Override
										public void receive(int code, Object result) {
											dismissProgress();
											switch (code) {
												case ResultCode.RESULT_OK:
													startActivity(new Intent(mContext, AddActivity.class));
													break;

												default:
													Toast.makeText(IndexGridActivity.this, "密码错误", Toast.LENGTH_SHORT)
															.show();
													break;

											}
										}
									});
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
				dialog.setRightButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});

		radioNear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					finish();
					startActivity(new Intent(mContext, ShopListActivity.class));
				}

			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		switch (position) {
			case 0:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 0));
				break;
			case 1:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 1));
				break;
			case 2:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 2));
				break;
			case 3:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 3));
				break;
			case 4:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 4));
				break;
			case 5:
				startActivity(new Intent(mContext, ShopListActivity.class).putExtra("type", 5));
				break;
		}
	}

	@Override
	protected void onResume() {
		radioCategory.setChecked(true);
		super.onResume();
	}
}
