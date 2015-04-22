package com.maogousoft.logisticsmobile.driver.activity.home;

// PR111 货运名片

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.activity.info.*;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;

/**
 * 货运名片
 *
 * @author lenovo
 */
public class MyBusinessCard extends BaseActivity {

    private Context mContext; // PR111
    // 返回,完善资料
    private Button mUpdate, mShareCard;
    private TextView mName, mNumber, mPhone, mUpdatePwd;
    private View mChangePath, my_info_card;
    private TextView mPath1, mPath2, mPath3, mCarNum, mCarlength, mCartype,
            mCarzhaizhong, myself_recommendation;
    private ImageView id_card_photo, car_photo1, car_photo2, car_photo3;
    private DisplayImageOptions options;
    // 个人abc信息
    private AbcInfo mAbcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card_layout);
        mContext = this; // PR111
        initViews();
        initUtils();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_home_business_card);

        setIsRightKeyIntoShare(false);
        mShareCard = (Button) findViewById(R.id.titlebar_id_more);
        mShareCard.setText("发名片");

        myself_recommendation = (TextView) findViewById(R.id.myself_recommendation);
        id_card_photo = (ImageView) findViewById(R.id.id_card_photo);
        car_photo1 = (ImageView) findViewById(R.id.car_photo1);
        car_photo2 = (ImageView) findViewById(R.id.car_photo2);
        car_photo3 = (ImageView) findViewById(R.id.car_photo3);

        mUpdate = (Button) findViewById(R.id.myabc_id_update);
        mName = (TextView) findViewById(R.id.business_card_part1_name);
        mNumber = (TextView) findViewById(R.id.business_card_part1_number);
        mPhone = (TextView) findViewById(R.id.business_card_part1_phone);
        mChangePath = findViewById(R.id.myabc_id_change_path);
        mUpdatePwd = (TextView) findViewById(R.id.myabc_id_updatepwd);

        mPath1 = (TextView) findViewById(R.id.myabc_id_path1);
        mPath2 = (TextView) findViewById(R.id.myabc_id_path2);
        mPath3 = (TextView) findViewById(R.id.myabc_id_path3);
        mCarNum = (TextView) findViewById(R.id.myabc_id_car_num);
        mCarlength = (TextView) findViewById(R.id.myabc_id_car_length111);
        mCartype = (TextView) findViewById(R.id.myabc_id_car_type);
        mCarzhaizhong = (TextView) findViewById(R.id.myabc_id_car_zhaizhong);

        my_info_card = findViewById(R.id.my_info_card);
        mShareCard.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mChangePath.setOnClickListener(this);
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
        if (v == mUpdatePwd) {
            startActivity(new Intent(context, UpdatePwdActivity.class));
        }
//        else if (v == mCreditContainer) {
//            startActivity(new Intent(mContext, MyCreditActivity.class).putExtra(
//                    "info", mAbcInfo));
//        } else if (v == mCharge) {
//            startActivity(new Intent(mContext, ChargeActivity.class));
//        } else if (v == mAccountRecord) {
//            startActivity(new Intent(mContext, AccountRecordActivity.class));
//        }
        else if (v == mUpdate) {
            startActivity(new Intent(context, OptionalActivity.class).putExtra(
                    "info", mAbcInfo));
        }
