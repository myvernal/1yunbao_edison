package com.maogousoft.logisticsmobile.driver.activity.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.widget.GestureImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 图片浏览
 * 
 * @author lenovo
 */
public class ImageShowFragment extends Fragment {

	private ImageView mGestureImageView;

	private String imageUrl;

	private DisplayImageOptions options;

	private ImageLoader mImageLoader;

	public static ImageShowFragment newInstance(String imageUrl) {
		ImageShowFragment fragment = new ImageShowFragment();
		Bundle args = new Bundle();
		args.putString("imageUrl", imageUrl);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("imageUrl")) {
			imageUrl = savedInstanceState.getString("imageUrl");
		} else {
			imageUrl = getArguments().getString("imageUrl");
		}
		mImageLoader = ((MGApplication) getActivity().getApplication())
				.getImageLoader();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_image, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mGestureImageView = (ImageView) view.findViewById(R.id.image);
		options = new DisplayImageOptions.Builder().resetViewBeforeLoading()
				.cacheOnDisc().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!TextUtils.isEmpty(imageUrl)) {
			mImageLoader.displayImage(imageUrl, mGestureImageView, options);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("imageUrl", imageUrl);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mGestureImageView != null) {
			mImageLoader.cancelDisplayTask(mGestureImageView);
			mGestureImageView.setImageDrawable(null);
		}
	}
}
