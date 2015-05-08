package com.maogousoft.logisticsmobile.driver.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class BaseListActivity extends BaseActivity {

    protected View mCitySelectView;
	protected ListView mListView;

	protected BaseListAdapter mAdapter;

	protected TextView mStandardEmptyView;

	protected View mProgressContainer;

	protected View mListContainer;

	protected CharSequence mEmptyText;

	protected boolean mListShown;

	private AnimationDrawable animationDrawable;

	// 当前处理加载数据中
	protected final static int ISREFRESHING = 0;
	// 当前没有加载数据
	protected final static int WAIT = 1;

	final private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			onListItemClick((ListView) parent, v, position, id);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_list);
		ensureList();
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
	}

	public void setListAdapter(BaseListAdapter adapter) {
		mAdapter = adapter;
		mListView.setAdapter(mAdapter);
	}

	public void setSelection(int position) {
		ensureList();
		mListView.setSelection(position);
	}

	public int getSelectedItemPosition() {
		ensureList();
		return mListView.getSelectedItemPosition();
	}

	public long getSelectedItemId() {
		ensureList();
		return mListView.getSelectedItemId();
	}

	/**
	 * Get the activity's list view widget.
	 */
	public ListView getListView() {
		ensureList();
		return mListView;
	}

	public void setEmptyText(CharSequence text) {
		ensureList();
		if (mStandardEmptyView == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		mStandardEmptyView.setText(text);
		if (mEmptyText == null) {
			mListView.setEmptyView(mStandardEmptyView);
		}
		mEmptyText = text;
	}

	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}

    public View getProgressContainer() {
        return mProgressContainer;
    }

	private void setListShown(boolean shown, boolean animate) {
		ensureList();
		if (mProgressContainer == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (shown) {
			if (animate) {
				animationDrawable.start();
				// mProgressContainer.startAnimation(AnimationUtils.loadAnimation(this,
				// android.R.anim.fade_out));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						this, android.R.anim.fade_in));
			} else {
				animationDrawable.stop();
				// mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				animationDrawable.start();
				// mProgressContainer.startAnimation(AnimationUtils.loadAnimation(this,
				// android.R.anim.fade_in));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						this, android.R.anim.fade_out));
			} else {
				animationDrawable.stop();
				// mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
		}
	}

	public ListAdapter getListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mListView != null) {
			return;
		}
        mCitySelectView = findViewById(R.id.common_city_selected);
		mStandardEmptyView = (TextView) findViewById(android.R.id.empty);
		mProgressContainer = findViewById(R.id.progressContainer);
		ImageView ivProgress = (ImageView) findViewById(R.id.iv_progress);
		ivProgress.setBackgroundResource(R.drawable.animation_progress);
		animationDrawable = (AnimationDrawable) ivProgress.getBackground();

		mListContainer = findViewById(R.id.listContainer);
		mListView = (ListView) findViewById(android.R.id.list);
		if (mListView == null) {
			throw new RuntimeException(
					"Your content must have a ListView whose id attribute is "
							+ "'android.R.id.list'");
		}
		mListView.setOnScrollListener(new PauseOnScrollListener(true, false));
		mStandardEmptyView.setText(mEmptyText);
		mListView.setEmptyView(mStandardEmptyView);
		mListShown = true;
		mListView.setOnItemClickListener(mOnClickListener);
		if (mProgressContainer != null) {
			setListShown(false, false);
		}
	}
}
