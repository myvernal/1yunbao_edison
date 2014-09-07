package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.ybxiang.driver.activity.MySourceActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aliang on 2014/9/6.
 */
public class MyBusinessCardUser extends BaseActivity {
    private Context mContext; // PR111
    // 返回,完善资料
    private Button mBack, mShareCard;
    private TextView insurance_count, fleet_count, verify_count, address, company_name, name, phone;
    private RelativeLayout mHistory;
    // 个人abc信息
    private HuoZhuUserInfo userInfo;
    private View my_abc_layout, shareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybusiness_card_user);
        mContext = this; // PR111
        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("物流名片");

        setIsRightKeyIntoShare(false);
        mShareCard = (Button) findViewById(R.id.titlebar_id_more);
        mShareCard.setText("发名片");

        my_abc_layout = findViewById(R.id.my_abc_layout);
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        insurance_count = (TextView) findViewById(R.id.insurance_count);
        address = (TextView) findViewById(R.id.address);
        verify_count = (TextView) findViewById(R.id.verify_count);
        company_name = (TextView) findViewById(R.id.company_name);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        fleet_count = (TextView) findViewById(R.id.fleet_count);
        shareInfo = findViewById(R.id.share_info);
        mHistory = (RelativeLayout) findViewById(R.id.myabc_id_history);
        mHistory.setClickable(true);
        mHistory.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getABCInfo();
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        if (v == mHistory) {
            startActivity(new Intent(context, MySourceActivity.class));
        } else if (v == mShareCard) {
            // PR111 end
            showSpecialProgress("正在制作物流名片,请稍后");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Bitmap.createBitmap(my_abc_layout.getWidth(), my_abc_layout.getHeight(), Bitmap.Config.ARGB_8888);
                    //利用bitmap生成画布
                    Canvas canvas = new Canvas(bitmap);
                    //把view中的内容绘制在画布上
                    my_abc_layout.draw(canvas);
                    final String path = saveBusinessCardUserBitmap(bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            //分享名片
                            shareCard(path);
                        }
                    });
                }
            }).start();
        }
    }

    // 获取我的abc信息
    private void getABCInfo() {
        // if (mAbcInfo != null) {
        // return;
        // }
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    HuoZhuUserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (HuoZhuUserInfo) result;

                                        if (!TextUtils.isEmpty(userInfo.getName())) {
                                            name.setText(userInfo.getName());
                                        }
                                        if (!TextUtils.isEmpty(userInfo.getCompany_name())) {
                                            company_name.setText(userInfo.getCompany_name());
                                        }
                                        phone.setText(userInfo.getPhone());
                                        address.setText(userInfo.getAddress());
                                        insurance_count.setText(userInfo.getInsurance_count());
                                        fleet_count.setText(userInfo.getFleet_count());
                                        verify_count.setText(userInfo.getVerify_count());
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null)
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

    /**
     * 保存货运名片图片
     *
     * @param mBitmap
     */
    public String saveBusinessCardUserBitmap(Bitmap mBitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "businessCardUser.png");
        FileOutputStream fileOutputStream = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (IOException e) {
            Toast.makeText(mContext, "在保存图片时出错：", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getPath();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getABCInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
