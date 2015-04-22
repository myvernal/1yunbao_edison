package com.maogousoft.logisticsmobile.driver.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.R;

/**
 * Created by aliang on 2015/4/19.
 */
public class HeaderView extends LinearLayout implements View.OnClickListener {

    private TextView mTitle;
    private TextView mMore;
    private TextView mTip;
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
        mMore = (TextView) view.findViewById(R.id.titlebar_id_more);
        mTip = (TextView) view.findViewById(R.id.titlebar_id_tip);
        mBack = view.findViewById(R.id.titlebar_id_back);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_id_back:
                ((Activity)mContext).finish();
                break;
        }
    }
}
