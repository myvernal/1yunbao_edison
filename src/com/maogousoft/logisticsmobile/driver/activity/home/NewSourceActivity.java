package com.maogousoft.logisticsmobile.driver.activity.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.NewSourceListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.FocuseLineInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新货源列表
 * 
 * @author lenovo
 */
public class NewSourceActivity extends BaseListActivity implements
		OnScrollListener {

	private Context mContext;// PR104
	private Button mBack, mSearchSource;
	private int rightButton = 0;// 0显示查找货源，1显示关注此线路,2不显示此button
	// 底部更多
	private View mFootView;
	private ProgressBar mFootProgress;
	private TextView mFootMsg;
	// 当前模式
	private int state = WAIT;
	// 当前页码
	private int pageIndex = 1;
	// 滑动状态
	private boolean state_idle = false;
	// 已加载全部
	private boolean load_all = false;

	private boolean isFromHomeActivity;// 是否从 主页跳转过来

	private List<NewSourceInfo> newSourceInfos = null;// 上一页传来的

	private FocuseLineInfo focuseLineInfo = new FocuseLineInfo();// 上一页传输过来的省市区

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = NewSourceActivity.this;// PR104
		initViews();

		initData();
		// onFeedback();// PR1.4 test
	}

	private void initViews() {
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		mSearchSource = (Button) findViewById(R.id.titlebar_id_more);
		mSearchSource.setText("查找货源");
		setIsRightKeyIntoShare(false);
		// mSearchSource.setTextColor(0xffFF7F00);
		// mSearchSource.setCompoundDrawablesWithIntrinsicBounds(
		// new BitmapDrawable(getResources(), BitmapFactory
		// .decodeResource(getResources(),
		// R.drawable.icon_newsource_search)), null, null,
		// null);

		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_title_newsource);
		// 页脚信息
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview,
				null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView
				.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		mListView.setOnScrollListener(this);
		mBack.setOnClickListener(this);
		mSearchSource.setOnClickListener(this);

		mAdapter = new NewSourceListAdapter(context);
		setListAdapter(mAdapter);
		setListShown(false);

	}

	private void initData() {

		if (getIntent().hasExtra("isFromHomeActivity")) {
			isFromHomeActivity = getIntent().getBooleanExtra(
					"isFromHomeActivity", false);
		}

		if (getIntent().hasExtra("NewSourceInfos")) {
			newSourceInfos = (List<NewSourceInfo>) getIntent()
					.getSerializableExtra("NewSourceInfos");

			// 从 搜索货源进入，不需要显示 搜索货源按钮 modify
			// mSearchSource.setVisibility(View.GONE);
			mSearchSource.setText("关注此路线");
			rightButton = 1;

			load_all = true;
			mFootProgress.setVisibility(View.GONE);
			mFootMsg.setText("已加载全部");

			((NewSourceListAdapter) mAdapter).addAll(sort(newSourceInfos));
			setListShown(true);

		}
		if (getIntent().hasExtra("focuseLineInfo")) {
			focuseLineInfo = (FocuseLineInfo) getIntent().getSerializableExtra(
					"focuseLineInfo");
		}

	}

	@Override
	public void onClick(View v) {
		((BaseListActivity) mContext).setIsRightKeyIntoShare(false);
		switch (v.getId()) {
		case R.id.titlebar_id_more:
			// mSearchSource
			switch (rightButton) {
			case 0:// 查找新货源
				startActivity(new Intent(context, SearchSourceActivity.class));
				finish();
				break;
			case 1:// 关注此线路
				doFocuse();
				//Toast.makeText(mContext, "关注此线路成功", Toast.LENGTH_SHORT).show();
				// startActivity(new Intent(context,
				// SearchSourceActivity.class));
				break;

			default:
				break;
			}
			break;
		}
		super.onClick(v);
	}

	// 点击关注此路线
	public void doFocuse() {
		final JSONObject jsonObject = new JSONObject();
		final JSONObject params = new JSONObject();
		try {

			jsonObject.put(Constants.ACTION, Constants.FOCUS_LINE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			if (focuseLineInfo!=null) {
				params.put("start_province", focuseLineInfo.getStart_province());
				params.put("start_city", focuseLineInfo.getStart_city());
				params.put("start_district", focuseLineInfo.getStart_district());
				
				params.put("end_province", focuseLineInfo.getEnd_province());
				params.put("end_city", focuseLineInfo.getEnd_city());
				params.put("end_district", focuseLineInfo.getEnd_district());
				
			}
			jsonObject.put(Constants.JSON, params);
			showDefaultProgress();
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								Toast.makeText(mContext, "关注此线路成功", Toast.LENGTH_SHORT).show();
								break;
							case ResultCode.RESULT_ERROR:
								if (result != null)
									showMsg(result.toString());

								break;
							case ResultCode.RESULT_FAILED:
								if (result != null) {
									// 您当月的免费搜索次数已经用完

									// if (result.equals("您当月的免费搜索次数已经用完")) {
									final MyAlertDialog dialog = new MyAlertDialog(
											context);
									dialog.show();
									dialog.setTitle("提示");
									// 您本月的搜索次数已达到10次，你须要向朋友分享易运宝才能继续使用搜索功能！
									dialog.setMessage(result.toString());
									dialog.setLeftButton("确定",
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													dialog.dismiss();

													String content = null;
													startActivity(new Intent(
															context,
															ShareActivity.class)
															.putExtra("share",
																	content));
													finish();
												}
											});

									// }
								}
								// showMsg(result.toString());
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (position < mAdapter.getList().size()) {

			// 先检测是否已经完善了资料
			if (application.checkIsRegOptional()) {
				final Intent intent = new Intent(context,
						SourceDetailActivity.class);
				intent.putExtra(
						SourceDetailActivity.ORDER_ID,
						((NewSourceListAdapter) mAdapter).getList()
								.get(position).getId());
				intent.putExtra("type", "NewSourceActivity");
				startActivityForResult(intent, 1000);
			} else {

				final MyAlertDialog dialog = new MyAlertDialog(context);
				dialog.show();
				dialog.setTitle("提示");
				dialog.setMessage("请完善信息，否则无法提供适合你车型、线路的货源。");
				dialog.setLeftButton("完善资料", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent = new Intent(context,
								OptionalActivity.class);
						intent.putExtra("isFormRegisterActivity", false);
						startActivity(intent);
						finish();
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

	@Override
	protected void onResume() {
		super.onResume();

		if (mAdapter.isEmpty()) {
			pageIndex = 1;
			getData(pageIndex);
		}
	}

	@Override
	public void onBackPressed() {

		if (isFromHomeActivity) {
			super.onBackPressed();
		} else {
			application.finishAllActivity();
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
		}

	}

	// 请求指定页数的数据
	private void getData(int page) {

		if (newSourceInfos != null) {
			// 如果上一页传了list数据，那么证明是从 查找货源进入。 不需要加载新货源数据
			return;
		}
		try {
			state = ISREFRESHING;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
					.toString());

			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NewSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							setListShown(true);
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									List<NewSourceInfo> mList = (List<NewSourceInfo>) result;
									if (mList == null || mList.isEmpty()) {
										load_all = true;
										mFootProgress.setVisibility(View.GONE);
										mFootMsg.setText("已加载全部");
									} else {
										if (mList.size() < 10) {
											load_all = true;
											mFootProgress
													.setVisibility(View.GONE);
											mFootMsg.setText("已加载全部");
										} else {
											load_all = false;
											mFootProgress
													.setVisibility(View.VISIBLE);
											mFootMsg.setText(R.string.tips_isloading);
										}

										((NewSourceListAdapter) mAdapter)
												.addAll(sort(mList));
									}

								}
								break;
							case ResultCode.RESULT_ERROR:
								if (result instanceof String)
									showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								if (result instanceof String)
									showMsg(result.toString());
								break;

							default:
								break;
							}
							if (mAdapter.isEmpty()) {
								setEmptyText("没有找到数据哦");
							}
							state = WAIT;
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != OnScrollListener.SCROLL_STATE_IDLE) {
			return;
		}
		if (state != WAIT) {
			return;
		}
		this.state_idle = true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!this.state_idle) {
			return;
		}
		if (firstVisibleItem == 0
				|| (firstVisibleItem + visibleItemCount) != totalItemCount) {
			return;
		}
		// 如果当前没有加载数据
		if (state != ISREFRESHING && !load_all) {
			getData(++pageIndex);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1000) {
			System.out.println("执行onActivityResult");
			if (data.hasExtra("sourceInfo")) {
				NewSourceInfo sourceInfo = (NewSourceInfo) data
						.getSerializableExtra("sourceInfo");
				try {
					for (int i = 0; i < mAdapter.getList().size(); i++) {
						if (sourceInfo.getId().intValue() == ((NewSourceInfo) mAdapter
								.getList().get(i)).getId().intValue()) {
							((NewSourceInfo) mAdapter.getList().get(i))
									.setFavorite_status(sourceInfo
											.getFavorite_status());
							mAdapter.notifyDataSetChanged();
							break;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 按照已关注，未读，已读 排序
	 */
	public List<NewSourceInfo> sort(List<NewSourceInfo> list) {
		ArrayList<NewSourceInfo> listRestul = new ArrayList<NewSourceInfo>();

		if (list == null || list.size() == 0) {
			return listRestul;
		}

		for (int i = 0; i < list.size(); i++) {
			int status = list.get(i).getFavorite_status();
			if (status == 1) {
				listRestul.add(list.get(i));
			}
		}

		for (int y = 0; y < list.size(); y++) {
			int status = list.get(y).getFavorite_status();
			if (status != 1) {
				listRestul.add(list.get(y));
			}
		}

		return listRestul;

	}

	// PR104 begin
	/**
	 * 货物状态反馈 货已拉走，价格太低，联系不上，虚假信息
	 * 
	 * @param view
	 */
	public void onFeedback(View view) {
		// public void onFeedback(){ only for test
		// Toast.makeText(mContext,R.string.feedback_msg,
		// Toast.LENGTH_LONG).show();
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setItems(R.array.feedback_type,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								switch (which) {
								case 0:
									Toast.makeText(mContext,
											R.string.feedback_hylz,
											Toast.LENGTH_SHORT).show();
									break;
								case 1:
									Toast.makeText(mContext,
											R.string.feedback_jgtd,
											Toast.LENGTH_SHORT).show();
									break;
								case 2:
									Toast.makeText(mContext,
											R.string.feedback_lxbs,
											Toast.LENGTH_SHORT).show();
									break;
								case 3:
									Toast.makeText(mContext,
											R.string.feedback_xjxx,
											Toast.LENGTH_SHORT).show();
									break;

								default:
									Toast.makeText(mContext,
											R.string.feedback_error,
											Toast.LENGTH_SHORT).show();
									break;
								}
							}
						});
		builder.create().show();
	}
	// PR104 end
}
