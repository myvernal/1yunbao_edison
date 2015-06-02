package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.adapter.InvoiceAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarrierInfo;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.ybxiang.driver.activity.MyCarsDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2014/11/11.
 */
public class InvoiceCarrierView extends LinearLayout {
    private static final String TAG = "InvoiceCarrierActivity";
    private LinearLayout qiandanLayout, priceLayout, phoneLayout;
    private View contentView;
    private TextView loadingView;
    private Context mContext;
    private InvoiceAdapter.DataCallBack callBack;

    public InvoiceCarrierView(Context context) {
        super(context);
        mContext = context;
        initUI();
    }

    public InvoiceCarrierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_invoice_carrier_layout, this, true);
        initView(view);
    }

    private void initView(View view) {
        qiandanLayout = (LinearLayout) view.findViewById(R.id.qiangdan_list_container);
        priceLayout = (LinearLayout) view.findViewById(R.id.price_list_container);
        phoneLayout = (LinearLayout) view.findViewById(R.id.phone_list_container);
        loadingView = (TextView) view.findViewById(R.id.loadingView);
        contentView = view.findViewById(R.id.contentView);
    }

    public void initData(int sourceId, List<CarrierInfo> list, MGApplication application, InvoiceAdapter.DataCallBack callBack) {
        this.callBack = callBack;
        if(list != null) {
            if (!list.isEmpty()) {
                loadingView.setVisibility(GONE);
                contentView.setVisibility(VISIBLE);
                displayData(list);
            } else {
                loadingView.setText("暂无数据");
            }
        } else {
            getData(sourceId, application);
        }
    }

    // 根据条件请求指定页数的数据
    private void getData(int orderId, MGApplication application) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_CARRIER_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("order_id", orderId).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarrierInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<CarrierInfo> mList = (List<CarrierInfo>) result;
                                        callBack.callBack(mList);
                                        if (mList != null && !mList.isEmpty()) {
                                            loadingView.setVisibility(GONE);
                                            contentView.setVisibility(VISIBLE);
                                            LayoutInflater inflater = LayoutInflater.from(mContext);
                                            displayData(mList);
                                        } else {
                                            loadingView.setText("暂无数据");
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        loadingView.setText(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
                                        loadingView.setText(result.toString());
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

    private void displayData(List<CarrierInfo> mList) {
        qiandanLayout.removeAllViews();
        priceLayout.removeAllViews();
        phoneLayout.removeAllViews();
        //遍历数据
        for (final CarrierInfo carrierInfo : mList) {
            if (TextUtils.equals("Y", carrierInfo.getIs_grab_single_car())) {
                TextView textView = new TextView(mContext);
                textView.setTextAppearance(mContext, R.style.CarrierItemStyle);
                textView.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t抢单");
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
                        mContext.startActivity(intent.putExtra(Constants.COMMON_KEY, carrierInfo.getDriver_id()).putExtra(Constants.COMMON_BOOLEAN_KEY, true));
                    }
                });
                qiandanLayout.addView(textView);
            }
            if (TextUtils.equals("Y", carrierInfo.getIs_phone_car())) {
                TextView textView = new TextView(mContext);
                textView.setTextAppearance(mContext, R.style.CarrierItemStyle);
                textView.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t电话");
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
                        mContext.startActivity(intent.putExtra(Constants.COMMON_KEY, carrierInfo.getDriver_id()).putExtra(Constants.COMMON_BOOLEAN_KEY, true));
                    }
                });
                phoneLayout.addView(textView);
            }
            if (TextUtils.equals("Y", carrierInfo.getIs_price_car())) {
                TextView textView = new TextView(mContext);
                textView.setTextAppearance(mContext, R.style.CarrierItemStyle);
                textView.setText(carrierInfo.getName() + "\t" + carrierInfo.getPhone() + "\t" + mContext.getString(R.string.agreement_carrier_price, carrierInfo.getDriver_price()));
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
                        mContext.startActivity(intent.putExtra(Constants.COMMON_KEY, carrierInfo.getDriver_id()).putExtra(Constants.COMMON_BOOLEAN_KEY, true));
                    }
                });
                priceLayout.addView(textView);
            }
        }
    }
}
