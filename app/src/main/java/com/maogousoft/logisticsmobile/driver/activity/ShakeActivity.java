package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.NewSourceActivity;

/**
 * Created by Edison on 2015/3/29.
 */
public class ShakeActivity extends BaseActivity {
    private ShakeListener mShakeListener = null;
    private Vibrator mVibrator;
    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_layout);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("摇一摇");

        //drawerSet ();//设置  drawer监听    切换 按钮的方向
        mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
        mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
        mShakeListener = new ShakeListener(context);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                //Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
                startAnim();  //开始 摇一摇手掌动画
                mShakeListener.stop();

                startVibrato(); //开始 震动
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
                        mVibrator.cancel();
                        mShakeListener.start();*/
                        Intent intent = new Intent(context, NewSourceActivity.class);
                        intent.putExtra(Constants.SEARCH_SOURCE, true);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        });
    }

    public void startAnim() {   //定义摇一摇动画动画
        AnimationSet animUp = new AnimationSet(true);
        TranslateAnimation animationStep0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        animationStep0.setDuration(500);
        TranslateAnimation animationStep1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        animationStep1.setDuration(500);
        animationStep1.setStartOffset(500);
        animUp.addAnimation(animationStep0);
        animUp.addAnimation(animationStep1);
        mImgUp.startAnimation(animUp);

        AnimationSet animDown = new AnimationSet(true);
        TranslateAnimation animationStep2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        animationStep2.setDuration(500);
        TranslateAnimation animationStep3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        animationStep3.setDuration(500);
        animationStep3.setStartOffset(500);
        animDown.addAnimation(animationStep2);
        animDown.addAnimation(animationStep3);
        mImgDn.startAnimation(animDown);
    }

    public void startVibrato() {
        MediaPlayer player;
        player = MediaPlayer.create(this, R.raw.shake);
        player.setLooping(false);
        player.start();
        //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
        //定义震动
        mVibrator.vibrate(new long[]{500, 200, 500, 200}, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShakeListener != null) {
            mShakeListener.stop();
        }
    }
}

/**
 * 一个检测手机摇晃的监听器
 */
class ShakeListener implements SensorEventListener {
    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 3000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 70;
    // 传感器管理器
    private SensorManager sensorManager;
    // 传感器
    private Sensor sensor;
    // 重力感应监听器
    private OnShakeListener onShakeListener;
    // 上下文
    private Context mContext;
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;
    // 上次检测时间
    private long lastUpdateTime;

    // 构造器
    public ShakeListener(Context c) {
        // 获得监听对象
        mContext = c;
        start();
    }

    // 开始
    public void start() {
        // 获得传感器管理器
        sensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // 获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        // 注册
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

    }

    // 停止检测
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    // 设置重力感应监听器
    public void setOnShakeListener(OnShakeListener listener) {
        onShakeListener = listener;
    }

    // 重力感应器感应获得变化数据
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;
        //sqrt 返回最近的双近似的平方根
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ) / timeInterval * 10000;
        Log.v("thelog", "===========log===================");
        // 达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {
            onShakeListener.onShake();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 摇晃监听接口
    public interface OnShakeListener {
        public void onShake();
    }

}