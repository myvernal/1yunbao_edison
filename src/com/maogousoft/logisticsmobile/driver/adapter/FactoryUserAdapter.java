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
        HolderView holderView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_factory_user, parent, false);
            holderView.name = ((TextView) convertView.findViewById(R.id.name));
            holderView.contact = ((TextView) convertView.findViewById(R.id.contact));
            holderView.phone = ((TextView) convertView.findViewById(R.id.phone));
            holderView.address = ((TextView) convertView.findViewById(R.id.address));
        } else {
            holderView = (HolderView) convertView.getTag(R.id.common_key);
        }
        holderView.name.setText(searchDpResultInfo.getCOMPANY_NAME());
        holderView.contact.setText(searchDpResultInfo.getNAME());
        holderView.phone.setText(searchDpResultInfo.getPHONE());
        holderView.address.setText(searchDpResultInfo.getADDRESS());
        convertView.setTag(searchDpResultInfo);
        convertView.setTag(R.id.common_key, holderView);
        convertView.setOnClickListener(this);
        return convertView;
    }

    class HolderView {
        private TextView name;
        private TextView contact;
        private TextView phone;
        private TextView address;
    }

    @Override
    public void onClick(View view) {
        SearchDpResultInfo searchDpResultInfo = (SearchDpResultInfo) view.getTag();
        Intent intent = new Intent(mContext, FactoryUserDetailActivity.class);
        intent.putExtra(Constants.COMMON_KEY, searchDpResultInfo);
        mContext.startActivity(intent);
    }
}