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
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.model.SafeSeaInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by aliang on 2014/8/29.
 */
public class SafeSeaEditActivity extends BaseActivity {

    private Button mTitleBarMore;
    private EditText start_date, insurer_name, insured_name, insurer_phone,
            shiping_number, packet_number, ship_type, ship_tool,
            plate_number, start_area, end_area;
    private Spinner package_type, cargo_type1, cargo_type2;
    private SafeSeaInfo safeSeaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_sea_edit_layout);
        initViews();
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("中国太平洋保险国内货运险");
        // 返回按钮生效

        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("保险记录");
        mTitleBarMore.setOnClickListener(this);

        start_date = (EditText) findViewById(R.id.start_date);
        insurer_name = (EditText) findViewById(R.id.insurer_name);
        insured_name = (EditText) findViewById(R.id.insured_name);
        insurer_phone = (EditText) findViewById(R.id.insurer_phone);
        shiping_number = (EditText) findViewById(R.id.shiping_number);
        packet_number = (EditText) findViewById(R.id.packet_number);
        ship_type = (EditText) findViewById(R.id.ship_type);
        ship_tool = (EditText) findViewById(R.id.ship_tool);
        plate_number = (EditText) findViewById(R.id.plate_number);
        start_area = (EditText) findViewById(R.id.start_area);
        end_area = (EditText) findViewById(R.id.end_area);
        start_date = (EditText) findViewById(R.id.start_date);

        package_type = (Spinner) findViewById(R.id.package_type);
        cargo_type1 = (Spinner) findViewById(R.id.cargo_type1);
        cargo_type2 = (Spinner) findViewById(R.id.cargo_type2);

        cargo_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //获取详细货物类别
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
                cargo_type2.setAdapter(arrayAdapter);
                cargo_type2.setEnabled(false);
                getSafeCargoType(Constants.getSeaSafeSourceTypeValues(i));
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
        safeSeaInfo = (SafeSeaInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if (safeSeaInfo != null) {
//            start_date.setText(safeSeaInfo.getStart_date()); //日期需要重新填写
            insurer_name.setText(safeSeaInfo.getInsurer_name());
            insured_name.setText(safeSeaInfo.getInsured_name());
            insurer_phone.setText(safeSeaInfo.getInsurer_phone());
            shiping_number.setText(safeSeaInfo.getShiping_number());
            packet_number.setText(safeSeaInfo.getPacket_number());
            ship_type.setText(safeSeaInfo.getShip_type());
            ship_tool.setText(safeSeaInfo.getShip_tool());
            plate_number.setText(safeSeaInfo.getPlate_number());
            start_area.setText(safeSeaInfo.getStart_area());
            end_area.setText(safeSeaInfo.getEnd_area());
            //显示包装代码
            if(safeSeaInfo.getPackage_type() != null) {
                for (int i = 0; i < Constants.seaSafeBZDMTypeValues.length; i++) {
                    if (Constants.seaSafeBZDMTypeValues[i] == safeSeaInfo.getPackage_type()) {
                        package_type.setSelection(i);
                        break;
                    }
                }
            }
            //显示货运类型1
            if(safeSeaInfo.getCargo_type1() > 0) {
                for (int i = 0; i < Constants.seaSafeSourceTypeValues.length; i++) {
                    if (Constants.seaSafeSourceTypeValues[i] == safeSeaInfo.getCargo_type1()) {
                        cargo_type1.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                Intent intent = new Intent(context, SafeListActivity.class);
                intent.putExtra(Constants.COMMON_KEY, Constants.SAFE_CPIC);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取第二条货物类别
     */
    Map<Integer, List<DictInfo>> cargoType2 = new HashMap<Integer, List<DictInfo>>();
    public void getSafeCargoType(final int parentId) {
        if(cargoType2.get(parentId) != null) {
            //有数据的话直接取数据,否则去网络上取
            List<DictInfo> dictInfoList = cargoType2.get(parentId);
            List<String> keyList = new ArrayList<String>();
            List<Integer> valuesList = new ArrayList<Integer>();
            for (DictInfo dictInfo : dictInfoList) {
                keyList.add(dictInfo.getName());
                valuesList.add(dictInfo.getId());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
            Constants.seaSafeSourceType2Values = valuesList.toArray(new Integer[valuesList.size()]);
            cargo_type2.setAdapter(arrayAdapter);
        } else {
            try {
                showSpecialProgress();
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.ACTION, Constants.GET_SAFE_SEA_CARGO_2);
                jsonObject.put(Constants.TOKEN, application.getToken());
                jsonObject.put(Constants.JSON, new JSONObject().put("pid", parentId));
                ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                        DictInfo.class, new AjaxCallBack() {
                            @Override
                            public void receive(int code, Object result) {
                                dismissProgress();
                                switch (code) {
                                    case ResultCode.RESULT_OK:
                                        if (result instanceof List) {
                                            List<DictInfo> dictInfoList = (List<DictInfo>) result;
                                            cargoType2.put(parentId, dictInfoList);
                                            List<String> keyList = new ArrayList<String>();
                                            List<Integer> valuesList = new ArrayList<Integer>();
                                            for (DictInfo dictInfo : dictInfoList) {
                                                keyList.add(dictInfo.getName());
                                                valuesList.add(dictInfo.getId());
                                            }
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keyList);
                                            Constants.seaSafeSourceType2Values = valuesList.toArray(new Integer[valuesList.size()]);
                                            cargo_type2.setAdapter(arrayAdapter);
                                            cargo_type2.setEnabled(true);
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
        if(TextUtils.isEmpty(start_date.getText()) || TextUtils.isEmpty(insurer_name.getText()) ||
                TextUtils.isEmpty(insured_name.getText()) || TextUtils.isEmpty(shiping_number.getText()) ||
                TextUtils.isEmpty(packet_number.getText()) || TextUtils.isEmpty(ship_type.getText()) || TextUtils.isEmpty(ship_tool.getText()) ||
                TextUtils.isEmpty(plate_number.getText()) || TextUtils.isEmpty(start_area.getText()) || TextUtils.isEmpty(end_area.getText()) ) {
            Toast.makeText(context, "请填写所有需要填写的数据!", Toast.LENGTH_SHORT).show();
            return;
        }
        safeSeaInfo.setStart_date(start_date.getText().toString());
        safeSeaInfo.setInsurer_name(insurer_name.getText().toString());
        safeSeaInfo.setInsured_name(insured_name.getText().toString());
        safeSeaInfo.setInsurer_phone(insurer_phone.getText().toString());
        safeSeaInfo.setShiping_number(shiping_number.getText().toString());
        safeSeaInfo.setPacket_number(packet_number.getText().toString());
        safeSeaInfo.setShip_type(ship_type.getText().toString());
        safeSeaInfo.setShip_tool(ship_tool.getText().toString());
        safeSeaInfo.setPlate_number(plate_number.getText().toString());
        safeSeaInfo.setStart_area(start_area.getText().toString());
        safeSeaInfo.setEnd_area(end_area.getText().toString());
        safeSeaInfo.setCargo_type1(Constants.getSeaSafeSourceTypeValues(cargo_type1.getSelectedItemPosition()));
        safeSeaInfo.setCargo_type2(Constants.getSeaSafeSourceType2Values(cargo_type2.getSelectedItemPosition()));
        safeSeaInfo.setPackage_type(Constants.getSeaSafeBZDMTypeValues(package_type.getSelectedItemPosition()));
        Intent intent = new Intent(context, SafeSeaDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, safeSeaInfo);
        startActivity(intent);
    }
}
