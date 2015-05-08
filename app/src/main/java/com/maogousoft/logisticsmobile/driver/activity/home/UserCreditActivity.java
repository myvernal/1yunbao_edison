package com.maogousoft.logisticsmobile.driver.activity.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
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
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

// 货主信誉
public class UserCreditActivity extends BaseListActivity implements AbsListView.OnScrollListener {

    private RatingBar credit_score, credit_score1, credit_score2, credit_score3;
    private String user_phone;
    private float user_score;
    private View shipperInfoLayout;
    private TextView tvName, tvCompanyName, tvAddr, tvPhone, tvAccountName;
    private ImageView mPhoto;
    private ShipperInfo shipperInfo = new ShipperInfo();
    private DriverInfo driverInfo = new DriverInfo();
    // 当前模式
    private int state = WAIT;
    // 当前页码
    private int pageIndex = 1;
    // 滑动状态
    private boolean state_idle = false;
    // 已加载全部
    private boolean load_all = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_credit_header_view, null);
        mListView.addHeaderView(view);
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
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvAddr = (TextView) findViewById(R.id.tv_addr);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAccountName = (TextView) findViewById(R.id.tv_account_name);
    }

    // 初始化数据
    private void initData() {
        int user_id = 0;
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if (serializable instanceof DriverInfo) {
            //司机信誉
            driverInfo = (DriverInfo) serializable;
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("司机信誉");

            tvName.setText(driverInfo.getName());
            tvPhone.setText(driverInfo.getPhone());
            tvAccountName.setText(driverInfo.getName());
            credit_score.setRating(driverInfo.getScore());
            credit_score1.setRating(driverInfo.getScroe1());
            credit_score2.setRating(driverInfo.getScroe2());
            credit_score3.setRating(driverInfo.getScroe3());
            ImageLoader.getInstance().displayImage(driverInfo.getId_card_photo(), mPhoto, options,
                    new Utils.MyImageLoadingListener(mContext, mPhoto));
            shipperInfoLayout.setVisibility(View.GONE);
        } else {
            //货主信誉
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("货主信誉");

            user_id = getIntent().getIntExtra("user_id", 0);
            user_phone = getIntent().getStringExtra("user_phone");
            user_score = getIntent().getFloatExtra("user_score", 0);
            credit_score.setRating(user_score == 0 ? 5 : user_score);
            float user_score1 = getIntent().getIntExtra("user_score1", 0);
            credit_score1.setRating(user_score1 == 0 ? 5 : user_score1);

            float user_score2 = getIntent().getIntExtra("user_score2", 0);
            credit_score2.setRating(user_score2 == 0 ? 5 : user_score2);

            float user_score3 = getIntent().getIntExtra("user_score3", 0);
            credit_score3.setRating(user_score3 == 0 ? 5 : user_score3);

            queryShipperInfo(user_id);
        }
        //请求评论数据
        queryData(user_id);
    }

    // 获取评论列表
    private void queryData(int userId) {
        final JSONObject jsonObject = new JSONObject();
        try {
            state = ISREFRESHING;
            jsonObject.put(Constants.ACTION, Constants.GET_USER_REPLY);
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
                                        List<EvaluateInfo> list = (List<EvaluateInfo>) result;
                                        mAdapter.addAll(list);
                                        if (list.size() < 10) {
                                            load_all = true;
                                        }
                                    }
                                    break;
                            }
                            state = WAIT;
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
                                    tvAccountName.setText(shipperInfo.getName());
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
