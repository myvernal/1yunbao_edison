package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.SafePinanInfo;
import com.maogousoft.logisticsmobile.driver.model.SafeSeaInfo;
import com.ybxiang.driver.activity.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/29.
 */
public class SafeListAdapter extends BaseListAdapter<SafePinanInfo> {

    private Context mContext;

    public SafeListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SafePinanInfo safeInfo = mList.get(position);
        HoldView holdView;
        if (convertView == null) {
            holdView = new HoldView();
            convertView = mInflater.inflate(R.layout.listitem_safe, parent, false);
            holdView.insured_name = ((TextView) convertView.findViewById(R.id.insured_name));
            holdView.shiping_number = ((TextView) convertView.findViewById(R.id.shiping_number));
            holdView.insurance_type = ((TextView) convertView.findViewById(R.id.insurance_type));
            holdView.start_date = ((TextView) convertView.findViewById(R.id.start_date));
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        //被保险人名称
        holdView.insured_name.setText(mContext.getString(R.string.safe_list_item_name, safeInfo.getInsured_name()));
        //运单号
        holdView.shiping_number.setText(mContext.getString(R.string.safe_list_item_number, safeInfo.getShiping_number()));
        //显示保险名称
        if (Constants.SAFE_CPIC == safeInfo.getType()) {
            String[] safeSeaType = mContext.getResources().getStringArray(R.array.safe_sea_types);
            Integer safeType = Integer.valueOf(safeInfo.getInsurance_type());
            if (safeType != null && safeType > 0) {
                for (int i = 0; i < Constants.seaSafeTypeValues.length; i++) {
                    if (Constants.seaSafeTypeValues[i] == safeType) {
                        holdView.insurance_type.setText(mContext.getString(R.string.safe_list_item_type, safeSeaType[i]));
                    }
                }
            }
        } else {
            String[] safeSeaType = mContext.getResources().getStringArray(R.array.safe_pingan_types);
            holdView.insurance_type.setText(safeSeaType[0]);
        }
        //起运时间
        if (!TextUtils.isEmpty(safeInfo.getStart_date())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(Long.parseLong(safeInfo.getStart_date()));
            holdView.start_date.setText(mContext.getString(R.string.safe_list_item_date, simpleDateFormat.format(date)));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, safeInfo.getType() == Constants.SAFE_CPIC ? SafeSeaDetailActivity.class : SafePinanDetailActivity.class);
                intent.putExtra(Constants.COMMON_KEY, safeInfo);
                mContext.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, safeInfo.getType() == Constants.SAFE_CPIC ? SafeSeaEditActivity.class : SafePinanEditActivity.class);
                intent.putExtra(Constants.COMMON_KEY, safeInfo);
                mContext.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(safeInfo.getTpy_Electronic_policy()) && safeInfo.getType() == Constants.SAFE_CPIC) {
//                    Intent intent = new Intent(mContext, SafeBaodanActivity.class);
//                    intent.putExtra(Constants.COMMON_KEY, safeInfo.getTpy_Electronic_policy());
//                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "正在通过浏览器下载保单,请注意查看通知栏状态", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(safeInfo.getTpy_Electronic_policy()));
                    mContext.startActivity(intent);
                } else if (safeInfo.getType() == Constants.SAFE_PINGAN && safeInfo.getId() > 0) {
                    Toast.makeText(mContext, "正在通过浏览器下载保单,请注意查看通知栏状态", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL + "/downLoadInsurancePdf?id=" + safeInfo.getId()));
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "该保险还没有上传电子保单,请稍后再查看", Toast.LENGTH_SHORT).show();
                }
            }
        });
        convertView.setTag(holdView);
        return convertView;
    }

    class HoldView {
        TextView insured_name;
        TextView shiping_number;
        TextView insurance_type;
        TextView start_date;
    }
}
