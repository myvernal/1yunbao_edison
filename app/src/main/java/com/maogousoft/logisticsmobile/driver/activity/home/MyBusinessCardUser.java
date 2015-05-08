package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.OptionalShipperActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ybxiang.driver.activity.MySourceActivity;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aliang on 2014/9/6.
 */
public class MyBusinessCardUser extends BaseActivity {
    // 返回,完善资料
    private Button mShareCard, mUpdate;
    private TextView insurance_count, fleet_count, verify_count, address, company_name, name, phone,
            telNum, company_recommendation;
    private RelativeLayout mHistory;
    // 个人abc信息
    private ShipperInfo userInfo;
    private View my_abc_layout;
    private ImageView company_photo, company_photo1, company_photo2, company_photo3;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybusiness_card_user);
        initViews();
        initUtils();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("物流名片");

        setIsRightKeyIntoShare(false);
        mShareCard = (Button) findViewById(R.id.titlebar_id_more);
        mShareCard.setText("发名片");

        company_recommendation = (TextView) findViewById(R.id.myself_recommendation);
        company_photo = (ImageView) findViewById(R.id.id_card_photo);
        company_photo1 = (ImageView) findViewById(R.id.car_photo1);
        company_photo2 = (ImageView) findViewById(R.id.car_photo2);
        company_photo3 = (ImageView) findViewById(R.id.car_photo3);
        mUpdate = (Button) findViewById(R.id.myabc_id_update);
        my_abc_layout = findViewById(R.id.my_abc_layout);

        insurance_count = (TextView) findViewById(R.id.insurance_count);
        address = (TextView) findViewById(R.id.address);
        verify_count = (TextView) findViewById(R.id.verify_count);
        company_name = (TextView) findViewById(R.id.company_name);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        telNum = (TextView) findViewById(R.id.telNum);
        fleet_count = (TextView) findViewById(R.id.fleet_count);
        mHistory = (RelativeLayout) findViewById(R.id.myabc_id_history);
        mHistory.setClickable(true);
        mHistory.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
    }

    /**
     * 初始化工具类 *
     */
    private void initUtils() {
        options = new DisplayImageOptions.Builder().resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageForEmptyUri(R.drawable.ic_img_loading)
                .displayer(new FadeInBitmapDisplayer(300)).build();
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
            startActivity(new Intent(mContext, MySourceActivity.class));
        } else if (v == mUpdate) {
            if(userInfo == null) {
                Toast.makeText(mContext, "正在获取用户信息,请稍后", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(mContext, OptionalShipperActivity.class).putExtra("info", userInfo));
        } else if (v == mShareCard) {
            // PR111 end
            mUpdate.setVisibility(View.GONE);
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
                            mUpdate.setVisibility(View.VISIBLE);
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
                    ShipperInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (ShipperInfo) result;

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
                                        telNum.setText(userInfo.getTelcom());

                                        if(!TextUtils.isEmpty(userInfo.getCompany_recommendation())) {
                                            company_recommendation.setText(userInfo.getCompany_recommendation());
                                        }
                                        //显示照片
                                        if(!TextUtils.isEmpty(userInfo.getCompany_logo())) {
                                            ImageLoader.getInstance().displayImage(userInfo.getCompany_logo(), company_photo, options,
                                                    new Utils.MyImageLoadingListener(mContext, company_photo));
                                        }
                                        if(!TextUtils.isEmpty(userInfo.getCompany_photo1())) {
                                            ImageLoader.getInstance().displayImage(userInfo.getCompany_photo1(), company_photo1, options,
                                                    new Utils.MyImageLoadingListener(mContext, company_photo1));
                                        }
                                        if(!TextUtils.isEmpty(userInfo.getCompany_photo2())) {
                                            ImageLoader.getInstance().displayImage(userInfo.getCompany_photo2(), company_photo2, options,
                                                    new Utils.MyImageLoadingListener(mContext, company_photo2));
                                        }
                                        if(!TextUtils.isEmpty(userInfo.getCompany_photo3())) {
                                            ImageLoader.getInstance().displayImage(userInfo.getCompany_photo3(), company_photo3, options,
                                                    new Utils.MyImageLoadingListener(mContext, company_photo3));
                                        }
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
