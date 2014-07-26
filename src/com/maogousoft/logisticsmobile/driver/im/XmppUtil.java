package com.maogousoft.logisticsmobile.driver.im;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.activity.home.ChatListActivity;
import com.maogousoft.logisticsmobile.driver.db.DBUtils;
import com.maogousoft.logisticsmobile.driver.model.MessageInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

public class XmppUtil {

	private static final String STATUS = "status";

	private static final String MESSAGE = "message";

	private static final String LOGTAG = LogUtil.makeLogTag(XmppUtil.class);

	private static final String XMPP_RESOURCE_NAME = "Android";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	private MGApplication application;

	private XmppMessageManager xmppMessageManager;

	private DBUtils dbUtils;

	public XmppUtil(NotificationService notificationService) {
		context = notificationService;
		application = (MGApplication) notificationService.getApplication();
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();
		xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "www.1yunbao.com");
		xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
		username = application.getDriverId();
		password = application.getPassword();
		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);
		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
		dbUtils = new DBUtils(application.getDB());
	}

	public Context getContext() {
		return context;
	}

	public void connect() {
		Log.d(LOGTAG, "connect()...");
		submitLoginTask();
	}

	public void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection() {
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			final XmppUtil xmppUtil = XmppUtil.this;

			public void run() {
				if (xmppUtil.isConnected()) {
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					// xmppUtil.getConnection().removePacketListener(xmppUtil.getNotificationPacketListener());
					if (xmppMessageManager != null) {
						xmppMessageManager.removeListener();
					}
					xmppUtil.getConnection().disconnect();
				}
				xmppUtil.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	public void startReconnectionThread() {
		synchronized (reconnection) {
			if (!reconnection.isAlive()) {
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler() {
		return handler;
	}

	public void reregisterAccount() {
		removeAccount();
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList() {
		return taskList;
	}

	public Future<?> getFutureTask() {
		return futureTask;
	}

	public void runTask() {
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}

	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated() {
		return connection != null && connection.isConnected() && connection.isAuthenticated();
	}

	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		submitConnectTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	private void removeAccount() {
		Editor editor = sharedPrefs.edit();
		editor.remove(Constants.XMPP_USERNAME);
		editor.remove(Constants.XMPP_PASSWORD);
		editor.commit();
	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		final XmppUtil xmppUtil;

		private ConnectTask() {
			this.xmppUtil = XmppUtil.this;
		}

		public void run() {
			Log.i(LOGTAG, "ConnectTask.run()...");

			if (!xmppUtil.isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(xmppHost, xmppPort);
				connConfig.setServiceName(xmppHost);
				connConfig.setSecurityMode(SecurityMode.enabled);
				connConfig.setCompressionEnabled(false);
				connConfig.setSendPresence(false);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);
				connConfig.setDebuggerEnabled(true);
				Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppUtil.setConnection(connection);

				try {
					// Connect to the server
					connection.connect();
					Log.i(LOGTAG, "XMPP connected successfully");

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification",
							new NotificationIQProvider());

				} catch (XMPPException e) {
					Log.e(LOGTAG, "XMPP connection failed", e);
				}

				xmppUtil.runTask();

			} else {
				Log.i(LOGTAG, "XMPP connected already");
				xmppUtil.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		final XmppUtil xmppUtil;

		private LoginTask() {
			this.xmppUtil = XmppUtil.this;
		}

		public void run() {
			Log.i(LOGTAG, "LoginTask.run()...");
			// 如果用户名和密码为空
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
				LogUtil.e(LOGTAG, "用户名或密码为空");
				return;
			}

			Log.d(LOGTAG, "username=" + username);
			Log.d(LOGTAG, "password=" + password);
			Log.d(LOGTAG, "password=" + MD5.encode(password));
			if (!xmppUtil.isAuthenticated()) {

				try {
					xmppUtil.getConnection().login(username, password, XMPP_RESOURCE_NAME);
					ServiceDiscoveryManager sdm = ServiceDiscoveryManager.getInstanceFor(connection);
					Presence presence = new Presence(Presence.Type.available);
					try {
						// 获取离线消息
						OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);
						offlineMessageManager.getMessages();
						offlineMessageManager.deleteMessages();
					} catch (Exception e) {
						e.printStackTrace();
					}
					connection.sendPacket(presence);
					Log.d(LOGTAG, "Loggedn in successfully");

					// connection listener
					if (xmppUtil.getConnectionListener() != null) {
						xmppUtil.getConnection().addConnectionListener(xmppUtil.getConnectionListener());
					}
					xmppMessageManager = new XmppMessageManager();

					// // packet filter
					// PacketFilter packetFilter = new
					// PacketTypeFilter(NotificationIQ.class);
					// // packet listener
					// PacketListener packetListener =
					// xmppUtil.getNotificationPacketListener();
					// connection.addPacketListener(packetListener,
					// packetFilter);
					if (!getConnection().isConnected()) {
						xmppUtil.runTask();
					}
					xmppUtil.runTask();
				} catch (XMPPException e) {
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						return;
					}
					// xmppUtil.startReconnectionThread();

				} catch (Exception e) {
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					// xmppUtil.startReconnectionThread();
				}
				xmppUtil.runTask();
			} else {
				Log.i(LOGTAG, "Logged in already");
				xmppUtil.runTask();
			}
		}
	}

	/** XMPP消息处理 **/
	private class XmppMessageManager implements ChatManagerListener {

		private ChatManager chatManager;

		public XmppMessageManager() {
			chatManager = connection.getChatManager();
			chatManager.addChatListener(this);
		}

		public void removeListener() {
			chatManager.removeChatListener(this);
		}

		@Override
		public void chatCreated(Chat chat, boolean blag) {
			chat.addMessageListener(new MessageListener() {

				@Override
				public void processMessage(Chat c, Message message) {
					if (message.getError() != null) {
						LogUtil.e(LOGTAG, "errorCode:" + message.getError().getCode() + "  errorMsg:"
								+ message.getError().getMessage());
						return;
					}

					LogUtil.e(LOGTAG, "XmppMessageManager 获取到消息");

					final MessageInfo messageInfo = new MessageInfo();
					final String msgFrom = message.getFrom().split("@")[0];
					final String msgTo = message.getTo().split("@")[0];
					final Collection<String> names = message.getPropertyNames();
					messageInfo.setMsgContent(message.getBody());
					messageInfo.setMsgFrom(msgFrom);
					messageInfo.setMsgTo(msgTo);
					if (names.contains("msgId")) {
						messageInfo.setMsgId(Long.parseLong(message.getProperty("msgId").toString()));
					}
					if (names.contains("msgType")) {
						messageInfo.setMsgType(Integer.parseInt(message.getProperty("msgType").toString()));
					}

					// 1普通文本，
					// 2图片消息，
					// 3语音消息，
					// 4位置信息
					// 5 新货源消息
					// 6 司机位置上报命令
					// 7 信息中心收到新消息推送

					// 如果收到订单消息
					if (messageInfo.getMsgType() == 1 || messageInfo.getMsgType() == 2 || messageInfo.getMsgType() == 3
							|| messageInfo.getMsgType() == 4) {
						dbUtils.addMessage(messageInfo);
						// 如果消息来自当前聊天对象
						if (msgFrom.equals(ChatListActivity.getCurrentChat())) {
							Intent intent = new Intent(Constants.CURRENT_MESSAGE_RECEIVE);
							intent.putExtra(Constants.XMPP_MESSAGE, messageInfo);
							context.sendBroadcast(intent);
						} else {

							Intent intent = new Intent(Constants.MESSAGE_RECEIVE);
							intent.putExtra(Constants.XMPP_MESSAGE, messageInfo);
							context.sendBroadcast(intent);
						}

					} else {

						Intent intent = new Intent(Constants.MESSAGE_RECEIVE);
						intent.putExtra(Constants.XMPP_MESSAGE, messageInfo);
						context.sendBroadcast(intent);
					}
				}
			});
		}

	}

	private class UploadFileTask extends AsyncTask<MessageInfo, Void, String> {

		private SendMessageCallback callback;

		private MessageInfo message = null;

		private Message msg;

		public UploadFileTask(MessageInfo message, SendMessageCallback callback) {
			this.callback = callback;
			this.message = message;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			callback.start(message);
			msg = new Message();
			msg.setBody(message.getMsgContent());
			msg.setFrom(message.getMsgFrom() + Constants.XMPP_SERVER_NAME);
			msg.setTo(message.getMsgTo() + Constants.XMPP_SERVER_NAME);
			msg.setProperty("msgId", message.getMsgId());
			msg.setProperty("msgType", message.getMsgType());
		}

		@Override
		protected String doInBackground(MessageInfo... params) {
			final int msgType = message.getMsgType();
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost post = new HttpPost(Constants.UPLOAD_FILE_URL);
			File file = new File(message.getMsgContent());

			LogUtil.e(LOGTAG, "上传文件源路径:" + message.getMsgContent());

			if (file != null && file.exists())
				try {
					ContentBody body = new FileBody(file, msgType == 2 ? "image/jpeg" : "audio/ogg");
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addPart("token", new StringBody(application.getToken()));
					entity.addPart("file_type", new StringBody(msgType == 2 ? "1" : "2"));
					entity.addPart("file", body);
					post.setEntity(entity);
					HttpResponse response = httpClient.execute(post);
					String serverResponse = EntityUtils.toString(response.getEntity());
					LogUtil.e(LOGTAG, "上传文件结果:" + serverResponse);
					return serverResponse;
				} catch (Exception e) {
					e.printStackTrace();
				}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (TextUtils.isEmpty(result)) {
				callback.failed(message);
			} else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					final int status = jsonObject.optInt(STATUS);
					// 上传文件成功
					if (status == 0) {
						String item = jsonObject.optString("item");
						this.message.setMsgContent(new JSONObject(item).getString("url"));
						msg.setBody(message.getMsgContent());
						System.out.println("上传返回url地址：" + new JSONObject(item).getString("url"));
						Chat chat = connection.getChatManager().createChat(
								message.getMsgTo() + Constants.XMPP_SERVER_NAME, null);
						try {
							chat.sendMessage(msg);
							callback.success(message);
						} catch (XMPPException e) {
							e.printStackTrace();
							callback.failed(message);
						}
					} else {
						callback.failed(this.message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					callback.failed(this.message);
				}
			}
		}

	}

	/** 发送xmpp消息 **/
	public void sendMessage(MessageInfo messageInfo, SendMessageCallback sendMessageCallback) {
		if (messageInfo == null) {
			return;
		}
		final int msgType = messageInfo.getMsgType();
		// 如果已通过验证
		if (!connection.isAuthenticated()) {
			connect();
		}
		// 如果是图片或语音
		if (msgType == 2 || msgType == 3) {
			new UploadFileTask(messageInfo, sendMessageCallback).execute();
		} else {
			sendMessageCallback.start(messageInfo);
			Message message = new Message();
			message.setBody(messageInfo.getMsgContent());
			message.setFrom(messageInfo.getMsgFrom() + Constants.XMPP_SERVER_NAME);
			message.setTo(messageInfo.getMsgTo() + Constants.XMPP_SERVER_NAME);
			message.setProperty("msgId", messageInfo.getMsgId());
			message.setProperty("msgType", msgType);
			Chat chat = connection.getChatManager().createChat(messageInfo.getMsgTo() + Constants.XMPP_SERVER_NAME,
					null);
			try {
				chat.sendMessage(message);
				sendMessageCallback.success(messageInfo);
			} catch (XMPPException e) {
				e.printStackTrace();
				sendMessageCallback.failed(messageInfo);
			}
		}
	}
}