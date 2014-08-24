package com.ybxiang.driver.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.MapActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.model.LocationInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/22.
 */
public class MyCarsDetailActivity extends BaseActivity {

    private int id;
    private TextView driver_name, phone, way, plate_number, car_weight, car_length,
            car_type, location_address, location_time;
    private View edit, delete, free_location, phone_location, back;
    private CityDBUtils dbUtils;
    private long phoneNumber = 0;
    private CarInfo carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_car_detail_layout);
        initViews();
        initData();
        getData();
    }

    private void initViews() {
        ((TextView)findViewById(R.id.titlebar_id_content)).setText("车辆详情");
        back = findViewById(R.id.titlebar_id_back);
        back.setOnClickListener(this);
        driver_name = (TextView) findViewById(R.id.driver_name);
        phone = (TextView) findViewById(R.id.phone);
        way = (TextView) findViewById(R.id.way);
        plate_number = (TextView) findViewById(R.id.plate_number);
        car_weight = (TextView) findViewById(R.id.car_weight);
        car_length = (TextView) findViewById(R.id.car_length);
        car_type = (TextView) findViewById(R.id.car_type);
        location_address = (TextView) findViewById(R.id.location_address);
        location_time = (TextView) findViewById(R.id.location_time);
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);
        free_location = findViewById(R.id.free_location);
        free_location.setOnClickListener(this);
        phone_location = findViewById(R.id.phone_location);
        phone_location.setOnClickListener(this);
    }

    private void initData(){
        id = getIntent().getIntExtra(Constants.COMMON_KEY, 0);
        dbUtils = new CityDBUtils(application.getCitySDB());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.titlebar_id_back:
                finish();
                break;
            case R.id.edit:
                if(carInfo != null) {
                    Intent intent = new Intent(context, AddCarActivity.class);
                    intent.putExtra(Constants.COMMON_KEY, carInfo);
                    intent.putExtra(Constants.CAR_EDIT_TYPE, Constants.EDIT_CAR);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(context, "没有车辆数据!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete:
                deleteData();
                break;
            case R.id.free_location:
                location(true);
                break;
            case R.id.phone_location:
                showPhoneLocationDialogConfirm();
                break;
        }
    }

    // 请求数据
    private void getData() {
        try {
            showDefaultProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_MY_FLEET_DETAIL);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("id", id).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof CarInfo) {
                                        carInfo = (CarInfo) result;
                                        //线路
                                        String wayStart = dbUtils.getCityInfo(carInfo.getStart_province(), carInfo.getStart_city(), carInfo.getStart_district());
                                        StringBuffer sb = new StringBuffer();
                                        if(carInfo.getEnd_province1() > 0 || carInfo.getEnd_city1() > 0 || carInfo.getEnd_district1() > 0) {
                                            String wayEnd1 = dbUtils.getCityInfo(carInfo.getEnd_province1(), carInfo.getEnd_city1(), carInfo.getEnd_district1());
                                            sb.append(wayStart + "--" + wayEnd1 + "\n");
                                        }
                                        if(carInfo.getEnd_province2() > 0 || carInfo.getEnd_city2() > 0 || carInfo.getEnd_district2() > 0) {
                                            String wayEnd2 = dbUtils.getCityInfo(carInfo.getEnd_province2(), carInfo.getEnd_city2(), carInfo.getEnd_district2());
                                            sb.append(wayStart + "--" + wayEnd2 + "\n");
                                        }
                                        if(carInfo.getEnd_province3() > 0 || carInfo.getEnd_city3() > 0 || carInfo.getEnd_district3() > 0) {
                                            String wayEnd3 = dbUtils.getCityInfo(carInfo.getEnd_province3(), carInfo.getEnd_city3(), carInfo.getEnd_district3());
                                            sb.append(wayStart + "--" + wayEnd3);
                                        }
                                        way.setText(sb.toString());
                                        plate_number.setText(plate_number.getText() + carInfo.getPlate_number());
                                        driver_name.setText(driver_name.getText() + carInfo.getDriver_name());
                                        phone.setText(phone.getText() + carInfo.getPhone());
                                        phoneNumber = Long.valueOf(carInfo.getPhone());
                                        car_length.setText(car_length.getText() + carInfo.getCar_length() + "米");
                                        //车型
                                        int carTypeValue = carInfo.getCar_type();
                                        String[] carTypeStr = context.getResources().getStringArray(R.array.car_types_name);
                                        for(int i=0;i<Constants.carTypeValues.length;i++) {
                                            if(Constants.carTypeValues[i] == carTypeValue){
                                                car_type.setText(car_type.getText().toString() + carTypeStr[i]);
                                            }
                                        }
                                        car_weight.setText(car_weight.getText().toString() + carInfo.getCar_weight() + "吨");
                                        if(!TextUtils.isEmpty(carInfo.getLocation())) {
                                            location_address.setText(location_address.getText() + carInfo.getLocation());
                                            location_address.setVisibility(View.VISIBLE);
                                        }
                                        if(!TextUtils.isEmpty(carInfo.getLocation_time()) && Long.valueOf(carInfo.getLocation_time()) > 0) {
                                            Date date = new Date(Long.valueOf(carInfo.getLocation_time()));
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
                                            String locationTime = simpleDateFormat.format(date);
                                            location_time.setText(location_time.getText() + locationTime);
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
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

    // 删除数据
    private void deleteData() {
        try {
            showDefaultProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.DELETE_MY_FLEET);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("id", id).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "删除车辆成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
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

    /**
     *  定位
     *  @param isFreeLocation 是否是免费定位
     */
    private void location(final boolean isFreeLocation) {
        try {
            showDefaultProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, isFreeLocation? Constants.FREE_LOCATION : Constants.PHONE_LOCATION);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("mobile", phoneNumber).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    LocationInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if(result instanceof LocationInfo) {
                                        LocationInfo info = (LocationInfo) result;
                                        if(!info.isDone()) {
                                            location_address.setText(info.getAddress());
                                            location_address.setVisibility(View.VISIBLE);
                                            location_time.setText(location_time.getText() + info.getTimestamp());
                                            Intent intent = new Intent(context, MapActivity.class);
                                            intent.putExtra(Constants.COMMON_KEY, info);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "定位超时", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String) {
                                        if(isFreeLocation) {
                                            showFreeLocationDialog();
                                        } else {
                                            showPhoneLocationDialogFailed();
                                        }
                                    }
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

    public void onSendMessage(View view) {
        if(phoneNumber <= 0) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        startActivity(intent);
    }

    public void onCall(View view) {
        if(phoneNumber <= 0) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    /**
     * 免费定位未注册提示
     */
    private void showFreeLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("邀请加入");
        builder.setMessage("对方现在还不是易运宝用户,邀请对方注册使用,从此平台随时自动更新司机位置.");
        builder.setPositiveButton("邀请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                share();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 收费定位未开通提示
     */
    private void showPhoneLocationDialogFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("定位授权");
        builder.setMessage("将发送信息给对方，对方回复Y后，请再次点击手机定位.司机也可以主动编辑短信Y，移动发送至10658012174，联通发送至106550101832224261");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 收费定位确认提示
     */
    private void showPhoneLocationDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认信息");
        builder.setMessage("您正在使用收费定位功能,将扣除您0.1个物流币!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                location(false);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
