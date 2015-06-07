package com.maogousoft.logisticsmobile.driver.adapter;
// PR104

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.OfferDriverActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * 新货源数据适配器
 *
 * @author lenovo
 */
public class NewSourceListAdapter extends BaseListAdapter<NewSourceInfo> {

    private MyProgressDialog progressDialog;
    private CityDBUtils dbUtils;

    public NewSourceListAdapter(Context context) {
        super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
        progressDialog = new MyProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_newsoure, parent, false);
            holder = new ViewHolder();
            holder.source_detail_phone = convertView.findViewById(R.id.source_detail_phone);
            holder.order_info = (TextView) convertView.findViewById(R.id.source_id_order_info);
            holder.order_info_detail = (TextView) convertView.findViewById(R.id.source_id_order_info_detail);
            holder.order_money = (TextView) convertView.findViewById(R.id.source_id_order_money);
            holder.tvDjs = (TextView) convertView.findViewById(R.id.tv_djs);
            holder.source_qiangdan = convertView.findViewById(R.id.source_id_order_grab);
            holder.source_baojia = convertView.findViewById(R.id.source_id_order_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewSourceInfo sourceInfo = (NewSourceInfo) getItem(position);
        final StringBuilder title = new StringBuilder();
        final StringBuilder detail = new StringBuilder();

        String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        if (sourceInfo.getEnd_province() > 0 || sourceInfo.getEnd_city() > 0 || sourceInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
            title.append(wayStart + "--" + wayEnd);
        }

        detail.append(sourceInfo.getCargo_type_str());

        if (sourceInfo.getCargo_number() != null && sourceInfo.getCargo_number() != 0) {
            detail.append("/")
                    .append(sourceInfo.getCargo_desc())
                    .append(sourceInfo.getCargo_number())
                    .append(CheckUtils.getCargoUnitName(mContext, sourceInfo.getCargo_unit()));
        }

        if (!TextUtils.isEmpty(sourceInfo.getCar_type_str())) {
            detail.append("/");
            detail.append(sourceInfo.getCar_type_str());

        }

        if (sourceInfo.getCar_length() != null && sourceInfo.getCar_length() != 0
                && sourceInfo.getCar_length() != 0.0) {
            detail.append("/");
            detail.append(sourceInfo.getCar_length() + "米");
        }

        holder.order_info.setText(title.toString());
        holder.order_info_detail.setText(detail.toString());
        holder.order_money.setText(Html.fromHtml(String.format(mResources
                        .getString(R.string.string_home_newsource_order_money),
                sourceInfo.getUser_bond())));

        Date date = new Date();
        String betweenTime = "刚发布";
        if (date.getTime() > sourceInfo.getCreate_time()) {
            long time = date.getTime() - sourceInfo.getCreate_time();
            long hour = time / (60 * 60 * 1000);
            long minites = (time % (60 * 60 * 1000)) / (60 * 1000);
            betweenTime = hour + "时" + minites + "分";
        }
        holder.tvDjs.setText(betweenTime + "前");
        //电话号码
        holder.source_detail_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = sourceInfo.getCargo_user_phone();
                if (!TextUtils.isEmpty(phoneStr)) {
                    //设置货主电话号码
                    Constants.CALL_NUMBER_SOURCE_ORDER_ID = sourceInfo.getId();
                    Constants.CALL_NUMBER_SOURCE = phoneStr;
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    mContext.startActivity(intent);
                }
            }
        });

        if (Integer.parseInt(application.getUserId()) == sourceInfo.getUser_id()) {
            holder.source_qiangdan.setVisibility(View.GONE);
            holder.source_baojia.setVisibility(View.GONE);
            holder.source_detail_phone.setVisibility(View.GONE);
        } else {
            holder.source_qiangdan.setVisibility(View.VISIBLE);
            holder.source_baojia.setVisibility(View.VISIBLE);
            holder.source_detail_phone.setVisibility(View.VISIBLE);
        }
        holder.source_qiangdan.setOnClickListener(new ClickListener(sourceInfo));
        holder.source_baojia.setOnClickListener(new ClickListener(sourceInfo));
        return convertView;
    }

    class ClickListener implements OnClickListener {

        private NewSourceInfo sourceInfo;

        public ClickListener(NewSourceInfo sourceInfo) {
            this.sourceInfo = sourceInfo;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.source_id_order_grab) {
                //抢单
                placeOrder(sourceInfo, (TextView) v);
            } else if(v.getId() == R.id.source_id_order_state) {
                //报价
                Intent intent = new Intent(mContext, OfferDriverActivity.class);
                intent.putExtra(Constants.COMMON_KEY, sourceInfo);
                mContext.startActivity(intent);
            }
        }
    }

    static class ViewHolder {
        TextView order_info, order_info_detail, order_money, tvDjs;
        View source_detail_phone, source_qiangdan, source_baojia;
    }

    /**
     * 抢单
     */
    public void placeOrder(NewSourceInfo mSourceInfo, final TextView view) {
        final JSONObject params = new JSONObject();
        try {
            params.put(Constants.ACTION, Constants.PLACE_SOURCE_ORDER);
            params.put(Constants.TOKEN, application.getToken());
            params.put(Constants.JSON, new JSONObject().put("order_id", mSourceInfo.getId()).toString());
            showProgress(mResources.getString(R.string.tips_sourcedetail_qiang));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params, null,
                    new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg(R.string.tips_sourcedetail_qiang_success);
                                    view.setText(R.string.tips_sourcedetail_qiang_over);
                                    view.setTextColor(mContext.getResources().getColor(R.color.title_bg));
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
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

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
