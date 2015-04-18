package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.maogousoft.logisticsmobile.driver.R;

/**
 * Created by aliang on 2014/8/2.
 */
public class OneGalleryBottomView extends LinearLayout {
    private Context mContext;
    private ImageView adBottomOne;
    private ImageView adBottomTwo;
    private ImageView adBottomThree;

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
        adBottomOne = (ImageView) view.findViewById(R.id.ad_bottom_one);
        adBottomTwo = (ImageView) view.findViewById(R.id.ad_bottom_two);
        adBottomThree = (ImageView) view.findViewById(R.id.ad_bottom_three);
    }

    public void setHighLight(int position) {
        switch (position) {
            case 0:
                adBottomOne.setImageResource(R.drawable.ad_focused_true);
                adBottomTwo.setImageResource(R.drawable.ad_focused_false);
                adBottomThree.setImageResource(R.drawable.ad_focused_false);
                break;
            case 1:
                adBottomOne.setImageResource(R.drawable.ad_focused_false);
                adBottomTwo.setImageResource(R.drawable.ad_focused_true);
                adBottomThree.setImageResource(R.drawable.ad_focused_false);
                break;
            case 2:
                adBottomOne.setImageResource(R.drawable.ad_focused_false);
                adBottomTwo.setImageResource(R.drawable.ad_focused_false);
                adBottomThree.setImageResource(R.drawable.ad_focused_true);
                break;
        }
    }
}
