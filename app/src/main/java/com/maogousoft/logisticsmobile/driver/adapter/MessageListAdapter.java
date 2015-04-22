package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.ImageShowActivity;
import com.maogousoft.logisticsmobile.driver.model.MessageInfo;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 聊天消息适配器
 * 
 * @author lenovo
 */
public class MessageListAdapter extends BaseListAdapter<MessageInfo> {

	private String fromName = "xxx";

	private String toName = "xxx";

	public MessageListAdapter(Context context) {
		super(context);
	}

	public void setChatObject(String str1, String str2) {
		fromName = str1;
		toName = str2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_chat, parent, false);
			holder.chat_image = (ImageView) convertView.findViewById(R.id.chat_id_img);
			holder.chat_nickfrom = (TextView) convertView.findViewById(R.id.chat_id_nick_from);
			holder.chat_nickto = (TextView) convertView.findViewById(R.id.chat_id_nick_to);
			holder.chat_content = (TextView) convertView.findViewById(R.id.chat_id_text);
			holder.chat_time = (TextView) convertView.findViewById(R.id.chat_id_time);
			holder.chat_audio_time_from = (TextView) convertView.findViewById(R.id.chat_id_audio_time_from);
			holder.chat_audio_time_to = (TextView) convertView.findViewById(R.id.chat_id_audio_time_to);
			holder.content = (LinearLayout) convertView.findViewById(R.id.chat_id_content);
			holder.msgContainer = (RelativeLayout) convertView.findViewById(R.id.chat_id_msgcontainer);
			holder.imgContainer = (FrameLayout) convertView.findViewById(R.id.chat_id_imgcontent);
			holder.msgFlagFrom = (ImageView) convertView.findViewById(R.id.chat_id_failed_from);
			holder.msgFlagTo = (ImageView) convertView.findViewById(R.id.chat_id_failed_to);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MessageInfo messageInfo = mList.get(position);
		if (messageInfo == null) {
			return convertView;
		}
		// 0已发送1发送失败
		final int state = messageInfo.getMsgState();
		final boolean isUser = messageInfo.getMsgFrom().equals(messageInfo.getCreateUser());
		// 如果当前消息是自己发的
		if (isUser) {
			holder.content.setGravity(Gravity.RIGHT);
			holder.msgContainer.setBackgroundResource(R.drawable.ic_chat_bg_right);
			holder.chat_nickfrom.setVisibility(View.GONE);
			holder.chat_nickto.setVisibility(View.VISIBLE);
			holder.chat_nickto.setText(fromName);
			holder.chat_audio_time_from.setVisibility(View.VISIBLE);
			holder.chat_audio_time_to.setVisibility(View.GONE);
			switch (state) {
			case 0:
				holder.msgFlagFrom.setVisibility(View.GONE);
				holder.msgFlagTo.setVisibility(View.GONE);
				break;
			case 1:
				holder.msgFlagFrom.setVisibility(View.VISIBLE);
				holder.msgFlagTo.setVisibility(View.GONE);
				break;

			default:
				holder.msgFlagFrom.setVisibility(View.GONE);
				holder.msgFlagTo.setVisibility(View.GONE);
				break;
			}
		} else {
			holder.content.setGravity(Gravity.LEFT);
			holder.msgContainer.setBackgroundResource(R.drawable.ic_chat_bg_left);
			holder.chat_nickfrom.setVisibility(View.VISIBLE);
			holder.chat_nickto.setVisibility(View.GONE);
			holder.chat_nickfrom.setText(toName);
			holder.chat_audio_time_from.setVisibility(View.GONE);
			holder.chat_audio_time_to.setVisibility(View.VISIBLE);
			switch (state) {
			case 0:
				holder.msgFlagFrom.setVisibility(View.GONE);
				holder.msgFlagTo.setVisibility(View.GONE);
				break;
			case 1:
				holder.msgFlagFrom.setVisibility(View.GONE);
				holder.msgFlagTo.setVisibility(View.VISIBLE);
				break;

			default:
				holder.msgFlagFrom.setVisibility(View.GONE);
				holder.msgFlagTo.setVisibility(View.GONE);
				break;
			}
		}
		holder.msgContainer.setOnClickListener(new ItemClickListener(messageInfo));
		// holder.chat_nick.setText(messageInfo.getMsgFrom());
		holder.chat_time.setText(TimeUtils.getDetailTime(messageInfo.getMsgId()));
		// 消息类型，1普通文本，2图片，3语音，4地理位置
		switch (messageInfo.getMsgType()) {
		case 1:
			holder.imgContainer.setVisibility(View.GONE);
			holder.chat_content.setVisibility(View.VISIBLE);
			holder.chat_content.setText(messageInfo.getMsgContent());
			holder.chat_audio_time_from.setVisibility(View.GONE);
			holder.chat_audio_time_to.setVisibility(View.GONE);
			break;
		case 2:
			holder.imgContainer.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(messageInfo.getMsgContent(), holder.chat_image);
			holder.chat_content.setVisibility(View.GONE);
			holder.chat_audio_time_from.setVisibility(View.GONE);
			holder.chat_audio_time_to.setVisibility(View.GONE);
			break;
		case 3:
			holder.imgContainer.setVisibility(View.VISIBLE);
			holder.chat_content.setVisibility(View.GONE);
			// 如果是自己发的消息
			if (isUser) {
				holder.chat_audio_time_from.setVisibility(View.VISIBLE);
				holder.chat_audio_time_to.setVisibility(View.GONE);
				holder.chat_audio_time_from.setText(messageInfo.getAudioTime() + "s");
			} else {
				holder.chat_audio_time_to.setVisibility(View.VISIBLE);
				holder.chat_audio_time_from.setVisibility(View.GONE);
				holder.chat_audio_time_to.setText(messageInfo.getAudioTime() + "s");
			}
			holder.chat_image.setImageResource(R.drawable.ic_chat_audio);
			break;
		case 4:
			holder.imgContainer.setVisibility(View.GONE);
			holder.chat_content.setVisibility(View.VISIBLE);
			holder.chat_content.setText(messageInfo.getMsgContent());
			holder.chat_audio_time_from.setVisibility(View.GONE);
			holder.chat_audio_time_to.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		return convertView;
	}

	final class ItemClickListener implements OnClickListener {

		final MessageInfo messageInfo;

		public ItemClickListener(MessageInfo messageInfo) {
			this.messageInfo = messageInfo;
		}

		@Override
		public void onClick(View v) {
			if (messageInfo == null) {
				return;
			}
			final int msgType = messageInfo.getMsgType();
			switch (msgType) {
			case 1:

				break;
			case 2:
				final Intent intent = new Intent(mContext, ImageShowActivity.class);
				intent.putExtra("imageUrl", messageInfo.getMsgContent());
				mContext.startActivity(intent);
				break;
			case 3:
				break;
			case 4:
				break;

			default:
				break;
			}
		}

	}

	static class ViewHolder {

		ImageView chat_image;

		// from昵称,to昵称，消息内容，消息时间，语音时长
		TextView chat_nickfrom, chat_nickto, chat_content, chat_time, chat_audio_time_from, chat_audio_time_to;

		// 消息布局
		LinearLayout content;

		// 消息主体
		RelativeLayout msgContainer;

		FrameLayout imgContainer;

		ImageView msgFlagFrom, msgFlagTo;
	}
}
