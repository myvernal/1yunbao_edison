package com.maogousoft.logisticsmobile.driver.activity.vip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CarTypeListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.utils.FileCache;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper.LocCallback;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.PickPhoto;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;

/**
 * 添加物流园区
 * 
 * @author lenovo
 */
public class AddActivity extends BaseActivity {

	private EditText mName, mAddr, vender_phone, mNormalPrice, mParkingNum, mVipContent, mOther;
	private RadioButton mProvince, mCity, mTowns;
	private Button mLBS;
	private Button mSubmit;
	private Spinner mType;

	private static final String LOGTAG = LogUtil.makeLogTag(AddActivity.class);

	// 选择照片的requestCode
	private final int CAR_CAMERA_CODE = 1006, CAR_GALLERY_CODE = 1007;

	// 查询图片地址
	private final String[] proj = { MediaStore.Images.Media.DATA };

	private final String CAR = "car";

	// 身份证，驾驶证，行驶证
	private Button mCarFlag;

	// 调用相机，相册的按钮
	private ImageButton mCarCamera, mCarGallery;

	private View mCar;

	private FileCache mFileCache;

	// 图片输出路径
	private Uri mCarOut;

	// 保存车辆照片的list
	private ArrayList<Uri> mCarPhotos = new ArrayList<Uri>();

	// 保存车辆照片url的list
	private ArrayList<String> mCarPhotosUrl = new ArrayList<String>();

	private double longitude;
	private double latitude;

