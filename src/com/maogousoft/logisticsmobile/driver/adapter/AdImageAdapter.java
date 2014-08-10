package com.maogousoft.logisticsmobile.driver.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressWarnings("deprecation")
public class AdImageAdapter extends BaseAdapter {

    private static final String TAG = "AdImageAdapter";
	private Context context;
	private LayoutParams layoutParams;
	private String[] urlArray;
	private Map<String, ImageView> viewMap;
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;

	public AdImageAdapter(Context ctx, String[] urlStringArray,ImageLoader mImageLoader) {
		context = ctx;
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// 数组拷贝 add by edison 2012-9-26
		urlArray = new String[urlStringArray.length];
		System.arraycopy(urlStringArray, 0, urlArray, 0, urlStringArray.length);
		viewMap = new HashMap<String, ImageView>();
        options = new DisplayImageOptions.Builder().resetViewBeforeLoading()
                .cacheOnDisc().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        this.mImageLoader = mImageLoader;
	}

    public void setNewAdList(String[] urlStringArray) {
        urlArray = urlStringArray;
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public int getDrawableCount() {
		return urlArray.length;
	}

	@Override
	public boolean isEmpty() {
		return (urlArray == null || urlArray.length == 0);
	}

	@Override
	public Object getItem(int position) {
		return urlArray[position % urlArray.length];
	}

	@Override
	public long getItemId(int position) {
		return position % urlArray.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position % urlArray.length;
		
		// 添加下面的广告缓存
		if (viewMap.containsKey(urlArray[pos])) {
			ImageView v = viewMap.get(urlArray[pos]);
			if (v != null) {
				return v;
			}
		}

		final ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //异步获取图片
        if(urlArray[pos].contains("http:")) {
            LogUtil.d(TAG, "img url:" + urlArray[pos]);
            mImageLoader.displayImage(urlArray[pos], imageView, options);
        } else {
            imageView.setImageResource(Integer.valueOf(urlArray[pos]));
        }
		viewMap.put(urlArray[pos], imageView);
		return imageView;
	}
}
