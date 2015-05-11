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

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by aliang on 2015/4/26.
 */
public class InvoiceAdapter extends BaseListAdapter<NewSourceInfo> {

    private CityDBUtils dbUtils;
    private int checkedPosition = -1;//用于记录被选中的RadioButton的状态，并保证只可选一个
    private RadioButton preRadioButton;//保存上一个被选中的radioButton

    public InvoiceAdapter(Context context) {
        super(context);
        dbUtils = new CityDBUtils(application.getCitySDB());
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
                if(checkedPosition == position) {
                    return;
                }
                //初始情况下没有被选中的radioButton
                if(preRadioButton != null) {
                    //重置之前被选中的radioButton
                    preRadioButton.setChecked(false);
                }
                //重置，确保最多只有一项被选中
                checkedPosition = position;
                //保存当前的被选中的radioButton
                preRadioButton = holder.radioButton;
            }
        });
        //单选按钮状态
        if (checkedPosition == position) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }
        return convertView;
    }

    public NewSourceInfo getSelectedSource() {
        if(checkedPosition != -1) {
            return mList.get(checkedPosition);
        } else {
            return null;
        }
    }

    class ViewHolder {
        TextView order_info, order_info_detail, order_money, order_id;
        View source_detail_phone;
        RadioButton radioButton;
    }
}
