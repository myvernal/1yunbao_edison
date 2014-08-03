package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.maogousoft.logisticsmobile.driver.R;

/**
 * Created by aliang on 2014/8/2.
 */
public class OneGalleryBottomView extends LinearLayout {
    private Context mContext;
    private View adBottomOne;
    private View adBottomTwo;
    private View adBottomThree;

    public OneGalleryBottomView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public OneGalleryBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.top_ad_view_layout, this, true);
        adBottomOne = view.findViewById(R.id.ad_bottom_one);
        adBottomTwo = view.findViewById(R.id.ad_bottom_two);
        adBottomThree = view.findViewById(R.id.ad_bottom_three);
    }

    public void setHighLight(int position) {
        switch (position) {
            case 0:
                adBottomOne.setBackgroundColor(getResources().getColor(R.color.TextColorRed));
                adBottomTwo.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                adBottomThree.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                break;
            case 1:
                adBottomOne.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                adBottomTwo.setBackgroundColor(getResources().getColor(R.color.TextColorRed));
                adBottomThree.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                break;
            case 2:
                adBottomOne.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                adBottomTwo.setBackgroundColor(getResources().getColor(R.color.font_gray3));
                adBottomThree.setBackgroundColor(getResources().getColor(R.color.TextColorRed));
                break;
        }
    }
}
