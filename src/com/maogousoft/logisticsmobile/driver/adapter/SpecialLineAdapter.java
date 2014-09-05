package com.maogousoft.logisticsmobile.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.ybxiang.driver.activity.SpecialLineDetailActivity;
import com.ybxiang.driver.model.SearchDpResultInfo;

/**
 * Created by aliang on 2014/9/6.
 */
public class SpecialLineAdapter extends BaseListAdapter<SearchDpResultInfo> implements View.OnClickListener {
    private Context mContext;

    public SpecialLineAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchDpResultInfo searchDpResultInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_special_line, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(searchDpResultInfo.getCOMPANY_NAME());
        ((TextView) convertView.findViewById(R.id.way)).setText(searchDpResultInfo.getFROMAREA() + searchDpResultInfo.getFROMAREA1() + "--"
                + searchDpResultInfo.getENDAREA() + searchDpResultInfo.getENDAREA1());
        ((TextView) convertView.findViewById(R.id.phone)).setText(searchDpResultInfo.getPHONE());
        convertView.setTag(searchDpResultInfo);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        SearchDpResultInfo searchDpResultInfo = (SearchDpResultInfo) view.getTag();
        Intent intent = new Intent(mContext, SpecialLineDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, searchDpResultInfo);
        mContext.startActivity(intent);
    }
}
