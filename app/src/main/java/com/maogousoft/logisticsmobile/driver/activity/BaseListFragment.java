package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.adapter.BaseListAdapter;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

/**
 * Created by aliang on 2015/4/26.
 */
public class BaseListFragment extends Fragment {
    public Context mContext;

    public View mCitySelectView;
    public ListView mListView;

    public BaseListAdapter mAdapter;

    public TextView mStandardEmptyView;

    public View mProgressContainer;

    public View mListContainer;

    public CharSequence mEmptyText;

    public boolean mListShown;

    private AnimationDrawable animationDrawable;
    public MGApplication application;
    private boolean isShown = false;
    private Toast toast;
    private View toastView;
    // 当前处理加载数据中
    public final static int ISREFRESHING = 0;
    // 当前没有加载数据
    public final static int WAIT = 1;

    final private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        application = (MGApplication) getActivity().getApplication();
        ensureList();
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list_fragment_layout, container, false);
        ensureList(view);
        return view;
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

    public void showMsg(int resId) {
        showMsg(getResources().getString(resId));
    }

    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg) || !isShown) {
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        if (null == toastView) {
            toastView = View.inflate(mContext, R.layout.view_toast, null);
        }
        ((TextView) toastView).setText(msg);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.setView(toastView);
        toast.show();
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
                mListContainer.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
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
                mListContainer.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
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

    public void ensureList() {
        ensureList(getView());
    }

    private void ensureList(View rootView) {
        if (mListView != null || rootView == null) {
            return;
        }
        mCitySelectView = rootView.findViewById(R.id.common_city_selected);
        mStandardEmptyView = (TextView) rootView.findViewById(android.R.id.empty);
        mProgressContainer = rootView.findViewById(R.id.progressContainer);
        ImageView ivProgress = (ImageView) rootView.findViewById(R.id.iv_progress);
        ivProgress.setBackgroundResource(R.drawable.animation_progress);
        animationDrawable = (AnimationDrawable) ivProgress.getBackground();

        mListContainer = rootView.findViewById(R.id.listContainer);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
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
