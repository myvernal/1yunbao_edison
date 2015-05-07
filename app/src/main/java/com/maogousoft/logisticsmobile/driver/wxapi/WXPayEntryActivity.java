package com.maogousoft.logisticsmobile.driver.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.PayOrderResult;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.PrepayOrderResult;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.WeChatPayConstants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_wechat_result);
        api = WXAPIFactory.createWXAPI(mContext, WeChatPayConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    //成功
                    if(resp instanceof PayResp) {
                        PayResp payResp = (PayResp) resp;
                        getPayResult(payResp.prepayId);
                    }
                    break;
                case -1:
                    if(resp instanceof PayResp) {
                        PayResp payResp = (PayResp) resp;
                        getPayResult(payResp.prepayId);
                    }
                    //错误
                    break;
                case -2:
                    //用户取消
                    finish();
                    break;
            }
        }
    }

    private void getPayResult(String prepayId) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.WEIXIN_QUERY_PAY_RESULT);
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject json = new JSONObject();
            json.put("out_trade_no", prepayId);
            jsonObject.put(Constants.JSON, json.toString());

            showProgress("正在查询支付结果...");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    PayOrderResult.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof PayOrderResult) {
                                        final PayOrderResult payOrderResult = (PayOrderResult) result;
                                        final MyAlertDialog dialog = new MyAlertDialog(mContext);
                                        dialog.show();
                                        dialog.setTitle(R.string.string_pay_wechat_tip);
                                        dialog.setMessage(payOrderResult.getTrade_state_desc());
                                        dialog.setCancelable(false);
                                        dialog.setLeftButton(getString(R.string.submit), new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
                                        Toast.makeText(mContext, result.toString(), Toast.LENGTH_SHORT).show();
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