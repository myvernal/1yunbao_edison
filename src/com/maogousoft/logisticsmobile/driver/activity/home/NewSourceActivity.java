package com.maogousoft.logisticsmobile.driver.activity.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.NewSourceListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.SourceCount;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.model.FocusLineInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新货源列表
 *
 * @author lenovo
 */
public class NewSourceActivity extends BaseListActivity implements
        OnScrollListener {

    private Context mContext;// PR104
    private Button mBack, mMore;
    private int rightButton = 0;// 0显示查找货源，1显示关注此线路,2不显示此button
    // 底部更多
    private View mFootView, mHeaderView;
    private ProgressBar mFootProgress;
    private TextView mFootMsg, titlebar_id_content;
    private String params;//路线快捷搜索的信息
    // 当前模式
    private int state = WAIT;
    // 当前页码
    private int pageIndex = 1;
    // 滑动状态
    private boolean state_idle = false;
    // 已加载全部
    private boolean load_all = false;

    private List<NewSourceInfo> newSourceInfos = null;// 上一页传来的
    private FocusLineInfo focusLineInfo = new FocusLineInfo();// 上一页传输过来的省市区
    //请求类型
    private String queryType = Constants.QUERY_SOURCE_ORDER;
    private JSONObject queryParams = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = NewSourceActivity.this;// PR104
        initViews();
        initData();
        // onFeedback();// PR1.4 test
    }

    private void initViews() {
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mMore = (Button) findViewById(R.id.titlebar_id_more);
        mMore.setText("查找货源");
        setIsRightKeyIntoShare(false);

        titlebar_id_content = (TextView) findViewById(R.id.titlebar_id_content);
        titlebar_id_content.setText(R.string.string_title_newsource);
        // 页脚信息
        mFootView = getLayoutInflater().inflate(R.layout.listview_footview, null);

        mFootView.setClickable(false);
        mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
        mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
        mListView.addFooterView(mFootView);

        mListView.setOnScrollListener(this);
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);

        mAdapter = new NewSourceListAdapter(context);
        setListAdapter(mAdapter);
        setListShown(false);
    }

    private void initData() {
        if (getIntent().hasExtra(Constants.COMMON_OBJECT_KEY)) {
            String params = getIntent().getStringExtra(Constants.COMMON_KEY);
            try {
                queryParams = new JSONObject(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            newSourceInfos = (List<NewSourceInfo>) getIntent().getSerializableExtra(Constants.COMMON_OBJECT_KEY);
            // 从 搜索货源进入，不需要显示 搜索货源按钮 modify
            mMore.setText("关注此路线");
            rightButton = 1;
            mAdapter.addAll(sort(newSourceInfos));
            setListShown(true);
            if(newSourceInfos.size() < 10) {
                load_all = true;
                mFootProgress.setVisibility(View.GONE);
                mFootMsg.setText("已加载全部");
            }
            state = WAIT;
        }
        if (getIntent().hasExtra("focusLineInfo")) {
            focusLineInfo = (FocusLineInfo) getIntent().getSerializableExtra("focusLineInfo");
            params = getIntent().getStringExtra("params");
        }
        if (getIntent().hasExtra("getFriendOrderList")) {
            if (getIntent().getBooleanExtra("getFriendOrderList", false)) {
                queryType = Constants.FRIEND_ORDER_LIST;//好友货源
                mMore.setText("好友管理");
                titlebar_id_content.setText("好友货源");
                rightButton = 2;
            }
        }
        if (getIntent().hasExtra("QUERY_MAIN_LINE_ORDER")) {
            if (getIntent().getBooleanExtra("QUERY_MAIN_LINE_ORDER", false)) {
                queryType = Constants.QUERY_MAIN_LINE_ORDER;//关注货源
                mMore.setText("主营线路");
                titlebar_id_content.setText("关注货源");
                rightButton = 3;
            }
        }
        if(getIntent().getBooleanExtra(Constants.SEARCH_SOURCE, false)) {
            //从搜索过来的货源需要显示总货源数
            addHeaderSearchView();
        }
    }

    private void addHeaderSearchView() {
        mHeaderView = getLayoutInflater().inflate(R.layout.listview_header_search_layout, null);
        mHeaderView.setOnClickListener(this);
        mListView.addHeaderView(mHeaderView);
        //查询今日货源数量
        queryAllSourceSize();
    }

    @Override
    public void onClick(View v) {
        ((BaseListActivity) mContext).setIsRightKeyIntoShare(false);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                // mMore
                switch (rightButton) {
                    case 0:// 查找新货源
                        startActivity(new Intent(context, SearchSourceActivity.class));
                        finish();
                        break;
                    case 1:// 关注此线路
                        doFocus();
                        //Toast.makeText(mContext, "关注此线路成功", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(mContext,
                        // SearchSourceActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(context, MyFriendsActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context, MainLineActivity.class));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.search:
                startActivity(new Intent(context, SearchSourceActivity.class));
                break;
        }
        super.onClick(v);
    }

    // 点击关注此路线
    public void doFocus() {
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {

            jsonObject.put(Constants.ACTION, Constants.FOCUS_LINE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            if (focusLineInfo != null) {
                params.put("start_province", focusLineInfo.getStart_province());
                params.put("start_city", focusLineInfo.getStart_city());
                params.put("start_district", focusLineInfo.getStart_district());

                params.put("end_province", focusLineInfo.getEnd_province());
                params.put("end_city", focusLineInfo.getEnd_city());
                params.put("end_district", focusLineInfo.getEnd_district());
                params.put("type", application.getUserType());
            }
            jsonObject.put(Constants.JSON, params);
            showSpecialProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(mContext, "关注此线路成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());

                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null) {
                                        // 您当月的免费搜索次数已经用完

                                        // if (result.equals("您当月的免费搜索次数已经用完")) {
                                        final MyAlertDialog dialog = new MyAlertDialog(
                                                context);
                                        dialog.show();
                                        dialog.setTitle("提示");
                                        // 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
                                        dialog.setMessage(result.toString());
                                        dialog.setLeftButton("确定",
                                                new OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();

                                                        String content = null;
                                                        startActivity(new Intent(
                                                                context,
                                                                ShareActivity.class)
                                                                .putExtra("share",
                                                                        content));
                                                        finish();
                                                    }
                                                });

                                        // }
                                    }
                                    // showMsg(result.toString());
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

    private void queryAllSourceSize() {
        final JSONObject jsonObject = new JSONObject();
        try {
            // 搜索货源总数
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER_COUNT);
            jsonObject.put(Constants.TOKEN, application.getToken());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    SourceCount.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    //显示货源总数
                                    SourceCount sourceCount = (SourceCount) result;
                                    changeSourceNumber(sourceCount.getCount());
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null)
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position < mAdapter.getList().size()) {

            // 先检测是否已经完善了资料
            if (!application.checkIsRegOptional() && application.getUserType() == Constants.USER_DRIVER) {
                final MyAlertDialog dialog = new MyAlertDialog(context);
                dialog.show();
                dialog.setTitle("提示");
                dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
                dialog.setLeftButton("完善资料", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, OptionalActivity.class);
                        intent.putExtra("isFormRegisterActivity", false);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setRightButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                final Intent intent = new Intent(context, SourceDetailActivity.class);
                intent.putExtra(SourceDetailActivity.ORDER_ID,
                        ((NewSourceListAdapter) mAdapter).getList().get(position).getId());
                intent.putExtra("type", "NewSourceActivity");
                startActivityForResult(intent, 1000);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter.isEmpty()) {
            pageIndex = 1;
            getData(pageIndex);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 请求指定页数的数据
    private void getData(final int page) {
        try {
            state = ISREFRESHING;
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, queryType);
            jsonObject.put(Constants.TOKEN, application.getToken());
            if(TextUtils.isEmpty(params)) {
                jsonObject.put(Constants.JSON, queryParams.put("page", page).toString());
            } else {
                JSONObject json = new JSONObject(params);
                jsonObject.put(Constants.JSON, json.put("page", page).toString());
            }

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            setListShown(true);
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<NewSourceInfo> mList = (List<NewSourceInfo>) result;
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
                                            mAdapter.addAll(sort(mList));
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

    private void changeSourceNumber(int size) {
        if(mHeaderView != null) {
            ((TextView) mHeaderView.findViewById(R.id.number)).setText(getString(R.string.source_number, size * 16));
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != OnScrollListener.SCROLL_STATE_IDLE) {
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
            getData(++pageIndex);
            mFootProgress.setVisibility(View.VISIBLE);
            mFootMsg.setText(R.string.tips_isloading);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            System.out.println("执行onActivityResult");
            if (data.hasExtra("sourceInfo")) {
                NewSourceInfo sourceInfo = (NewSourceInfo) data
                        .getSerializableExtra("sourceInfo");
                try {
                    for (int i = 0; i < mAdapter.getList().size(); i++) {
                        if (sourceInfo.getId().intValue() == ((NewSourceInfo) mAdapter
                                .getList().get(i)).getId().intValue()) {
                            ((NewSourceInfo) mAdapter.getList().get(i))
                                    .setFavorite_status(sourceInfo
                                            .getFavorite_status());
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按照已关注，未读，已读 排序
     */
    public List<NewSourceInfo> sort(List<NewSourceInfo> list) {
        ArrayList<NewSourceInfo> listRestul = new ArrayList<NewSourceInfo>();

        if (list == null || list.size() == 0) {
            return listRestul;
        }

        for (int i = 0; i < list.size(); i++) {
            int status = list.get(i).getFavorite_status();
            if (status == 1) {
                listRestul.add(list.get(i));
            }
        }

        for (int y = 0; y < list.size(); y++) {
            int status = list.get(y).getFavorite_status();
            if (status != 1) {
                listRestul.add(list.get(y));
            }
        }

        return listRestul;

    }

    // PR104 begin

    /**
     * 货物状态反馈 货已拉走，价格太低，联系不上，虚假信息
     *
     * @param view
     */
    public void onFeedback(View view) {
        // public void onFeedback(){ only for test
        // Toast.makeText(mContext,R.string.feedback_msg,
        // Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setItems(R.array.feedback_type,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        Toast.makeText(mContext,
                                                R.string.feedback_hylz,
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(mContext,
                                                R.string.feedback_jgtd,
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(mContext,
                                                R.string.feedback_lxbs,
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Toast.makeText(mContext,
                                                R.string.feedback_xjxx,
                                                Toast.LENGTH_SHORT).show();
                                        break;

                                    default:
                                        Toast.makeText(mContext,
                                                R.string.feedback_error,
                                                Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
        builder.create().show();
    }
    // PR104 end
}
