package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 待定货源列表
 * 
 * @author lenovo
 */
public class UnMatchedSourceListAdapter extends BaseListAdapter<NewSourceInfo> {

	public UnMatchedSourceListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_unmatched,
					parent, false);
			holder.order_image = (ImageView) convertView
					.findViewById(R.id.source_id_order_image);
			holder.order_info = (TextView) convertView
					.findViewById(R.id.source_id_order_info);
			holder.order_number = (TextView) convertView
					.findViewById(R.id.source_id_order_number);
			holder.order_nums = (TextView) convertView
					.findViewById(R.id.source_id_order_nums);
			holder.order_time = (TextView) convertView
					.findViewById(R.id.source_id_order_time);
			holder.order_score = (RatingBar) convertView
					.findViewById(R.id.source_id_order_star);
			holder.order_score.setIsIndicator(true);
			holder.order_cancel = (Button) convertView
					.findViewById(R.id.source_id_order_cancelgrab);
			holder.order_imagetips = convertView
					.findViewById(R.id.source_id_order_imagetips);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final NewSourceInfo sourceInfo = mList.get(position);
		if (sourceInfo != null) {
			holder.order_cancel.setOnClickListener(new CancelListener(
					sourceInfo, position));
			mImageLoader.displayImage(sourceInfo.getCargo_photo1(),
					holder.order_image);
			final StringBuilder title = new StringBuilder();

			if (sourceInfo.getStart_province_str().equals(
					sourceInfo.getStart_city_str())) {

				// 避免直辖市，显示如 ：上海上海

				title.append(
						CheckUtils.checkIsNull(sourceInfo.getStart_city_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getStart_district_str()));

			} else {

				title.append(
						CheckUtils.checkIsNull(sourceInfo
								.getStart_province_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getStart_city_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getStart_district_str()));
			}

			title.append("-");

			if (sourceInfo.getStart_province_str().equals(
					sourceInfo.getStart_city_str())) {

				// 避免直辖市，显示如 ：上海上海
				title.append(
						CheckUtils.checkIsNull(sourceInfo.getEnd_city_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getEnd_district_str()));
			} else {
				title.append(
						CheckUtils.checkIsNull(sourceInfo.getEnd_province_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getEnd_city_str()))
						.append(CheckUtils.checkIsNull(sourceInfo
								.getEnd_district_str()));
			}
			title.append("/")
					.append(sourceInfo.getCargo_type_str())
					.append("/")
					.append(sourceInfo.getCargo_desc())
					.append(sourceInfo.getCargo_number())
					.append(CheckUtils.getCargoUnitName(sourceInfo
							.getCargo_unit()));
			holder.order_number.setText(String.format(
					mResources.getString(R.string.sourcedetail_number),
					sourceInfo.getId()));
			holder.order_info.setText(title.toString());
			holder.order_time.setText(String.format(
					mResources.getString(R.string.sourcedetail_validtime),
					TimeUtils.getDetailTime(sourceInfo.getValidate_time())));
			holder.order_score.setRating(Float.parseFloat(String
					.valueOf(sourceInfo.getScore())));
			holder.order_nums.setText(Html.fromHtml(String.format(
					mResources.getString(R.string.string_unmatched_nums),
					sourceInfo.getVie_driver_count())));
			mImageLoader.displayImage(sourceInfo.getCargo_photo1(),
					holder.order_image);
			holder.order_imagetips.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<String> list = new ArrayList<String>();
					if (!TextUtils.isEmpty(sourceInfo.getCargo_photo1())) {
						list.add(sourceInfo.getCargo_photo1());
					}
					if (!TextUtils.isEmpty(sourceInfo.getCargo_photo2())) {
						list.add(sourceInfo.getCargo_photo2());
					}
					if (!TextUtils.isEmpty(sourceInfo.getCargo_photo3())) {
						list.add(sourceInfo.getCargo_photo3());
					}
					mContext.startActivity(new Intent(mContext,
							ImagePagerActivity.class).putStringArrayListExtra(
							"images", list));
				}
			});
		}
		return convertView;
	}

	final class CancelListener implements OnClickListener {

		private NewSourceInfo sourceInfo;

		private int position;

		public CancelListener(NewSourceInfo sourceInfo, int position) {
			this.sourceInfo = sourceInfo;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (sourceInfo != null) {
				final MyAlertDialog dialog = new MyAlertDialog(mContext);
				dialog.show();
				dialog.setTitle("提示");
				dialog.setMessage("您确定要取消抢单吗？");
				dialog.setLeftButton("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						final JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put(Constants.ACTION,
									Constants.CANCEL_PLACE_SOURCE_ORDER);
							jsonObject.put(Constants.TOKEN,
									application.getToken());
							jsonObject.put(
									Constants.JSON,
									new JSONObject().put("order_id",
											sourceInfo.getId()));
							((BaseActivity) mContext).showProgress("请求提交中,请稍候");
							ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
									jsonObject, null, new AjaxCallBack() {

										@Override
										public void receive(int code,
												Object result) {
											((BaseActivity) mContext)
													.dismissProgress();
											switch (code) {
											case ResultCode.RESULT_OK:
												((BaseActivity) mContext)
														.showMsg("取消成功!");
												remove(position);
												break;
											case ResultCode.RESULT_ERROR:
												if (result != null)
													((BaseActivity) mContext).showMsg(result
															.toString());
												break;
											case ResultCode.RESULT_FAILED:
												if (result != null)
													((BaseActivity) mContext).showMsg(result
															.toString());
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
				});
				dialog.setRightButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		}

	}

	static class ViewHolder {

		TextView order_number, order_info, order_time, order_nums;

		ImageView order_image;

		RatingBar order_score;

		Button order_cancel;

		View order_imagetips;
	}
}
