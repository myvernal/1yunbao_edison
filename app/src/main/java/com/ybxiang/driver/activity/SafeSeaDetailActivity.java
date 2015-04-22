package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.model.SafeSeaInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/29.
 */
public class SafeSeaDetailActivity extends BaseActivity {

    private TextView start_date, insurer_name, insured_name, insurer_phone,
            insured_phone, shiping_number, packet_number, ship_type, ship_tool,
            plate_number, start_area, end_area;
    private TextView package_type, cargo_type1, cargo_type2;
    private TextView insurance_type,ratio,amount_covered,insurance_charge;
    private SafeSeaInfo safeSeaInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_sea_confirm_layout);
        initViews();
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("中国太平洋保险投保系统确认订单");
        // 返回按钮生效

        // 更多按钮隐藏
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);

        insurance_charge = (TextView) findViewById(R.id.insurance_charge);
        amount_covered = (TextView) findViewById(R.id.amount_covered);
        insurance_type = (TextView) findViewById(R.id.insurance_type);
        ratio = (TextView) findViewById(R.id.ratio);
        start_date = (TextView) findViewById(R.id.start_date);
        insurer_name = (TextView) findViewById(R.id.insurer_name);
        insured_name = (TextView) findViewById(R.id.insured_name);
        insurer_phone = (TextView) findViewById(R.id.insurer_phone);
        insured_phone = (TextView) findViewById(R.id.insured_phone);
        shiping_number = (TextView) findViewById(R.id.shiping_number);
        packet_number = (TextView) findViewById(R.id.packet_number);
        ship_type = (TextView) findViewById(R.id.ship_type);
        ship_tool = (TextView) findViewById(R.id.ship_tool);
        plate_number = (TextView) findViewById(R.id.plate_number);
        start_area = (TextView) findViewById(R.id.start_area);
        end_area = (TextView) findViewById(R.id.end_area);
        start_date = (TextView) findViewById(R.id.start_date);

        package_type = (TextView) findViewById(R.id.package_type);
        cargo_type1 = (TextView) findViewById(R.id.cargo_type1);
        cargo_type2 = (TextView) findViewById(R.id.cargo_type2);
    }

    private void initData() {
        safeSeaInfo = (SafeSeaInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if(safeSeaInfo != null) {
            //显示保险名称
            String[] safeSeaType = getResources().getStringArray(R.array.safe_sea_types);
            Integer safeType = Integer.valueOf(safeSeaInfo.getInsurance_type());
            if(safeType != null && safeType>0) {
                for (int i = 0; i < Constants.seaSafeTypeValues.length; i++) {
                    if (Constants.seaSafeTypeValues[i] == safeType) {
                        insurance_type.setText(insurance_type.getText() + safeSeaType[i]);
                    }
                }
            }
            ratio.setText(ratio.getText().toString() + safeSeaInfo.getRatio() + "%");
            amount_covered.setText(amount_covered.getText().toString() + safeSeaInfo.getAmount_covered() +"万元(人民币)");
            insurance_charge.setText(insurance_charge.getText().toString() + safeSeaInfo.getInsurance_charge() + "元");

            start_date.setText(safeSeaInfo.getStart_date());
            insurer_name.setText(safeSeaInfo.getInsurer_name());
            insured_name.setText(safeSeaInfo.getInsured_name());
            insurer_phone.setText(safeSeaInfo.getInsurer_phone());
            insured_phone.setText(safeSeaInfo.getInsured_phone());
            shiping_number.setText(safeSeaInfo.getShiping_number());
            packet_number.setText(safeSeaInfo.getPacket_number());
            ship_type.setText(safeSeaInfo.getShip_type());
            ship_tool.setText(safeSeaInfo.getShip_tool());
            plate_number.setText(safeSeaInfo.getPlate_number());
            start_area.setText(safeSeaInfo.getStart_area());
            end_area.setText(safeSeaInfo.getEnd_area());
            //显示包装代码
            String[] safeBZDMType = getResources().getStringArray(R.array.safe_bzdm_types);
            String bzdmType = safeSeaInfo.getPackage_type();
            if(!TextUtils.isEmpty(bzdmType)) {
                for (int i = 0; i < Constants.seaSafeBZDMTypeValues.length; i++) {
                    if (Constants.seaSafeBZDMTypeValues[i].equals(bzdmType)) {
                        package_type.setText(safeBZDMType[i]);
                    }
                }
            }
            //显示货运类型1
            String[] safeSourceType = getResources().getStringArray(R.array.safe_sea_source_types);
            Integer sourceType = Integer.valueOf(safeSeaInfo.getCargo_type1());
            if(sourceType != null && sourceType>0) {
                for (int i = 0; i < Constants.seaSafeSourceTypeValues.length; i++) {
                    if (Constants.seaSafeSourceTypeValues[i] == sourceType) {
                        cargo_type1.setText(safeSourceType[i]);
                    }
                }
            }

            //显示货运类型2
            String[] safeSourceType1 = getResources().getStringArray(R.array.safe_sea_source_types);
            Integer sourceType1 = Integer.valueOf(safeSeaInfo.getCargo_type1());
            if(sourceType1 != null && sourceType1>0) {
                for (int i = 0; i < Constants.seaSafeSourceTypeValues.length; i++) {
                    if (Constants.seaSafeSourceTypeValues[i] == sourceType1) {
                        cargo_type2.setText(safeSourceType1[i]);
                    }
                }
            }
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void onClickBack(View view) {
        onBackPressed();
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void onClickNext(View view) {
        try {
            showSpecialProgress("正在投保,请稍后");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.SAVE_CPIC);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("type", Constants.SAFE_CPIC)
                    .put("insurer_name", safeSeaInfo.getInsurer_name())
                    .put("insured_name", safeSeaInfo.getInsured_name())
                    .put("insurer_phone", safeSeaInfo.getInsurer_phone())
                    .put("insurer_name", safeSeaInfo.getInsurer_name())
                    .put("insured_phone", safeSeaInfo.getInsured_phone())
                    .put("shiping_number", safeSeaInfo.getShiping_number())
                    .put("packet_number", safeSeaInfo.getPacket_number())
                    .put("ship_type", safeSeaInfo.getShip_type())
                    .put("ship_tool", safeSeaInfo.getShip_tool())
                    .put("plate_number", safeSeaInfo.getPlate_number())
                    .put("start_date", safeSeaInfo.getStart_date())
                    .put("amount_covered", Double.valueOf(safeSeaInfo.getAmount_covered()) * 10000)
                    .put("insurance_type", safeSeaInfo.getInsurance_type())
                    .put("ratio", safeSeaInfo.getRatio())
                    .put("insurance_charge", safeSeaInfo.getInsurance_charge())
                    .put("start_area", safeSeaInfo.getStart_area())
                    .put("end_area", safeSeaInfo.getEnd_area())
                    .put("package_type", safeSeaInfo.getPackage_type())
                    .put("cargo_type1", safeSeaInfo.getCargo_type1())
                    .put("cargo_type2", safeSeaInfo.getCargo_type2()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "添加保单成功!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SafeListActivity.class);
                                    intent.putExtra(Constants.COMMON_KEY, Constants.SAFE_CPIC);
                                    startActivity(intent);
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
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
