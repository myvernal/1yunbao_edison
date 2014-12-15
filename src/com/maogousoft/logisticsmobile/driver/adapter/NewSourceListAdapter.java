package com.maogousoft.logisticsmobile.driver.adapter;
// PR104
import java.util.ArrayList;
import java.util.Date;

import android.net.Uri;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.ybxiang.driver.util.Utils;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.RenZhengActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import com.maogousoft.logisticsmobile.driver.utils.MyProgressDialog;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 新货源数据适配器
 * 
 * @author lenovo
 */
public class NewSourceListAdapter extends BaseListAdapter<NewSourceInfo> {

	private MyProgressDialog progressDialog;

	// 信息费
	private double messagePrice;

	private Context mContext;
    private CityDBUtils dbUtils;

	public NewSourceListAdapter(Context context) {
		super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
		mContext = context;
		progressDialog = new MyProgressDialog(context);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_newsoure, parent,
					false);
			holder = new ViewHolder();
            holder.source_detail_phone = (Button) convertView
                    .findViewById(R.id.source_detail_phone);
			holder.order_image = (ImageView) convertView
					.findViewById(R.id.source_id_order_image);
			holder.order_number = (TextView) convertView
					.findViewById(R.id.source_id_order_number);
			holder.order_info = (TextView) convertView
					.findViewById(R.id.source_id_order_info);
			holder.order_money = (TextView) convertView
					.findViewById(R.id.source_id_order_money);
			//PR104 begin
//			holder.order_star = (RatingBar) convertView
//					.findViewById(R.id.source_id_order_star);
//			holder.order_star.setIsIndicator(true);
			//PR104 end
			holder.order_imagetips = convertView
					.findViewById(R.id.source_id_order_imagetips);
			holder.order_grab = (Button) convertView
					.findViewById(R.id.source_id_order_grab);
			holder.order_state = (Button) convertView
					.findViewById(R.id.source_id_order_state);
			holder.tvDjs = (TextView) convertView.findViewById(R.id.tv_djs);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final NewSourceInfo sourceInfo = (NewSourceInfo) getItem(position);
		// holder.order_grab.setOnClickListener(new ClickListener(position,
		// sourceInfo));
		holder.order_number.setText(mResources.getString(R.string.string_home_newsource_order_number, sourceInfo.getId()));
		mImageLoader.displayImage(sourceInfo.getCargo_photo1(), holder.order_image);
		final StringBuilder title = new StringBuilder();

        String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        if (sourceInfo.getEnd_province() > 0 || sourceInfo.getEnd_city() > 0 || sourceInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
            title.append(wayStart + "--" + wayEnd);
        }

		title.append("/").append(sourceInfo.getCargo_type_str());

		if (sourceInfo.getCargo_number() != null && sourceInfo.getCargo_number() != 0) {
			title.append("/")
					.append(sourceInfo.getCargo_desc())
					.append(sourceInfo.getCargo_number())
					.append(CheckUtils.getCargoUnitName(sourceInfo
							.getCargo_unit()));
		}

		if (!TextUtils.isEmpty(sourceInfo.getCar_type_str())) {
			title.append("/");
			title.append(sourceInfo.getCar_type_str());

		}

		if (sourceInfo.getCar_length() != null && sourceInfo.getCar_length() != 0
				&& sourceInfo.getCar_length() != 0.0) {
			title.append("/");
			title.append(sourceInfo.getCar_length() + "米");
		}

		// title.append(TimeUtils.getDetailTime(sourceInfo.getValidate_time()));

		holder.order_info.setText(title.toString());
		holder.order_money.setText(Html.fromHtml(String.format(mResources
				.getString(R.string.string_home_newsource_order_money),
				sourceInfo.getUser_bond())));
		// PR104 begin
//		if (sourceInfo.getScore() == null || sourceInfo.getScore() == 0) {
//			holder.order_star.setRating(5);
//		} else {
//			holder.order_star.setRating(sourceInfo.getScore());
//		}
		// PR104 end
		// 已关注
		if (sourceInfo.getFavorite_status() == 1) {
			holder.order_state.setText("已关注");
			//holder.order_state.setVisibility(View.VISIBLE);
			// holder.order_state.setTextColor(Color.RED);
			// holder.order_state.setBackgroundResource(R.drawable.ic_button_gray_normal);
		} else {
			holder.order_state.setVisibility(View.GONE);
			// holder.order_state.setText(sourceInfo.getRead_status() == 0 ?
			// "未读" : "已读");
			// holder.order_state.setTextColor(mResources.getColor(android.R.color.white));
			// holder.order_state.setBackgroundResource(R.drawable.ic_button_blue_normal);
		}

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

