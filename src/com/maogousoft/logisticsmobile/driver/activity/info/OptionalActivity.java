package com.maogousoft.logisticsmobile.driver.activity.info;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CarTypeListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 完善司机资料
 * 
 * @author lenovo
 */
public class OptionalActivity extends BaseActivity {

	private Button mBack, mLogin, mRegister;
	private EditText mShuiChePhone, mName, mLength, mWeight, mNumber,
			mChezhuPhone;
	private Spinner mCarType;
	private CarTypeListAdapter mCarTypeAdapter;
	private boolean isFormRegisterActivity;
	private boolean isFormPushSourceDetail;// 是否从推送进入，并且没有完善资料，然后进入的本页面
	private AbcInfo abcInfo;
    // 保存车辆照片的list
    private ArrayList<Uri> mCarPhotos = new ArrayList<Uri>();
    // 保存车辆照片url的list
    private ArrayList<String> mCarPhotosUrl = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_optional);
		initViews();
		initUtils();
		initDatas();
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_register_complete_title);
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mLogin = (Button) findViewById(R.id.titlebar_id_more);
		mRegister = (Button) findViewById(R.id.info_id_register_submit);

		mShuiChePhone = (EditText) findViewById(R.id.info_id_register_shuiche_phone);
		mChezhuPhone = (EditText) findViewById(R.id.info_id_register_chezhu_phone);
		mName = (EditText) findViewById(R.id.info_id_register_name);
		mLength = (EditText) findViewById(R.id.info_id_register_length);
		mWeight = (EditText) findViewById(R.id.info_id_register_weight);
		mNumber = (EditText) findViewById(R.id.info_id_register_number);

		mCarType = (Spinner) findViewById(R.id.info_id_register_type);
		// mSkan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// mSkan.getPaint().setUnderlineText(true);

		mBack.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mRegister.setOnClickListener(this);

		mShuiChePhone.setOnFocusChangeListener(mOnFocusChangeListener);
		mChezhuPhone.setOnFocusChangeListener(mOnFocusChangeListener);
		mLength.setOnFocusChangeListener(mOnFocusChangeListener);
		mWeight.setOnFocusChangeListener(mOnFocusChangeListener);
		mNumber.setOnFocusChangeListener(mOnFocusChangeListener);
		mName.setOnFocusChangeListener(mOnFocusChangeListener);

	}

	/** 初始化工具类 **/
	private void initUtils() {

	}

	// 初始化数据，获取车型
	private void initDatas() {

		if (getIntent().hasExtra("isFormRegisterActivity")) {
			isFormRegisterActivity = getIntent().getBooleanExtra(
					"isFormRegisterActivity", false);
		}

		if (getIntent().hasExtra("isFormPushSourceDetail")) {
			isFormPushSourceDetail = getIntent().getBooleanExtra(
					"isFormPushSourceDetail", false);
		}

		mCarTypeAdapter = new CarTypeListAdapter(context);
		mCarType.setAdapter(mCarTypeAdapter);
		mCarType.setPrompt("高栏");
		application.getDictList(new AjaxCallBack() {

			@Override
			public void receive(int code, Object result) {
				switch (code) {
				case ResultCode.RESULT_OK:
					@SuppressWarnings("unchecked")
					List<DictInfo> mList = (List<DictInfo>) result;
					List<DictInfo> mList2 = new ArrayList<DictInfo>();
					for (DictInfo d : mList) {
						if (d.getDict_type() != null
								&& d.getDict_type().equals(Constants.CAR_TYPE)
								&& (!d.getName().equals("不限")))
							mList2.add(d);
					}
					mCarTypeAdapter.setList(mList2);

					// 第一次完善资料，默认显示高栏
					for (int y = 0; y < mList2.size(); y++) {
						DictInfo d = mList2.get(y);
						if (d.getName().equals("高栏")) {
							mCarType.setSelection(y);
						}
					}

					// 司机本身已经设置过 车辆类型，需要显示到界面
					if (abcInfo != null) {
						if (mCarTypeAdapter.getList() != null) {
							for (int i = 0; i < mCarTypeAdapter.getList()
									.size(); i++) {
								if (mCarTypeAdapter.getList().get(i).getId() == abcInfo
										.getCar_type()) {
									mCarType.setSelection(i);
									break;
								}
							}
						}
					}

					break;
				case ResultCode.RESULT_ERROR:
					showMsg(result.toString());
					break;
				case ResultCode.RESULT_FAILED:
					showMsg(result.toString());
					break;

				default:
					break;
				}
			}
		});

		// 随车手机默认显示注册手机号码
		mShuiChePhone.setText(application.getUserName());

		if (getIntent().hasExtra("info")) {
			abcInfo = (AbcInfo) getIntent().getSerializableExtra("info");
			if (abcInfo != null) {

				mShuiChePhone.setText(abcInfo.getCar_phone());

				// 没有车主phone，需要后台给
				mChezhuPhone.setText(abcInfo.getCar_phone());
				mName.setText(abcInfo.getName());

				mLength.setText(abcInfo.getCar_length() + "");
				mWeight.setText(abcInfo.getCar_weight() + "");

				if (mCarTypeAdapter.getList() != null) {
					for (int i = 0; i < mCarTypeAdapter.getList().size(); i++) {
						if (mCarTypeAdapter.getList().get(i).getId() == abcInfo
								.getCar_type()) {
							mCarType.setSelection(i);
							break;
						}
					}
				}

				mNumber.setText(abcInfo.getPlate_number());

			}
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {

		case R.id.info_id_register_submit:
            uploadImageAndSubmit();
			break;

		default:
			break;
		}
	}

	// 输入框失去焦点事件监听
	private final OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == mShuiChePhone && !hasFocus) {

			} else if (v == mName && !hasFocus) {

			} else if (v == mLength && !hasFocus) {

			} else if (v == mWeight && !hasFocus) {

			} else if (v == mNumber && !hasFocus) {

			} else if (v == mChezhuPhone && !hasFocus) {

			}

		}
	};

	// 提交注册
	private void submit() {
		if (CheckUtils.checkIsEmpty(mShuiChePhone)) {
			if (!CheckUtils.checkPhone(mShuiChePhone.getText().toString())) {
				showMsg("随车手机号码格式不正确");
				mShuiChePhone.requestFocus();
				mShuiChePhone.selectAll();
				return;
			}
		}

		if (!CheckUtils.checkIsEmpty(mName)) {
			showMsg("司机姓名不能为空");
			mName.requestFocus();
			return;
		}

		if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
			if (!CheckUtils.checkPhone(mChezhuPhone.getText().toString())) {
				showMsg("车主手机号码格式不正确");
				mChezhuPhone.requestFocus();
				mChezhuPhone.selectAll();
				return;
			}
		}

		if (!CheckUtils.checkIsEmpty(mLength)) {
			showMsg("车长不能为空");
			mLength.requestFocus();
			return;
		}
		if (!CheckUtils.checkIsEmpty(mWeight)) {
			showMsg("载重量不能为空");
			mWeight.requestFocus();
			return;
		}
		if (!CheckUtils.checkIsEmpty(mNumber)) {
			showMsg("车牌号不能为空");
			mNumber.requestFocus();
			return;
		}

		final JSONObject jsonObject = new JSONObject();
		final JSONObject params = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_REG_OPTIONAL);
			jsonObject.put(Constants.TOKEN, null);

			if (!CheckUtils.checkIsEmpty(mShuiChePhone)) {
				params.put("car_phone", application.getUserName());
			} else {
				params.put("car_phone", mShuiChePhone.getText().toString());
			}

			if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
				params.put("owner_phone", mChezhuPhone.getText().toString());
			}

			// params.put("vcode", mVerifyCode.getText().toString());
			// params.put("password",
			// MD5.encode(mPassword.getText().toString()));
			// mChezhuPhone
			params.put("driver_name", mName.getText().toString());
			params.put("car_length", mLength.getText().toString());
			params.put(
					"car_type",
					mCarTypeAdapter.getList()
							.get(mCarType.getSelectedItemPosition()).getId());
			params.put("car_weight", mWeight.getText().toString());
			params.put("plate_number", mNumber.getText().toString());

			params.put("device_type", Constants.DEVICE_TYPE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, params);
			showDefaultProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					UserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:

								if (isFormRegisterActivity) {
									startActivity(new Intent(context,
											MainActivity.class));
									// 第一次进入，获取 所有 新货源
									getNewSourceData(1);
								}

								onBackPressed();

								showMsg("完善资料成功.");

								application.writeIsRegOptional(true);

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

	// 获取所有新货源推送
	private void getNewSourceData(int page) {
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
					.toString());

			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NewSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									List<NewSourceInfo> mList = (List<NewSourceInfo>) result;

									if (mList != null) {
										NotificationManager mNotificationManager = (NotificationManager) context
												.getSystemService(Context.NOTIFICATION_SERVICE);

										for (int i = 0; i < mList.size(); i++) {
											NewSourceInfo newSourceInfo = mList
													.get(i);

											StringBuffer sb = new StringBuffer();
											sb.append("有新的货源,");
											sb.append(newSourceInfo
													.getCargo_desc());
											sb.append(",");
											sb.append(newSourceInfo
													.getCargo_number());

											switch (newSourceInfo
													.getCargo_unit()) {
											case 1:
												sb.append("车");
												break;
											case 2:
												sb.append("吨");
												break;
											case 3:
												sb.append("方");
												break;
											}

											mNotificationManager.notify(
													i,
													getOrderNotification(
															context,
															sb.toString(),
															newSourceInfo, i));
										}
									}
								}
								break;
							}

						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 跳入新货源详情
	private Notification getOrderNotification(Context context, String title,
			NewSourceInfo sourceInfo, int num) {
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());
		final Intent intent = new Intent(context, SourceDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(SourceDetailActivity.ORDER_INFO, sourceInfo);
		intent.putExtra("type", "MessageBroadCastReceiver");
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, num,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, "点击查看订单详情",
				pendingIntent);

		if (application.checkIsRingNewSource()) {
			notification.sound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + R.raw.mm);
		} else {
			notification.sound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + R.raw.silence);
		}

		// notification.sound = Uri.parse("android.resource://"
		// + context.getPackageName() + "/" + R.raw.mm);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		return notification;
	}

	@Override
	public void onBackPressed() {
		if (isFormPushSourceDetail) {
			application.finishAllActivity();
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);

		} else {
			super.onBackPressed();
		}
	}

    public void uploadImageAndSubmit() {
        if (!mCarPhotos.isEmpty()) {
            try {
                for (int i = 0; i < mCarPhotos.size(); i++) {
//                    System.out.println(mCarPhotos.get(i - 1).getPath());
//                    params.put("photo" + i, HttpUtils.getBitmapBase64(context, mCarPhotos.get(i - 1).getPath()));
                    new UploadImageFileTask().execute(mCarPhotos.get(i).getPath());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            submit();
        }
    }

    private class UploadImageFileTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            System.out.println("开启线程上传图片");
            showDefaultProgress();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String filePath = params[0];
            String serverResponse = null;
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost post = new HttpPost(Constants.UPLOAD_FILE_URL);
            File file = new File(filePath);
            if (file != null && file.exists()) {
                try {
                    ContentBody body = new FileBody(file, "image/jpeg");
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("token", new StringBody(application.getToken()));
                    entity.addPart("file_type", new StringBody("1"));
                    entity.addPart("file", body);
                    post.setEntity(entity);
                    HttpResponse response = httpClient.execute(post);
                    serverResponse = EntityUtils.toString(response.getEntity());
                    LogUtil.e(TAG, "上传文件结果:" + serverResponse);
                    return serverResponse;
                } catch (Exception e) {
                    LogUtil.e(TAG, "上传文件出错");
                    e.printStackTrace();
                }
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final int status = jsonObject.optInt("status");
                    // 上传文件成功
                    if (status == 0) {
                        String item = jsonObject.optString("item");
                        String url = new JSONObject(item).getString("url");
                        System.out.println("上传图片返回url地址：" + url);
                        mCarPhotosUrl.add(url);
                        if (mCarPhotosUrl.size() == mCarPhotos.size()) {
                            // 上传所有图片完毕
                            submit();
                        }
                    } else {
                        System.out.println("上传图片失败");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("上传图片结果为空");
            }
        }
    }
}
