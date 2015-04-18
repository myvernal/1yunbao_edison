package com.maogousoft.logisticsmobile.driver.activity.other;

import android.os.Bundle;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

/**
 * 关于我们
 * @author admin
 *
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_about);
		initViews();
	}
	
	private void initViews(){

		((TextView)findViewById(R.id.titlebar_id_content)).setText(R.string.string_other_about_title);
	}
}