//        else if (v == mHistory) {
//            startActivity(new Intent(mContext, HistroyOrderActivity.class)
//                    .putExtra("info", mAbcInfo));
//        }
        else if (v == mChangePath) {
            // if (mAbcInfo == null) {
            // showMsg("请等待获取线路");
            // } else {

            String[] array = new String[]{"线路1", "线路2", "线路3"};
            new com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog.Builder(
                    context).setTitle("选择需要修改的路线")
                    .setItems(array, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(context,
                                    ChangePathActivity.class);
                            // intent.putExtra("info", mAbcInfo);
                            intent.putExtra("path", which);
                            startActivityForResult(intent, 1);
                        }
                    }).show();

            // }
        } else if (v == mShareCard) {
            // PR111 end
            showSpecialProgress("正在制作货运名片,请稍后");
            mUpdate.setVisibility(View.GONE);
            mChangePath.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Bitmap.createBitmap(my_info_card.getWidth(), my_info_card.getHeight(), Bitmap.Config.ARGB_8888);
                    //利用bitmap生成画布
                    Canvas canvas = new Canvas(bitmap);
                    //把view中的内容绘制在画布上
                    my_info_card.draw(canvas);
                    final String path = saveBusinessCardBitmap(bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mUpdate.setVisibility(View.VISIBLE);
                            mChangePath.setVisibility(View.VISIBLE);
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
            jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, "");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AbcInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        mAbcInfo = (AbcInfo) result;

                                        if (!TextUtils.isEmpty(mAbcInfo.getName())) {
                                            mName.setText(mAbcInfo.getName());
                                        }
                                        if (!TextUtils.isEmpty(mAbcInfo.getPlate_number())) {
                                            mNumber.setText(mAbcInfo.getPlate_number());
                                        }
                                        mPhone.setText(mAbcInfo.getPhone());

                                        CityDBUtils mDBUtils = new CityDBUtils(
                                                application.getCitySDB());
                                        String path1Str = mDBUtils.getStartEndStr(
                                                mAbcInfo.getStart_province(),
                                                mAbcInfo.getStart_city(),
                                                mAbcInfo.getEnd_province(),
                                                mAbcInfo.getEnd_city());
                                        String path2Str = mDBUtils.getStartEndStr(
                                                mAbcInfo.getStart_province2(),
                                                mAbcInfo.getStart_city2(),
                                                mAbcInfo.getEnd_province2(),
                                                mAbcInfo.getEnd_city2());
                                        String path3Str = mDBUtils.getStartEndStr(
                                                mAbcInfo.getStart_province3(),
                                                mAbcInfo.getStart_city3(),
                                                mAbcInfo.getEnd_province3(),
                                                mAbcInfo.getEnd_city3());

                                        mPath1.setText(path1Str);
                                        mPath2.setText(path2Str);
                                        mPath3.setText(path3Str);

                                        if (!TextUtils.isEmpty(mAbcInfo
                                                .getPlate_number())) {
                                            mCarNum.setText(mAbcInfo
                                                    .getPlate_number());
                                        }

                                        mCarlength.setText(mAbcInfo.getCar_length()
                                                + "米");
                                        if (!TextUtils.isEmpty(mAbcInfo
                                                .getCar_type_str())) {
                                            mCartype.setText(mAbcInfo
                                                    .getCar_type_str());
                                        }
                                        mCarzhaizhong.setText(mAbcInfo.getCar_weight() + "吨");
                                        if(!TextUtils.isEmpty(mAbcInfo.getMyself_recommendation())) {
                                            myself_recommendation.setText(mAbcInfo.getMyself_recommendation());
                                        }
                                        //显示照片
                                        if(!TextUtils.isEmpty(mAbcInfo.getId_card_photo())) {
                                            ImageLoader.getInstance().displayImage(mAbcInfo.getId_card_photo(), id_card_photo, options,
                                                    new Utils.MyImageLoadingListener(context, id_card_photo));
                                        }
                                        if(!TextUtils.isEmpty(mAbcInfo.getCar_photo1())) {
                                            ImageLoader.getInstance().displayImage(mAbcInfo.getCar_photo1(), car_photo1, options,
                                                    new Utils.MyImageLoadingListener(context, car_photo1));
                                        }
                                        if(!TextUtils.isEmpty(mAbcInfo.getCar_photo2())) {
                                            ImageLoader.getInstance().displayImage(mAbcInfo.getCar_photo2(), car_photo2, options,
                                                    new Utils.MyImageLoadingListener(context, car_photo2));
                                        }
                                        if(!TextUtils.isEmpty(mAbcInfo.getCar_photo3())) {
                                            ImageLoader.getInstance().displayImage(mAbcInfo.getCar_photo3(), car_photo3, options,
                                                    new Utils.MyImageLoadingListener(context, car_photo3));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getABCInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        application.finishAllActivity();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 保存货运名片图片
     *
     * @param mBitmap
     */
    public String saveBusinessCardBitmap(Bitmap mBitmap) {
//        File file = new File(mContext.getFilesDir().getPath() + File.separator + "businessCard.png");
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "businessCard.png");
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

}
