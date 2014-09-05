package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.ybxiang.driver.activity.FactoryUserDetailActivity;
import com.ybxiang.driver.activity.SpecialLineDetailActivity;
import com.ybxiang.driver.model.SearchDpResultInfo;

/**
 * Created by aliang on 2014/9/6.
 */
public class FactoryUserAdapter extends BaseListAdapter<SearchDpResultInfo> implements View.OnClickListener {

    private Context mContext;

    public FactoryUserAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchDpResultInfo searchDpResultInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_factory_user, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(searchDpResultInfo.getCOMPANY_NAME());
        ((TextView) convertView.findViewById(R.id.contact)).setText(searchDpResultInfo.getNAME());
        ((TextView) convertView.findViewById(R.id.phone)).setText(searchDpResultInfo.getPHONE());
        ((TextView) convertView.findViewById(R.id.address)).setText(searchDpResultInfo.getADDRESS());
        convertView.setTag(searchDpResultInfo);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        SearchDpResultInfo searchDpResultInfo = (SearchDpResultInfo) view.getTag();
        Intent intent = new Intent(mContext, FactoryUserDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, searchDpResultInfo);
        mContext.startActivity(intent);
    }
}