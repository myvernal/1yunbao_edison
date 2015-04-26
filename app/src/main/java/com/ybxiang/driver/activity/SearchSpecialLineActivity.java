package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.FocusLineAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2014/9/5.
 */
public class SearchSpecialLineActivity extends BaseActivity {

    private Button mQuery;
    private CitySelectView citySelectStart;
    private CitySelectView citySelectEnd;
    private CityDBUtils dbUtils;
    private BaseListAdapter mAdapter;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_special_line_layout);
        initViews();
        initUtils();
        initData();
    }

    // 初始化视图
    private void initViews() {

        ((TextView) findViewById(R.id.titlebar_id_content)).setText("零担专线");
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
        mQuery = (Button) findViewById(R.id.query);

        citySelectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        citySelectEnd = (CitySelectView) findViewById(R.id.cityselect_end);
        mGridView = (GridView) findViewById(R.id.focus_line_gridview);
        mQuery.setOnClickListener(this);

        //关注线路
        mAdapter = new FocusLineAdapter(mContext);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FocusLineInfo focusLineInfo = (FocusLineInfo) view.getTag(R.id.focus_line_key);
                fastSearchSpecialLine(focusLineInfo);
            }
        });
        mGridView.setAdapter(mAdapter);
    }

    private void initUtils() {
        dbUtils = new CityDBUtils(application.getCitySDB());
    }

    private void initData() {
        queryAllFocusLine();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query:
                if (citySelectStart.getSelectedCity() == null || citySelectEnd.getSelectedCity() == null) {
                    Toast.makeText(mContext, "请至少选择到城市一级", Toast.LENGTH_SHORT).show();
                    return;
                }
                final JSONObject params = new JSONObject();
                try {
                    params.put("area_start", citySelectStart.getSelectedProvince() == null ? "" : citySelectStart.getSelectedProvince().getName());
                    params.put("area_end", citySelectEnd.getSelectedProvince() == null ? "" : citySelectEnd.getSelectedProvince().getName());
                    params.put("city_start", citySelectStart.getSelectedCity() == null ? "" : citySelectStart.getSelectedCity().getName());
                    params.put("city_end", citySelectEnd.getSelectedCity() == null ? "" : citySelectEnd.getSelectedCity().getName());
                    params.put("distict_start", citySelectStart.getSelectedTowns() == null ? "" : citySelectStart.getSelectedTowns().getName());
                    params.put("distict_end", citySelectEnd.getSelectedTowns() == null ? "" : citySelectEnd.getSelectedTowns().getName());
                    params.put("page", 1);
                    params.put("device_type", Constants.DEVICE_TYPE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(mContext, SearchDPListActivity.class);
                intent.putExtra(Constants.COMMON_KEY, params.toString());
                intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_SPECIAL_LINE);
                startActivity(intent);
                break;
        }
    }


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
                                                    //显示用
                                                    focusLineInfo.setStart_str(start);
                                                    focusLineInfo.setEnd_str(end);
                                                    //查询用
                                                    focusLineInfo.setStart_area_str(dbUtils.getCityInfo(focusLineInfo.getStart_province()));
                                                    focusLineInfo.setEnd_area_str(dbUtils.getCityInfo(focusLineInfo.getEnd_province()));
                                                    focusLineInfo.setStart_city_str(dbUtils.getCityInfo(focusLineInfo.getStart_city()));
                                                    focusLineInfo.setEnd_city_str(dbUtils.getCityInfo(focusLineInfo.getEnd_city()));
                                                }
                                                //刷新数据
                                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
                                                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                                        int size = mContext.getResources().getDimensionPixelSize(R.dimen.focus_line_item_height);
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
        Intent intent = new Intent(mContext, FocusLineInfoActivity.class);
        intent.putExtra(Constants.COMMON_KEY, Constants.SOURCE_SEARCH_TYPE_SPECIAL);
        startActivity(intent);
    }
}
