package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.SafeInfo;
import com.ybxiang.driver.activity.SafeSeaDetailActivity;
import com.ybxiang.driver.activity.SafeSeaEditActivity;

/**
 * Created by aliang on 2014/8/29.
 */
public class SafeListAdapter extends BaseListAdapter<SafeInfo> {

    private Context mContext;

    public SafeListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SafeInfo safeInfo = mList.get(position);
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
        holdView.insured_name.setText(holdView.insured_name.getText() + safeInfo.getInsured_name());
        //运单号
        holdView.shiping_number.setText(holdView.shiping_number.getText() + safeInfo.getShiping_number());
        //显示保险名称
        String[] safeSeaType = mContext.getResources().getStringArray(R.array.safe_sea_types);
        Integer safeType = Integer.valueOf(safeInfo.getInsurance_type());
        if(safeType != null && safeType>0) {
            for (int i = 0; i < Constants.seaSafeTypeValues.length; i++) {
                if (Constants.seaSafeTypeValues[i] == safeType) {
                    holdView.insurance_type.setText(holdView.insurance_type.getText() + safeSeaType[i]);
                }
            }
        }
        //起运时间
        holdView.start_date.setText(holdView.start_date.getText() + safeInfo.getStart_date());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SafeSeaDetailActivity.class);
                intent.putExtra(Constants.COMMON_KEY, safeInfo);
                mContext.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SafeSeaEditActivity.class);
                intent.putExtra(Constants.COMMON_KEY, safeInfo);
                mContext.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "查看电子保单:" + safeInfo.getInsured_name(), Toast.LENGTH_SHORT).show();
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