	private MyGridView mGridView;
	private CityDBUtils mDBUtils;
	private CityListAdapter mAdapter;
	// 当前选中的一级城市，二级城市
	private CityInfo currentProvince, currentCity, currentTowns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_add);
		readSaveInstanceState(savedInstanceState);
		initViews();
		initUtils();
		//initData();
	}

	// 初始化视图
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_vip_add_shop);

		mName = (EditText) findViewById(R.id.vip_id_add_name);
		mType = (Spinner) findViewById(R.id.vip_id_add_type);
		mProvince = (RadioButton) findViewById(R.id.vip_id_add_province);
		mCity = (RadioButton) findViewById(R.id.vip_id_add_city);
		mTowns = (RadioButton) findViewById(R.id.vip_id_add_towns);
		mAddr = (EditText) findViewById(R.id.vip_id_add_addr);
		mLBS = (Button) findViewById(R.id.vip_id_add_lbs);
		vender_phone = (EditText) findViewById(R.id.vender_phone);
		mNormalPrice = (EditText) findViewById(R.id.vip_id_add_normalprice);
		mParkingNum = (EditText) findViewById(R.id.parking_spaces_num);
        mVipContent = (EditText) findViewById(R.id.member_price);
		mOther = (EditText) findViewById(R.id.vip_id_add_other);
		// mPhoto = (EditText) findViewById(R.id.vip_id_add_photo);
		mSubmit = (Button) findViewById(R.id.vip_id_add_submit);

		mGridView = (MyGridView) findViewById(R.id.info_id_register_city);

		mCar = findViewById(R.id.vip_id_add_photo);

		mCarFlag = (Button) mCar.findViewById(R.id.common_id_select_flag);
		mCarFlag.setText(Html.fromHtml("<u>查看图片</u>"));

		mCarCamera = (ImageButton) mCar.findViewById(R.id.common_id_select_camera);
		mCarGallery = (ImageButton) mCar.findViewById(R.id.common_id_select_gallery);
		((TextView) mCar.findViewById(R.id.common_id_select_title)).setText("照片");

		mCarCamera.setOnClickListener(this);
		mCarGallery.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mCarFlag.setOnClickListener(this);
		mLBS.setOnClickListener(this);

		mProvince.setOnClickListener(this);
		mCity.setOnClickListener(this);
		mTowns.setOnClickListener(this);

	}

	private void initUtils() {
		mFileCache = new FileCache(context);

		mDBUtils = new CityDBUtils(application.getCitySDB());
		mAdapter = new CityListAdapter(context);

		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(mOnItemClickListener);
	}

	// 初始化data
	private void initData() {
		CarTypeListAdapter mCarTypeAdapter = new CarTypeListAdapter(context);
		mType.setAdapter(mCarTypeAdapter);
		mType.setPrompt("请选择");

		List<DictInfo> mList2 = new ArrayList<DictInfo>();
		for (int i = 0; i < 6; i++) {
			DictInfo d = new DictInfo();
			d.setId(i);
			String categoryStr = "";
			switch (i) {

				case 0:
					categoryStr = "住宿优惠";
					break;
				case 1:
					categoryStr = "加油优惠";
					break;
				case 2:
					categoryStr = "餐饮折扣";
					break;
				case 3:
					categoryStr = "休闲优惠";
					break;
				case 4:
					categoryStr = "维修保养";
					break;
				case 5:
					categoryStr = "其他";
					break;
			}
			d.setName(categoryStr);
			mList2.add(d);
		}
		mCarTypeAdapter.setList(mList2);
	}

	// private void initMap() {
	// if (application.getBMapManager() == null) {
	// application.initBMapManager();
	// }
	//
	// LocationClientOption option = new LocationClientOption();
	// // option.setOpenGps(true);// 打开gps
	// option.setCoorType("bd09ll"); // 设置坐标类型
	// option.setScanSpan(200); // 定位时间，毫秒 小于1秒则一次定位;大于等于1秒则定时定位
	// option.setAddrType("all");
	// option.setPriority(LocationClientOption.NetWorkFirst);
	// mLocClient = new LocationClient(this);
	// mLocClient.setLocOption(option);
	//
	// }

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			if (mGridView.isShown()) {
				mGridView.setVisibility(View.GONE);
			}

			final CityInfo mCityInfo = (CityInfo) mAdapter.getItem(position);
			final int mDeep = mCityInfo.getDeep();
			switch (mDeep) {
				case 1:
					currentProvince = mCityInfo;
					currentCity = null;
					currentTowns = null;
					mProvince.setText(mCityInfo.getName());
					mCity.setText(R.string.string_city);
					mTowns.setText("区县");
					List<CityInfo> mList2 = mDBUtils.getSecondCity(currentProvince.getId());
					mAdapter.setList(mList2);
                    mAddr.setText(currentProvince.getName());
					break;
				case 2:
					currentCity = mCityInfo;
					currentTowns = null;
					mCity.setText(mCityInfo.getName());
					mTowns.setText("区县");
					List<CityInfo> mList3 = mDBUtils.getThridCity(currentCity.getId());
					mAdapter.setList(mList3);
                    mAddr.setText(mAddr.getText() + currentCity.getName());
					break;
				case 3:
					currentTowns = mCityInfo;
					mTowns.setText(mCityInfo.getName());
					mGridView.setVisibility(View.GONE);
                    mAddr.setText(mAddr.getText() + currentTowns.getName());
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mCarCamera) {
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
		} else if (v == mSubmit) {
			uploadImageAndSubmit();
		} else if (v == mCarFlag) {

			final ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < mCarPhotos.size(); i++) {
				if (!TextUtils.isEmpty(mCarPhotos.get(i).getPath())) {
					list.add(mCarPhotos.get(i).getPath());
				}
			}
			startActivity(new Intent(context, ImagePagerActivity.class).putStringArrayListExtra("images", list));
		} else if (v == mLBS) {
			showProgress("正在获取经纬度");

			LocHelper.getInstance(context).getResult(new LocCallback() {

				@Override
				public void onRecivedLoc(double lat, double lng, String addr) {

					// location String 是 位置字符串
					// longitude double 是 经度
					// latitude double 是 纬度

					dismissProgress();

					if (lat == 0 || lng == 0 || TextUtils.isEmpty(addr)) {
						return;
					}

					longitude = lat;
					latitude = lng;

					if (TextUtils.isEmpty(addr) || addr.equals("null")) {
						if (longitude != 0 || latitude != 0) {
							mLBS.setText("已获取到位置");
						} else {
							mLBS.setText("点击定位");
						}
					} else {
						mLBS.setText(addr);
					}

				}
			});
		} else if (v == mProvince) {

			if (mGridView.isShown()) {
				mGridView.setVisibility(View.GONE);
				return;
			}
			mGridView.setVisibility(View.VISIBLE);
			List<CityInfo> mList = mDBUtils.getFirstCity();
			mAdapter.setList(mList);

		} else if (v == mCity) {

			if (currentProvince == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (mGridView.isShown()) {
				mGridView.setVisibility(View.GONE);
				return;
			}
			mGridView.setVisibility(View.VISIBLE);
			List<CityInfo> mList2 = mDBUtils.getSecondCity(currentProvince.getId());
			mAdapter.setList(mList2);

		} else if (v == mTowns) {

			if (currentProvince == null) {
				showMsg(R.string.tips_register_select_province);
				return;
			}
			if (currentCity == null) {
				showMsg("请先选择城市!");
				return;
			}
			if (mGridView.isShown()) {
				mGridView.setVisibility(View.GONE);
				return;
			}

			mGridView.setVisibility(View.VISIBLE);
			List<CityInfo> mList3 = mDBUtils.getThridCity(currentCity.getId());
			mAdapter.setList(mList3);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Cursor cursor;
		int index;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

				case CAR_CAMERA_CODE:
					if (mCarOut != null) {

						String path = PickPhoto.scalePicture(context, mCarOut.getPath(), 480, 800);
						mCarOut = Uri.fromFile(new File(path));
						mCarPhotos.add(mCarOut);
					}
					break;
				case CAR_GALLERY_CODE:
					cursor = getContentResolver().query(data.getData(), proj, null, null, null);
					index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String out = cursor.getString(index);
					cursor.close();

					String path = PickPhoto.scalePicture(context, out, 480, 800);

					mCarOut = Uri.fromFile(new File(path));

					if (mCarOut != null) {
						mCarPhotos.add(mCarOut);
					}
					break;

				default:
					break;
			}
		}

		if (!mCarPhotos.isEmpty()) {
			try {
				for (int i = 0; i < mCarPhotos.size(); i++) {
					System.out.println(mCarPhotos.get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 提交完善资料
	private void submit() {

		if (TextUtils.isEmpty(mName.getText().toString())) {
			showMsg("请输入园区名称");
			return;
		}

		if (TextUtils.isEmpty(mAddr.getText().toString())) {
			showMsg("请输入园区地址");
			return;
		}

		if (longitude == 0 || latitude == 0) {
			showMsg("请首先点击定位");
			return;
		}

		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.ADD_VENDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			final JSONObject params = new JSONObject();
			params.put("vender_name", mName.getText().toString());
			params.put("vender_address", mLBS.getText().toString());

			if (currentProvince != null) {
				params.put("vender_province", currentProvince.getId());
			}
			if (currentCity != null) {
				params.put("vender_city", currentCity.getId());
			}
			if (currentTowns != null) {
				params.put("vender_district", currentTowns.getId());
			}
			params.put("longitude", longitude);
			params.put("latitude", latitude);
			params.put("vender_phone", vender_phone.getText().toString());
            params.put("parking_spaces_num", mParkingNum.getText().toString());
			params.put("normal_price", mNormalPrice.getText().toString());
			params.put("member_price", mVipContent.getText().toString());
			params.put("campus_activities", mOther.getText().toString());
            params.put("category", 5);
			// vender_name int 是 商家名称
			// category int 是 分类
			// vender_address String 是 地址
			// vender_province int 省
			// vender_city int 市
			// vender_district int 区
			// longitude double 经度
			// latitude double 纬度
			// contact String 联系人
			// vender_mobile String 联系手机
			// vender_phone String 联系电话
			// goods_name String 商品名称
			// normal_price String 正常价格
			// member_price String 会员特惠
			// other String 其他

			// if (!mCarPhotos.isEmpty()) {
			// try {
			// for (int i = 1; i <= mCarPhotos.size(); i++) {
			// System.out.println(mCarPhotos.get(i - 1).getPath());
			// params.put("photo" + i, HttpUtils.getBitmapBase64(context, mCarPhotos.get(i - 1).getPath()));
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }

			if (!mCarPhotosUrl.isEmpty()) {
				try {
					for (int i = 0; i < mCarPhotosUrl.size(); i++) {
						System.out.println(mCarPhotosUrl.get(i));
						params.put("photo" + (i + 1), mCarPhotosUrl.get(i));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			jsonObject.put(Constants.JSON, params.toString());
            showSpecialProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, null, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					dismissProgress();
					switch (code) {
						case ResultCode.RESULT_OK:
							showMsg("提交信息成功");
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

		if (saveInstance.containsKey(CAR)) {
			mCarOut = saveInstance.getParcelable(CAR);
		}
		if (saveInstance.containsKey("carPhotos")) {
			mCarPhotos = saveInstance.getParcelableArrayList("carPhotos");
		}
	}

	public void uploadImageAndSubmit() {
		if (!mCarPhotos.isEmpty()) {
			try {
				for (int i = 0; i < mCarPhotos.size(); i++) {
					// System.out.println(mCarPhotos.get(i - 1).getPath());
					// params.put("photo" + i, HttpUtils.getBitmapBase64(context, mCarPhotos.get(i - 1).getPath()));
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
					LogUtil.e(LOGTAG, "上传文件结果:" + serverResponse);
					return serverResponse;
				} catch (Exception e) {
					LogUtil.e(LOGTAG, "上传文件出错");
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
