package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EvaluateListAdapter extends BaseListAdapter<EvaluateInfo> {

    private int type;
    private boolean isMyReputation;
	public EvaluateListAdapter(Context context, int type, boolean isMyReputation) {
		super(context);
        this.type = type;
        this.isMyReputation = isMyReputation;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_evaluate, parent, false);
			holder = new ViewHolder();
			holder.commentName = (TextView) convertView.findViewById(R.id.comment_name);
			holder.commentPhoto = (ImageView) convertView.findViewById(R.id.comment_photo);
			holder.commentTime = (TextView) convertView.findViewById(R.id.comment_time);
			holder.commentContent = (TextView) convertView.findViewById(R.id.comment_content);
			holder.return_name = (TextView) convertView.findViewById(R.id.return_name);
			holder.return_time = (TextView) convertView.findViewById(R.id.return_time);
            holder.return_content_layout = convertView.findViewById(R.id.return_content_layout);
            holder.return_input_layout = convertView.findViewById(R.id.return_input_layout);
            holder.return_button = convertView.findViewById(R.id.return_button);
            holder.return_commit = convertView.findViewById(R.id.return_commit);
            holder.return_content = (EditText) convertView.findViewById(R.id.return_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final EvaluateInfo evaluateInfo = mList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String replyTime = simpleDateFormat.format(new Date(evaluateInfo.getReply_time()));
        holder.commentTime.setText(replyTime);
        holder.commentContent.setText(evaluateInfo.getReply_content());

        if(type == Constants.USER_DRIVER) {
            //当前用户是司机
            if(isMyReputation) {
                holder.commentName.setText(Html.fromHtml(mContext.getString(R.string.comment_name, evaluateInfo.getUser_name(), evaluateInfo.getDriver_name())));
            } else {
                holder.commentName.setText(Html.fromHtml(mContext.getString(R.string.comment_name, evaluateInfo.getDriver_name(), evaluateInfo.getUser_name())));
            }
        } else {
            //当前用户是货主
            if(isMyReputation) {
                holder.commentName.setText(Html.fromHtml(mContext.getString(R.string.comment_name, evaluateInfo.getDriver_name(), evaluateInfo.getUser_name())));
            } else {
                holder.commentName.setText(Html.fromHtml(mContext.getString(R.string.comment_name, evaluateInfo.getUser_name(), evaluateInfo.getDriver_name())));
            }
        }
        if(TextUtils.isEmpty(evaluateInfo.getCar_photo())) {
            ImageLoader.getInstance().displayImage(evaluateInfo.getCompany_logo(), holder.commentPhoto);
        } else {
            ImageLoader.getInstance().displayImage(evaluateInfo.getCar_photo(), holder.commentPhoto);
        }
        //如果没有回复内容，则显示回复评论的按钮
        if(TextUtils.isEmpty(evaluateInfo.getReturn_content())) {
            holder.return_button.setVisibility(View.VISIBLE);
        } else {
            holder.return_button.setVisibility(View.GONE);
            holder.return_input_layout.setVisibility(View.GONE);
            holder.return_content_layout.setVisibility(View.VISIBLE);
            String returnTime = simpleDateFormat.format(new Date(evaluateInfo.getReturn_time()));
            holder.return_time.setText(returnTime);
            String returnUser;
            if(type == Constants.USER_DRIVER) {
                //当前用户是司机
                if (isMyReputation) {
                    returnUser = evaluateInfo.getDriver_name();
                } else {
                    returnUser = evaluateInfo.getUser_name();
                }
            } else {
                //当前用户是货主
                if(isMyReputation) {
                    returnUser = evaluateInfo.getUser_name();
                } else {
                    returnUser = evaluateInfo.getDriver_name();
                }
            }
            String returnName = mContext.getString(R.string.return_name, returnUser, evaluateInfo.getReturn_content());
            holder.return_name.setText(Html.fromHtml(returnName));
        }
        //显示回复评论的界面
        holder.return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.return_input_layout.setVisibility(View.VISIBLE);
            }
        });
        //回复评论
        holder.return_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replay(evaluateInfo, holder.return_content);
            }
        });
        return convertView;
	}

    public void replay(final EvaluateInfo evaluateInfo, EditText returnInput) {
        final JSONObject jsonObject = new JSONObject();
        showProgress("正在评论");
        try {
            jsonObject.put(Constants.TOKEN, application.getToken());
            if(type == Constants.USER_DRIVER) {
                //给司机回复评价
                jsonObject.put(Constants.ACTION, Constants.DRIVER_RETURN_REPLY);
            } else {
                //给货主回复评价
                jsonObject.put(Constants.ACTION, Constants.USER_RETURN_REPLY);
            }
            JSONObject params = new JSONObject();
            params.put("id", evaluateInfo.getId());
            final CharSequence returnContent = TextUtils.isEmpty(returnInput.getText()) ? returnInput.getHint() : returnInput.getText();
            params.put("return_content", returnContent);
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg("评论成功");
                                    evaluateInfo.setReturn_content(returnContent.toString());
                                    evaluateInfo.setReply_time(System.currentTimeMillis());
                                    notifyDataSetChanged();
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    showMsg(result.toString());
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	final static class ViewHolder {
		TextView commentName, commentTime, commentContent, return_name, return_time;
        View return_content_layout, return_input_layout, return_button, return_commit;
        EditText return_content;
		ImageView commentPhoto;
	}
}
