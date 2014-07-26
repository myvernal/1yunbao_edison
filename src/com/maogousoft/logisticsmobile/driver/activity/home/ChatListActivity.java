package com.maogousoft.logisticsmobile.driver.activity.home;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.MessageListAdapter;
import com.maogousoft.logisticsmobile.driver.db.DBUtils;
import com.maogousoft.logisticsmobile.driver.im.SendMessageCallback;
import com.maogousoft.logisticsmobile.driver.model.MessageInfo;
import com.maogousoft.logisticsmobile.driver.model.OnlineSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.FileCache;
import com.maogousoft.logisticsmobile.driver.utils.PickPhoto;
import com.maogousoft.logisticsmobile.driver.widget.RecordButton;
import com.maogousoft.logisticsmobile.driver.widget.RecordButton.OnRecordFinishListener;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

/**
 * 对话界面
 * 
 * @author admin
 */
public class ChatListActivity extends BaseActivity {

	// 默认将消息发送给对方
	private static String CHAT_TO = null;
	// 选择照片的requstCode
	private final int CAMERA_CODE = 1000, GALLERY_CODE = 1001;
	private final String[] proj = { MediaStore.Images.Media.DATA };

	private Button mBack;
	private Button mSend;
	private RecordButton mPress;
	private ImageButton mKeyboard, mImage;
	private EditText mContent;

	private ListView mListView;

	private MessageListAdapter mAdapter;

	// 选择图片后输出uri
	private Uri mOutUri;
	// 输入法管理
	private InputMethodManager mInputMethodManager;
	private FileCache mFileCache;

	private DBUtils mDbUtils;

	private MessageReceiver receiver;

	private IntentFilter filter;

	// 得到当前聊天的对象
	public static String getCurrentChat() {
		return CHAT_TO;
	}

	// 发送消息的回调接口
	private SendMessageCallback callback;

