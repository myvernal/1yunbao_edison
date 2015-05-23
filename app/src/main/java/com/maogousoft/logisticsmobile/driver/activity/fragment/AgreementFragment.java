package com.maogousoft.logisticsmobile.driver.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListFragment;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.AgreementPreviewActivity;
import com.maogousoft.logisticsmobile.driver.adapter.AgreementAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.InvoiceAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AgreementInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2015/4/25.
 */
public class AgreementFragment extends BaseListFragment implements AbsListView.OnScrollListener {
    private static final String TAG = "AgreementFragment";
    private int agreementType = 0;
    // 底部更多
    private View mFootView;
    private ProgressBar mFootProgress;
    private TextView mFootMsg;
    // 当前模式
    private int state = WAIT;
    // 当前页码
    private int pageIndex = 1;
    // 滑动状态
    private boolean state_idle = false;
    // 已加载全部
    private boolean load_all = false;

    public static AgreementFragment newInstance(int agreementType) {
        //1 执行中的合同 2近半月的合同 4历史合同
        AgreementFragment newFragment = new AgreementFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.AGREEMENT_TYPE, agreementType);
        newFragment.setArguments(bundle);
        //bundle还可以在每个标签里传送数据
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Bundle args = getArguments();
        agreementType = args.getInt(Constants.AGREEMENT_TYPE, 0);
        // 页脚信息
        mFootView = LayoutInflater.from(mContext).inflate(R.layout.listview_footview, null);
        mFootView.setClickable(false);
        mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
        mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
        mListView.addFooterView(mFootView);
        mListView.setOnScrollListener(this);
        mAdapter = new AgreementAdapter(mContext, application.getUserType());
        setListAdapter(mAdapter);
        setListShown(false);

        getData(agreementType, pageIndex);
        return view;
    }

    // 根据条件请求指定页数的数据
    private void getData(int invoiceType, int page) {
        try {
            state = ISREFRESHING;
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_CONTRACT_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject json = new JSONObject();
            json.put("page", page);
            json.put("contract_status", invoiceType);
            if (application.getUserType() == Constants.USER_DRIVER) {
                json.put("driver_id", application.getUserId());
            } else {
                json.put("user_id", application.getUserId());
            }
            jsonObject.put(Constants.JSON, json.toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AgreementInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            setListShown(true);
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<AgreementInfo> mList = (List<AgreementInfo>) result;
                                        if (mList == null || mList.isEmpty()) {
                                            load_all = true;
                                            mFootProgress.setVisibility(View.GONE);
                                            mFootMsg.setText("已加载全部");
                                        } else {
                                            if (mList.size() < 10) {
                                                load_all = true;
                                                mFootProgress.setVisibility(View.GONE);
                                                mFootMsg.setText("已加载全部");
                                            }
                                            mAdapter.addAll(mList);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;

                                default:
                                    break;
                            }
                            if (mAdapter.isEmpty()) {
                                setEmptyText("没有找到数据哦");
                            }
                            state = WAIT;
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String testUrl = "http://112.124.33.14:8083/service/page/contract?order_id=2005&driver_phone=13683427216&user_id=2&type=2";
        final Intent intent = new Intent(mContext, AgreementPreviewActivity.class);
        //intent.putExtra(Constants.COMMON_KEY, ((InvoiceAdapter) mAdapter).getList().get(position).getId());
        intent.putExtra(Constants.COMMON_KEY, testUrl);
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            return;
        }
        if (state != WAIT) {
            return;
        }
        this.state_idle = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!this.state_idle) {
            return;
        }
        if (firstVisibleItem == 0
                || (firstVisibleItem + visibleItemCount) != totalItemCount) {
            return;
        }
        // 如果当前没有加载数据
        if (state != ISREFRESHING && !load_all) {
            getData(agreementType, ++pageIndex);
            mFootProgress.setVisibility(View.VISIBLE);
            mFootMsg.setText(R.string.tips_isloading);
        }
    }
}
