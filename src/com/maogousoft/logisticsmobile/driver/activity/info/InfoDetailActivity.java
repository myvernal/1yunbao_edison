package com.maogousoft.logisticsmobile.driver.activity.info;

import android.os.Bundle;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.model.NoticeInfo;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 信息详情
 * 
 * @author admin
 */
public class InfoDetailActivity extends BaseActivity {

	private TextView mTitle, mType, mContent, mTime;

	private NoticeInfo noticeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_detail);
		initViews();
		initData();
	}

	private void initViews() {
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_info_title);
		mTitle = (TextView) findViewById(R.id.notify_title);
		mType = (TextView) findViewById(R.id.notify_type);
		mContent = (TextView) findViewById(R.id.notify_content);
		mTime = (TextView) findViewById(R.id.notify_time);
	}

	// 初始化数据
	private void initData() {
		if (getIntent().hasExtra("info")) {
			noticeInfo = getIntent().getParcelableExtra("info");
			mTitle.setText(noticeInfo.getTitle());
			mType.setText(noticeInfo.getCategory() == 0 ? "全站公告" : "私密个人信息");
			mContent.setText(noticeInfo.getContent());
			mTime.setText(String.format(getString(R.string.string_info_detail_time), TimeUtils.getDetailTime(noticeInfo.getCreate_time())));
		}
	}
}
