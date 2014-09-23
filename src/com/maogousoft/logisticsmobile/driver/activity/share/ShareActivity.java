package com.maogousoft.logisticsmobile.driver.activity.share;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseFragmentActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.adapter.MultiChoiceBaseAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * 分享给好友
 * 
 * @author lenovo
 */
public class ShareActivity extends BaseFragmentActivity implements
		LoaderCallbacks<Cursor>, OnItemClickListener {

	private final String[] CONTACTS_SUMMARY_PROJECTION = { Phone._ID,
			Phone.DISPLAY_NAME, Phone.NUMBER };

	private EditText mSearch;

	private EditText mContent;

	private ListView mListView;

	private Button mSend, mBack;

	private MultiChoiceBaseAdapter mAdapter;

	private String mCurFilter;

	private TextView tvShareHint;

	private boolean isHasContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initViews(savedInstanceState);
		initData();
	}

	private void initViews(Bundle savedInstanceState) {
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mBack.setOnClickListener(this);
		mListView = (ListView) findViewById(android.R.id.list);
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.share_get_gift);
		findViewById(R.id.titlebar_id_more).setVisibility(View.INVISIBLE);

		tvShareHint = (TextView) findViewById(R.id.tv_sharehint);
		mSearch = (EditText) findViewById(android.R.id.edit);
		mContent = (EditText) findViewById(R.id.share_id_content);
		mSend = (Button) findViewById(R.id.info_id_login_loginbtn);

		mAdapter = new MultiChoiceBaseAdapter(savedInstanceState, this,
				R.layout.listview_contact, null, new String[] {
						Phone.DISPLAY_NAME, Phone.NUMBER }, new int[] {
						android.R.id.text1, android.R.id.text2 }, 0);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		mSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				mCurFilter = !TextUtils.isEmpty(s) ? s.toString() : null;
				getSupportLoaderManager().restartLoader(0, null, ShareActivity.this);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mSend.setOnClickListener(this);
		if (getIntent().hasExtra("share")) {
			isHasContent = true;
			findViewById(R.id.titlebar_id_back).setVisibility(View.VISIBLE);
			findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		}
	}

	private void initData() {
		getShareHint();
		// 获取联系人列表
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri baseUri;
		if (mCurFilter != null) {
			baseUri = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI,
					Uri.encode(mCurFilter));
		} else {
			baseUri = Phone.CONTENT_URI;
		}
		String select = "((" + Phone.DISPLAY_NAME + " NOTNULL) AND ("
				+ Phone.DISPLAY_NAME + " != '' ))";
		return new CursorLoader(this, baseUri, CONTACTS_SUMMARY_PROJECTION,
				select, null, "sort_key COLLATE LOCALIZED asc");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAdapter.save(outState);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		boolean flag = mAdapter.isSelected(position);
		CheckBox checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
		checkBox.setChecked(!flag);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
        if (v == mBack) {
            finish();
        } else if (v == mSend) {
			if (TextUtils.isEmpty(mContent.getText().toString())) {
				showMsg("请填写分享内容");
				return;
			}
			// 如果选中联系人为空
			if (mAdapter.getSelectionCount() <= 0) {
				String phone = mSearch.getText().toString();
				if (!TextUtils.isEmpty(phone) && (phone.length() == 11)) {
					String content = mContent.getText().toString();
					showProgress("正在发送分享短信");
					try {
						SmsManager smsManager = SmsManager.getDefault();
						PendingIntent sentIntent = PendingIntent.getBroadcast(ShareActivity.this, 0, new Intent(), 0);
						if (content.length() > 70) {
							List<String> list = smsManager.divideMessage(content);
							for (String msg : list) {
								smsManager.sendTextMessage(phone, null, msg,sentIntent, null);
							}
						} else {
							smsManager.sendTextMessage(phone, null, content,sentIntent, null);
						}
						dismissProgress();
						showMsg("分享成功!");
                        //请求接口,增加分享次数
						addSearchSourceCount();
					} catch (Exception e) {
						dismissProgress();
						showMsg("分享失败");
					}
				} else {
					showMsg("请选择联系人");
				}

				return;
			}
			Set<Long> set = mAdapter.getSelection();
			String content = mContent.getText().toString();
			showProgress("正在发送分享短信");
			try {
				SmsManager smsManager = SmsManager.getDefault();
				PendingIntent sentIntent = PendingIntent.getBroadcast(ShareActivity.this, 0, new Intent(), 0);
				for (long s : set) {
					String phone = ((Cursor) mAdapter.getItem((int) s)).getString(2);
					if (content.length() > 70) {
						List<String> list = smsManager.divideMessage(content);
						for (String msg : list) {
							smsManager.sendTextMessage(phone, null, msg,
									sentIntent, null);
						}
					} else {
						smsManager.sendTextMessage(phone, null, content,
								sentIntent, null);
					}
				}
				dismissProgress();
				showMsg("分享成功!");

				addSearchSourceCount();

			} catch (Exception e) {
				dismissProgress();
				showMsg("分享失败");
			}
			// Uri smsToUri = Uri.parse("smsto://" + sb.deleteCharAt(sb.length()
			// - 1).toString());
			// Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
			// smsToUri);
			// mIntent.putExtra("sms_body", mContent.getText().toString());
			// startActivity(mIntent);
		}
	}

	private void getShareHint() {
		// showDefaultProgress();
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.GET_SHARE_INFO);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", "1").toString());

			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							// dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								LogUtil.i("ShareActivity", "result:" + result);
								try {
									JSONObject jsonResult = new JSONObject(result.toString());
									tvShareHint.setText(jsonResult.get("content").toString());
									tvShareHint.setVisibility(View.VISIBLE);
								} catch (JSONException e) {
									e.printStackTrace();
								}

								break;
							case ResultCode.RESULT_ERROR:
								// showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								// showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 添加搜索货源的可用次数
	private void addSearchSourceCount() {
		// showDefaultProgress();
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.ADD_SEARCH_COUNT);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", "1").toString());

			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							// dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								LogUtil.i("ShareActivity","addSearchSourceCount result:" + result);
								if (result != null) {
									showMsg("成功增加搜索货源可用次数");
								}
								break;
							case ResultCode.RESULT_ERROR:
								if (result != null)
									showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								if (result != null)
									showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {

		if (isHasContent) {
			super.onBackPressed();
		} else {
			sendBroadcast(new Intent(MainActivity.ACTION_SWITCH_MAINACTIVITY));
		}
	}
}
