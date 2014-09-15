package com.maogousoft.logisticsmobile.driver.activity.home;
// PR203 找车【三方】

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.CarCloudSearchActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.FocusLineAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.ybxiang.driver.activity.CarsListActivity;
import com.ybxiang.driver.activity.FocusLineInfoActivity;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 找车
 *
 * @author ybxiang
 */
public class SearchCarSourceActivity extends BaseActivity {
    private Button mBack, mSubmit, mRightButton;
    private CitySelectView citySelectStart, citySelectEnd;
    private GridView mGridView;
    private Spinner carTypeSpinner;
    private EditText edtCarLength;
    private ArrayList<HashMap<String, Object>> listCarTypes = new ArrayList<HashMap<String, Object>>();// 车辆类型集合
    private int selectedCarType = 0;// 选择的车辆类型
    private CityDBUtils dbUtils;
    private BaseListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_car_source);
        initViews();
        initData();
        queryAllFocusLine();
    }

    // 初始化视图
    private void initViews() {
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("查找车源");
        mRightButton = ((Button) findViewById(R.id.titlebar_id_more));
        mRightButton.setText("附近车源");
        carTypeSpinner = (Spinner) findViewById(R.id.search_car_type);
        edtCarLength = (EditText) findViewById(R.id.edt_search_source_car_length);
        mGridView = (GridView) findViewById(R.id.focus_line_gridview);
        mSubmit = (Button) findViewById(R.id.search_source__submit);

        citySelectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        citySelectEnd = (CitySelectView) findViewById(R.id.cityselect_end);

        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        //关注线路
        mAdapter = new FocusLineAdapter(context);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FocusLineInfo focusLineInfo = (FocusLineInfo) view.getTag(R.id.focus_line_key);
                fastSearchCar(focusLineInfo);
            }
        });
        mGridView.setAdapter(mAdapter);
    }

    // 初始化数据，获取车型
    private void initData() {
        dbUtils = new CityDBUtils(application.getCitySDB());
    }

    // add for PR1.3 begin
    /**
     * 运输方式：零担或者整车，二者必选其一
     *
     * @param view
     */
    public void onChooseCarWay(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运输方式【零担，整车】");
            return;
        }
        switch (view.getId()) {
            case R.id.car_way_part:
                // 空车
                selectedCarType = 1;
                break;
            case R.id.car_way_whole:
                selectedCarType = 0;
                // 不限
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        switch (id) {

            case R.id.search_source__submit:
                // 先检测是否已经完善了资料
                if (application.checkIsRegOptional()) {
                    submit();
                } else {
                    final MyAlertDialog dialog = new MyAlertDialog(context);
                    dialog.show();
                    dialog.setTitle("提示");
                    dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
                    dialog.setLeftButton("完善资料", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context,
                                    OptionalActivity.class);
                            intent.putExtra("isFormRegisterActivity", false);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.setRightButton("取消", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }

                break;
            // PR206 附近车源 begin
            case R.id.titlebar_id_more:
//                Intent nearbyCarSourceIntent = new Intent();
//                nearbyCarSourceIntent.setClass(context, NearbyCarSourceActivity.class);
//                startActivity(nearbyCarSourceIntent);
                startActivity(new Intent(context, CarCloudSearchActivity.class));
                break;
            // PR206 附近车源 end
            default:
                break;
        }
    }

    private void submit() {
        if (citySelectStart.getSelectedProvince() == null || citySelectEnd.getSelectedProvince() == null) {
            showMsg("请选择出发地，目的地。");
            return;
        }
        final JSONObject params = new JSONObject();
        try {
            params.put("start_province", citySelectStart.getSelectedProvince().getId());
            params.put("end_province", citySelectEnd.getSelectedProvince().getId());
            if (citySelectStart.getSelectedCity() != null) {
                params.put("start_city", citySelectStart.getSelectedCity().getId());
            }
            if (citySelectEnd.getSelectedCity() != null) {
                params.put("end_city", citySelectEnd.getSelectedCity().getId());
            }
            if (citySelectStart.getSelectedTowns() != null) {
                params.put("start_district", citySelectStart.getSelectedTowns().getId());
            }
            if (citySelectEnd.getSelectedTowns() != null) {
                params.put("end_district", citySelectEnd.getSelectedTowns().getId());
            }
            //1 空车 0 不限
            if (selectedCarType != 0) {
                params.put("isEmpty", selectedCarType);
            }
            if (!TextUtils.isEmpty(edtCarLength.getText())) {
                params.put("car_length", edtCarLength.getText().toString());
            }
            params.put("page", 1);
            params.put("device_type", Constants.DEVICE_TYPE);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, CarsListActivity.class);
        intent.putExtra(Constants.COMMON_KEY, params.toString());
        intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_CAR_SOURCE);
        //将搜索条件传下去
        context.startActivity(intent);
    }
    // add for PR1.3  begin

    // 搜索全部已关注线路
    private void queryAllFocusLine() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.QUERY_ALL_FOCUS_LINE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("type", application.getUserType()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    FocusLineInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        final List<FocusLineInfo> focusLineInfoList = (List<FocusLineInfo>) result;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //解析数据
                                                for (FocusLineInfo focusLineInfo : focusLineInfoList) {
                                                    String start = dbUtils.getCityInfo(focusLineInfo.getStart_province());
                                                    String end = dbUtils.getCityInfo(focusLineInfo.getEnd_province());
                                                    if (focusLineInfo.getStart_city() > 0) {
                                                        start = dbUtils.getCityInfo(focusLineInfo.getStart_city());
                                                        if (focusLineInfo.getStart_district() > 0) {
                                                            start = dbUtils.getCityInfo(focusLineInfo.getStart_district());
                                                        }
                                                    }
                                                    if (focusLineInfo.getEnd_city() > 0) {
                                                        end = dbUtils.getCityInfo(focusLineInfo.getEnd_city());
                                                        if (focusLineInfo.getEnd_district() > 0) {
                                                            end = dbUtils.getCityInfo(focusLineInfo.getEnd_district());
                                                        }
                                                    }
                                                    focusLineInfo.setStart_str(start);
                                                    focusLineInfo.setEnd_str(end);
                                                }
                                                //刷新数据
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
                                                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                                        int size = context.getResources().getDimensionPixelSize(R.dimen.focus_line_item_height);
                                                        int row = (focusLineInfoList.size() / 2) + (focusLineInfoList.size() % 2);
                                                        lp.height = size * row * 2;
                                                        mAdapter.addAll(focusLineInfoList);
                                                        mAdapter.notifyDataSetChanged();
                                                        mGridView.setLayoutParams(lp);
                                                        mGridView.invalidate();
                                                    }
                                                });
                                            }
                                        }).start();
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
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 已关注路线的快捷设置
     */
    public void onFastSetting(View view) {
        Intent intent = new Intent(context, FocusLineInfoActivity.class);
        intent.putExtra(Constants.COMMON_KEY, Constants.CAR_SEARCH_TYPE);
        startActivity(intent);
    }

}
