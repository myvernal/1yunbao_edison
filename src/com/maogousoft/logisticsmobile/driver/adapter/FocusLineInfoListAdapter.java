package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.NewSourceActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注路线的的adapter 省市区 
 * 
 * @author ybxiang
 * 
 */
public class FocusLineInfoListAdapter extends BaseListAdapter<FocusLineInfo> {

    private CityDBUtils dbUtils;
    
	public FocusLineInfoListAdapter(Context context) {
		super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
	}
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
		if (convertView == null) {
            holder = new Holder();
			convertView = mInflater.inflate(R.layout.listitem_focusline_info, parent, false);
            holder.wayBegin = (TextView) convertView.findViewById(R.id.wayBegin);
            holder.wayEnd = (TextView) convertView.findViewById(R.id.wayEnd);
            holder.deleteView = convertView.findViewById(R.id.delete_btn);
		} else {
            holder = (Holder) convertView.getTag();
        }
        final FocusLineInfo focusLineInfo = mList.get(position);
        holder.wayBegin.setText(dbUtils.getCityInfo(focusLineInfo.getStart_province(), focusLineInfo.getStart_city(), focusLineInfo.getStart_district()));
        holder.wayEnd.setText(dbUtils.getCityInfo(focusLineInfo.getEnd_province(), focusLineInfo.getEnd_city(), focusLineInfo.getEnd_district()));
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFocusLine(focusLineInfo);
            }
        });
        convertView.setTag(R.id.common_city_selected, focusLineInfo);
		return convertView;
	}

    private void fastSearch(final FocusLineInfo focusLineInfo) {
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            // 搜索新货源
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
            jsonObject.put(Constants.TOKEN, application.getToken());
            params.put("start_province", focusLineInfo.getStart_province());
            params.put("end_province", focusLineInfo.getStart_city());
            params.put("start_city", focusLineInfo.getStart_district());
            params.put("end_city", focusLineInfo.getEnd_province());
            params.put("start_district", focusLineInfo.getEnd_city());
            params.put("end_district", focusLineInfo.getEnd_district());
            params.put("ship_type", 0);
            params.put("device_type", Constants.DEVICE_TYPE);
            jsonObject.put(Constants.JSON, params);
            showProgress("正在查找货源...");
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
                                            Intent intent = new Intent(mContext, NewSourceActivity.class);
                                            intent.putExtra("NewSourceInfos", (Serializable) mList);
                                            intent.putExtra("focuseLineInfo", focusLineInfo);
                                            mContext.startActivity(intent);
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
                                        final MyAlertDialog dialog = new MyAlertDialog(mContext);
                                        dialog.show();
                                        dialog.setTitle("提示");
                                        // 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
                                        dialog.setMessage(result.toString());
                                        dialog.setLeftButton("确定",
                                                new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();

                                                        String content = null;
                                                        mContext.startActivity(new Intent(mContext,
                                                                ShareActivity.class).putExtra("share", content));
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
     * 删除已关注线路
     * @param info
     */
    private void deleteFocusLine(final FocusLineInfo info) {
        final JSONObject jsonObject = new JSONObject();
        try {
            // 搜索新货源
            jsonObject.put(Constants.ACTION, Constants.DELETE_ALL_FOCUS_LINE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("id", info.getId()).toString());
            showProgress("正在删除...");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    FocusLineInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        showMsg("删除成功");
                                        mList.remove(info);
                                    }
                                    notifyDataSetChanged();
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

    class Holder {
        private TextView wayBegin;
        private TextView wayEnd;
        private View deleteView;
    }
}
