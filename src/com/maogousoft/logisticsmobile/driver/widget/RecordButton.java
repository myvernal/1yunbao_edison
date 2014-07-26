package com.maogousoft.logisticsmobile.driver.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.utils.RecorderAndPlayerbackMediaRecorderImpl;

/**
 * 录音按钮
 * 
 * @author lenovo
 */
public class RecordButton extends Button {
	
	private RecorderAndPlayerbackMediaRecorderImpl audioRecorderAndPlaybackInterface;

	private Context mContext;

	private Handler mVolumeHandler;
	
	private Handler mTimeHander;

	private Dialog mRecorderIndicator;
	
	private TextView mTime;

	private ImageView mVolume;
	
	private ObtainDecibelThread mThread;
	
	private OnRecordFinishListener mListener;
	
	private Timer timer=new Timer();
	
	//用于更新录音时间
	private TimerTask mTask;
	
	//录音时长
	private int count=0;
	
	private final int[] icons = { R.drawable.amp1, R.drawable.amp2, R.drawable.amp3, R.drawable.amp4 };

	public RecordButton(Context context) {
		super(context);
		init(context);
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void setOnRecordFinishListener(OnRecordFinishListener listener){
		this.mListener=listener;
	}

	private void init(Context context) {
		this.mContext = context;
		audioRecorderAndPlaybackInterface = new RecorderAndPlayerbackMediaRecorderImpl(
				getContext());
		mVolumeHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what < icons.length) {
					if (mVolume != null) {
						mVolume.setImageResource(icons[msg.what]);
					}
				}
			}
		};
		mTimeHander=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mTime!=null) {
					mTime.setText(msg.what+"S");
				}
			};
		};
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (event.getY() < 0) {
			System.out.println("取消录音");
			stopRecord();
			audioRecorderAndPlaybackInterface.cancelRecording();
			if (mListener!=null) {
				mListener.cancel(audioRecorderAndPlaybackInterface.getAudioFilePath());
			}
			return true;
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			startRecord();
			break;
		case MotionEvent.ACTION_CANCEL:
			audioRecorderAndPlaybackInterface.cancelRecording();
			if (mListener!=null) {
				mListener.cancel(audioRecorderAndPlaybackInterface.getAudioFilePath());
			}
			break;
		case MotionEvent.ACTION_UP:
			stopRecord();
			if (mListener!=null) {
				mListener.finish(audioRecorderAndPlaybackInterface.getAudioFilePath());
			}
			break;

		default:
			break;
		}
		return true;
	}

	// 开始录音
	private void startRecord() {
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_audiorecord, null);
		mVolume = (ImageView) view.findViewById(R.id.recorder_volume);
		mTime=(TextView)view.findViewById(R.id.recorder_time);

		mRecorderIndicator = new Dialog(mContext, R.style.DialogTheme);
		mRecorderIndicator.setContentView(view);
		mRecorderIndicator.setCanceledOnTouchOutside(false);
		mThread=new ObtainDecibelThread();
		audioRecorderAndPlaybackInterface.startRecording();
		mThread.start();
		mRecorderIndicator.show();
		count=0;
		if (timer!=null) {
			if (mTask!=null) {
				mTask.cancel();
			}
			mTask=new UpdateTask();
			timer.schedule(mTask, 0, 1000);
		}
	}

	// 停止录音
	private void stopRecord() {
		if (mRecorderIndicator != null) {
			mRecorderIndicator.dismiss();
		}
		if (mTask!=null) {
			mTask.cancel();
			mTask=null;
		}
		if (mThread!=null) {
			mThread.exit();
			mThread=null;
		}
		audioRecorderAndPlaybackInterface.stopRecording();
	}
	
	private class UpdateTask extends TimerTask{
		@Override
		public void run() {
			count++;
			mTimeHander.sendEmptyMessage(count);
		}
	}

	private class ObtainDecibelThread extends Thread {

		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					sleep(200);
				} catch (Exception e) {
				}
				if (!running) {
					break;
				}
				int x = audioRecorderAndPlaybackInterface.getMaxAmplitude();
				if (x != 0) {
					int f = (int) (10 * Math.log(x) / Math.log(10));
					if (f < 26) {
						mVolumeHandler.sendEmptyMessage(0);
					} else if (f < 32) {
						mVolumeHandler.sendEmptyMessage(1);
					} else if (f < 38) {
						mVolumeHandler.sendEmptyMessage(2);
					} else {
						mVolumeHandler.sendEmptyMessage(3);
					}
				}
			}
		}
	}
	
	public interface OnRecordFinishListener{
		public void finish(String path);
		
		public void failed(String path);
		
		public void cancel(String path);
	}
}
