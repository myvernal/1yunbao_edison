package com.maogousoft.logisticsmobile.driver.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.other.MapActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.ybxiang.driver.activity.MyCarsDetailActivity;
import com.ybxiang.driver.model.LocationInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/9/8.
 */
public class CloudSearchAdapter extends BaseListAdapter<CarInfo> {

    private Context mContext;

    public CloudSearchAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CarInfo carInfo = mList.get(position);
        final HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = mInflater.inflate(R.layout.listitem_cloud_search, parent, false);
            holderView.nameId = ((TextView) convertView.findViewById(R.id.nameId));
            holderView.plate_numberId = ((TextView) convertView.findViewById(R.id.plate_numberId));
            holderView.phone = ((TextView) convertView.findViewById(R.id.phone));
            holderView.location_time = ((TextView) convertView.findViewById(R.id.location_time));
            holderView.locationId = ((TextView) convertView.findViewById(R.id.locationId));
            holderView.right = convertView.findViewById(R.id.right);
        } else {
            holderView = (HolderView) convertView.getTag(R.id.common_key);
        }
        holderView.nameId.setText(carInfo.getDriver_name());
        holderView.plate_numberId.setText(carInfo.getPlate_number());
        holderView.phone.setText(carInfo.getPhone());
        if(!TextUtils.isEmpty(carInfo.getLocation_time()) && Long.valueOf(carInfo.getLocation_time()) > 0) {
            Date date = new Date(Long.valueOf(carInfo.getLocation_time()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
            String locationTime = simpleDateFormat.format(date);
            holderView.location_time.setText(locationTime);
        } else if(carInfo.getPulish_date() > 0) {
            Date date = new Date(Long.valueOf(carInfo.getPulish_date()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
            String locationTime = simpleDateFormat.format(date);
            holderView.location_time.setText(locationTime);
        }
        holderView.locationId.setText(carInfo.getLocation());
        convertView.setTag(carInfo);
        convertView.setTag(R.id.common_key, holderView);
        holderView.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = new String[]{"免费定位", "收费定位"};
                new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
                        mContext).setTitle("请选择定位类型")
                        .setItems(array, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    location(true, carInfo, holderView);
                                } else {
                                    location(false, carInfo, holderView);
                                }
                            }
                        }).show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarInfo carInfo = (CarInfo) view.getTag();
                Intent intent = new Intent(mContext, MyCarsDetailActivity.class);
                intent.putExtra(Constants.COMMON_KEY, carInfo.getId());
                intent.putExtra(Constants.COMMON_ACTION_KEY, "search");
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class HolderView {
        public TextView nameId;
        public TextView plate_numberId;
        public TextView phone;
        public TextView location_time;
        public TextView locationId;
        public View right;
    }

    /**
     * 定位
     *
     * @param isFreeLocation 是否是免费定位
     */
    private void location(final boolean isFreeLocation, final CarInfo carInfo, final HolderView holderView) {
        try {
            Toast.makeText(mContext, "正在定位,请稍后", Toast.LENGTH_SHORT).show();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, isFreeLocation ? Constants.FREE_LOCATION : Constants.PHONE_LOCATION);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("mobile", carInfo.getPhone()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    LocationInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof LocationInfo) {
                                        LocationInfo info = (LocationInfo) result;
                                        if (!info.isDone()) {
                                            holderView.locationId.setText(info.getAddress());
                                            holderView.locationId.setVisibility(View.VISIBLE);
                                            //定位时间
                                            if(null != info.getTimestamp()) {
                                                Date date = new Date(Long.valueOf(info.getTimestamp()));
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
                                                String locationTime = simpleDateFormat.format(date);
                                                holderView.location_time.setText(locationTime);
                                            }

                                            Intent intent = new Intent(mContext, MapActivity.class);
                                            intent.putExtra(Constants.COMMON_KEY, info);
                                            mContext.startActivity(intent);
                                        } else {
                                            Toast.makeText(mContext, "定位超时", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result instanceof String)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result instanceof String) {
                                        if (isFreeLocation) {
                                            showFreeLocationDialog();
                                        } else {
                                            showPhoneLocationDialogFailed();
                                        }
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

    /**
     * 免费定位未注册提示
     */
    private void showFreeLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("邀请加入");
        builder.setMessage("对方现在还不是易运宝用户,邀请对方注册使用,从此平台随时自动更新司机位置.");
        builder.setPositiveButton("邀请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.string_share_tips));
                mContext.startActivity(Intent.createChooser(intent, "分享"));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 收费定位未开通提示
     */
    private void showPhoneLocationDialogFailed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("定位授权");
        builder.setMessage("将发送信息给对方，对方回复Y后，请再次点击手机定位.司机也可以主动编辑短信Y，移动发送至10658012174，联通发送至106550101832224261");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}

