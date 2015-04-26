package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

public class CheckSafeActivity extends BaseActivity implements OnClickListener {

	private View mSeaBtn, mPinganBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_safe);
		findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);

		((TextView) findViewById(R.id.titlebar_id_content)).setText("货运保险");

        mSeaBtn = findViewById(R.id.safeSeaBtn);
        mSeaBtn.setOnClickListener(this);
        mPinganBtn = findViewById(R.id.safePinanBtn);
        mPinganBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
        switch (v.getId()) {
            case R.id.safePinanBtn:
                startActivity(new Intent(mContext, SafePinanActivity.class));
                break;
            case R.id.safeSeaBtn:
                startActivity(new Intent(mContext, SafeSeaActivity.class));
                break;
        }
	}
}
