package com.maogousoft.logisticsmobile.driver.activity.home;

// PR111 货运名片

import java.util.List;

import android.widget.*;
import com.maogousoft.logisticsmobile.driver.activity.info.*;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 货运名片
 *
 * @author lenovo
 */
public class MyBusinessCard extends BaseActivity {

    private Context mContext; // PR111
    // 返回,完善资料
    private Button mBack, mUpdate;

    private Button mShareCard;

    private TextView mName, mNumber, mPhone, mUpdatePwd;

    private View mChangePath;
    private TextView mPath1, mPath2, mPath3, mCarNum, mCarlength, mCartype,
            mCarzhaizhong;

    // 个人abc信息
    private AbcInfo mAbcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card_layout);
        mContext = this; // PR111
        initViews();
        // 隐藏我的易运宝左边的返回按钮
        //((Button)findViewById(R.id.titlebar_id_back)).setVisibility(View.GONE);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_business_card);

        setIsRightKeyIntoShare(false);
        mShareCard = (Button) findViewById(R.id.titlebar_id_more);
        mShareCard.setText("发名片");

        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mUpdate = (Button) findViewById(R.id.myabc_id_update);
        mName = (TextView) findViewById(R.id.business_card_part1_name);
        mNumber = (TextView) findViewById(R.id.business_card_part1_number);
        mPhone = (TextView) findViewById(R.id.business_card_part1_phone);
        mChangePath = findViewById(R.id.myabc_id_change_path);
        mUpdatePwd = (TextView) findViewById(R.id.myabc_id_updatepwd);

        mPath1 = (TextView) findViewById(R.id.myabc_id_path1);
        mPath2 = (TextView) findViewById(R.id.myabc_id_path2);
        mPath3 = (TextView) findViewById(R.id.myabc_id_path3);
        mCarNum = (TextView) findViewById(R.id.myabc_id_car_num);
        mCarlength = (TextView) findViewById(R.id.myabc_id_car_length111);
        mCartype = (TextView) findViewById(R.id.myabc_id_car_type);
        mCarzhaizhong = (TextView) findViewById(R.id.myabc_id_car_zhaizhong);

        mShareCard.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mChangePath.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getABCInfo();
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        if(v == mBack) {
            finish();
        } else if (v == mUpdatePwd) {
            startActivity(new Intent(context, UpdatePwdActivity.class));
        }
//        else if (v == mCreditContainer) {
//            startActivity(new Intent(mContext, MyCreditActivity.class).putExtra(
//                    "info", mAbcInfo));
//        } else if (v == mCharge) {
//            startActivity(new Intent(mContext, ChargeActivity.class));
//        } else if (v == mAccountRecord) {
//            startActivity(new Intent(mContext, AccountRecordActivity.class));
//        }
        else if (v == mUpdate) {
            startActivity(new Intent(context, OptionalActivity.class).putExtra(
                    "info", mAbcInfo));
        }
//        else if (v == mHistory) {
//            startActivity(new Intent(mContext, HistroyOrderActivity.class)
//                    .putExtra("info", mAbcInfo));
//        }
        else if (v == mChangePath) {
            // if (mAbcInfo == null) {
            // showMsg("请等待获取线路");
            // } else {

            String[] array = new String[] { "线路1", "线路2", "线路3" };
            new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
                    context).setTitle("选择需要修改的路线")
                    .setItems(array, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(context,
                                    ChangePathActivity.class);
                            // intent.putExtra("info", mAbcInfo);
                            intent.putExtra("path", which);
                            startActivityForResult(intent, 1);
                        }
                    }).show();

            // }
        } else if (v == mShareCard) {
            share();
            // PR111 end
        }
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

                                        if (!TextUtils.isEmpty(mAbcInfo.getName())) {
                                            mName.setText(mAbcInfo.getName());
                                        }
                                        if (!TextUtils.isEmpty(mAbcInfo.getPlate_number())) {
                                            mNumber.setText(mAbcInfo.getPlate_number());
                                        }
                                        mPhone.setText(mAbcInfo.getPhone());

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

                                        if (!TextUtils.isEmpty(mAbcInfo
                                                .getPlate_number())) {
                                            mCarNum.setText(mAbcInfo
                                                    .getPlate_number());
                                        }

                                        mCarlength.setText(mAbcInfo.getCar_length()
                                                + "米");
                                        if (!TextUtils.isEmpty(mAbcInfo
                                                .getCar_type_str())) {
                                            mCartype.setText(mAbcInfo
                                                    .getCar_type_str());
                                        }
                                        mCarzhaizhong.setText(mAbcInfo.getCar_weight() + "吨");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            getABCInfo();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        sendBroadcast(new Intent(MainActivity.ACTION_SWITCH_MAINACTIVITY));
    }
}
