package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.RenZhengActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ImageGridAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 货源详情
 *
 * @author lenovo
 */
public class SourceDetailActivity extends BaseActivity {

    private static final String TAG = "SourceDetailActivity";
    // 传递订单编号
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_INFO = "sourceInfo";
    private int order_id;
    private Button mBack, mPlace, mAttention, mPhone, mShare;
    private MyGridView mGridView;
    private ImageGridAdapter mAdapter;
    private TextView mOrderNumber, mName, mLine, mSourceName, mSourceType, mShipType, mSourceCarLength,
            mSourceCarType, mSourcePrice, mSourceGold, mValidateTime, mZhuangCheTime, mWeight, sourceOther;
    private RatingBar mScore, ratingbarScore1, ratingbarScore2, ratingbarScore3;
    private Resources mResources;
    private NewSourceInfo mSourceInfo;
    private RelativeLayout mPingjia;
    private CityDBUtils dbUtils;
    /**
     * 司机账户余额
     */
    private double balance;
    /**
     * 信息费
     */
    private double messagePrice;
    private boolean isFromPush = false;
    private StringBuffer shareContent = new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sourcedetail);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content))
                .setText(R.string.string_home_sourcedetail_title);
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mPlace = (Button) findViewById(R.id.source_id_detail_place);
        mShare = (Button) findViewById(R.id.titlebar_id_more);
        mShare.setText("分享");
        mAttention = (Button) findViewById(R.id.source_id_detail_attention);
        mGridView = (MyGridView) findViewById(R.id.source_id_order_gridview);

        mOrderNumber = (TextView) findViewById(R.id.source_id_order_number);

        mName = (TextView) findViewById(R.id.source_detail_name);
        mPhone = (Button) findViewById(R.id.source_detail_phone);
        mPingjia = (RelativeLayout) findViewById(R.id.source_id_pj);
        mPingjia.setClickable(false);

        mScore = (RatingBar) findViewById(R.id.source_id_detail_score);
        mScore.setIsIndicator(true);

        ratingbarScore1 = (RatingBar) findViewById(R.id.ratingbar_score1);
        ratingbarScore1.setIsIndicator(true);
        ratingbarScore2 = (RatingBar) findViewById(R.id.ratingbar_score2);
        ratingbarScore2.setIsIndicator(true);
        ratingbarScore3 = (RatingBar) findViewById(R.id.ratingbar_score3);
        ratingbarScore3.setIsIndicator(true);

        sourceOther = (TextView) findViewById(R.id.source_detail_other);

        mBack.setOnClickListener(this);
        mPlace.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mPingjia.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mShare.setOnClickListener(this);

        mResources = getResources();

        mLine = (TextView) findViewById(R.id.source_detail_xianlu);
        mSourceName = (TextView) findViewById(R.id.source_detail_mingchen);
        mSourceType = (TextView) findViewById(R.id.source_detail_leixing);
        mShipType = (TextView) findViewById(R.id.source_detail_yunshufangshi);
        mSourceCarLength = (TextView) findViewById(R.id.source_detail_chechang);
        mSourceCarType = (TextView) findViewById(R.id.source_detail_chexing);
        mSourcePrice = (TextView) findViewById(R.id.source_detail_baojia);
        mSourceGold = (TextView) findViewById(R.id.source_detail_baozhengjin);
        mValidateTime = (TextView) findViewById(R.id.source_detail_yifabu);
        mZhuangCheTime = (TextView) findViewById(R.id.source_detail_zhuangche);
        mWeight = (TextView) findViewById(R.id.source_detail_weight);

        //如果是货主身份,不显示好友
        if(application.getUserType() == Constants.USER_SHIPPER) {
            mAttention.setVisibility(View.GONE);
        }
    }

    // 初始化数据source_detail_phone
    private void initData() {
        dbUtils = new CityDBUtils(application.getCitySDB());
        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals(
                    "MessageBroadCastReceiver")) {
                isFromPush = true;

                LogUtil.i(TAG, "进入新货源详情，来自 推送进入");
                application.finishUnlessCurrentAllActivity(this);
            }
        } else {
            LogUtil.i(TAG, "进入新货源详情，来自 新货源列表进入");
        }

        mAdapter = new ImageGridAdapter(context);
        mGridView.setAdapter(mAdapter);

        String type = getIntent().getStringExtra("type");

        if (type.equals("MessageBroadCastReceiver")) {

            // 如果是点击推送，进入详情， 先检测是否已经完善了资料
            if (application.checkIsRegOptional()) {

                mPingjia.setClickable(true);
                order_id = getIntent().getIntExtra(ORDER_ID, 0);
                getSourceDetail(order_id);
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

                        intent.putExtra("isFormPushSourceDetail", true);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setRightButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        } else if (type.equals("NewSourceActivity")) {
            order_id = getIntent().getIntExtra(ORDER_ID, 0);
            getSourceDetail(order_id);
        } else if (type.equals("UnMatchedSourceActivity")) {
            mPlace.setText("取消订单");
            ((TextView) findViewById(R.id.titlebar_id_content))
                    .setText("待定货源详情");

            order_id = getIntent().getIntExtra(ORDER_ID, 0);
            getSourceDetail(order_id);

        } else {
            mPlace.setText("取消订单");
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("已发布货源详情");
            order_id = getIntent().getIntExtra(ORDER_ID, 0);
            getSourceDetail(order_id);
        }
    }

    @Override
    public void onBackPressed() {

        if (isFromPush) {

            application.finishAllActivity();

            Intent intent = new Intent(context, NewSourceActivity.class);
            startActivity(intent);

        } else {
            super.onBackPressed();
        }
    }

    // 展示货源详情
    private void showSourceDetail(NewSourceInfo sourceInfo) {

        // mOrderNumber.setText(String.format(
        // mResources.getString(R.string.sourcedetail_number),
        // sourceInfo.getId()));
        mOrderNumber.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(sourceInfo.getCargo_user_name())) {
            mName.setText(sourceInfo.getCargo_user_name());
        } else {

            if (!TextUtils.isEmpty(sourceInfo.getUser_name())) {
                mName.setText(sourceInfo.getUser_name());
            } else {
                mName.setText("无");
            }

        }
        String phone = "";
        if (!TextUtils.isEmpty(sourceInfo.getCargo_user_phone())) {
            phone = sourceInfo.getCargo_user_phone();
            mPhone.setText(phone);
        } else {
            if (!TextUtils.isEmpty(sourceInfo.getUser_phone())) {
                phone = sourceInfo.getUser_phone();
                mPhone.setText(phone);
            } else {
                mPhone.setText("无");
            }
        }

        // 备注：使用 Cargo_remark参数 来传递的 发货详细内容
        // webview.loadData(sourceInfo.getCargo_remark(), "text/html", "UTF-8");
        String other = sourceInfo.getCargo_remark() + sourceInfo.getCargo_tip();
        sourceOther.setText(other);
        //线路
        String way = "";
        String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        if (sourceInfo.getEnd_province() > 0 || sourceInfo.getEnd_city() > 0 || sourceInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
            way = wayStart + "--" + wayEnd;
        }

        mLine.setText(Html.fromHtml(mLine.getText() + Utils.textFormatGreen(way)));
        String sourceName = sourceInfo.getCargo_desc();
        mSourceName.setText(Html.fromHtml(mSourceName.getText() + Utils.textFormatBlue(sourceName)));
       //货物类型
        Integer sourceType = mSourceInfo.getCargo_type();
        if(sourceType != null && sourceType > 0) {
            String[] sourceTypeStr = context.getResources().getStringArray(R.array.goods_types);
            for (int i = 0; i < Constants.sourceTypeValues.length; i++) {
                if (Constants.sourceTypeValues[i] == sourceType) {
                    mSourceType.setText(Html.fromHtml(mSourceType.getText() + Utils.textFormatBlue(sourceTypeStr[i])));
                }
            }
        }
       //载重
        Integer unitType = mSourceInfo.getCargo_unit();
        String weight = "";
        if(unitType != null && unitType > 0) {
            String[] priceUnit = context.getResources().getStringArray(R.array.car_price_unit);
            for (int i = 0; i < Constants.unitTypeValues.length; i++) {
                if (Constants.unitTypeValues[i] == unitType) {
                    weight = mSourceInfo.getCargo_number() + priceUnit[i];
                    mWeight.setText(Html.fromHtml(mWeight.getText().toString() + Utils.textFormatBlue(weight)));
                }
            }
        }
        //运输方式
        Integer shipType = mSourceInfo.getShip_type();
        if(shipType != null && shipType> 0) {
            String[] shipTypeStr = context.getResources().getStringArray(R.array.ship_type);
            for (int i = 0; i < Constants.shipTypeValues.length; i++) {
                if (Constants.shipTypeValues[i] == shipType) {
                    mShipType.setText(Html.fromHtml(mShipType.getText() + Utils.textFormatBlue(shipTypeStr[i])));
                }
            }
        }
        //车长
        String carLength = sourceInfo.getCar_length() + "米";
        mSourceCarLength.setText(mSourceCarLength.getText().toString() + carLength);
        //车型
        Integer carTypeValue = mSourceInfo.getCar_type();
        if(carTypeValue != null && carTypeValue>0) {
            String[] carTypeStr = context.getResources().getStringArray(R.array.car_types_name);
            for (int i = 0; i < Constants.carTypeValues.length; i++) {
                if (Constants.carTypeValues[i] == carTypeValue) {
                    mSourceCarType.setText(mSourceCarType.getText() + carTypeStr[i]);
                }
            }
        }
        //报价单位
        Integer unitPrice = sourceInfo.getCargo_unit();
        if(unitPrice != null && unitPrice>0) {
            String[] unitPriceStr = context.getResources().getStringArray(R.array.car_price_unit);
            for (int i = 0; i < Constants.unitTypeValues.length; i++) {
                if (Constants.unitTypeValues[i] == unitPrice) {
                    mSourcePrice.setText(mSourcePrice.getText().toString() + sourceInfo.getUnit_price() + "元/" + unitPriceStr[i]);
                }
            }
        }
        //
        mSourceGold.setText(mSourceGold.getText().toString() + sourceInfo.getUser_bond() + "元");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date(sourceInfo.getValidate_time());
        mValidateTime.setText(mValidateTime.getText().toString() + sdf.format(date));
        mZhuangCheTime.setText(mZhuangCheTime.getText().toString() + sdf.format(date) + "之前");

        float score = Float.parseFloat(String.valueOf(sourceInfo.getScore()));
        if (score == 0) {
            score = 5;
        }
        mScore.setRating(score);

        float score1 = Float.parseFloat(String.valueOf(sourceInfo.getScore1()));
        if (score1 == 0) {
            score1 = 5;
        }
        ratingbarScore1.setRating(score1);

        float score2 = Float.parseFloat(String.valueOf(sourceInfo.getScore2()));
        if (score2 == 0) {
            score2 = 5;
        }
        ratingbarScore2.setRating(score2);

        float score3 = Float.parseFloat(String.valueOf(sourceInfo.getScore3()));
        if (score3 == 0) {
            score3 = 5;
        }
        ratingbarScore3.setRating(score3);

        final ArrayList<String> list = new ArrayList<String>();
        if (!TextUtils.isEmpty(sourceInfo.getCargo_photo1())) {
            list.add(sourceInfo.getCargo_photo1());
        }
        if (!TextUtils.isEmpty(sourceInfo.getCargo_photo2())) {
            list.add(sourceInfo.getCargo_photo2());
        }
        if (!TextUtils.isEmpty(sourceInfo.getCargo_photo3())) {
            list.add(sourceInfo.getCargo_photo3());
        }
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                startActivity(new Intent(context, ImagePagerActivity.class)
                        .putStringArrayListExtra("images", list));
            }
        });
        mAdapter.setList(list);
        mPingjia.setClickable(true);

        if(mSourceInfo.getFavorite_status() == 0) {
            mAttention.setText("关注货主为好友");
        } else {
            mAttention.setText("取消关注货主");
        }

        Date dateNow = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String time = simpleDateFormat.format(dateNow);
        String commonInfo = getString(R.string.common_share_info);
        shareContent.append(way).append(" ")
                .append(weight).append(" ")
                .append(carLength).append(" ")
                .append(sourceType).append(" ")
                .append(other).append(" \n")
                .append(phone).append(" \n")
                .append(time).append(" \n")
                .append(commonInfo);
    }

    // 货源详情
    private void getSourceDetail(int order_id) {
        final JSONObject params = new JSONObject();
        try {
            params.put(Constants.ACTION, Constants.GET_SOURCE_ORDER_DETAIL);
            params.put(Constants.TOKEN, application.getToken());
            params.put(Constants.JSON, new JSONObject().put("order_id", order_id).toString());
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    mSourceInfo = (NewSourceInfo) result;
                                    showSourceDetail(mSourceInfo);
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

    // 获取余额，并抢单
    private void getBalance() {

        // 信息费为运价的3%，不超过200元

        if (mSourceInfo.getPrice() == null
                || mSourceInfo.getPrice().equalsIgnoreCase("")) {
            messagePrice = 0;
        } else {
            messagePrice = Double.parseDouble(mSourceInfo.getPrice()) * 0.03;
            if (messagePrice > 200) {
                messagePrice = 200;
            }
        }

        final GrabDialog dialog = new GrabDialog(context);
        dialog.show();
        final EditText mInput = (EditText) dialog
                .findViewById(android.R.id.text1);
        final EditText mInput2 = (EditText) dialog
                .findViewById(android.R.id.text2);
        mInput2.setVisibility(View.VISIBLE);
        TextView t = (TextView) dialog.findViewById(R.id.grabdialog_text);
        TextView t1 = (TextView) dialog.findViewById(R.id.grabdialog_text1);
        TextView t2 = (TextView) dialog.findViewById(R.id.grabdialog_text2);
        t.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        if (mSourceInfo.getPrice() == null
                || mSourceInfo.getPrice().equalsIgnoreCase("")) {
            mInput2.setText("0");
        } else {
            mInput2.setText(mSourceInfo.getPrice());
        }
        dialog.setTitle("提示");

        mInput.setText(mSourceInfo.getUser_bond() + "");

        String messageStr = String.format("请输入保证金,保证金必须大于等于%d元。",
                mSourceInfo.getUser_bond())
                + String.format("如达成交易，将付出信息费%d物流币。", Integer
                .parseInt(new java.text.DecimalFormat("0")
                        .format(messagePrice)));
        dialog.setMessage(messageStr);
        dialog.setLeftButton("确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(mInput.getText())
                            || mSourceInfo.getUser_bond() > Integer
                            .parseInt(mInput.getText().toString())) {
                        showMsg(String.format("保证金必须大于等于%d元",
                                mSourceInfo.getUser_bond()));
                    } else if (TextUtils.isEmpty(mInput2.getText())
                            || Integer.parseInt(mInput2.getText().toString()) < 0) {
                        Toast.makeText(context, "自报价：必须大于0元哦",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        final JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(Constants.ACTION,
                                    Constants.GET_ACCOUNT_GOLD);
                            jsonObject.put(Constants.TOKEN,
                                    application.getToken());
                            showProgress(mResources
                                    .getString(R.string.tips_sourcedetail_submit));
                            ApiClient.doWithObject(Constants.COMMON_SERVER_URL,
                                    jsonObject, null, new AjaxCallBack() {

                                        @Override
                                        public void receive(int code,
                                                            Object result) {
                                            switch (code) {
                                                case ResultCode.RESULT_OK:
                                                    JSONObject object = (JSONObject) result;
                                                    // mBalance.setText(String.format(getString(R.string.string_home_myabc_balance),
                                                    // object.optDouble("gold")));
                                                    balance = object
                                                            .optDouble("gold");

                                                    if (balance < (messagePrice + Double
                                                            .parseDouble(mInput
                                                                    .getText()
                                                                    .toString()))) {

                                                        dismissProgress();

                                                        final MyAlertDialog dialogCharg = new MyAlertDialog(
                                                                context);
                                                        dialogCharg.show();
                                                        dialogCharg.setTitle("提示");
                                                        dialogCharg.setMessage("需要金额"
                                                                + (messagePrice + Double
                                                                .parseDouble(mInput
                                                                        .getText()
                                                                        .toString()))
                                                                + "元，您的余额不足，请先充值。");
                                                        dialogCharg
                                                                .setLeftButton(
                                                                        "去充值",
                                                                        new OnClickListener() {

                                                                            @Override
                                                                            public void onClick(
                                                                                    View v) {

                                                                                Intent intent = new Intent(
                                                                                        SourceDetailActivity.this,
                                                                                        ChargeActivity.class);
                                                                                SourceDetailActivity.this
                                                                                        .startActivity(intent);
                                                                            }
                                                                        });

                                                        dialogCharg
                                                                .setRightButton(
                                                                        "取消",
                                                                        new OnClickListener() {

                                                                            @Override
                                                                            public void onClick(
                                                                                    View v) {
                                                                                dialogCharg
                                                                                        .dismiss();
                                                                            }
                                                                        });

                                                    } else {
                                                        placeOrder(mInput.getText()
                                                                        .toString(),
                                                                mInput2.getText()
                                                                        .toString());
                                                    }

                                                    break;
                                                case ResultCode.RESULT_FAILED:
                                                    dismissProgress();
                                                    showMsg(result.toString());
                                                    break;
                                                case ResultCode.RESULT_ERROR:
                                                    dismissProgress();
                                                    showMsg(result.toString());
                                                    break;

                                                default:
                                                    break;
                                            }
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setRightButton("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    // 关注货主
    private void attentionOrder() {
        final JSONObject params = new JSONObject();
        try {
            params.put(Constants.ACTION, mSourceInfo.getFavorite_status() == 0? Constants.ATTENTION_SOURCE_USER : Constants.CANCEL_ATTENTION_SOURCE_USER);
            params.put(Constants.TOKEN, application.getToken());
            params.put(Constants.JSON,
                    new JSONObject().put("userId", mSourceInfo.getUser_id()).toString());
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params, null,
                    new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if(mSourceInfo.getFavorite_status() == 0) {
                                        mSourceInfo.setFavorite_status(1);
                                        showMsg(R.string.tips_sourcedetail_attention_success);
                                    }else {
                                        mSourceInfo.setFavorite_status(0);
                                        showMsg(R.string.tips_sourcedetail_cancel_attention_success);
                                    }
                                    if(mSourceInfo.getFavorite_status() == 0) {
                                        mAttention.setText("关注货主为好友");
                                    } else {
                                        mAttention.setText("取消关注货主");
                                    }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().hasExtra(ORDER_INFO)) {
            mSourceInfo = (NewSourceInfo) getIntent().getSerializableExtra(
                    ORDER_INFO);
            order_id = mSourceInfo.getId();
            showSourceDetail(mSourceInfo);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        switch (id) {
            case R.id.titlebar_id_more:
                share(shareContent.toString());
                break;
            case R.id.source_id_detail_attention:
                attentionOrder();
                break;
            case R.id.source_id_detail_place:

                // 先检测是否已经 通过了 诚信认证
                if (application.checkIsThroughRezheng()) {
                    if (mPlace.getText().toString().equals("抢单或报价")) {
                        getBalance();
                    } else {
                        cancelOrder();
                    }
                } else {
                    final MyAlertDialog dialog = new MyAlertDialog(context);
                    dialog.show();
                    dialog.setTitle("提示");
                    dialog.setMessage("为确保诚信交易，你必须提供相关证件方可得到货主的认可。");
                    dialog.setLeftButton("诚信认证", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(context,
                                    RenZhengActivity.class);
                            startActivity(intent);
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
            case R.id.source_id_pj:

                startActivity(new Intent(context, UserCreditActivity.class)
                        .putExtra("user_id", mSourceInfo.getUser_id())
                        .putExtra("user_score", mSourceInfo.getScore())
                        .putExtra("user_phone", mSourceInfo.getUser_phone())
                        .putExtra("user_score1", mSourceInfo.getScore1())
                        .putExtra("user_score2", mSourceInfo.getScore2())
                        .putExtra("user_score3", mSourceInfo.getScore3()));

                break;

            case R.id.source_detail_phone:
                String phoneStr = mPhone.getText().toString();
                if (!TextUtils.isEmpty(phoneStr) && !phoneStr.equals("无")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            + phoneStr));
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 抢单
     *
     * @param mInput
     */
    public void placeOrder(String mInput, String mInput2) {
        final JSONObject params = new JSONObject();
        try {
            params.put(Constants.ACTION, Constants.PLACE_SOURCE_ORDER);
            params.put(Constants.TOKEN, application.getToken());
            params.put(
                    Constants.JSON,
                    new JSONObject().put("order_id", mSourceInfo.getId())
                            .put("driver_bond", mInput)
                            .put("driver_proportion", 0)
                            .put("driver_price", mInput2).toString());
            showProgress(mResources.getString(R.string.tips_sourcedetail_qiang));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params, null,
                    new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg(R.string.tips_sourcedetail_qiang_success);
                                    finish();
                                    break;
                                case ResultCode.RESULT_ERROR:

                                    // 你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）

                                    // 已经承接了其他货单，不能再抢单了
                                    if (result.toString().contains("已经承接了其他货单")) {
                                        final MyAlertDialog dialog = new MyAlertDialog(
                                                context);
                                        dialog.show();
                                        dialog.setTitle("温馨提示");
                                        dialog.setMessage("你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）");
                                        dialog.setLeftButton("确定",
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        showMsg(result.toString());
                                    }

                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result.toString().contains("已经承接了其他货单")) {
                                        final MyAlertDialog dialog = new MyAlertDialog(
                                                context);
                                        dialog.show();
                                        dialog.setTitle("温馨提示");
                                        dialog.setMessage("你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）");
                                        dialog.setLeftButton("确定",
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        showMsg(result.toString());
                                    }
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
     * 取消订单
     */
    public void cancelOrder() {

        final MyAlertDialog dialog = new MyAlertDialog(
                SourceDetailActivity.this);
        dialog.show();
        dialog.setTitle("提示");
        dialog.setMessage("您确定要取消抢单吗？");
        dialog.setLeftButton("确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Constants.ACTION,
                            Constants.CANCEL_PLACE_SOURCE_ORDER);
                    jsonObject.put(Constants.TOKEN, application.getToken());
                    jsonObject.put(
                            Constants.JSON,
                            new JSONObject().put("order_id",
                                    mSourceInfo.getId()));
                    ((BaseActivity) SourceDetailActivity.this)
                            .showProgress("请求提交中,请稍候");
                    ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
                            jsonObject, null, new AjaxCallBack() {

                                @Override
                                public void receive(int code, Object result) {
                                    ((BaseActivity) SourceDetailActivity.this)
                                            .dismissProgress();
                                    switch (code) {
                                        case ResultCode.RESULT_OK:
                                            ((BaseActivity) SourceDetailActivity.this)
                                                    .showMsg("取消成功!");

                                            finish();
                                            // startActivity(new
                                            // Intent(SourceDetailActivity.this,
                                            // NewSourceActivity.class));
                                            // 待定货源中，如果有取消抢单，则立即转移到新货源中

                                            break;
                                        case ResultCode.RESULT_ERROR:
                                            if (result != null)
                                                ((BaseActivity) SourceDetailActivity.this)
                                                        .showMsg(result.toString());
                                            break;
                                        case ResultCode.RESULT_FAILED:
                                            if (result != null)
                                                ((BaseActivity) SourceDetailActivity.this)
                                                        .showMsg(result.toString());
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
        });
        dialog.setRightButton("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
