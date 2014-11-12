package com.maogousoft.logisticsmobile.driver.activity.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.EvaluateListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.maogousoft.logisticsmobile.driver.model.PraiseInfo;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 货主信誉
public class UserCreditActivity extends BaseListActivity implements AbsListView.OnScrollListener {

    private RatingBar credit_score, credit_score1, credit_score2, credit_score3;
    private TextView commentCount, likeCount;
    private EditText commentContent;
    private float user_score;
    private View shipperInfoLayout;
    private TextView nameTip,tvName, tvCompanyName, tvAddr, tvPhone, tvAccountName;
    private ImageView mPhoto;
    private ShipperInfo shipperInfo;
    private DriverInfo driverInfo;
    private int userId = 0;//被查看信誉的id
    private int orderId;//订单号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_credit_header_view, null);
        mListView.addHeaderView(view, null, false);
        mAdapter = new EvaluateListAdapter(mContext);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);
        setListShown(false);

        shipperInfoLayout = findViewById(R.id.shipper_info_layout);
        credit_score = (RatingBar) findViewById(R.id.credit_score);
        credit_score1 = (RatingBar) findViewById(R.id.credit_score1);
        credit_score2 = (RatingBar) findViewById(R.id.credit_score2);
        credit_score3 = (RatingBar) findViewById(R.id.credit_score3);
        credit_score.setIsIndicator(true);
        credit_score1.setIsIndicator(true);
        credit_score2.setIsIndicator(true);
        credit_score3.setIsIndicator(true);

        mPhoto = (ImageView) findViewById(R.id.account_photo);
        tvName = (TextView) findViewById(R.id.tv_name);
        nameTip = (TextView) findViewById(R.id.nameTip);
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvAddr = (TextView) findViewById(R.id.tv_addr);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAccountName = (TextView) findViewById(R.id.tv_account_name);

        commentCount = (TextView) findViewById(R.id.comment_count);
        commentContent = (EditText) findViewById(R.id.comment_content);
        likeCount = (TextView) findViewById(R.id.like_count);

        commentContent.setEnabled(true);
        commentContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        commentContent.requestFocus();
        commentContent.requestFocusFromTouch();
    }

    // 初始化数据
    private void initData() {
        orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        boolean isMyReputation = getIntent().getBooleanExtra(Constants.IS_MY_REPUTATION, false);
        boolean isCarReputation = getIntent().getBooleanExtra(Constants.IS_CAR_REPUTATION, false);
        if (isMyReputation) {
            Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
            //我的信誉
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("我的信誉");
            if(application.getUserType() == Constants.USER_DRIVER) {
                //司机信誉
                driverInfo = (DriverInfo) serializable;
                shipperInfoLayout.setVisibility(View.GONE);
                displayDriverData();
            } else {
                //货主信誉
                shipperInfo = (ShipperInfo) serializable;
                tvCompanyName.setText(tvCompanyName.getText() + shipperInfo.getCompany_name());
                tvAddr.setText(tvAddr.getText() + shipperInfo.getAddress());
                displayShipperData();
            }
        } else if (isCarReputation) {
            Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
            //司机信誉
            driverInfo = (DriverInfo) serializable;
            userId = driverInfo.getId();
            ((TextView) findViewById(R.id.titlebar_id_content)).setText(driverInfo.getName());
            shipperInfoLayout.setVisibility(View.GONE);
            displayDriverData();
        } else {
            //查看对方信誉
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("货主信誉");
            userId = getIntent().getIntExtra("user_id", 0);
            user_score = getIntent().getFloatExtra("user_score", 0);
            credit_score.setRating(user_score == 0 ? 5 : user_score);
            float user_score1 = getIntent().getIntExtra("user_score1", 0);
            credit_score1.setRating(user_score1 == 0 ? 5 : user_score1);
            float user_score2 = getIntent().getIntExtra("user_score2", 0);
            credit_score2.setRating(user_score2 == 0 ? 5 : user_score2);
            float user_score3 = getIntent().getIntExtra("user_score3", 0);
            credit_score3.setRating(user_score3 == 0 ? 5 : user_score3);
            queryShipperInfo(userId);
        }
        //请求评论数据
        queryData();
        //获取点赞数
        getPraiseCount();
    }

    private void displayDriverData() {
        tvName.setText(driverInfo.getName());
        tvPhone.setText(driverInfo.getPhone());
        tvAccountName.setText(driverInfo.getPhone());
        credit_score.setRating(driverInfo.getScore() == 0 ? 5 : driverInfo.getScore());
        credit_score1.setRating(driverInfo.getScroe1() == 0 ? 5 : driverInfo.getScroe1());
        credit_score2.setRating(driverInfo.getScroe2() == 0 ? 5 : driverInfo.getScroe2());
        credit_score3.setRating(driverInfo.getScroe3() == 0 ? 5 : driverInfo.getScroe3());
        ImageLoader.getInstance().displayImage(driverInfo.getId_card_photo(), mPhoto, options,
                new Utils.MyImageLoadingListener(mContext, mPhoto));
    }

    private void displayShipperData() {
        nameTip.setText(getString(R.string.string_home_myabc_title_name));
        tvName.setText(shipperInfo.getName());
        tvPhone.setText(shipperInfo.getPhone());
        tvAccountName.setText(shipperInfo.getPhone());
        credit_score.setRating(shipperInfo.getScore() == 0 ? 5 : shipperInfo.getScore());
        credit_score1.setRating(shipperInfo.getScore1() == 0 ? 5 : shipperInfo.getScore1());
        credit_score2.setRating(shipperInfo.getScore2() == 0 ? 5 : shipperInfo.getScore2());
        credit_score3.setRating(shipperInfo.getScore3() == 0 ? 5 : shipperInfo.getScore3());
        ImageLoader.getInstance().displayImage(shipperInfo.getCompany_logo(), mPhoto, options,
                new Utils.MyImageLoadingListener(mContext, mPhoto));
    }

    // 获取评论列表
    private void queryData() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (driverInfo != null) {
                jsonObject.put(Constants.ACTION, Constants.GET_DRIVER_REPLY);
            } else {
                jsonObject.put(Constants.ACTION, Constants.GET_USER_REPLY);
            }
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("user_id", userId).toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    EvaluateInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            setListShown(true);
                            mListView.setVisibility(View.VISIBLE);
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof ArrayList) {
                                        mAdapter.removeAll();
                                        List<EvaluateInfo> list = (List<EvaluateInfo>) result;
                                        if(!list.isEmpty()) {
                                            mAdapter.addAll(list);
                                        }
                                        commentCount.setText(getString(R.string.comment_count, list.size()));
                                    }
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取货主信息
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
                            setListShown(true);
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    shipperInfo = (ShipperInfo) result;
                                    tvName.setText(shipperInfo.getName());
                                    tvPhone.setText(shipperInfo.getPhone());
                                    tvAccountName.setText(shipperInfo.getPhone());
                                    tvCompanyName.setText(tvCompanyName.getText() + shipperInfo.getCompany_name());
                                    tvAddr.setText(tvAddr.getText() + shipperInfo.getAddress());
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

    //给货主点赞
    public void onLike(View view) {
        LogUtil.d(TAG, "onLike");
        final JSONObject jsonObject = new JSONObject();
        showProgress("正在处理");
        try {
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject params = new JSONObject();
            if (driverInfo != null) {
                //给司机点赞
                jsonObject.put(Constants.ACTION, Constants.USER_TO_DRIVER_PRAISE);
                params.put("driverId", userId);
            } else {
                //给货主点赞
                jsonObject.put(Constants.ACTION, Constants.DRIVER_TO_USER_PRAISE);
                params.put("userId", userId);
            }
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    //点赞成功后,查询点赞结果
                                    getPraiseCount();
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

    //评论
    public void onComment(View view) {
        final JSONObject jsonObject = new JSONObject();
        showProgress("正在评论");
        try {
            jsonObject.put(Constants.TOKEN, application.getToken());
            if (driverInfo != null) {
                //给司机评价
                jsonObject.put(Constants.ACTION, Constants.RATING_TO_DRIVER);
            } else {
                //给货主评价
                jsonObject.put(Constants.ACTION, Constants.RATING_TO_USER);
            }
            JSONObject params = new JSONObject();
            params.put("order_id", orderId);
            params.put("reply_content", TextUtils.isEmpty(commentContent.getText()) ? commentContent.getHint() : commentContent.getText());
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg("评论成功");
                                    queryData();
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

    //显示发表评论按钮
    public void onCommentLayout(View view) {
        LogUtil.d(TAG, "onCommentLayout");
        findViewById(R.id.comment_layout).setVisibility(View.VISIBLE);
    }

    /**
     * 获取点赞数
     */
    private void getPraiseCount() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_PRAISE_COUNT);
            jsonObject.put(Constants.TOKEN, application.getToken());
            if (driverInfo != null) {
                //获取司机点赞数
                jsonObject.put(Constants.JSON, new JSONObject().put("uid", "d" + userId).toString());
            } else {
                //获取货主点赞数
                jsonObject.put(Constants.JSON, new JSONObject().put("uid", "u" + userId).toString());
            }
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    PraiseInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof PraiseInfo) {
                                        PraiseInfo praiseInfo = (PraiseInfo) result;
                                        likeCount.setText(praiseInfo.getCount() + "");
                                        if (TextUtils.equals("Y", praiseInfo.getIsEnablePraise())) {
                                            likeCount.setEnabled(true);
                                        } else {
                                            likeCount.setEnabled(false);
                                        }
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
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