	// 图片和语音消息回调接口
	private SendMessageCallback fileMsgCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_chat);
		initViews();
		initUtils();
		initData();
	}

	// 初始化视图控件
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_home_chat_title);
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mPress = (RecordButton) findViewById(R.id.home_id_chat_press);
		mSend = (Button) findViewById(R.id.home_id_chat_send);
		mKeyboard = (ImageButton) findViewById(R.id.home_id_chat_keyboard);
		mImage = (ImageButton) findViewById(R.id.home_id_chat_img);
		mContent = (EditText) findViewById(R.id.home_id_chat_content);

		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setOnScrollListener(new PauseOnScrollListener(true, false));

		mBack.setOnClickListener(this);
		mSend.setOnClickListener(this);
		mKeyboard.setOnClickListener(this);
		mImage.setOnClickListener(this);
		mPress.setOnRecordFinishListener(mOnRecordFinishListener);

	}

	// 初始化工具类
	private void initUtils() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		receiver = new MessageReceiver();
		filter = new IntentFilter(Constants.CURRENT_MESSAGE_RECEIVE);
		registerReceiver(receiver, filter);
		mFileCache = new FileCache(context);

		mDbUtils = new DBUtils(sdb);
		callback = new SendMessageCallback() {

			@Override
			public void success(MessageInfo messageInfo) {
				messageInfo.setMsgState(0);
				mAdapter.notifyDataSetChanged();
				mDbUtils.updateMsgState(messageInfo.getMsgId(), 0);
			}

			@Override
			public void start(MessageInfo messageInfo) {
				messageInfo.setMsgState(0);
				mAdapter.add(messageInfo);
				mDbUtils.addMessage(messageInfo);
				mListView.smoothScrollToPosition(mAdapter.getCount());
			}

			@Override
			public void failed(MessageInfo messageInfo) {
				messageInfo.setMsgState(1);
				mAdapter.notifyDataSetChanged();
				mDbUtils.updateMsgState(messageInfo.getMsgId(), 1);
			}
		};

		fileMsgCallback = new SendMessageCallback() {

			@Override
			public void success(MessageInfo messageInfo) {
				messageInfo.setMsgState(0);
				mAdapter.notifyDataSetChanged();
				mDbUtils.updateMsgState(messageInfo.getMsgId(), 0);
			}

			@Override
			public void start(MessageInfo messageInfo) {
				messageInfo.setMsgState(0);
				mAdapter.add(messageInfo);
				mDbUtils.addMessage(messageInfo);
				mListView.smoothScrollToPosition(mAdapter.getCount());
			}

			@Override
			public void failed(MessageInfo messageInfo) {
				messageInfo.setMsgState(1);
				mAdapter.notifyDataSetChanged();
				mDbUtils.updateMsgContent(messageInfo.getMsgId(),
						messageInfo.getMsgContent());
				mDbUtils.updateMsgState(messageInfo.getMsgId(), 1);
			}
		};
	}

	// 查询聊天记录
	private void initData() {

		if (getIntent().hasExtra("user_id")) {
			String userId = getIntent().getStringExtra("user_id");
			if (userId.contains("u")) {
				CHAT_TO = userId;
			} else {
				CHAT_TO = "u" + userId;
			}

		} else {
			// 获取货主id错误。无法聊天
			finish();
			return;
		}

		mAdapter = new MessageListAdapter(context);

		if (getIntent().hasExtra("user_name")) {
			mAdapter.setChatObject(application.getDriverName(), getIntent()
					.getStringExtra("user_name"));
		} else {
			mAdapter.setChatObject(application.getDriverName(), "货主");
		}

		mListView.setAdapter(mAdapter);
		List<MessageInfo> mList = mDbUtils.queryMessage(
				application.getDriverId(), CHAT_TO);
		mAdapter.setList(mList);
	}

	private OnRecordFinishListener mOnRecordFinishListener = new OnRecordFinishListener() {

		@Override
		public void finish(String path) {
			MessageInfo m1 = new MessageInfo();
			m1.setMsgContent(path);
			m1.setMsgFrom(application.getDriverId());
			m1.setMsgTo(CHAT_TO);
			m1.setMsgType(3);
			m1.setAudioTime(50);
			m1.setMsgId(System.currentTimeMillis());
			m1.setCreateUser(application.getDriverId());
			mBinder.sendMessage(m1, fileMsgCallback);
		}

		@Override
		public void failed(String path) {

		}

		@Override
		public void cancel(String path) {

		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		final int id = v.getId();
		switch (id) {
		case R.id.home_id_chat_img:
			new AlertDialog.Builder(context)
					.setTitle(R.string.string_tips_select)
					.setItems(R.array.select_photo,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										mOutUri = mFileCache.makeImageUrl();
										PickPhoto.takePhoto(
												ChatListActivity.this, mOutUri,
												CAMERA_CODE);
									} else if (which == 1) {
										PickPhoto.pickPhoto(
												ChatListActivity.this,
												GALLERY_CODE);
									}
								}
							}).show();
			break;
		case R.id.home_id_chat_keyboard:
			// 如果当前是发送语音模式
			if (mPress.isShown()) {
				mPress.setVisibility(View.GONE);
				mContent.setVisibility(View.VISIBLE);
				mSend.setVisibility(View.VISIBLE);
				mContent.requestFocus();
				mInputMethodManager.showSoftInputFromInputMethod(
						mContent.getApplicationWindowToken(), 0);
				mKeyboard.setImageResource(R.drawable.selector_chat_audio);
			} else {
				mInputMethodManager.hideSoftInputFromWindow(
						mContent.getApplicationWindowToken(), 0);
				mContent.setVisibility(View.GONE);
				mSend.setVisibility(View.GONE);
				mPress.setVisibility(View.VISIBLE);
				mKeyboard.setImageResource(R.drawable.selector_chat_keyboard);
			}
			break;
		case R.id.home_id_chat_send:
			if (TextUtils.isEmpty(mContent.getText())) {
				return;
			}
			MessageInfo m1 = new MessageInfo();
			m1.setMsgContent(mContent.getText().toString());
			m1.setMsgFrom(application.getDriverId());
			m1.setMsgTo(CHAT_TO);
			m1.setMsgType(1);
			m1.setMsgId(System.currentTimeMillis());
			m1.setCreateUser(application.getDriverId());
			mBinder.sendMessage(m1, callback);
			mContent.getText().clear();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mOutUri != null) {
			outState.putParcelable("outUri", mOutUri);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("outUri")) {
			mOutUri = savedInstanceState.getParcelable("outUri");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(receiver);
			CHAT_TO = null;
		} catch (Exception e) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CAMERA_CODE:
			if (mOutUri != null) {
				final String output = mOutUri.getPath();
				MessageInfo m1 = new MessageInfo();
				m1.setMsgContent(PickPhoto.scalePicture(context, output, 480,
						800));
				m1.setMsgFrom(application.getDriverId());
				m1.setMsgTo(CHAT_TO);
				m1.setMsgType(2);
				m1.setMsgId(System.currentTimeMillis());
				m1.setCreateUser(application.getDriverId());
				if (mBinder != null) {
					mBinder.sendMessage(m1, fileMsgCallback);
				}
			}
			break;
		case GALLERY_CODE:
			Cursor cursor = getContentResolver().query(data.getData(), proj,
					null, null, null);
			int index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String out = cursor.getString(index);
			mOutUri = Uri.fromFile(new File(out));
			cursor.close();
			MessageInfo m1 = new MessageInfo();
			m1.setMsgContent(PickPhoto.scalePicture(context, out, 480, 800));
			m1.setMsgFrom(application.getDriverId());
			m1.setMsgTo(CHAT_TO);
			m1.setMsgType(2);
			m1.setCreateUser(application.getDriverId());
			m1.setMsgId(System.currentTimeMillis());
			if (mBinder != null) {
				mBinder.sendMessage(m1, fileMsgCallback);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(Constants.CURRENT_MESSAGE_RECEIVE)) {
				MessageInfo messageInfo = intent
						.getParcelableExtra(Constants.XMPP_MESSAGE);
				mAdapter.add(messageInfo);
				mListView.smoothScrollToPosition(mAdapter.getCount());
			}
		}
	}
}
