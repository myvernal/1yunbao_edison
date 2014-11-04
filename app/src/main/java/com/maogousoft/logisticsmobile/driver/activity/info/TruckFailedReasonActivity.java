package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
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
 * Created by aliang on 2014/11/4.
 */
public class TruckFailedReasonActivity extends BaseActivity {

    private EditText moreReason;
    private View userShipper, userDriver, userThird;
    private ImageView photo1, photo2, photo3;
    private int responsibleCause = 1;
    private NewSourceInfo sourceInfo;
    // 保存车辆照片的list
    private String mCarPhotos1;
    private String mCarPhotos2;
    private String mCarPhotos3;
    // 保存车辆照片url的list
    private String mCarPhotosUrl1;
    private String mCarPhotosUrl2;
    private String mCarPhotosUrl3;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO1 = 1001;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO2 = 1002;
    private static final int RESULT_CAPTURE_IMAGE_CAR_PHOTO3 = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zc_faild_reason);
        initView();
        initData();
    }

    private void initView() {
        moreReason = (EditText) findViewById(R.id.more_reason);
        photo1 = (ImageView) findViewById(R.id.photo1);
        photo2 = (ImageView) findViewById(R.id.photo2);
        photo3 = (ImageView) findViewById(R.id.photo3);
        userShipper = findViewById(R.id.user_shipper);
        userDriver = findViewById(R.id.user_driver);
        userThird = findViewById(R.id.user_third);
        userShipper.setOnClickListener(this);
        userDriver.setOnClickListener(this);
        userThird.setOnClickListener(this);
    }

    private void initData() {
        sourceInfo = (NewSourceInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_shipper:
                responsibleCause = 1;
                userShipper.setSelected(true);
                userThird.setSelected(false);
                userDriver.setSelected(false);
                break;
            case R.id.user_third:
                responsibleCause = 2;
                userShipper.setSelected(false);
                userThird.setSelected(true);
                userDriver.setSelected(false);
                break;
            case R.id.user_driver:
                responsibleCause = 3;
                userShipper.setSelected(false);
                userThird.setSelected(false);
                userDriver.setSelected(true);
                break;

        }
    }

    public void onPicture(View view) {

    }

    public void onPhotoCam(View view) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(mCarPhotos1 == null) {
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "responsible1.jpg")));
            startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE_CAR_PHOTO1);
        } else if(mCarPhotos2 == null) {
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "responsible2.jpg")));
            startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE_CAR_PHOTO2);
        } else if(mCarPhotos3 == null) {
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "responsible3.jpg")));
            startActivityForResult(imageCaptureIntent, RESULT_CAPTURE_IMAGE_CAR_PHOTO3);
        } else {
            showMsg("最多支持3张照片");
        }
    }

    public void onSubmit(View view) {
        uploadImageAndSubmit();
    }

    /**
     * 执行对应的操作
     */
    private void submitAllData() {
        try {
            showProgress("正在处理");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.TRUCK_LOADING_FAIL);
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject params = new JSONObject();
            params.put("order_id", sourceInfo.getId());
            params.put("responsible_people", responsibleCause);
            params.put("evidence_material", moreReason.getText());
            StringBuilder stringBuilder = new StringBuilder();
            if(!TextUtils.isEmpty(mCarPhotosUrl1)) {
                stringBuilder.append(mCarPhotosUrl1);
            }
            if(!TextUtils.isEmpty(mCarPhotosUrl2)){
                stringBuilder.append("@").append(mCarPhotosUrl2);
            }
            if(!TextUtils.isEmpty(mCarPhotosUrl3)) {
                stringBuilder.append("@").append(mCarPhotosUrl3);
            }
            params.put("evicdence_pic", stringBuilder.toString());//图片链接
            jsonObject.put(Constants.JSON, params.toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    showMsg("操作成功");
                                    finish();
                                    break;
                                default:
                                    showMsg(result.toString());
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void uploadImageAndSubmit() {
        showSpecialProgress("正在上传资料,请稍后");
        if (!TextUtils.isEmpty(mCarPhotos1) || !TextUtils.isEmpty(mCarPhotos2) || !TextUtils.isEmpty(mCarPhotos3)) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            submitAllData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO1:
                    String CAR_PHOTO1 = Environment.getExternalStorageDirectory() + File.separator + "responsible1.jpg";
                    displayPhoto(CAR_PHOTO1, photo1);
                    mCarPhotos1 = CAR_PHOTO1;
                    break;
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO2:
                    String CAR_PHOTO2 = Environment.getExternalStorageDirectory() + File.separator + "responsible2.jpg";
                    displayPhoto(CAR_PHOTO2, photo2);
                    mCarPhotos2 = CAR_PHOTO2;
                    break;
                case RESULT_CAPTURE_IMAGE_CAR_PHOTO3:
                    String CAR_PHOTO3 = Environment.getExternalStorageDirectory() + File.separator + "responsible3.jpg";
                    displayPhoto(CAR_PHOTO3, photo3);
                    mCarPhotos3 = CAR_PHOTO3;
                    break;
                default:
                    break;
            }
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
                        if (type == 1) {
                            mCarPhotosUrl1 = url;
                        } else if (type == 2) {
                            mCarPhotosUrl2 = url;
                        } else if (type == 3) {
                            mCarPhotosUrl3 = url;
                        }
                        if ((TextUtils.isEmpty(mCarPhotos1) || (!TextUtils.isEmpty(mCarPhotos1) && !TextUtils.isEmpty(mCarPhotosUrl1)))
                                && (TextUtils.isEmpty(mCarPhotos2) || (!TextUtils.isEmpty(mCarPhotos2) && !TextUtils.isEmpty(mCarPhotosUrl2)))
                                && (TextUtils.isEmpty(mCarPhotos3) || (!TextUtils.isEmpty(mCarPhotos3) && !TextUtils.isEmpty(mCarPhotosUrl3)))) {
                            // 上传所有图片完毕
                            submitAllData();
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

    /**
     * 调整图片并显示
     *
     * @param url
     * @param imgView
     */
    private void displayPhoto(final String url, final ImageView imgView) {
        imageLoader.displayImage(url, imgView);
        imgView.setVisibility(View.VISIBLE);
    }
}
