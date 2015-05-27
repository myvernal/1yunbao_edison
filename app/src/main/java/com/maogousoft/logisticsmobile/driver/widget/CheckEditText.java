package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by EdisonZhao on 15/5/27.
 */
public class CheckEditText extends EditText {

    public CheckEditText(Context context) {
        super(context);
    }

    public CheckEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(CharSequence text, boolean isCheck) {
        if(isCheck && !TextUtils.isEmpty(text)) {
            setEnabled(false);
        }
        super.setText(text);
    }
}
