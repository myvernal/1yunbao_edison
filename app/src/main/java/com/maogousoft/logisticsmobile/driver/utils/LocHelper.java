package com.maogousoft.logisticsmobile.driver.utils;

import java.util.HashMap;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.maogousoft.logisticsmobile.driver.AlarmReceiver;
import com.maogousoft.logisticsmobile.driver.Constants;

public class LocHelper {

	private static LocHelper locHelper;
	private LocCallback resultCallback;
	private static int flag;
	public static final int FLAG_DEFAULT = 0;

	private LocationClient mLocationClient = null;
	private MyLocationListenner myListener = null;

	private static Context ctx;

	static final String TAG = "LocHelper";

	private static PendingIntent alarmPendingIntent;

	public interface LocCallback {

		void onRecivedLoc(double lat, double lng, String addr);
	}

	// public static LocHelper getInstance(Context ctx, int flag) {
	// if (locHelper == null) {
	// locHelper = new LocHelper();
	// }
	// locHelper.ctx = ctx;
	// locHelper.flag = flag;
	//
	// return locHelper;
	// }

	public static LocHelper getInstance(Context context) {
		if (locHelper == null) {
			locHelper = new LocHelper();
		}
		ctx = context;
		flag = FLAG_DEFAULT;

		return locHelper;
	}

	public void getResult(LocCallback resultCallback) {
		this.resultCallback = resultCallback;
		start();
	}

	private LocHelper() {

	}

	/**
	 * 开始定位
	 */
	private void start() {

		init();
		// if (mLocationClient == null) {
		// mLocationClient = new LocationClient(ctx.getApplicationContext());
		// setLocationOption();
		// }
		// if (myListener == null) {
		// myListener = new MyLocationListenner();
		// mLocationClient.registerLocationListener(myListener);
		// }
		//
		// if (!mLocationClient.isStarted()) {
		// mLocationClient.start();
		// }

		// 定位一次
		mLocationClient.requestLocation();
		LogUtil.i(TAG, "发起一次定位");

	}

	/**
	 * 释放资源
	 */
	public void release() {

		if (mLocationClient != null && myListener != null) {
			mLocationClient.unRegisterLocationListener(myListener);
		}

		if (mLocationClient != null) {
			mLocationClient.stop();
		}

		myListener = null;
		mLocationClient = null;
	}

	/**
	 * 定位初始化
	 */
	public void init() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(ctx.getApplicationContext());
			setLocationOption();
		}
		if (myListener == null) {
			myListener = new MyLocationListenner();
			mLocationClient.registerLocationListener(myListener);
		}

		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}

		setLocationOption();
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				LogUtil.e(TAG, "定位结果为空");
				if (resultCallback != null) {
					resultCallback.onRecivedLoc(0, 0, null);
				}
				return;
			}

			int resultFlag = location.getLocType();

			if (resultFlag != 161 && resultFlag != 61) {
				LogUtil.e(TAG, "定位结果类型不为161，61,定位失败");
				if (resultCallback != null) {
					resultCallback.onRecivedLoc(0, 0, null);
				}
				return;
			}

			LogUtil.e(TAG, "定位结果类型：" + resultFlag);

			// 61 ： GPS定位结果
			// 62 ： 扫描整合定位依据失败。此时定位结果无效。
			// 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
			// 65 ： 定位缓存的结果。
			// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
			// 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
			// 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
			// 161： 表示网络定位结果
			// 162~167： 服务端定位失败。

			StringBuffer sb = new StringBuffer(256);
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			LogUtil.i(TAG, "定位结果：" + sb.toString());

			if (TextUtils.isEmpty(location.getAddrStr())) {
				LogUtil.i(TAG, "定位结果地址为null");
				getAddr(location.getLatitude(), location.getLongitude());
			} else {
				if (resultCallback != null) {
					resultCallback.onRecivedLoc(location.getLatitude(), location.getLongitude(), location.getAddrStr());
				}
			}
			// // 释放资源
			// release();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll");// 设置坐标类型
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
	}

	/**
	 * 添加定时上报闹钟
	 * 
	 * @param context 上下文
	 * @param start 开始时间
	 * @param intv 上报间隔
	 * @return
	 */
	public static void addAlarm(Context context, long start, int intv) {
        MGApplication application = (MGApplication) context.getApplicationContext();
        if(application.getUserType() == Constants.USER_SHIPPER) {
            //货主无需上报
            return;
        }

		AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		if (alarmPendingIntent != null) {
			mAlarmManager.cancel(alarmPendingIntent);
			alarmPendingIntent = null;
		}
		Intent pendIntent = new Intent(context, AlarmReceiver.class);
		alarmPendingIntent = PendingIntent.getBroadcast(context, 0, pendIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, start, intv, alarmPendingIntent);
	}

	public void getAddr(final double lat, final double lng) {
		new AsyncTask<BDLocation, Object, HashMap<String, Object>>() {

			@Override
			protected void onPostExecute(HashMap<String, Object> result) {
				if (result != null) {
					String addr = (String) result.get("addr");
					LogUtil.e(TAG, "geocoder，获取到的数据地址是：" + addr);

					if (resultCallback != null) {
						resultCallback.onRecivedLoc(lat, lng, addr);
					}
				} else {
					if (resultCallback != null) {
						resultCallback.onRecivedLoc(0, 0, null);
					}
				}
                //释放资源
                release();
				super.onPostExecute(result);
			}

			@Override
			protected HashMap<String, Object> doInBackground(BDLocation... params) {

				String url = "http://api.map.baidu.com/geocoder?" + "location=" + lat + "," + lng + "&output=json&key="
						+ Constants.strKey;

				// 时效宝 百度key b66fbb5a289082fa86ef1a7df81ab57f

				String returnStr = HttpUtils.getURLData(url);

				HashMap<String, Object> hmResult = null;

				JSONObject jObject;
				try {
					jObject = new JSONObject(returnStr);

					if (!jObject.getString("status").equalsIgnoreCase("OK")) {
						LogUtil.e(TAG, "geocoder，获取到的数据status不是OK");
						return null;
					}

					hmResult = new HashMap<String, Object>();
					String addr = jObject.getJSONObject("result").getString("formatted_address");
					hmResult.put("addr", addr);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return hmResult;
			}
		}.execute();
	}
}
