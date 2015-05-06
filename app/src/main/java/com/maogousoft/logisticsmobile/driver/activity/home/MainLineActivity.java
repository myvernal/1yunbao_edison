package com.maogousoft.logisticsmobile.driver.activity.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import com.ybxiang.driver.util.SettingUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChangePathActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;

/**
 * 主营路线
 * 
 * @author ybxiang
 */
public class MainLineActivity extends BaseActivity {
	private Button mChangePath;
	private TextView mPath1, mPath2, mPath3, mTip;
    private ImageView sound_line_1, sound_line_2, sound_line_3;
	// 个人abc信息
	private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_line);
		initViews();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getABCInfo();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("主营路线");
        findViewById(R.id.titlebar_id_more).setVisibility(View.VISIBLE);

		mChangePath = (Button) findViewById(R.id.myabc_id_change_path);
		mPath1 = (TextView) findViewById(R.id.myabc_id_path1);
		mPath2 = (TextView) findViewById(R.id.myabc_id_path2);
		mPath3 = (TextView) findViewById(R.id.myabc_id_path3);
        sound_line_1 = (ImageView) findViewById(R.id.sound_line_1);
        sound_line_2 = (ImageView) findViewById(R.id.sound_line_2);
        sound_line_3 = (ImageView) findViewById(R.id.sound_line_3);
        sound_line_1.setOnClickListener(this);
        sound_line_2.setOnClickListener(this);
        sound_line_3.setOnClickListener(this);
        mTip = (TextView) findViewById(R.id.main_line_tip);
	}

	private void initData() {
        //初始化语音提示按钮
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.notify_on);
        ImageSpan imgSpan = new ImageSpan(this, b);
        String value = getString(R.string.main_line_tip2);
        SpannableString spanString = new SpannableString(value);
        spanString.setSpan(imgSpan, 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTip.setText(spanString);
        //初始化线路提示icon
        initSoundNotifyIcon(SettingUtil.MAIN_LINE_1, sound_line_1);
        initSoundNotifyIcon(SettingUtil.MAIN_LINE_2, sound_line_2);
        initSoundNotifyIcon(SettingUtil.MAIN_LINE_3, sound_line_3);
	}

	// 获取我的abc信息
	private void getABCInfo() {
		// if (mAbcInfo != null) {
		// return;
		// }
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, "");
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					AbcInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result != null) {
									mAbcInfo = (AbcInfo) result;
									CityDBUtils mDBUtils = new CityDBUtils(
											application.getCitySDB());
									String path1Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province(),
											mAbcInfo.getStart_city(),
											mAbcInfo.getEnd_province(),
											mAbcInfo.getEnd_city());
									String path2Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province2(),
											mAbcInfo.getStart_city2(),
											mAbcInfo.getEnd_province2(),
											mAbcInfo.getEnd_city2());
									String path3Str = mDBUtils.getStartEndStr(
											mAbcInfo.getStart_province3(),
											mAbcInfo.getStart_city3(),
											mAbcInfo.getEnd_province3(),
											mAbcInfo.getEnd_city3());

									mPath1.setText(path1Str);
									mPath2.setText(path2Str);
									mPath3.setText(path3Str);

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
	public void onClick(View v) {
		super.onClick(v);
        switch (v.getId()) {
            case R.id.sound_line_1:
                changeSoundNotifyIcon(SettingUtil.MAIN_LINE_1, v);
                break;
            case R.id.sound_line_2:
                changeSoundNotifyIcon(SettingUtil.MAIN_LINE_2, v);
                break;
            case R.id.sound_line_3:
                changeSoundNotifyIcon(SettingUtil.MAIN_LINE_3, v);
                break;
        }
	}

    /**
     * 改变线路新货源通知状态
     * @param name
     */
    public void changeSoundNotifyIcon(String name,View view) {
        boolean status = SettingUtil.getInstance(mContext).getMainLineNotifyStatus(name);
        SettingUtil.getInstance(mContext).setMainLineNotifyStatus(name, !status);
        if(!status) {
            ((ImageView)view).setImageResource(R.drawable.notify_on);
        } else {
            ((ImageView)view).setImageResource(R.drawable.notify_off);
        }
    }

    /**
     * 改变线路新货源通知状态
     * @param name
     */
    public void initSoundNotifyIcon(String name,View view) {
        boolean status = SettingUtil.getInstance(mContext).getMainLineNotifyStatus(name);
        if(status) {
            ((ImageView)view).setImageResource(R.drawable.notify_on);
        } else {
            ((ImageView)view).setImageResource(R.drawable.notify_off);
        }
    }

	/**
	 * 修改路线
	 * 
	 * @param view
	 */
	public void onChangePath(View view) {
		String[] array = new String[] { "线路1", "线路2", "线路3" };
		new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
                mContext).setTitle("选择需要修改的路线")
				.setItems(array, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(mContext, ChangePathActivity.class);
						// intent.putExtra("info", mAbcInfo);
						intent.putExtra("path", which);
						startActivityForResult(intent, 1);
					}
				}).show();
	}
}
