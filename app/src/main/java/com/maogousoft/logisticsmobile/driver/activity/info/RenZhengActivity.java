package com.maogousoft.logisticsmobile.driver.activity.info;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.FileCache;
import com.maogousoft.logisticsmobile.driver.utils.HttpUtils;
import com.maogousoft.logisticsmobile.driver.utils.PickPhoto;

/**
 * 诚信认证
 * 
 * @author lenovo
 */
public class RenZhengActivity extends BaseActivity {

	// 选择照片的requestCode
	private final int CARD_CAMERA_CODE = 1000, CARD_GALLERY_CODE = 1001,
			LICENSE_CAMERA_CODE = 1002, LICENSE_GALLERY_CODE = 1003,
			REGISTRATION_CAMERA_CODE = 1004, REGISTRATION_GALLERY_CODE = 1005,
			CAR_CAMERA_CODE = 1006, CAR_GALLERY_CODE = 1007;

	// 查询图片地址
	private final String[] proj = { MediaStore.Images.Media.DATA };

	private final String CARD = "card", LICENSE = "license",
			REGISTRATION = "registration", CAR = "car";
	// 返回，跳过，提交
	private Button mDump, mSubmit;

	// 身份证，驾驶证，行驶证
	private Button mCardFlag, mLicenseFlag, mRegistrationFlag, mCarFlag;

	// 调用相机，相册的按钮
	private ImageButton mCardCamera, mCardGallery, mLicenseCamera,
			mLicenseGallery, mRegistrationCamera, mRegistrationGallery,
			mCarCamera, mCarGallery;

	private View mCard, mLicense, mRegistration, mCar;

	// 司机姓名，手机号码，身份证姓名，身份证号码，驾驶证姓名，驾驶证号码，行驶证姓名，行驶证号码
	private EditText mCardName, mCardNumber, mLicenseName, mLicenseNumber,
			mRegistrationName, mRegistrationNumber;

	private FileCache mFileCache;

	// 图片输出路径
	private Uri mCardOut, mLicenseOut, mRegistrationOut, mCarOut;

