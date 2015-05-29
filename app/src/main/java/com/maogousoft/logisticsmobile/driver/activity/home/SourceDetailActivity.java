package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ImageGridAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.PopupMenuInfo;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 货源详情
 *
 * @author lenovo
 */
public class SourceDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "SourceDetailActivity";
    public static final String ORDER_INFO = "sourceInfo";
    private int order_id;
    private Button mPhone;
    private MyGridView mGridView;
    private ImageGridAdapter mAdapter;
    private ImageView mPhoto;
    private TextView mOrderNumber, mContactName, mShipperName, mLine, mSourceName, mSourceType, mShipType, mSourceCarLength,
            mSourceCarType, mSourcePrice, mSourceGold, mValidateTime, mZhuangCheTime, mWeight, sourceOther;
    private RatingBar mScore, ratingbarScore1, ratingbarScore2, ratingbarScore3;
    private Resources mResources;
    private NewSourceInfo mSourceInfo;
    private RelativeLayout mPingjia;
    private CityDBUtils dbUtils;
    private boolean isFromPush = false;
    private StringBuffer shareContent = new StringBuffer();
    private PopupWindow popWindow;// popupwindow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sourcedetail);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        HeaderView headerView = (HeaderView) findViewById(R.id.headerView);
        headerView.getTipViewVisible().setOnClickListener(this);
        headerView.setTitle(R.string.string_home_sourcedetail_title);
        popWindow = headerView.createPopupWindow(this, initPopupData());

        mGridView = (MyGridView) findViewById(R.id.source_id_order_gridview);
        mOrderNumber = (TextView) findViewById(R.id.source_id_order_number);

        mContactName = (TextView) findViewById(R.id.source_detail_name);
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
        mPingjia.setOnClickListener(this);
        mPhone.setOnClickListener(this);

        mResources = getResources();
        mPhoto = (ImageView) findViewById(R.id.source_detail_shipper_photo);
        mShipperName = (TextView) findViewById(R.id.source_detail_shipper_name);
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
        if (application.getUserType() == Constants.USER_SHIPPER) {
            //mAttention.setVisibility(View.GONE);
        }
    }

    // 初始化数据source_detail_phone
    private void initData() {
        dbUtils = new CityDBUtils(application.getCitySDB());
        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("MessageBroadCastReceiver")) {
                isFromPush = true;
                LogUtil.i(TAG, "进入新货源详情，来自 推送进入");
                application.finishUnlessCurrentAllActivity(this);
            }
        } else {
            LogUtil.i(TAG, "进入新货源详情，来自 新货源列表进入");
        }

        mAdapter = new ImageGridAdapter(mContext);
        mGridView.setAdapter(mAdapter);

        String type = getIntent().getStringExtra("type");

        if (type.equals("MessageBroadCastReceiver")) {
            // 如果是点击推送，进入详情， 先检测是否已经完善了资料
            if (application.checkIsRegOptional()) {
                mPingjia.setClickable(true);
                order_id = getIntent().getIntExtra(Constants.ORDER_ID, 0);
                getSourceDetail(order_id);
            } else {
                final MyAlertDialog dialog = new MyAlertDialog(mContext);
                dialog.show();
                dialog.setTitle("提示");
                dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
                dialog.setLeftButton("完善资料", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext,
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
            order_id = getIntent().getIntExtra(Constants.ORDER_ID, 0);
            getSourceDetail(order_id);
        } else if (type.equals("InvoiceActivity")) {
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("货单详情");
            order_id = getIntent().getIntExtra(Constants.ORDER_ID, 0);
            getSourceDetail(order_id);
        } else {
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("已发布货源详情");
            order_id = getIntent().getIntExtra(Constants.ORDER_ID, 0);
            getSourceDetail(order_id);
        }
    }

    @Override
    public void onBackPressed() {
        if (isFromPush) {
            application.finishAllActivity();

            Intent intent = new Intent(mContext, NewSourceActivity.class);
            //从推送进入的,按返回后进入关注货源列表
            intent.putExtra("QUERY_MAIN_LINE_ORDER", true);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    // 展示货源详情
    private void showSourceDetail(NewSourceInfo sourceInfo) {

        // mOrderNumber.setText(String.format(
        // mResources.getString(R.string.sourcedetail_number),
        // sourceInfo.getId()));
        mOrderNumber.setVisibility(View.GONE);
        //联系人
        if (!TextUtils.isEmpty(sourceInfo.getCargo_user_name())) {
            mContactName.setText(mContactName.getText() + sourceInfo.getCargo_user_name());
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

        mLine.setText(mLine.getText() + way);
        String sourceName = sourceInfo.getCargo_desc();
        mSourceName.setText(mSourceName.getText() + sourceName);
        //货物类型
        Integer sourceType = mSourceInfo.getCargo_type();
        if (sourceType != null && sourceType > 0) {
            String[] sourceTypeStr = mContext.getResources().getStringArray(R.array.goods_types);
            for (int i = 0; i < Constants.sourceTypeValues.length; i++) {
                if (Constants.sourceTypeValues[i] == sourceType) {
                    mSourceType.setText(mSourceType.getText() + Utils.textFormatBlue(sourceTypeStr[i]));
                    break;
                }
            }
        } else {
            mSourceType.setVisibility(View.GONE);
        }
        //载重
        String weight = "";
        if (mSourceInfo.getCargo_number() != null && mSourceInfo.getCargo_number() > 0) {
            Integer unitType = mSourceInfo.getCargo_unit();
            String[] priceUnit = mContext.getResources().getStringArray(R.array.car_price_unit);
            for (int i = 0; i < Constants.unitTypeValues.length; i++) {
                if (Constants.unitTypeValues[i] == unitType) {
                    weight = mSourceInfo.getCargo_number() + priceUnit[i];
                    mWeight.setText(mWeight.getText().toString() + weight);
                    break;
                }
            }
        } else {
            mWeight.setVisibility(View.GONE);
        }
        //运输方式
        Integer shipType = mSourceInfo.getShip_type();
        if (shipType != null && shipType > 0) {
            String[] shipTypeStr = mContext.getResources().getStringArray(R.array.ship_type);
            for (int i = 0; i < Constants.shipTypeValues.length; i++) {
                if (Constants.shipTypeValues[i] == shipType) {
                    mShipType.setText(Html.fromHtml(mShipType.getText() + Utils.textFormatBlue(shipTypeStr[i])));
                    break;
                }
            }
        } else {
            mShipType.setVisibility(View.GONE);
        }
        //车长
        String carLength = sourceInfo.getCar_length() + "米";
        if (null != sourceInfo.getCar_length() && sourceInfo.getCar_length() > 0) {
            mSourceCarLength.setText(Html.fromHtml(mSourceCarLength.getText().toString() + Utils.textFormatBlue(sourceInfo.getCar_length() + "米")));
        } else {
            mSourceCarLength.setVisibility(View.GONE);
        }
        //车型
        Integer carTypeValue = mSourceInfo.getCar_type();
        if (carTypeValue != null && carTypeValue > 0) {
            String[] carTypeStr = mContext.getResources().getStringArray(R.array.car_types_name);
            for (int i = 0; i < Constants.carTypeValues.length; i++) {
                if (Constants.carTypeValues[i] == carTypeValue) {
                    mSourceCarType.setText(Html.fromHtml(mSourceCarType.getText() + Utils.textFormatBlue(carTypeStr[i])));
                    break;
                }
            }
        } else {
            mSourceCarType.setVisibility(View.GONE);
        }
        //参考运费
        mSourcePrice.setText(Html.fromHtml(mSourcePrice.getText().toString() + Utils.textFormatRed(sourceInfo.getUnit_price() + "元")));
        //
        mSourceGold.setText(mSourceGold.getText().toString() + sourceInfo.getUser_bond() + "元");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String createTime = format.format(new Date(sourceInfo.getCreate_time()));
        mValidateTime.setText(mValidateTime.getText() + createTime);

        float score = Float.parseFloat(String.valueOf(sourceInfo.getScore()));
        mScore.setRating(score == 0 ? 5 : score);

        float score1 = Float.parseFloat(String.valueOf(sourceInfo.getScore1()));
        ratingbarScore1.setRating(score1 == 0 ? 5 : score1);

        float score2 = Float.parseFloat(String.valueOf(sourceInfo.getScore2()));
        ratingbarScore2.setRating(score2 == 0 ? 5 : score2);

        float score3 = Float.parseFloat(String.valueOf(sourceInfo.getScore3()));
        ratingbarScore3.setRating(score3 == 0 ? 5 : score3);

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
                startActivity(new Intent(mContext, ImagePagerActivity.class).putStringArrayListExtra("images", list));
            }
        });
        mAdapter.setList(list);
        mPingjia.setClickable(true);

        if (mSourceInfo.getFavorite_status() == 0) {
            //TODO mAttention.setText("关注货主为好友");
        } else {
            //TODO mAttention.setText("取消关注货主");
        }

        Date dateNow = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
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
            showSpecialProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    mSourceInfo = (NewSourceInfo) result;
                                    showSourceDetail(mSourceInfo);
                                    queryShipperInfo(mSourceInfo.getUser_id());
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

    // 关注货主
    private void attentionOrder() {
        final JSONObject params = new JSONObject();
        try {
            params.put(Constants.ACTION, mSourceInfo.getFavorite_status() == 0 ? Constants.ATTENTION_SOURCE_USER : Constants.CANCEL_ATTENTION_SOURCE_USER);
            params.put(Constants.TOKEN, application.getToken());
            params.put(Constants.JSON,
                    new JSONObject().put("userPhone", mSourceInfo.getUser_phone()));
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params, null,
                    new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (mSourceInfo.getFavorite_status() == 0) {
                                        mSourceInfo.setFavorite_status(1);
                                        showMsg(R.string.tips_sourcedetail_attention_success);
                                    } else {
                                        mSourceInfo.setFavorite_status(0);
                                        showMsg(R.string.tips_sourcedetail_cancel_attention_success);
                                    }
                                    if (mSourceInfo.getFavorite_status() == 0) {
                                        //TODO mAttention.setText("关注货主为好友");
                                    } else {
                                        //TODO mAttention.setText("取消关注货主");
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
            case R.id.source_id_detail_attention:
                attentionOrder();
                break;
            case R.id.source_id_pj:
                startActivity(new Intent(mContext, UserCreditActivity.class)
                        .putExtra("user_id", mSourceInfo.getUser_id())
                        .putExtra("user_score", mSourceInfo.getScore())
                        .putExtra("user_phone", mSourceInfo.getUser_phone())
                        .putExtra("user_score1", mSourceInfo.getScore1())
                        .putExtra("user_score2", mSourceInfo.getScore2())
                        .putExtra("user_score3", mSourceInfo.getScore3())
                        .putExtra(Constants.ORDER_ID, mSourceInfo.getId()));
                break;

            case R.id.source_detail_phone:
                String phoneStr = mPhone.getText().toString();
                if (!TextUtils.isEmpty(phoneStr) && !phoneStr.equals("无")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    startActivity(intent);
                }
                break;
            case R.id.titlebar_id_tip:
                if (popWindow.isShowing()) {
                    // 关闭
                    popWindow.dismiss();
                } else {
                    // 显示
                    popWindow.showAsDropDown(v);
                }
                break;
            default:
                break;
        }
    }

    //获取货主信息,显示头像等
    private void queryShipperInfo(int userId) {
        final JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put(Constants.ACTION, Constants.GET_USER_INFO);
            jsonObject1.put(Constants.TOKEN, application.getToken());
            jsonObject1.put(Constants.JSON, new JSONObject().put("user_id", userId).toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject1,
                    ShipperInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    ShipperInfo shipperInfo = (ShipperInfo) result;
                                    //发货方
                                    if (!TextUtils.isEmpty(shipperInfo.getCompany_name())) {
                                        mShipperName.setText(mShipperName.getText() + shipperInfo.getCompany_name());
                                    }
                                    ImageLoader.getInstance().displayImage(shipperInfo.getCompany_logo(), mPhoto, options,
                                            new Utils.MyImageLoadingListener(mContext, mPhoto));
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

    /**
     * 抢单
     */
    private void placeOrder() {
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

    //报价
    private void onBaojia() {
        Intent intent = new Intent(mContext, OfferDriverActivity.class);
        intent.putExtra(Constants.COMMON_KEY, mSourceInfo);
        mContext.startActivity(intent);
    }

    private void onSendMessage() {
        CharSequence phoneNumber = mPhone.getText();
        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        startActivity(intent);
    }

    private void onCall() {
        CharSequence phoneNumber = mPhone.getText();
        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    /*header更多菜单*/
    private List<PopupMenuInfo> initPopupData() {
        List<PopupMenuInfo> moreList = new ArrayList<PopupMenuInfo>();
        moreList.add(new PopupMenuInfo("分享", R.drawable.header_more_share));
        moreList.add(new PopupMenuInfo("礼品", R.drawable.header_more_present));
        moreList.add(new PopupMenuInfo("关注", R.drawable.header_more_focus));
        moreList.add(new PopupMenuInfo("好友", R.drawable.header_more_follow));
        moreList.add(new PopupMenuInfo("抢单", R.drawable.header_more_qd));
        moreList.add(new PopupMenuInfo("报价", R.drawable.header_more_money));
        moreList.add(new PopupMenuInfo("短信", R.drawable.header_more_message));
        moreList.add(new PopupMenuInfo("电话", R.drawable.header_more_call));
        return moreList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        popWindow.dismiss();
        switch (position) {
            case 0:
                share();
                break;
            case 1:
                startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case 2:
                attentionOrder();
                break;
            case 3:
                attentionOrder();
                break;
            case 4:
                placeOrder();
                break;
            case 5:
                onBaojia();
                break;
            case 6:
                onSendMessage();
                break;
            case 7:
                onCall();
                break;
        }
    }
}
