package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
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

/**
 * 完善货主资料
 *
 * @author ybxiang
 */
public class OptionalShipperActivity extends BaseActivity {
    private Button mLogin, mRegister;
    private EditText mName, mPhone, mTelNum, mAddress, mCompanyName, mSelfDesc;
    private HuoZhuUserInfo abcInfo;
    // 公司LOGO
    private String userPhoto;
    private String userPhotoUrl;
    // 保存公司照片的list
    private String mCarPhotos1;
    private String mCarPhotos2;
    private String mCarPhotos3;
    // 保存公司照片url的list
    private String mCarPhotosUrl1;
    private String mCarPhotosUrl2;
    private String mCarPhotosUrl3;
    private DisplayImageOptions options;
    private ImageView id_card_photo, car_photo1, car_photo2, car_photo3;
    private static final int RESULT_CAPTURE_IMAGE_COMPANY_PHOTO = 1005;
    private static final int RESULT_CAPTURE_IMAGE_COMPANY_PHOTO1 = 1006;
    private static final int RESULT_CAPTURE_IMAGE_COMPANY_PHOTO2 = 1007;
    private static final int RESULT_CAPTURE_IMAGE_COMPANY_PHOTO3 = 1008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_optional_shipper);
        setIsShowAnonymousActivity(false);
        initViews();
        initUtils();
        initData();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content))
                .setText(R.string.string_register_complete_title);

        mLogin = (Button) findViewById(R.id.titlebar_id_more);
        mRegister = (Button) findViewById(R.id.info_id_register_submit);

        mTelNum = (EditText) findViewById(R.id.info_id_register_telNum);
        mAddress = (EditText) findViewById(R.id.info_id_register_company_address);
        mCompanyName = (EditText) findViewById(R.id.info_id_register_company_name);
        mSelfDesc = (EditText) findViewById(R.id.myself_recommendation);
        mPhone = (EditText) findViewById(R.id.info_id_register_phone);
        mName = (EditText) findViewById(R.id.info_id_register_name);
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

        mPhone.setOnFocusChangeListener(mOnFocusChangeListener);
        mName.setOnFocusChangeListener(mOnFocusChangeListener);

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

    private void initData() {
        if (getIntent().hasExtra("info")) {
            abcInfo = (HuoZhuUserInfo) getIntent().getSerializableExtra("info");
            mName.setText(abcInfo.getName());
            mPhone.setText(abcInfo.getPhone());
            mTelNum.setText(abcInfo.getTelcom());
            mAddress.setText(abcInfo.getAddress());
            mCompanyName.setText(abcInfo.getCompany_name());
            mSelfDesc.setText(abcInfo.getCompany_recommendation());
            if (!TextUtils.isEmpty(abcInfo.getCompany_logo())) {
                ImageLoader.getInstance().displayImage(abcInfo.getCompany_logo(), id_card_photo, options,
                        new Utils.MyImageLoadingListener(mContext, id_card_photo));
                userPhotoUrl = abcInfo.getCompany_logo();
            }
            if (!TextUtils.isEmpty(abcInfo.getCompany_photo1())) {
                ImageLoader.getInstance().displayImage(abcInfo.getCompany_photo1(), car_photo1, options,
                        new Utils.MyImageLoadingListener(mContext, car_photo1));
                mCarPhotosUrl1 = abcInfo.getCompany_photo1();
            }
            if (!TextUtils.isEmpty(abcInfo.getCompany_photo2())) {
                ImageLoader.getInstance().displayImage(abcInfo.getCompany_photo2(), car_photo2, options,
                        new Utils.MyImageLoadingListener(mContext, car_photo2));
                mCarPhotosUrl2 = abcInfo.getCompany_photo2();
            }
            if (!TextUtils.isEmpty(abcInfo.getCompany_photo3())) {
                ImageLoader.getInstance().displayImage(abcInfo.getCompany_photo3(), car_photo3, options,
                        new Utils.MyImageLoadingListener(mContext, car_photo3));
                mCarPhotosUrl3 = abcInfo.getCompany_photo3();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        final int id = v.getId();
        switch (id) {
            case R.id.info_id_register_submit:
                if (!CheckUtils.checkIsEmpty(mName)) {
                    showMsg("联系人不能为空");
                    mName.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mCompanyName)) {
                    showMsg("公司名称不能为空");
                    mCompanyName.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mAddress)) {
                    showMsg("公司地址不能为空");
                    mAddress.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mTelNum)) {
                    showMsg("座机号码不能为空");
                    mTelNum.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mPhone)) {
                    showMsg("手机号码不能为空");
                    mPhone.requestFocus();
                    clearPhoto();
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mSelfDesc)) {
                    showMsg("自我推荐不能为空");
                    mSelfDesc.requestFocus();
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
                imageCaptureIntent0.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO.jpg")));//照片存储目录
                startActivityForResult(imageCaptureIntent0, RESULT_CAPTURE_IMAGE_COMPANY_PHOTO);
                break;
            case R.id.car_photo1:
                Intent imageCaptureIntent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO1.jpg")));
                startActivityForResult(imageCaptureIntent1, RESULT_CAPTURE_IMAGE_COMPANY_PHOTO1);
                break;
            case R.id.car_photo2:
                Intent imageCaptureIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO2.jpg")));
                startActivityForResult(imageCaptureIntent2, RESULT_CAPTURE_IMAGE_COMPANY_PHOTO2);
                break;
            case R.id.car_photo3:
                Intent imageCaptureIntent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageCaptureIntent3.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO3.jpg")));
                startActivityForResult(imageCaptureIntent3, RESULT_CAPTURE_IMAGE_COMPANY_PHOTO3);
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
                case RESULT_CAPTURE_IMAGE_COMPANY_PHOTO://拍照
                    String ID_PHOTO = Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO.jpg";
                    displayPhoto(ID_PHOTO, id_card_photo);
                    userPhoto = ID_PHOTO;
                    break;
                case RESULT_CAPTURE_IMAGE_COMPANY_PHOTO1:
                    String CAR_PHOTO1 = Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO1.jpg";
                    displayPhoto(CAR_PHOTO1, car_photo1);
                    mCarPhotos1 = CAR_PHOTO1;
                    break;
                case RESULT_CAPTURE_IMAGE_COMPANY_PHOTO2:
                    String CAR_PHOTO2 = Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO2.jpg";
                    displayPhoto(CAR_PHOTO2, car_photo2);
                    mCarPhotos2 = CAR_PHOTO2;
                    break;
                case RESULT_CAPTURE_IMAGE_COMPANY_PHOTO3:
                    String CAR_PHOTO3 = Environment.getExternalStorageDirectory() + File.separator + "COMPANY_PHOTO3.jpg";
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
        File file = new File(url);
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
        }
    }

    // 输入框失去焦点事件监听
    private final OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == mName && !hasFocus) {

            } else if (v == mPhone && !hasFocus) {

            }

        }
    };

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
            jsonObject.put(Constants.ACTION, Constants.SHIPPER_REG_OPTIONAL2);
            jsonObject.put(Constants.TOKEN, null);
            params.put("phone", mPhone.getText().toString());
            params.put("telcom", mTelNum.getText().toString());
            params.put("contact", mName.getText().toString());
            params.put("company_name", mCompanyName.getText().toString());
            params.put("address", mAddress.getText().toString());
            params.put("company_recommendation", mSelfDesc.getText().toString());
            if (!TextUtils.isEmpty(userPhotoUrl)) {
                params.put("company_logo", userPhotoUrl);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl1)) {
                params.put("company_photo1", mCarPhotosUrl1);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl2)) {
                params.put("company_photo2", mCarPhotosUrl2);
            }
            if (!TextUtils.isEmpty(mCarPhotosUrl3)) {
                params.put("company_photo3", mCarPhotosUrl3);
            }
            params.put("device_type", Constants.DEVICE_TYPE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, params);
            showSpecialProgress();
            ApiClient.doWithObject(Constants.SHIPPER_SERVER_URL, jsonObject,
                    UserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    onBackPressed();
                                    showMsg("完善资料成功.");
                                    application.writeIsRegOptional(true);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void uploadImageAndSubmit() {
        showSpecialProgress("正在上传资料,请稍后");
        if (!TextUtils.isEmpty(mCarPhotos1) || !TextUtils.isEmpty(mCarPhotos2)
                || !TextUtils.isEmpty(mCarPhotos3) || !TextUtils.isEmpty(userPhoto)) {
            try {
                if (!TextUtils.isEmpty(userPhoto)) {
                    //公司LOGO
                    new UploadImageFileTask(0).execute(userPhoto);
                }
                //公司照片
                if (!TextUtils.isEmpty(mCarPhotos1)) {
                    new UploadImageFileTask(1).execute(mCarPhotos1);
                }
                if (!TextUtils.isEmpty(mCarPhotos2)) {
                    new UploadImageFileTask(2).execute(mCarPhotos2);
                }
                if (!TextUtils.isEmpty(mCarPhotos3)) {
                    new UploadImageFileTask(3).execute(mCarPhotos3);
                }
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
