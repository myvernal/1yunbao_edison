package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.AccountRecordInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.TimeUtils;

/**
 * 账户记录
 * 
 * @author admin
 * 
 */
public class AccountRecordAdapter extends BaseListAdapter<AccountRecordInfo> {

    private static final String TAG = "AccountRecordAdapter";
	private Context context;

	private String phoneS = "";

	public AccountRecordAdapter(Context context) {
		super(context);
		this.context = context;
		phoneS = application.getDriverName();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_charge, parent, false);
			holder.serialnumber = new AQuery(convertView).id(R.id.listitem_recharge_record_serialnumber);
			holder.phone = new AQuery(convertView).id(R.id.listitem_recharge_record_phone);
			holder.mode = new AQuery(convertView).id(R.id.listitem_recharge_record_mode);
			holder.money = new AQuery(convertView).id(R.id.listitem_recharge_record_money);
			holder.time = new AQuery(convertView).id(R.id.listitem_recharge_record_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AccountRecordInfo c = mList.get(position);
		holder.serialnumber.text(String.format(
				context.getResources().getString(R.string.string_other_recharge_record_serialnumber), c.getId()));
		holder.phone.text(phoneS);

		String typeStr = "";
        LogUtil.e(TAG, "交易类型:" + c.getBusiness_type());
		switch (c.getBusiness_type()) {
            case AccountRecordInfo.BUSINESS_TYPE_RECHARGE:
                typeStr = "充值";
                break;
			case AccountRecordInfo.BUSINESS_TYPE_VIOLATE:
				typeStr = "违约扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_DEAL:
				typeStr = "成功交易扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_VERIFY:
				typeStr = "验证身份扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_FREEZE:
				typeStr = "冻结";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_DEPOSIT:
				typeStr = "保证金扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_SMS:
				typeStr = "短信扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_DEPOSIT_RETURN:
				typeStr = "保证金返还";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_VIOLATE_RETURN:
				typeStr = "对方违约返还";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_LOCATION:
				typeStr = "手机定位扣除";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_AWARD:
				typeStr = "成功交易奖励";
				break;
			case AccountRecordInfo.BUSINESS_TYPE_VIOLATE_PAID:
				typeStr = "对方违约赔付";
				break;
            case AccountRecordInfo.BUSINESS_TYPE_INSURE_PAID:
                typeStr = "购买保险扣除";
                break;
		}

		holder.mode.text(typeStr);

		holder.money.text(String.valueOf(c.getBusiness_amount() + "元"));
		String create_time = TimeUtils.getDetailTime(c.getCreate_time());
		holder.time.text(create_time);
		return convertView;
	}

	static class ViewHolder {
		AQuery serialnumber, phone, mode, money, time;
	}
}