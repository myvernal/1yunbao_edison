package com.maogousoft.logisticsmobile.driver.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.info.AgreementPreviewActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;

/**
 * Created by aliang on 2015/4/19.
 */
public class HeaderView extends LinearLayout implements View.OnClickListener {

    private TextView mTitle;
    private Button mMore;
    private ImageView mTip;
    private View mBack;
    private Context mContext;

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
        }
    }
}
