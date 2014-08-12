package com.maogousoft.logisticsmobile.driver.activity.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.widget.*;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.FocuseLineInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.ybxiang.driver.activity.FocusLineInfoActivity;

/**
 * 查找 新货源
 *
 * @author lenovo
 */
public class SearchSourceActivity extends BaseActivity {

    private static final String LOGTAG = LogUtil
            .makeLogTag(SearchSourceActivity.class);

    private Button mBack;
    private Button mTitleBarMore;

    private Button mSubmit;

    private CitySelectView cityselectStart, cityselectEnd;

    private Spinner carTypeSpinner;
    private Spinner edtCarLength;
    private LinearLayout focus_line_container;
    private int car_type = 43; // 车型 ：
    private int ship_type = 0; // 运输方式： 0 不限 1整车 2 零担
    private Context mContext;
    private CityDBUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_home_search_source);
        initViews();
        dbUtils = new CityDBUtils(application.getCitySDB());
        queryAllFocusLine();
    }

    // 初始化视图
    private void initViews() {
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("查找货源");
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setVisibility(View.GONE);
        carTypeSpinner = (Spinner) findViewById(R.id.search_car_type);
        edtCarLength = (Spinner) findViewById(R.id.edt_search_source_carlength);

        mSubmit = (Button) findViewById(R.id.search_source__submit);

        cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);
        focus_line_container = (LinearLayout) findViewById(R.id.focus_line_container);
        mBack.setOnClickListener(this);
        mTitleBarMore.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        // 获取车型
        carTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                car_type = Constants.getCarTypeValues(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        setIsRightKeyIntoShare(false);// 设置分享按钮失效
        super.onClick(v);
        final int id = v.getId();
        switch (id) {
            // 长期货源
            case R.id.titlebar_id_more:
                // 参数1代表长期货源
                search(1);
                break;
            case R.id.search_source__submit:
                // 参数0代表短期货源
                search(0);
                break;

            default:
                break;
        }
    }

    private void search(int validateDateLong) {
        // 先检测是否已经完善了资料
        if (application.checkIsRegOptional()) {
            // 参数0代表短期货源
            submit(validateDateLong);
        } else {
            final MyAlertDialog dialog = new MyAlertDialog(context);
            dialog.show();
            dialog.setTitle("提示");
            dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
            dialog.setLeftButton("完善资料", new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, OptionalActivity.class);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryAllFocusLine();
    }

    private void submit(int validateDateLong) {
        // 计算路线的开始ID,结束ID
        final FocuseLineInfo focuseLineInfo = new FocuseLineInfo();
        if (cityselectStart.getSelectedProvince() == null
                | cityselectEnd.getSelectedProvince() == null) {
            showMsg("请选择出发地，目的地。");
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            // 搜索新货源
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
            jsonObject.put(Constants.TOKEN, application.getToken());
            // 添加长期货源
            if (validateDateLong == 1) {
                params.put("validateDateLong", 1);
            }

            params.put("start_province", cityselectStart.getSelectedProvince()
                    .getId());
            focuseLineInfo.setStart_province(cityselectStart
                    .getSelectedProvince().getId());
            params.put("end_province", cityselectEnd.getSelectedProvince()
                    .getId());
            focuseLineInfo.setEnd_province(cityselectEnd.getSelectedProvince()
                    .getId());
            if (cityselectStart.getSelectedCity() != null) {
                params.put("start_city", cityselectStart.getSelectedCity()
                        .getId());
                focuseLineInfo.setStart_city(cityselectStart.getSelectedCity()
                        .getId());
            }

            if (cityselectEnd.getSelectedCity() != null) {
                params.put("end_city", cityselectEnd.getSelectedCity().getId());
                focuseLineInfo.setEnd_city(cityselectEnd.getSelectedCity()
                        .getId());
            }

            if (cityselectStart.getSelectedTowns() != null) {
                params.put("start_district", cityselectStart.getSelectedTowns()
                        .getId());
                focuseLineInfo.setStart_district(cityselectStart
                        .getSelectedTowns().getId());
            }

            if (cityselectEnd.getSelectedTowns() != null) {
                params.put("end_district", cityselectEnd.getSelectedTowns()
                        .getId());
                focuseLineInfo.setEnd_district(cityselectEnd.getSelectedTowns()
                        .getId());
            }
            // 车型
            if (car_type != 43) {
                params.put("car_type", car_type);
            }
            // add 车长范围
            if (edtCarLength.getSelectedItemPosition() != 0) {
                params.put("car_length", edtCarLength.getSelectedItemPosition());
            }
            params.put("ship_type", ship_type); // add 运输方式
            params.put("device_type", Constants.DEVICE_TYPE);
            jsonObject.put(Constants.JSON, params);
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<NewSourceInfo> mList = (ArrayList<NewSourceInfo>) result;

                                        if (mList.size() != 0) {
                                            Intent intent = new Intent(context,
                                                    NewSourceActivity.class);
                                            intent.putExtra("isFromHomeActivity",
                                                    true);
                                            intent.putExtra("NewSourceInfos",
                                                    (Serializable) mList);
                                            intent.putExtra("focuseLineInfo", focuseLineInfo);
                                            context.startActivity(intent);
                                        } else {
                                            showMsg("暂无满足条件的信息，请扩大搜索范围再试。");
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());

                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null) {
                                        // 您当月的免费搜索次数已经用完

                                        // if (result.equals("您当月的免费搜索次数已经用完")) {
                                        final MyAlertDialog dialog = new MyAlertDialog(
                                                context);
                                        dialog.show();
                                        dialog.setTitle("提示");
                                        // 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
                                        dialog.setMessage(result.toString());
                                        dialog.setLeftButton("确定",
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();

                                                        String content = null;
                                                        startActivity(new Intent(
                                                                context,
                                                                ShareActivity.class)
                                                                .putExtra("share",
                                                                        content));
                                                        finish();
                                                    }
                                                });

                                        // }
                                    }
                                    // showMsg(result.toString());
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
     * 运输方式
     *
     * @param view
     */
    public void onChooseCarWay(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运输方式【零担，整车，不限】");
            return;
        }
        switch (view.getId()) {
            case R.id.car_way_part:
                // 零担
                ship_type = 2;
                break;
            case R.id.car_way_whole:
                ship_type = 1;
                // 整车
                break;
            case R.id.car_way_all:
                ship_type = 0;
                // 不限
                break;
        }
    }

    private void queryAllFocusLine() {
        final JSONObject jsonObject = new JSONObject();
        try {
            // 搜索新货源
            jsonObject.put(Constants.ACTION, Constants.QUERY_ALL_FOCUS_LINE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, "");
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    FocusLineInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<FocusLineInfo> focusLineInfoList = (List<FocusLineInfo>) result;
                                        focus_line_container.removeAllViews();
                                        for(FocusLineInfo focuseLineInfo:focusLineInfoList) {
                                            TextView textView = new TextView(mContext);
                                            textView.setBackgroundResource(R.drawable.bg_button);
                                            String way = String.format(getResources().getString(R.string.string_evaluate_line),
                                                    dbUtils.getCityInfo(focuseLineInfo.getStart_province(), focuseLineInfo.getStart_city(), focuseLineInfo.getStart_district()),
                                                    dbUtils.getCityInfo(focuseLineInfo.getEnd_province(), focuseLineInfo.getEnd_city(), focuseLineInfo.getEnd_district()));
                                            textView.setText(way);
                                            focus_line_container.addView(textView);
                                        }
                                    }
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
        startActivity(new Intent(mContext, FocusLineInfoActivity.class));
    }

}
