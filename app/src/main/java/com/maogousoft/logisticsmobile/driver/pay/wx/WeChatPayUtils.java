package com.maogousoft.logisticsmobile.driver.pay.wx;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.WeChatPayConstants;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.MD5;
import com.maogousoft.logisticsmobile.driver.pay.wx.utils.Util;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeChatPayUtils {
    private static final String TAG = "WeChatPayUtils";
    private final IWXAPI msgApi;
    private StringBuffer stringBuffer;
    private Context mContext;

    public WeChatPayUtils(Context context) {
        mContext = context;
        msgApi = WXAPIFactory.createWXAPI(context, null);
    }

    /**
     * 微信支付
     */
    public void pay(double price) {
        stringBuffer = new StringBuffer();
        msgApi.registerApp(WeChatPayConstants.APP_ID);
        /**
         * step 1. 获取订单号
         * step 2. 生成签名参数
         * step 3. 开始支付
         */
        new GetPrepayIdTask(price).execute();
    }

    /**
     * 生成签名
     */
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WeChatPayConstants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        LogUtil.e(TAG, packageSign);
        return packageSign;
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

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        LogUtil.e(TAG, sb.toString());
        return sb.toString();
    }

    /**
     * 获取预支付订单
     */
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
        private double price;
        private ProgressDialog dialog;

        public GetPrepayIdTask(double price){
            this.price = price;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext,
                    mContext.getString(R.string.string_pay_wechat_tip),
                    mContext.getString(R.string.string_pay_wechat_getting_prepayid));
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();
            LogUtil.e(TAG, entity);
            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            LogUtil.e(TAG, content);
            Map<String, String> xml = decodeXml(content);
            return xml;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            stringBuffer.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            LogUtil.d(TAG, stringBuffer.toString());
            //step 2. 生成签名参数
            genPayReq(result);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return null;

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", WeChatPayConstants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "weixin"));
            packageParams.add(new BasicNameValuePair("mch_id", WeChatPayConstants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlString = toXml(packageParams);
            return xmlString;
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    /**
     * step 2. 生成签名参数
     * @param resultUnifiedOrder
     */
    private void genPayReq(Map<String, String> resultUnifiedOrder) {
        PayReq req = new PayReq();
        req.appId = WeChatPayConstants.APP_ID;
        req.partnerId = WeChatPayConstants.MCH_ID;
        req.prepayId = resultUnifiedOrder.get("prepay_id");
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
}

