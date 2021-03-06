package com.maogousoft.logisticsmobile.driver.activity.info;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CarTypeListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.DictInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.CheckEditText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ybxiang.driver.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 完善司机资料
 *
 * @author lenovo
 */
public class OptionalActivity extends BaseActivity {
    private Button mLogin, mRegister;
    private CheckEditText mShuiChePhone, mName, mLength, mWeight, mNumber, info_id_register_id_card,
            mChezhuPhone, myself_recommendation, optional_linkman, optional_frame_number, optional_bank,
            optional_bank_account, optional_account_name, optional_engine_number, optional_frame_car_number, optional_car_registration_number;
    private Spinner mCarType;
    private CarTypeListAdapter mCarTypeAdapter;
    private boolean isFormRegisterActivity;
    private boolean isFormPushSourceDetail;// 是否从推送进入，并且没有完善资料，然后进入的本页面
    private DriverInfo driverInfo;
    //头像
    private String userPhoto;
    private String userPhotoUrl;
    // 保存车辆照片的list
    private String mCarPhotos1;
    private String mCarPhotos2;
    private String mCarPhotos3;
    // 保存车辆照片url的list
    private String mCarPhotosUrl1;
    private String mCarPhotosUrl2;
    private String mCarPhotosUrl3;

    private ImageView id_card_photo, car_photo1, car_photo2, car_photo3;
    private static final int RESULT_CAPTURE_IMAGE_ID_PHOTO = 1001;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO1 = 1002;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO2 = 1003;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO3 = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_optional);
        setIsShowAnonymousActivity(false);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_register_complete_title);

        mLogin = (Button) findViewById(R.id.titlebar_id_more);
        mRegister = (Button) findViewById(R.id.info_id_register_submit);

        mShuiChePhone = (CheckEditText) findViewById(R.id.info_id_register_shuiche_phone);
        mChezhuPhone = (CheckEditText) findViewById(R.id.info_id_register_chezhu_phone);
        mName = (CheckEditText) findViewById(R.id.info_id_register_name);
        mLength = (CheckEditText) findViewById(R.id.info_id_register_length);
        mWeight = (CheckEditText) findViewById(R.id.info_id_register_weight);
        mNumber = (CheckEditText) findViewById(R.id.info_id_register_number);
        myself_recommendation = (CheckEditText) findViewById(R.id.myself_recommendation);
        info_id_register_id_card = (CheckEditText) findViewById(R.id.info_id_register_id_card);
        optional_linkman = (CheckEditText) findViewById(R.id.optional_linkman);
        optional_frame_number = (CheckEditText) findViewById(R.id.optional_frame_number);
        optional_bank = (CheckEditText) findViewById(R.id.optional_bank);
        optional_bank_account = (CheckEditText) findViewById(R.id.optional_bank_account);
        optional_account_name = (CheckEditText) findViewById(R.id.optional_account_name);
        optional_engine_number = (CheckEditText) findViewById(R.id.optional_engine_number);
        optional_frame_car_number = (CheckEditText) findViewById(R.id.optional_frame_car_number);
        optional_car_registration_number = (CheckEditText) findViewById(R.id.optional_car_registration_number);

        mCarType = (Spinner) findViewById(R.id.info_id_register_type);
        // mSkan.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        // mSkan.getPaint().setUnderlineText(true);
        id_card_photo = (ImageView) findViewById(R.id.id_card_photo);
        car_photo1 = (ImageView) findViewById(R.id.car_photo1);
        car_photo2 = (ImageView) findViewById(R.id.car_photo2);
        car_photo3 = (ImageView) findViewById(R.id.car_photo3);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        id_card_photo.setOnClickListener(this);
        car_photo1.setOnClickListener(this);
        car_photo2.setOnClickListener(this);
        car_photo3.setOnClickListener(this);
    }

    // 初始化数据，获取车型
    private void initData() {
        if (getIntent().hasExtra("isFormRegisterActivity")) {
            isFormRegisterActivity = getIntent().getBooleanExtra("isFormRegisterActivity", false);
        }

        if (getIntent().hasExtra("isFormPushSourceDetail")) {
            isFormPushSourceDetail = getIntent().getBooleanExtra("isFormPushSourceDetail", false);
        }

        mCarTypeAdapter = new CarTypeListAdapter(mContext);
        mCarType.setAdapter(mCarTypeAdapter);
        mCarType.setPrompt("高栏");
        application.getDictList(new AjaxCallBack() {

            @Override
            public void receive(int code, Object result) {
                switch (code) {
                    case ResultCode.RESULT_OK:
                        @SuppressWarnings("unchecked")
                        List<DictInfo> mList = (List<DictInfo>) result;
                        List<DictInfo> mList2 = new ArrayList<DictInfo>();
                        for (DictInfo d : mList) {
                            if (d.getDict_type() != null
                                    && d.getDict_type().equals(Constants.CAR_TYPE)
                                    && (!d.getName().equals("不限")))
                                mList2.add(d);
                        }
                        mCarTypeAdapter.setList(mList2);

                        // 第一次完善资料，默认显示高栏
                        for (int y = 0; y < mList2.size(); y++) {
                            DictInfo d = mList2.get(y);
                            if (d.getName().equals("高栏")) {
                                mCarType.setSelection(y);
                            }
                        }

                        // 司机本身已经设置过 车辆类型，需要显示到界面
                        if (driverInfo != null) {
                            if (mCarTypeAdapter.getList() != null) {
                                for (int i = 0; i < mCarTypeAdapter.getList()
                                        .size(); i++) {
                                    if (mCarTypeAdapter.getList().get(i).getId() == driverInfo.getCar_type()) {
                                        mCarType.setSelection(i);
                                        mCarType.setEnabled(false);
                                        break;
                                    }
                                }
                            }
                        }

                        break;
                    case ResultCode.RESULT_ERROR:
                        showMsg(result.toString());
                        break;
                    case ResultCode.RESULT_FAILED:
                        showMsg(result.toString());
                        break;

                    default:
                        break;
                }
            }
        });

        // 随车手机默认显示注册手机号码
        mShuiChePhone.setText(application.getUserName());

        if (getIntent().hasExtra("info")) {
            driverInfo = (DriverInfo) getIntent().getSerializableExtra("info");
            if (driverInfo != null) {
                info_id_register_id_card.setText(driverInfo.getId_card(), true);
                optional_linkman.setText(driverInfo.getLinkman(), false);
                optional_frame_number.setText(driverInfo.getFrame_number(), true);
                optional_engine_number.setText(driverInfo.getEngine_number(), true);
                optional_frame_car_number.setText(driverInfo.getFrame_car_number(), true);
                optional_car_registration_number.setText(driverInfo.getCar_registration_number(), true);
                mShuiChePhone.setText(driverInfo.getCar_phone(), false);
                mChezhuPhone.setText(driverInfo.getOwner_phone(), false);
                mName.setText(driverInfo.getName(), true);
                mNumber.setText(driverInfo.getPlate_number(), true);
                mLength.setText(driverInfo.getCar_length() + "", true);
                mWeight.setText(driverInfo.getCar_weight() + "", true);
                if (mCarTypeAdapter.getList() != null) {
                    for (int i = 0; i < mCarTypeAdapter.getList().size(); i++) {
                        if (mCarTypeAdapter.getList().get(i).getId() == driverInfo.getCar_type()) {
                            mCarType.setSelection(i);
                            mCarType.setEnabled(false);
                            break;
                        }
                    }
                }

                optional_bank_account.setText(driverInfo.getBank_account(), true);
                optional_account_name.setText(driverInfo.getAccount_name(), true);
                optional_bank.setText(driverInfo.getBank(), true);
                if (TextUtils.isEmpty(driverInfo.getMyself_recommendation())) {
                    myself_recommendation.setText(R.string.business_description);
                } else {
                    myself_recommendation.setText(driverInfo.getMyself_recommendation());
                }
                if (!TextUtils.isEmpty(driverInfo.getId_card_photo())) {
                    ImageLoader.getInstance().displayImage(driverInfo.getId_card_photo(), id_card_photo, options,
                            new Utils.MyImageLoadingListener(mContext, id_card_photo));
                    userPhotoUrl = driverInfo.getId_card_photo();
                }
                if (!TextUtils.isEmpty(driverInfo.getCar_photo1())) {
                    ImageLoader.getInstance().displayImage(driverInfo.getCar_photo1(), car_photo1, options,
                            new Utils.MyImageLoadingListener(mContext, car_photo1));
                    mCarPhotosUrl1 = driverInfo.getCar_photo1();
                }
                if (!TextUtils.isEmpty(driverInfo.getCar_photo2())) {
                    ImageLoader.getInstance().displayImage(driverInfo.getCar_photo2(), car_photo2, options,
                            new Utils.MyImageLoadingListener(mContext, car_photo2));
                    mCarPhotosUrl2 = driverInfo.getCar_photo2();
                }
                if (!TextUtils.isEmpty(driverInfo.getCar_photo3())) {
                    ImageLoader.getInstance().displayImage(driverInfo.getCar_photo3(), car_photo3, options,
                            new Utils.MyImageLoadingListener(mContext, car_photo3));
                    mCarPhotosUrl3 = driverInfo.getCar_photo3();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        switch (id) {
            case R.id.info_id_register_submit:
                if (CheckUtils.checkIsEmpty(mShuiChePhone)) {
                    if (!CheckUtils.checkPhone(mShuiChePhone.getText().toString())) {
                        showMsg("随车手机号码格式不正确");
                        mShuiChePhone.requestFocus();
                        mShuiChePhone.selectAll();
                        clearPhoto();
                        return;
                    }
                }
                if (!CheckUtils.checkIsEmpty(mName)) {
                    showMsg("司机姓名不能为空");
                    mName.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsBankCard(optional_bank_account)) {
                    showMsg("请输入正确的银行卡号");
                    mName.requestFocus();
                    clearPhoto();
                    return;
                }
                if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
                    if (!CheckUtils.checkPhone(mChezhuPhone.getText().toString())) {
                        showMsg("车主手机号码格式不正确");
                        mChezhuPhone.requestFocus();
                        mChezhuPhone.selectAll();
                        clearPhoto();
                        return;
                    }
                }
                if (!CheckUtils.checkIsEmpty(mLength)) {
                    showMsg("车长不能为空");
                    mLength.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mWeight)) {
                    showMsg("载重量不能为空");
                    mWeight.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mNumber)) {
                    showMsg("车牌号不能为空");
                    mNumber.requestFocus();
                    clearPhoto();
                    return;
                }
                uploadImageAndSubmit();
                break;
            case R.id.titlebar_id_more:
                startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case R.id.id_card_photo:
                Intent imageCaptureIntent0 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent0.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "ID_PHOTO.jpg")));//照片存储目录
                startActivityForResult(imageCaptureIntent0, RESULT_CAPTURE_IMAGE_ID_PHOTO);
                break;
            case R.id.car_photo1:
                Intent imageCaptureIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO1.jpg")));
                startActivityForResult(imageCaptureIntent1, RESULT_CAPTURE_IMAGE_CAR_PHOTO1);
                break;
            case R.id.car_photo2:
                Intent imageCaptureIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO2.jpg")));
                startActivityForResult(imageCaptureIntent2, RESULT_CAPTURE_IMAGE_CAR_PHOTO2);
                break;
            case R.id.car_photo3:
                Intent imageCaptureIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent3.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO3.jpg")));
                startActivityForResult(imageCaptureIntent3, RESULT_CAPTURE_IMAGE_CAR_PHOTO3);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAPTURE_IMAGE_ID_PHOTO://拍照
                    String ID_PHOTO = Environment.getExternalStorageDirectory() + File.separator + "ID_PHOTO.jpg";
                    displayPhoto(ID_PHOTO, id_card_photo);
                    userPhoto = ID_PHOTO;
                    break;
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO1:
                    String CAR_PHOTO1 = Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO1.jpg";
                    displayPhoto(CAR_PHOTO1, car_photo1);
                    mCarPhotos1 = CAR_PHOTO1;
                    break;
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO2:
                    String CAR_PHOTO2 = Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO2.jpg";
                    displayPhoto(CAR_PHOTO2, car_photo2);
                    mCarPhotos2 = CAR_PHOTO2;
                    break;
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO3:
                    String CAR_PHOTO3 = Environment.getExternalStorageDirectory() + File.separator + "CAR_PHOTO3.jpg";
                    displayPhoto(CAR_PHOTO3, car_photo3);
                    mCarPhotos3 = CAR_PHOTO3;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 调整图片并显示
     *
     * @param url
     * @param imgView
     */
    private void displayPhoto(final String url, final ImageView imgView) {
        imageLoader.displayImage(url, imgView);
       /* File file = new File(url);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(url);
            final Bitmap smallBitmap = Utils.getScaleBitmapSmall(bm);
            final Bitmap bitmap = Utils.rotateBitmap(smallBitmap, Utils.readPictureDegree(url));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Utils.saveBitmap(url, bitmap);
                    //将图片显示到ImageView中
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            imgView.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        }*/
    }

    private void clearPhoto() {
        userPhotoUrl = null;
        mCarPhotosUrl1 = null;
        mCarPhotosUrl2 = null;
        mCarPhotosUrl3 = null;
    }

    // 提交注册
    private void submit() {
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.DRIVER_REG_OPTIONAL);
            jsonObject.put(Constants.TOKEN, null);

            if (!CheckUtils.checkIsEmpty(mShuiChePhone)) {
                params.put("car_phone", application.getUserName());
            } else {
                params.put("car_phone", mShuiChePhone.getText().toString());
            }

            if (CheckUtils.checkIsEmpty(mChezhuPhone)) {
                params.put("owner_phone", mChezhuPhone.getText().toString());
            }

            // params.put("vcode", mVerifyCode.getText().toString());
            // params.put("password",
            // MD5.encode(mPassword.getText().toString()));
            // mChezhuPhone
            params.put("driver_name", mName.getText().toString());
            params.put("car_length", mLength.getText().toString());
            params.put("car_type", mCarTypeAdapter.getList().get(mCarType.getSelectedItemPosition()).getId());
            params.put("car_weight", mWeight.getText().toString());
            params.put("plate_number", mNumber.getText().toString());
            params.put("myself_recommendation", myself_recommendation.getText().toString());
            params.put("id_card", info_id_register_id_card.getText().toString());
            params.put("linkman", optional_linkman.getText().toString());
            params.put("account_name", optional_account_name.getText().toString());
            params.put("frame_number", optional_frame_number.getText().toString());
            params.put("engine_number", optional_engine_number.getText().toString());
            params.put("frame_car_number", optional_frame_car_number.getText().toString());
            params.put("car_registration_number", optional_car_registration_number.getText().toString());
            params.put("bank", optional_bank.getText().toString());
            params.put("bank_account", optional_bank_account.getText().toString());
            if (!TextUtils.isEmpty(userPhotoUrl)) {
                params.put("id_card_photo", userPhotoUrl);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl1)) {
                params.put("car_photo1", mCarPhotosUrl1);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl2)) {
                params.put("car_photo2", mCarPhotosUrl2);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl3)) {
                params.put("car_photo3", mCarPhotosUrl3);
            }
            params.put("device_type", Constants.DEVICE_TYPE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, params);
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (isFormRegisterActivity) {
                                        startActivity(new Intent(mContext, MainActivity.class));
                                        // 第一次进入，获取 所有 新货源
                                        getNewSourceData(1);
                                    }
                                    onBackPressed();
                                    showMsg("完善资料成功.");
                                    application.writeIsRegOptional(true);
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
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

    // 获取所有新货源推送
    private void getNewSourceData(int page) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.QUERY_SOURCE_ORDER);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
                    .toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof List) {
                                        List<NewSourceInfo> mList = (List<NewSourceInfo>) result;

                                        if (mList != null) {
                                            NotificationManager mNotificationManager = (NotificationManager) mContext
                                                    .getSystemService(Context.NOTIFICATION_SERVICE);

                                            for (int i = 0; i < mList.size(); i++) {
                                                NewSourceInfo newSourceInfo = mList
                                                        .get(i);

                                                StringBuffer sb = new StringBuffer();
                                                sb.append("有新的货源,");
                                                sb.append(newSourceInfo.getCargo_desc());
                                                sb.append(",");
                                                sb.append(newSourceInfo.getCargo_number());
                                                Integer unitPrice = newSourceInfo.getCargo_unit();
                                                if(unitPrice != null && unitPrice>0) {
                                                    String[] unitPriceStr = mContext.getResources().getStringArray(R.array.car_price_unit);
                                                    for (int j = 0; j < Constants.unitTypeValues.length; j++) {
                                                        if (Constants.unitTypeValues[i] == unitPrice) {
                                                            sb.append(unitPriceStr[i]);
                                                        }
                                                    }
                                                }
                                                mNotificationManager.notify(i, getOrderNotification(
                                                        mContext,
                                                                sb.toString(),
                                                                newSourceInfo, i));
                                            }
                                        }
                                    }
                                    break;
                            }

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 跳入新货源详情
    private Notification getOrderNotification(Context context, String title,
                                              NewSourceInfo sourceInfo, int num) {
        Notification notification = new Notification(R.drawable.ic_launcher,
                title, System.currentTimeMillis());
        final Intent intent = new Intent(context, SourceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SourceDetailActivity.ORDER_INFO, sourceInfo);
        intent.putExtra("type", "MessageBroadCastReceiver");
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, num,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, "点击查看订单详情",
                pendingIntent);

        if (application.checkIsRingNewSource()) {
            notification.sound = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.mm);
        } else {
            notification.sound = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.silence);
        }

        // notification.sound = Uri.parse("android.resource://"
        // + mContext.getPackageName() + "/" + R.raw.mm);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    @Override
    public void onBackPressed() {
        if (isFormPushSourceDetail) {
            application.finishAllActivity();
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    public void uploadImageAndSubmit() {
        showSpecialProgress("正在上传资料,请稍后");
        if (!TextUtils.isEmpty(mCarPhotos1) || !TextUtils.isEmpty(mCarPhotos2)
                || !TextUtils.isEmpty(mCarPhotos3) || !TextUtils.isEmpty(userPhoto)) {
            try {
                if (!TextUtils.isEmpty(userPhoto)) {
                    //头像
                    new UploadImageFileTask(0).execute(userPhoto);
                }
                //车辆照片
                if (!TextUtils.isEmpty(mCarPhotos1)) {
                    new UploadImageFileTask(1).execute(mCarPhotos1);
                }
                if (!TextUtils.isEmpty(mCarPhotos2)) {
                    new UploadImageFileTask(2).execute(mCarPhotos2);
                }
                if (!TextUtils.isEmpty(mCarPhotos3)) {
                    new UploadImageFileTask(3).execute(mCarPhotos3);
                }
//                    System.out.println(mCarPhotos.get(i - 1).getPath());
//                    params.put("photo" + i, HttpUtils.getBitmapBase64(mContext, mCarPhotos.get(i - 1).getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            submit();
        }
    }


    private class UploadImageFileTask extends AsyncTask<String, Void, String> {

        private int type = -1;

        UploadImageFileTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            LogUtil.d(TAG, "开启线程上传图片");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String filePath = params[0];
            String serverResponse = null;
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost post = new HttpPost(Constants.UPLOAD_FILE_URL);
            File file = new File(filePath);
            if (file != null && file.exists()) {
                try {
                    ContentBody body = new FileBody(file, "image/jpeg");
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    entity.addPart("token", new StringBody(application.getToken()));
                    entity.addPart("file_type", new StringBody("1"));
                    entity.addPart("file", body);
                    post.setEntity(entity);
                    HttpResponse response = httpClient.execute(post);
                    serverResponse = EntityUtils.toString(response.getEntity());
                    LogUtil.e(TAG, "上传文件结果:" + serverResponse);
                    return serverResponse;
                } catch (Exception e) {
                    LogUtil.e(TAG, "上传文件出错");
                    e.printStackTrace();
                }
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final int status = jsonObject.optInt("status");
                    // 上传文件成功
                    if (status == 0) {
                        String item = jsonObject.optString("item");
                        String url = new JSONObject(item).getString("url");
                        LogUtil.d(TAG, "上传图片返回url地址：" + url);
                        if (type == 0) {
                            userPhotoUrl = url;
                        } else if (type == 1) {
                            mCarPhotosUrl1 = url;
                        } else if (type == 2) {
                            mCarPhotosUrl2 = url;
                        } else if (type == 3) {
                            mCarPhotosUrl3 = url;
                        }
                        if ((TextUtils.isEmpty(userPhoto) || (!TextUtils.isEmpty(userPhoto) && !TextUtils.isEmpty(userPhotoUrl)))
                                && (TextUtils.isEmpty(mCarPhotos1) || (!TextUtils.isEmpty(mCarPhotos1) && !TextUtils.isEmpty(mCarPhotosUrl1)))
                                && (TextUtils.isEmpty(mCarPhotos2) || (!TextUtils.isEmpty(mCarPhotos2) && !TextUtils.isEmpty(mCarPhotosUrl2)))
                                && (TextUtils.isEmpty(mCarPhotos3) || (!TextUtils.isEmpty(mCarPhotos3) && !TextUtils.isEmpty(mCarPhotosUrl3)))) {
                            // 上传所有图片完毕
                            submit();
                        }
                    } else {
                        LogUtil.d(TAG, "上传图片失败");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                LogUtil.d(TAG, "上传图片结果为空");
            }
        }
    }
}