        Date date = new Date();
        String betweenTime = "刚发布";
        if(date.getTime() > sourceInfo.getCreate_time()) {
            long time = date.getTime() - sourceInfo.getCreate_time();
            long hour = time / (60 * 60 * 1000);
            long minites = (time % (60 * 60 * 1000)) / (60 * 1000);
            betweenTime = "{" + hour + "时" + minites + "分}";
        }
        holder.tvDjs.setText(Html.fromHtml("已发布:" + Utils.textFormatRed(betweenTime)));
        //电话号码
        holder.source_detail_phone.setText(sourceInfo.getCargo_user_phone());
        holder.source_detail_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = ((Button)view).getText().toString();
                if (!TextUtils.isEmpty(phoneStr) && !phoneStr.equals("无")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    mContext.startActivity(intent);
                }
            }
        });

		holder.order_grab.setOnClickListener(new ClickListener(position,
				sourceInfo, mContext));

		return convertView;
	}

	class ClickListener implements OnClickListener {

		private int position;

		private NewSourceInfo sourceInfo;

		private Context ctx;

		public ClickListener(int position, NewSourceInfo sourceInfo, Context ctx) {
			this.position = position;
			this.sourceInfo = sourceInfo;
			this.ctx = ctx;
		}

		@Override
		public void onClick(View v) {
			// 先检测是否已经 通过了 诚信认证
			if (application.checkIsThroughRezheng()) {
				getBalance(sourceInfo, ctx);
			} else {
				final MyAlertDialog dialog = new MyAlertDialog(ctx);
				dialog.show();
				dialog.setTitle("提示");
				dialog.setMessage("为确保诚信交易，你必须提供相关证件方可得到货主的认可。");
				dialog.setLeftButton("诚信认证", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent = new Intent(ctx, RenZhengActivity.class);
						ctx.startActivity(intent);
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

		ImageView order_image;

		TextView order_number, order_info, order_money;

		Button order_state;

		// RatingBar order_star; PR104

		View order_imagetips;

		Button order_grab, source_detail_phone;

		TextView tvDjs;
	}

	// 获取余额，并抢单
	private void getBalance(final NewSourceInfo mSourceInfo,
			final Context context) {

		messagePrice = 0;

		// 信息费为运价的3%，不超过200元

		try {
			if (mSourceInfo.getPrice() == null
					|| mSourceInfo.getPrice().equalsIgnoreCase("")) {
				messagePrice = 0;
			} else {
				messagePrice = Double.parseDouble(mSourceInfo.getPrice()) * 0.03;
				if (messagePrice > 200) {
					messagePrice = 200;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		final GrabDialog dialog = new GrabDialog(context);
		dialog.show();
		final EditText mInput = (EditText) dialog
				.findViewById(android.R.id.text1);
		final EditText mInput2 = (EditText) dialog
				.findViewById(android.R.id.text2);
		TextView t = (TextView) dialog.findViewById(R.id.grabdialog_text);
		TextView t1 = (TextView) dialog.findViewById(R.id.grabdialog_text1);
		TextView t2 = (TextView) dialog.findViewById(R.id.grabdialog_text2);
		t.setVisibility(View.VISIBLE);
		t1.setVisibility(View.VISIBLE);
		t2.setVisibility(View.VISIBLE);
		mInput2.setVisibility(View.VISIBLE);
		if (mSourceInfo.getPrice() == null
				|| mSourceInfo.getPrice().equalsIgnoreCase("")) {
			mInput2.setText("0");
		} else {
			mInput2.setText(mSourceInfo.getPrice());
		}
		dialog.setTitle("提示");

		mInput.setText(mSourceInfo.getUser_bond() + "");

		String messageStr = String.format("请输入保证金,保证金必须大于等于%d元。",
				mSourceInfo.getUser_bond())
				+ String.format("如达成交易，将付出信息费%d物流币。", Integer
						.parseInt(new java.text.DecimalFormat("0")
								.format(messagePrice)));
		dialog.setMessage(messageStr);
		dialog.setLeftButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (TextUtils.isEmpty(mInput.getText())
							|| mSourceInfo.getUser_bond() > Integer
									.parseInt(mInput.getText().toString())) {
						Toast.makeText(
								context,
								String.format("保证金必须大于等于%d元",
										mSourceInfo.getUser_bond()),
								Toast.LENGTH_SHORT).show();
					} else if (TextUtils.isEmpty(mInput2.getText())
							|| Integer.parseInt(mInput2.getText().toString()) < 0) {
						Toast.makeText(context, "自报价：必须大于0元哦",
								Toast.LENGTH_SHORT).show();
					} else {
						dialog.dismiss();
						final JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put(Constants.ACTION,
									Constants.GET_ACCOUNT_GOLD);
							jsonObject.put(Constants.TOKEN,
									application.getToken());
							showProgress(mResources
									.getString(R.string.tips_sourcedetail_qiang));
							ApiClient.doWithObject(Constants.COMMON_SERVER_URL,
									jsonObject, null, new AjaxCallBack() {

										@Override
										public void receive(int code,
												Object result) {
											switch (code) {
											case ResultCode.RESULT_OK:
												JSONObject object = (JSONObject) result;
												// mBalance.setText(String.format(getString(R.string.string_home_myabc_balance),
												// object.optDouble("gold")));
												double balance = object
														.optDouble("gold");

												if (balance < (messagePrice + Double
														.parseDouble(mInput
																.getText()
																.toString()))) {
													dismissProgress();

													final MyAlertDialog dialogCharg = new MyAlertDialog(
															context);
													dialogCharg.show();
													dialogCharg.setTitle("提示");
													dialogCharg.setMessage("需要金额"
															+ (messagePrice + Double
																	.parseDouble(mInput
																			.getText()
																			.toString()))
															+ "元，您的余额不足，请先充值。");
													dialogCharg
															.setLeftButton(
																	"去充值",
																	new OnClickListener() {

																		@Override
																		public void onClick(
																				View v) {

																			Intent intent = new Intent(
																					context,
																					ChargeActivity.class);
																			context.startActivity(intent);
																		}
																	});

													dialogCharg
															.setRightButton(
																	"取消",
																	new OnClickListener() {

																		@Override
																		public void onClick(
																				View v) {
																			dialogCharg
																					.dismiss();
																		}
																	});

												} else {
													placeOrder(
															mInput.getText()
																	.toString(),
															mInput2.getText()
																	.toString(),
															context,
															mSourceInfo);
												}

												break;
											case ResultCode.RESULT_FAILED:
												dismissProgress();
												Toast.makeText(context,
														result.toString(),
														Toast.LENGTH_SHORT)
														.show();
												break;
											case ResultCode.RESULT_ERROR:
												dismissProgress();
												Toast.makeText(context,
														result.toString(),
														Toast.LENGTH_SHORT)
														.show();
												break;

											default:
												break;
											}
										}
									});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (NumberFormatException e) {
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

	/**
	 * 抢单
	 * 
	 * @param mInput
	 */
	public void placeOrder(String mInput, String mInput2,
			final Context context, NewSourceInfo mSourceInfo) {
		final JSONObject params = new JSONObject();
		try {
			params.put(Constants.ACTION, Constants.PLACE_SOURCE_ORDER);
			params.put(Constants.TOKEN, application.getToken());
			params.put(
					Constants.JSON,
					new JSONObject().put("order_id", mSourceInfo.getId())
							.put("driver_bond", mInput)
							.put("driver_proportion", 0)
							.put("driver_price", mInput2).toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, params, null,
					new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:

								Toast.makeText(
										context,
										R.string.tips_sourcedetail_qiang_success,
										Toast.LENGTH_SHORT).show();
								break;
							case ResultCode.RESULT_ERROR:

								// 你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）

								// 已经承接了其他货单，不能再抢单了
								if (result.toString().contains("已经承接了其他货单")) {
									final MyAlertDialog dialog = new MyAlertDialog(
											context);
									dialog.show();
									dialog.setTitle("温馨提示");
									dialog.setMessage("你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）");
									dialog.setLeftButton("确定",
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											});
								} else {

									Toast.makeText(context, result.toString(),
											Toast.LENGTH_SHORT).show();

								}

								break;
							case ResultCode.RESULT_FAILED:
								if (result.toString().contains("已经承接了其他货单")) {
									final MyAlertDialog dialog = new MyAlertDialog(
											context);
									dialog.show();
									dialog.setTitle("温馨提示");
									dialog.setMessage("你仍处于在途货运中，输入回单密码确认货运完成后方可再次抢单。（到货时请向收货方或发货方索取回单密码）");
									dialog.setLeftButton("确定",
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											});
								} else {
									Toast.makeText(context, result.toString(),
											Toast.LENGTH_SHORT).show();

								}
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

	public void showProgress(String message) {
		progressDialog.setMessage(message);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	public void dismissProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
