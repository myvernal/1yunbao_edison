package com.ybxiang.driver.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.model.PinganPackageTypeInfo;
import com.maogousoft.logisticsmobile.driver.model.SafePinanInfo;
import com.ybxiang.driver.model.PinanAreaInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by aliang on 2014/9/1.
 */
public class SafePinanEditActivity extends BaseActivity {
    private Button mTitleBarBack, mTitleBarMore;
    private EditText start_date, insured_name, insured_phone, shiping_number, plate_number, guache_Num, lianxiren_name;
    private TextView peichang_area;
    private Spinner package_type, start_area, start_city, end_area, end_city;
    private CitySelectView citySelectView;
    private SafePinanInfo safePinanInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_pingan_edit_layout);
        initViews();
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("中国平安国内公路货运险");
        // 返回按钮生效
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarBack.setOnClickListener(this);
        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("保险记录");
        mTitleBarMore.setOnClickListener(this);

        lianxiren_name = (EditText) findViewById(R.id.lianxiren_name);
        guache_Num = (EditText) findViewById(R.id.guache_Num);
        start_date = (EditText) findViewById(R.id.start_date);
        insured_name = (EditText) findViewById(R.id.insured_name);
        insured_phone = (EditText) findViewById(R.id.insured_phone);
        shiping_number = (EditText) findViewById(R.id.shiping_number);
        plate_number = (EditText) findViewById(R.id.plate_number);
        start_date = (EditText) findViewById(R.id.start_date);
        peichang_area = (TextView) findViewById(R.id.peichang_area);
        package_type = (Spinner) findViewById(R.id.package_type);
        start_area = (Spinner) findViewById(R.id.start_area);
        start_city = (Spinner) findViewById(R.id.start_city);
        end_area = (Spinner) findViewById(R.id.end_area);
        end_city = (Spinner) findViewById(R.id.end_city);
        citySelectView = (CitySelectView) findViewById(R.id.citySelectView);

        start_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取详细货物类别
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
                start_city.setAdapter(arrayAdapter);
                start_city.setEnabled(false);
                if(!provinceInfoList.isEmpty()) {
                    getSafeCityAreaInfo(Constants.getPinanStartProvinceType2Values(i), start_area);
                    //保存起运地省名字
                    safePinanInfo.setStart_area_str(provinceInfoList.get(start_area.getSelectedItemPosition()).getProvince());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        start_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //组合赔偿地地址
                peichang_area.setText(provinceInfoList.get(start_area.getSelectedItemPosition()).getProvince()
                        + cityStartInfoList.get(start_city.getSelectedItemPosition()).getCity());
                //保存起运地城市名字
                safePinanInfo.setStart_city_str(cityStartInfoList.get(start_city.getSelectedItemPosition()).getCity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        end_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取详细货物类别
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
                end_city.setAdapter(arrayAdapter);
                end_city.setEnabled(false);
                if(!provinceInfoList.isEmpty()) {
                    getSafeCityAreaInfo(Constants.getPinanEndProvinceType2Values(i), end_area);
                    //保存目的地省名字
                    safePinanInfo.setEnd_area_str(provinceInfoList.get(end_area.getSelectedItemPosition()).getProvince());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        end_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //保存目的地城市名字
                safePinanInfo.setEnd_city_str(cityEndInfoList.get(end_city.getSelectedItemPosition()).getCity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //包装方式
        package_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!packageTypeList.isEmpty()) {
                    //保存包装方式名称
                    safePinanInfo.setPackage_type_str(packageTypeList.get(package_type.getSelectedItemPosition()).getDiscribe());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        start_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) {
                    showDateDialog();
                }
            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
    }

    private void initData() {
        safePinanInfo = (SafePinanInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if (safePinanInfo != null) {
//            start_date.setText(safePinanInfo.getStart_date()); //日期需要重新填写
            insured_name.setText(safePinanInfo.getInsured_name());
            insured_phone.setText(safePinanInfo.getInsured_phone());
            shiping_number.setText(safePinanInfo.getShiping_number());
            plate_number.setText(safePinanInfo.getPlate_number());
        }
        //获得平安省区域
        getSafeProvinceAreaInfo();
        //获得包装类型
        getSafeSourceType();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                startActivity(new Intent(context, SafeListActivity.class));
                break;
        }
    }

    protected void showDateDialog() {
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                hideIM(start_date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        Dialog dialog = new DatePickerDialog(context, mDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    // 隐藏手机键盘
    private void hideIM(View edt) {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {
                im.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void onClickBack(View view) {
        onBackPressed();
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void onClickNext(View view) {
        if (TextUtils.isEmpty(start_date.getText()) || TextUtils.isEmpty(insured_name.getText())
                || TextUtils.isEmpty(insured_phone.getText()) || TextUtils.isEmpty(shiping_number.getText())
                || TextUtils.isEmpty(lianxiren_name.getText()) || TextUtils.isEmpty(guache_Num.getText())
                || null == citySelectView.getSelectedProvince() || null == citySelectView.getSelectedCity()
                || TextUtils.isEmpty(plate_number.getText())) {
            Toast.makeText(context, "请填写所有需要填写的数据!", Toast.LENGTH_SHORT).show();
            return;
        }
        safePinanInfo.setLianxiren_name(lianxiren_name.getText().toString());
        safePinanInfo.setStart_date(start_date.getText().toString());
        safePinanInfo.setInsured_name(insured_name.getText().toString());
        safePinanInfo.setInsured_phone(insured_phone.getText().toString());
        safePinanInfo.setShiping_number(shiping_number.getText().toString());
        safePinanInfo.setPlate_number(plate_number.getText().toString());
        safePinanInfo.setGuache_Num(guache_Num.getText().toString());
        safePinanInfo.setProvinceListArea("" + citySelectView.getSelectedProvince().getName());
        safePinanInfo.setProvinceListCity("" + citySelectView.getSelectedCity().getName());
        if(citySelectView.getSelectedTowns() != null) {
            safePinanInfo.setDistrictList("" + citySelectView.getSelectedTowns().getName());
        }
        if(Constants.pinanSourceType2Values.length > 0) {
            safePinanInfo.setPackage_type(Constants.getPinanSafeSourceType2Values(package_type.getSelectedItemPosition()));
        }
        safePinanInfo.setStart_area(Constants.getPinanStartProvinceType2Values(start_area.getSelectedItemPosition()));
        safePinanInfo.setStart_city(Constants.getPinanStartCityType2Values(start_city.getSelectedItemPosition()));
        safePinanInfo.setEnd_area(Constants.getPinanEndProvinceType2Values(end_area.getSelectedItemPosition()));
        safePinanInfo.setEnd_city(Constants.getPinanEndCityType2Values(end_city.getSelectedItemPosition()));
        safePinanInfo.setPeichang_area(peichang_area.getText().toString());
        Intent intent = new Intent(context, SafePinanDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, safePinanInfo);
        startActivity(intent);
    }

    /**
     * 获取包装类型
     */
    List<PinganPackageTypeInfo> packageTypeList = new ArrayList<PinganPackageTypeInfo>();
    public void getSafeSourceType() {
        try {
            showSpecialProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_SAFE_PINAN_PACK_TYPE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, "");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    PinganPackageTypeInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        packageTypeList = (List<PinganPackageTypeInfo>) result;
                                        List<String> keyList = new ArrayList<String>();
                                        List<String> valuesList = new ArrayList<String>();
                                        for (PinganPackageTypeInfo info : packageTypeList) {
                                            keyList.add(info.getDiscribe());
                                            valuesList.add(info.getCode());
                                        }
                                        ArrayAdapter<String> arrayEndAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
                                        Constants.pinanSourceType2Values = valuesList.toArray(new String[valuesList.size()]);
                                        package_type.setAdapter(arrayEndAdapter);
                                        package_type.setEnabled(true);
                                        //显示包装代码
                                        if (!TextUtils.isEmpty(safePinanInfo.getPackage_type())) {
                                            for (int i = 0; i < Constants.pinanSourceType2Values.length; i++) {
                                                if (TextUtils.equals(Constants.pinanSourceType2Values[i], safePinanInfo.getPackage_type())) {
                                                    package_type.setSelection(i);
                                                    break;
                                                }
                                            }
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

    /**
     * 获取省
     */
    List<PinanAreaInfo> provinceInfoList = new ArrayList<PinanAreaInfo>();
    public void getSafeProvinceAreaInfo() {
        try {
            showSpecialProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_SAFE_PINAN_PROVINCE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, "");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    PinanAreaInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        provinceInfoList = (List<PinanAreaInfo>) result;
                                        List<String> keyList = new ArrayList<String>();
                                        List<String> valuesList = new ArrayList<String>();
                                        for (PinanAreaInfo info : provinceInfoList) {
                                            keyList.add(info.getProvince());
                                            valuesList.add(info.getPcode());
                                        }
                                        ArrayAdapter<String> arrayStartAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
                                        ArrayAdapter<String> arrayEndAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
                                        Constants.pinanStartProvinceType2Values = valuesList.toArray(new String[valuesList.size()]);
                                        Constants.pinanEndProvinceType2Values = valuesList.toArray(new String[valuesList.size()]);
                                        start_area.setAdapter(arrayStartAdapter);
                                        end_area.setAdapter(arrayEndAdapter);
                                        start_area.setEnabled(true);
                                        end_area.setEnabled(true);
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

    /**
     * 获取市
     */
    List<PinanAreaInfo> cityStartInfoList = new ArrayList<PinanAreaInfo>();
    List<PinanAreaInfo> cityEndInfoList = new ArrayList<PinanAreaInfo>();
    Map<String, List<PinanAreaInfo>> areaMap = new HashMap<String, List<PinanAreaInfo>>();
    public void getSafeCityAreaInfo(final String parentId, final Spinner spinner) {
        if (areaMap.get(parentId) != null) {
            //有数据的话直接取数据,否则去网络上取
            List<PinanAreaInfo> areaInfoList = areaMap.get(parentId);
            List<String> keyList = new ArrayList<String>();
            List<String> valuesList = new ArrayList<String>();
            for (PinanAreaInfo cityInfo : areaInfoList) {
                keyList.add(cityInfo.getCity());
                valuesList.add(cityInfo.getCitycode());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
            if(spinner == start_area) {
                cityStartInfoList = areaInfoList;
                Constants.pinanStartCityType2Values = valuesList.toArray(new String[valuesList.size()]);
                start_city.setAdapter(arrayAdapter);
                start_city.setEnabled(true);
            } else {
                cityEndInfoList = areaInfoList;
                Constants.pinanEndCityType2Values = valuesList.toArray(new String[valuesList.size()]);
                end_city.setAdapter(arrayAdapter);
                end_city.setEnabled(true);
            }
        } else {
            try {
                showSpecialProgress();
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.ACTION, Constants.GET_SAFE_PINAN_CITY);
                jsonObject.put(Constants.TOKEN, application.getToken());
                jsonObject.put(Constants.JSON, new JSONObject().put("pcode", parentId));
                ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                        PinanAreaInfo.class, new AjaxCallBack() {
                            @Override
                            public void receive(int code, Object result) {
                                dismissProgress();
                                switch (code) {
                                    case ResultCode.RESULT_OK:
                                        if (result instanceof List) {
                                            List<PinanAreaInfo> cityInfoList = (List<PinanAreaInfo>) result;
                                            areaMap.put(parentId, cityInfoList);
                                            List<String> keyList = new ArrayList<String>();
                                            List<String> valuesList = new ArrayList<String>();
                                            for (PinanAreaInfo dictInfo : cityInfoList) {
                                                keyList.add(dictInfo.getCity());
                                                valuesList.add(dictInfo.getCitycode());
                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
                                            if(spinner == start_area) {
                                                cityStartInfoList = cityInfoList;
                                                Constants.pinanStartCityType2Values = valuesList.toArray(new String[valuesList.size()]);
                                                start_city.setAdapter(arrayAdapter);
                                                start_city.setEnabled(true);
                                            } else {
                                                cityEndInfoList = cityInfoList;
                                                Constants.pinanEndCityType2Values = valuesList.toArray(new String[valuesList.size()]);
                                                end_city.setAdapter(arrayAdapter);
                                                end_city.setEnabled(true);
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
    }
}
