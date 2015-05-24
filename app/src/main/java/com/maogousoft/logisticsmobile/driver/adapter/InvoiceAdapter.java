package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.InvoiceActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * Created by aliang on 2015/4/26.
 */
public class InvoiceAdapter extends BaseListAdapter<NewSourceInfo> {

    private CityDBUtils dbUtils;
    private int checkedPosition = -1;//用于记录被选中的RadioButton的状态，并保证只可选一个
    private RadioButton preRadioButton;//保存上一个被选中的radioButton
    private boolean isShowRadioButton;
    private int userType;
    private InvoiceActivity.SelectItemCallBack callBack;
    public InvoiceAdapter(Context context, boolean isShowRadioButton, int userType, InvoiceActivity.SelectItemCallBack itemCallBack) {
        super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
        this.isShowRadioButton = isShowRadioButton;
        this.userType = userType;
        this.callBack = itemCallBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_invoice_adapter_layout, parent, false);
            holder = new ViewHolder();
            holder.source_detail_phone = convertView.findViewById(R.id.source_detail_phone);
            holder.order_info = (TextView) convertView.findViewById(R.id.source_id_order_info);
            holder.order_info_detail = (TextView) convertView.findViewById(R.id.source_id_order_info_detail);
            holder.order_money = (TextView) convertView.findViewById(R.id.source_id_order_money);
            holder.order_id = (TextView) convertView.findViewById(R.id.source_id);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);

            holder.source_detail_right_shipper = convertView.findViewById(R.id.source_detail_right_shipper);
            holder.invoice_qd = (TextView) convertView.findViewById(R.id.invoice_qd);
            holder.invoice_bj = (TextView) convertView.findViewById(R.id.invoice_bj);
            holder.invoice_call = (TextView) convertView.findViewById(R.id.invoice_call);
            if (!isShowRadioButton) {
                holder.radioButton.setVisibility(View.GONE);
            }
            if(userType == Constants.USER_SHIPPER) {
                holder.source_detail_right_shipper.setVisibility(View.VISIBLE);
                holder.source_detail_phone.setVisibility(View.GONE);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NewSourceInfo sourceInfo = (NewSourceInfo) getItem(position);
        final StringBuilder title = new StringBuilder();
        final StringBuilder detail = new StringBuilder();

        String wayStart = dbUtils.getCityInfo(sourceInfo.getStart_province(), sourceInfo.getStart_city(), sourceInfo.getStart_district());
        if (sourceInfo.getEnd_province() > 0 || sourceInfo.getEnd_city() > 0 || sourceInfo.getEnd_district() > 0) {
            String wayEnd = dbUtils.getCityInfo(sourceInfo.getEnd_province(), sourceInfo.getEnd_city(), sourceInfo.getEnd_district());
            title.append(wayStart).append("--").append(wayEnd);
        }

        detail.append(sourceInfo.getCargo_type_str());

        if (sourceInfo.getCargo_number() != null && sourceInfo.getCargo_number() != 0) {
            detail.append("/")
                    .append(sourceInfo.getCargo_desc())
                    .append(sourceInfo.getCargo_number())
                    .append(CheckUtils.getCargoUnitName(mContext, sourceInfo.getCargo_unit()));
        }

        if (!TextUtils.isEmpty(sourceInfo.getCar_type_str())) {
            detail.append("/");
            detail.append(sourceInfo.getCar_type_str());

        }

        if (sourceInfo.getCar_length() != null && sourceInfo.getCar_length() != 0
                && sourceInfo.getCar_length() != 0.0) {
            detail.append("/").append(sourceInfo.getCar_length()).append("米");
        }
        //是否可以订单确认
        if(TextUtils.equals("Y", sourceInfo.getIs_able_confim_contract())) {
            holder.order_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.source_line_h, 0, 0, 0);
        } else {
            holder.order_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.source_line, 0, 0, 0);
        }
        //是否可以接受邀约
        if(TextUtils.equals("Y", sourceInfo.getIs_has_invite())) {
            holder.order_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.source_line_h, 0, 0, 0);
        } else {
            holder.order_info.setCompoundDrawablesWithIntrinsicBounds(R.drawable.source_line, 0, 0, 0);
        }
        holder.order_info.setText(title.toString());
        holder.order_info_detail.setText(detail.toString());
        holder.order_money.setText(Html.fromHtml(String.format(mResources
                        .getString(R.string.string_home_newsource_order_money),
                sourceInfo.getUser_bond())));

        holder.order_id.setText("" + sourceInfo.getId());
        //电话号码
        holder.source_detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneStr = sourceInfo.getCargo_user_phone();
                if (!TextUtils.isEmpty(phoneStr)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneStr));
                    mContext.startActivity(intent);
                }
            }
        });
        //单选按钮
        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (checkedPosition == position) {
                    return;
                }
                //初始情况下没有被选中的radioButton
                if (preRadioButton != null) {
                    //重置之前被选中的radioButton
                    preRadioButton.setChecked(false);
                }
                //重置，确保最多只有一项被选中
                checkedPosition = position;
                //保存当前的被选中的radioButton
                preRadioButton = holder.radioButton;
                if(callBack != null) {
                    //回调货单状态
                    callBack.onItemCallBack(sourceInfo);
                }
            }
        });
        //单选按钮状态
        if (checkedPosition == position) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        if (userType == Constants.USER_SHIPPER) {
            int num1 = sourceInfo.getVie_driver_count();
            if (num1 > 0) {
                holder.invoice_qd.setTextColor(mContext.getResources().getColor(R.color.common_btn_bg));
                holder.invoice_qd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_qd_h, 0, 0, 0);
            } else {
                holder.invoice_qd.setTextColor(mContext.getResources().getColor(R.color.TextColorGray));
                holder.invoice_qd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_qd, 0, 0, 0);
            }
            holder.invoice_qd.setText(mContext.getString(R.string.invoice_action_number, num1));

            int num2 = sourceInfo.getOrder_vie_count();
            if (num2 > 0) {
                holder.invoice_bj.setTextColor(mContext.getResources().getColor(R.color.common_btn_bg));
                holder.invoice_bj.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_bj_h, 0, 0, 0);
            } else {
                holder.invoice_bj.setTextColor(mContext.getResources().getColor(R.color.TextColorGray));
                holder.invoice_bj.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_bj, 0, 0, 0);
            }
            holder.invoice_bj.setText(mContext.getString(R.string.invoice_action_number, num2));

            int num3 = sourceInfo.getOrder_place_count();
            if (num3 > 0) {
                holder.invoice_call.setTextColor(mContext.getResources().getColor(R.color.common_btn_bg));
                holder.invoice_call.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_call_h, 0, 0, 0);
            } else {
                holder.invoice_call.setTextColor(mContext.getResources().getColor(R.color.TextColorGray));
                holder.invoice_call.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invoice_call, 0, 0, 0);
            }
            holder.invoice_call.setText(mContext.getString(R.string.invoice_action_number, num3));
        }
        //合同是否签约成功
        if(TextUtils.equals("2", sourceInfo.getContract_status())) {
            holder.order_id.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invoice_icon, 0);
        } else {
            holder.order_id.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果状态是2,则合同签约失败,可查看失败状态
                if(TextUtils.equals("2", sourceInfo.getContract_status())) {
                    final MyAlertDialog dialog = new MyAlertDialog(mContext);
                    dialog.setTitle("签约失败");
                    dialog.setMessage(sourceInfo.getSigning_failed_reason());
                    dialog.setLeftButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //查看后消除小红点
                            dialog.dismiss();
                            sourceInfo.setContract_status("");
                            holder.order_id.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    Intent intent = new Intent(mContext, SourceDetailActivity.class);
                    intent.putExtra(Constants.ORDER_ID, sourceInfo.getId());
                    intent.putExtra("type", "InvoiceActivity");
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public NewSourceInfo getSelectedSource() {
        if (checkedPosition != -1) {
            return mList.get(checkedPosition);
        } else {
            return null;
        }
    }

    class ViewHolder {
        TextView order_info, order_info_detail, order_money, order_id;
        TextView invoice_qd, invoice_bj, invoice_call;
        View source_detail_phone, source_detail_right_shipper;
        RadioButton radioButton;
    }
}