	// 保存车辆照片的list
	private ArrayList<Uri> mCarPhotos = new ArrayList<Uri>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_renzheng);
		readSaveInstanceState(savedInstanceState);
		initViews();
		initUtils();
		initData();
	}

	// 初始化视图
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText("诚信认证");

		mCardName = (EditText) findViewById(R.id.info_id_optional_card_name);
		mCardNumber = (EditText) findViewById(R.id.info_id_optional_card_number);
		mLicenseName = (EditText) findViewById(R.id.info_id_optional_license_name);
		mLicenseNumber = (EditText) findViewById(R.id.info_id_optional_license_number);
		mRegistrationName = (EditText) findViewById(R.id.info_id_optional_registration_name);
		mRegistrationNumber = (EditText) findViewById(R.id.info_id_optional_registration_number);

		mCard = findViewById(R.id.info_id_optional_card);
		mLicense = findViewById(R.id.info_id_optional_license);
		mRegistration = findViewById(R.id.info_id_optional_registration);
		mCar = findViewById(R.id.info_id_optional_car);

		mCardFlag = (Button) mCard.findViewById(R.id.common_id_select_flag);
		mLicenseFlag = (Button) mLicense
				.findViewById(R.id.common_id_select_flag);
		mRegistrationFlag = (Button) mRegistration
				.findViewById(R.id.common_id_select_flag);
		mCarFlag = (Button) mCar.findViewById(R.id.common_id_select_flag);

		mCardFlag.setText(Html.fromHtml("<u>查看图片</u>"));
		mLicenseFlag.setText(Html.fromHtml("<u>查看图片</u>"));
		mRegistrationFlag.setText(Html.fromHtml("<u>查看图片</u>"));
		mCarFlag.setText(Html.fromHtml("<u>查看图片</u>"));

		// mCardFlag.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		// mLicenseFlag.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		// mRegistrationFlag.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		// mCarFlag.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		mCardCamera = (ImageButton) mCard
				.findViewById(R.id.common_id_select_camera);
		mCardGallery = (ImageButton) mCard
				.findViewById(R.id.common_id_select_gallery);
		mLicenseCamera = (ImageButton) mLicense
				.findViewById(R.id.common_id_select_camera);
		mLicenseGallery = (ImageButton) mLicense
				.findViewById(R.id.common_id_select_gallery);
		mRegistrationCamera = (ImageButton) mRegistration
				.findViewById(R.id.common_id_select_camera);
		mRegistrationGallery = (ImageButton) mRegistration
				.findViewById(R.id.common_id_select_gallery);
		mCarCamera = (ImageButton) mCar
				.findViewById(R.id.common_id_select_camera);
		mCarGallery = (ImageButton) mCar
				.findViewById(R.id.common_id_select_gallery);

		((TextView) mCard.findViewById(R.id.common_id_select_title))
				.setText(R.string.string_tips_optional_card);
		((TextView) mLicense.findViewById(R.id.common_id_select_title))
				.setText(R.string.string_tips_optional_license);
		((TextView) mRegistration.findViewById(R.id.common_id_select_title))
				.setText(R.string.string_tips_optional_registration);
		((TextView) mCar.findViewById(R.id.common_id_select_title))
				.setText(R.string.string_car_img);

		mDump = (Button) findViewById(R.id.info_id_optional_dump);
		mDump.setVisibility(View.GONE);
		mSubmit = (Button) findViewById(R.id.info_id_optional_submit);

		mCardCamera.setOnClickListener(this);
		mLicenseCamera.setOnClickListener(this);
		mRegistrationCamera.setOnClickListener(this);
		mCardGallery.setOnClickListener(this);
		mLicenseGallery.setOnClickListener(this);
		mRegistrationGallery.setOnClickListener(this);
		mCarCamera.setOnClickListener(this);
		mCarGallery.setOnClickListener(this);
		mDump.setOnClickListener(this);
		mSubmit.setOnClickListener(this);

		mCardFlag.setOnClickListener(this);
		mLicenseFlag.setOnClickListener(this);
		mRegistrationFlag.setOnClickListener(this);
		mCarFlag.setOnClickListener(this);
	}

	private void initUtils() {
		mFileCache = new FileCache(mContext);
	}

	// 初始化data
	private void initData() {
		if (getIntent().hasExtra("info")) {
			DriverInfo mDriverInfo = (DriverInfo) getIntent().getSerializableExtra(
					"info");
			if (mDriverInfo != null) {
				mLicenseName.setText(CheckUtils.checkIsNull(mDriverInfo
						.getLicense_name()));
				mLicenseNumber.setText(CheckUtils.checkIsNull(mDriverInfo
						.getLicense()));
				mRegistrationName.setText(CheckUtils.checkIsNull(mDriverInfo
						.getRegistration_name()));
				mRegistrationNumber.setText(CheckUtils.checkIsNull(mDriverInfo
						.getRegistration()));
				mCardName.setText(CheckUtils.checkIsNull(mDriverInfo
						.getId_card_name()));
				mCardNumber.setText(CheckUtils.checkIsNull(mDriverInfo
						.getId_card()));

			}

		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mCardCamera) {
			mCardOut = mFileCache.makeImageUrl();
			PickPhoto.takePhoto(this, mCardOut, CARD_CAMERA_CODE);
		} else if (v == mCardGallery) {
			PickPhoto.pickPhoto(this, CARD_GALLERY_CODE);
		} else if (v == mLicenseCamera) {
			mLicenseOut = mFileCache.makeImageUrl();
			PickPhoto.takePhoto(this, mLicenseOut, LICENSE_CAMERA_CODE);
		} else if (v == mLicenseGallery) {
			PickPhoto.pickPhoto(this, LICENSE_GALLERY_CODE);
		} else if (v == mRegistrationCamera) {
			mRegistrationOut = mFileCache.makeImageUrl();
			PickPhoto.takePhoto(this, mRegistrationOut,
					REGISTRATION_CAMERA_CODE);
		} else if (v == mRegistrationGallery) {
			PickPhoto.pickPhoto(this, REGISTRATION_GALLERY_CODE);
		} else if (v == mCarCamera) {
			if (mCarPhotos.size() >= 3) {
				showMsg("最多只能选择3张车辆照片");
				return;
			}
			mCarOut = mFileCache.makeImageUrl();
			PickPhoto.takePhoto(this, mCarOut, CAR_CAMERA_CODE);
		} else if (v == mCarGallery) {
			if (mCarPhotos.size() >= 3) {
				showMsg("最多只能选择3张车辆照片");
				return;
			}
			PickPhoto.pickPhoto(this, CAR_GALLERY_CODE);
		} else if (v == mDump) {
			// 判断是从我的易运宝跳转过来的
			if (getIntent().hasExtra("info")) {
				finish();
			} else {
				startActivity(new Intent(mContext, MainActivity.class));
				finish();
			}
		} else if (v == mSubmit) {
			submit();
		} else if (v == mCardFlag) {

			// 图片输出路径
			// private Uri mCardOut, mLicenseOut, mRegistrationOut, mCarOut;
			final ArrayList<String> list = new ArrayList<String>();
			if (mCardOut != null && !TextUtils.isEmpty(mCardOut.getPath())) {
				list.add(mCardOut.getPath());
				startActivity(new Intent(mContext, ImagePagerActivity.class)
						.putStringArrayListExtra("images", list));
			}

		} else if (v == mLicenseFlag) {
			final ArrayList<String> list = new ArrayList<String>();
			if (mLicenseOut != null
					&& !TextUtils.isEmpty(mLicenseOut.getPath())) {
				list.add(mLicenseOut.getPath());
				startActivity(new Intent(mContext, ImagePagerActivity.class)
						.putStringArrayListExtra("images", list));
			}
		} else if (v == mRegistrationFlag) {
			final ArrayList<String> list = new ArrayList<String>();
			if (mRegistrationOut != null
					&& !TextUtils.isEmpty(mRegistrationOut.getPath())) {
				list.add(mRegistrationOut.getPath());
				startActivity(new Intent(mContext, ImagePagerActivity.class)
						.putStringArrayListExtra("images", list));
			}
		} else if (v == mCarFlag) {

			final ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < mCarPhotos.size(); i++) {
				if (!TextUtils.isEmpty(mCarPhotos.get(i).getPath())) {
					list.add(mCarPhotos.get(i).getPath());
				}
			}
			startActivity(new Intent(mContext, ImagePagerActivity.class)
					.putStringArrayListExtra("images", list));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Cursor cursor;
		int index;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CARD_CAMERA_CODE:
				if (mCardOut != null) {
					// mCardFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case CARD_GALLERY_CODE:
				cursor = getContentResolver().query(data.getData(), proj, null,
						null, null);
				index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				mCardOut = Uri.fromFile(new File(cursor.getString(index)));
				cursor.close();
				if (mCardOut != null) {
					// mCardFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case LICENSE_CAMERA_CODE:
				if (mLicenseOut != null) {
					// mLicenseFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case LICENSE_GALLERY_CODE:
				cursor = getContentResolver().query(data.getData(), proj, null,
						null, null);
				index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				mLicenseOut = Uri.fromFile(new File(cursor.getString(index)));
				cursor.close();
				if (mLicenseOut != null) {
					// mLicenseFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case REGISTRATION_CAMERA_CODE:
				if (mRegistrationOut != null) {
					// mRegistrationFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case REGISTRATION_GALLERY_CODE:
				cursor = getContentResolver().query(data.getData(), proj, null,
						null, null);
				index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				mRegistrationOut = Uri.fromFile(new File(cursor
						.getString(index)));
				cursor.close();
				if (mRegistrationOut != null) {
					// mRegistrationFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case CAR_CAMERA_CODE:
				if (mCarOut != null) {
					mCarPhotos.add(mCarOut);
					// mCarFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;
			case CAR_GALLERY_CODE:
				cursor = getContentResolver().query(data.getData(), proj, null,
						null, null);
				index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				mCarOut = Uri.fromFile(new File(cursor.getString(index)));
				cursor.close();
				if (mCarOut != null) {
					mCarPhotos.add(mCarOut);
					// mCarFlag.setImageResource(R.drawable.ic_register_select);
				}
				break;

			default:
				break;
			}
		}
	}

	// 提交完善资料
	private void submit() {

		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_REG_RENZHENG);
			jsonObject.put(Constants.TOKEN, application.getToken());
			final JSONObject params = new JSONObject();
			// params.put("driver_name", mDriverName.getText().toString());
			// params.put("owner_phone", mDriverPhone.getText().toString());
			// if (!isFormRegisterActivity) {
			// params.put("car_phone", mShuiChePhone.getText().toString());
			// }
			params.put("id_card", mCardNumber.getText().toString());
			params.put("id_card_name", mCardName.getText().toString());
			if (mCardOut != null) {
				params.put("id_card_photo",
						HttpUtils.getBitmapBase64(mContext, mCardOut.getPath()));
			}
			params.put("registration", mRegistrationNumber.getText().toString());
			params.put("registration_name", mRegistrationName.getText()
					.toString());
			if (mRegistrationOut != null) {
				params.put(
						"registration_photo",
						HttpUtils.getBitmapBase64(mContext,
								mRegistrationOut.getPath()));
			}
			params.put("license", mLicenseNumber.getText().toString());
			params.put("license_name", mLicenseName.getText().toString());
			if (mLicenseOut != null) {
				params.put(
						"license_photo",
						HttpUtils.getBitmapBase64(mContext,
								mLicenseOut.getPath()));
			}
			if (!mCarPhotos.isEmpty()) {
				try {
					for (int i = 1; i <= mCarPhotos.size(); i++) {
						System.out.println(mCarPhotos.get(i - 1).getPath());
						params.put("car_photo" + i, HttpUtils.getBitmapBase64(
                                mContext, mCarPhotos.get(i - 1).getPath()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			jsonObject.put(Constants.JSON, params.toString());
            showSpecialProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								showMsg("提交信息成功");
								// 判断是从我的易运宝跳转过来的
								application.writeIsThroughRezheng(true);
								finish();
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mCardOut != null) {
			outState.putParcelable(CARD, mCardOut);
		}
		if (mLicenseOut != null) {
			outState.putParcelable(CARD, mLicenseOut);
		}
		if (mRegistrationOut != null) {
			outState.putParcelable(REGISTRATION, mRegistrationOut);
		}
		if (mCarOut != null) {
			outState.putParcelable(CAR, mCarOut);
		}
		if (!mCarPhotos.isEmpty()) {
			outState.putParcelableArrayList("carPhotos", mCarPhotos);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		readSaveInstanceState(savedInstanceState);
	}

	// 读取保存的状态
	private void readSaveInstanceState(Bundle saveInstance) {
		if (saveInstance == null) {
			return;
		}
		if (saveInstance.containsKey(CARD)) {
			mCardOut = saveInstance.getParcelable(CARD);
		}
		if (saveInstance.containsKey(LICENSE)) {
			mLicenseOut = saveInstance.getParcelable(LICENSE);
		}
		if (saveInstance.containsKey(REGISTRATION)) {
			mRegistrationOut = saveInstance.getParcelable(REGISTRATION);
		}
		if (saveInstance.containsKey(CAR)) {
			mCarOut = saveInstance.getParcelable(CAR);
		}
		if (saveInstance.containsKey("carPhotos")) {
			mCarPhotos = saveInstance.getParcelableArrayList("carPhotos");
		}
	}

}
