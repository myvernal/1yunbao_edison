package com.maogousoft.logisticsmobile.driver.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.maogousoft.logisticsmobile.driver.model.PopupMenuInfo;
import com.maogousoft.logisticsmobile.driver.service.SharedPreferencesProvider;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

import java.util.List;

/**
 * Created by aliang on 2015/4/19.
 */
public class HeaderView extends LinearLayout implements View.OnClickListener {

    private TextView mTitle;
    private Button mMore;
    private ImageView mTip;
    private View mBack;
    private Context mContext;
    //服务器配置彩蛋
    private final long btwTime = 2000;
    private long preEggTime;
    private int eggNum;

    public HeaderView(Context context) {
        super(context);
        init(context);
        mContext = context;
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        mContext = context;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_title, this, true);
        mTitle = (TextView) view.findViewById(R.id.titlebar_id_content);
        mMore = (Button) view.findViewById(R.id.titlebar_id_more);
        mTip = (ImageView) view.findViewById(R.id.titlebar_id_tip);
        mBack = view.findViewById(R.id.titlebar_id_back);
        mBack.setOnClickListener(this);
        mTip.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mTitle.setOnClickListener(this);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
        mTitle.setVisibility(VISIBLE);
    }

    public void setTitle(int titleId) {
        mTitle.setText(titleId);
        mTitle.setVisibility(VISIBLE);
    }

    public void setMoreTitle(String tip) {
        mMore.setText(tip);
        mMore.setVisibility(VISIBLE);
    }

    public void setMoreTitle(int tipId) {
        mMore.setText(tipId);
        mMore.setVisibility(VISIBLE);
    }

    public View getTipViewVisible() {
        mMore.setVisibility(GONE);
        mTip.setVisibility(VISIBLE);
        return mTip;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_id_back:
                ((Activity)mContext).finish();
                break;
            case R.id.titlebar_id_more:
                mContext.startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case R.id.titlebar_id_tip:
                mContext.startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case R.id.titlebar_id_content:
                if(eggNum <= 5) {
                    if ((System.currentTimeMillis() - preEggTime) > btwTime) {
                        eggNum++;
                    }
                } else {
                    //开启对话框,配置服务器地址
                    final MyAlertDialog dialog = new MyAlertDialog(mContext);
                    dialog.show();
                    dialog.setCancelable(false);
                    dialog.setTitle("修改服务器地址");
                    dialog.displayInputView();
                    //初始化地址
                    dialog.getInputView().setText(SharedPreferencesProvider.getInstance(mContext).getBaseUrl());
                    dialog.getInputView().setSelection(dialog.getInputView().length());
                    dialog.setLeftButton("保存", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (dialog.getInputView().length() == 0) {
                                Toast.makeText(mContext, "请输入服务器地址", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferencesProvider.getInstance(mContext).saveBaseUrl(dialog.getInputView().getText().toString());
                                Toast.makeText(mContext, "保存成功,请退出程序,重新登录", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    eggNum = 0;
                }
                break;
        }
    }

    public PopupWindow createPopupWindow(AdapterView.OnItemClickListener onItemClickListener, List<PopupMenuInfo> moreList) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);
        ListView lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);// popupwindow中的ListView
        PopupWindow pwMyPopWindow = new PopupWindow(layout);// popupwindow
        pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

        BaseListAdapter adapter = new PopupWindowAdapter(mContext);
        adapter.addAll(moreList);
        lvPopupList.setAdapter(adapter);
        lvPopupList.setOnItemClickListener(onItemClickListener);

        // 控制popupwindow的宽度和高度自适应
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20) * moreList.size());

        // 控制popupwindow点击屏幕其他地方消失
        pwMyPopWindow.setBackgroundDrawable(new BitmapDrawable());// 设置背景图片，不能在布局中设置，要通过代码来设置
        pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
        return pwMyPopWindow;
    }

    class PopupWindowAdapter extends BaseListAdapter<PopupMenuInfo> {

        public PopupWindowAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PopupMenuInfo popupMenuInfo = mList.get(position);
            convertView = mInflater.inflate(R.layout.list_item_popupwindow, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_list_item);
            textView.setText(popupMenuInfo.getName());
            textView.setCompoundDrawablesWithIntrinsicBounds(popupMenuInfo.getDrawableId(), 0, 0, 0);
            return convertView;
        }
    }
}
