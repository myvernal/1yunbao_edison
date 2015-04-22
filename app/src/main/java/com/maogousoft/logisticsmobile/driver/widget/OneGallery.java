package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Gallery;
import android.widget.SpinnerAdapter;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.adapter.AdImageAdapter;

public class OneGallery extends Gallery {
    private Handler handler = new Handler();

    private Runnable runSlide = null;
    private boolean canScroll = false;
    private boolean isAutoScroll = false;
    private int totalCount = 0;

    private static final int SLIDE_ANIMATION = 900;
    private static final int SLIDE_DURATION = 5000;

    public OneGallery(Context context) {
        super(context);
        init();
    }

    public OneGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public OneGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (adapter == null || adapter.isEmpty() || !AdImageAdapter.class.isInstance(adapter)) {
            return;
        }
        totalCount = ((AdImageAdapter) adapter).getDrawableCount();
        canScroll = (totalCount > 2);

        super.setAdapter(adapter);
    }

    private void init() {
        setAnimationDuration(SLIDE_ANIMATION);
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.adv_animation);
        setLayoutAnimation(new LayoutAnimationController(a));

        runSlide = new Runnable() {
            @Override
            public void run() {
                slideToNext();

                if (isAutoScroll && canScroll) {
                    handler.postDelayed(this, SLIDE_DURATION);
                }
            }
        };
    }


    private void slideToNext() {
        if (canScroll) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (isAutoScroll) {
                setSlideRunnable(false);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (isAutoScroll) {
                setSlideRunnable(true);
            }
        }
        return super.onTouchEvent(event);
    }

    public void setAutoScroll(boolean autoScroll) {
        if (isAutoScroll && autoScroll) {
            return;
        }
        isAutoScroll = autoScroll;

        setSlideRunnable(isAutoScroll);
    }

    private void setSlideRunnable(boolean isAdd) {
        Log.i("OneGallery", "setSlideRunnable(" + isAdd + ", canScroll:" + canScroll);
        if (isAdd) {
            handler.postDelayed(runSlide, SLIDE_DURATION);
        } else {
            handler.removeCallbacks(runSlide);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() > e1.getX()) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
        } else {
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }

        return false;
    }

};
