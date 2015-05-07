package com.maogousoft.logisticsmobile.driver.pay.wx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.MD5;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.PrepayOrderResult;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.WeChatPayConstants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WeChatPayUtils {
    private static final String TAG = "WeChatPayUtils";
    private final IWXAPI msgApi;
    private StringBuffer stringBuffer;
    private Context mContext;
    private MGApplication mApplication;
    private AlertDialog progressDialog;

    public WeChatPayUtils(Context context, MGApplication application) {
        mContext = context;
        mApplication = application;
        progressDialog = new ProgressDialog.Builder(mContext).setMessage(mContext.getString(R.string.string_pay_wechat_getting_prepayid)).create();
        progressDialog.setCancelable(false);
        msgApi = WXAPIFactory.createWXAPI(context, null);
    }

    /**
     * 微信支付
     */
    public void pay(int price) {
        progressDialog.show();

        stringBuffer = new StringBuffer();
        msgApi.registerApp(WeChatPayConstants.APP_ID);
        /**
         * step 1. 获取订单号
         * step 2. 生成签名参数
         * step 3. 开始支付
         */
        //new GetPrepayIdTask(price).execute();
        String clientIp = getLocalIpAddress();
        if (TextUtils.isEmpty(clientIp)) {
            Toast.makeText(mContext, "支付失败:ip地址为空,请检查网络!", Toast.LENGTH_SHORT).show();
        } else {
            getPrepayIdTask(price, clientIp);
        }
    }

    // step 1.请求订单号
    private void getPrepayIdTask(int price, String clientIp) {
        try {
            progressDialog.show();

            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.WEIXIN_PLACE_ORDER);
            jsonObject.put(Constants.TOKEN, mApplication.getToken());
            JSONObject json = new JSONObject();
            json.put("money", price);
            json.put("clientIp", clientIp);
            jsonObject.put(Constants.JSON, json.toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    PrepayOrderResult.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            progressDialog.dismiss();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof PrepayOrderResult) {
                                        final PrepayOrderResult prepayOrderResult = (PrepayOrderResult) result;
                                        LogUtil.d(TAG, prepayOrderResult.getPrepay_id());
                                        //发起支付
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                genPayReq(prepayOrderResult);
                                            }
                                        }).start();
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

    /**
     * step 2. 生成签名参数
     *
     * @param prepayOrderResult
     */
    private void genPayReq(PrepayOrderResult prepayOrderResult) {
        PayReq req = new PayReq();
        req.appId = prepayOrderResult.getAppid();
        req.partnerId = prepayOrderResult.getMch_id();
        req.prepayId = prepayOrderResult.getPrepay_id();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);

        stringBuffer.append("sign\n" + req.sign + "\n\n");
        LogUtil.e(TAG, stringBuffer.toString());
        LogUtil.e(TAG, signParams.toString());
        //发起支付
        sendPayReq(req);
    }

    /**
     * step 3. 发起支付
     */
    private void sendPayReq(PayReq req) {
        msgApi.registerApp(WeChatPayConstants.APP_ID);
        msgApi.sendReq(req);
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WeChatPayConstants.API_KEY);

        this.stringBuffer.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        LogUtil.e(TAG, appSign);
        return appSign;
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    /*public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            LogUtil.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }*/

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            LogUtil.e("WifiPreference IpAddress", e.toString());
        }
        return null;
    }
}

