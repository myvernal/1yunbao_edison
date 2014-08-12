package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

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
            holder.wayView = (TextView) convertView.findViewById(R.id.wayId);
            holder.updateView = convertView.findViewById(R.id.update_btn);
            holder.deleteView = convertView.findViewById(R.id.delete_btn);
		} else {
            holder = (Holder) convertView.getTag();
        }
        final FocusLineInfo focusLineInfo = mList.get(position);
        String way = String.format(mContext.getString(R.string.string_evaluate_line),
                dbUtils.getCityInfo(focusLineInfo.getStart_province(), focusLineInfo.getStart_city(), focusLineInfo.getStart_district()),
                dbUtils.getCityInfo(focusLineInfo.getEnd_province(), focusLineInfo.getEnd_city(), focusLineInfo.getEnd_district()));
        holder.wayView.setText(way);
        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFocusLine(focusLineInfo);
            }
        });
		return convertView;
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
            jsonObject.put(Constants.JSON, "id=" + info.getId());
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
        private TextView wayView;
        private View updateView;
        private View deleteView;
    }
}
