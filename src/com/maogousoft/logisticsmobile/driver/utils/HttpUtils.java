package com.maogousoft.logisticsmobile.driver.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

import com.maogousoft.logisticsmobile.driver.AppException;

public class HttpUtils {

	// 超时时间，20S
	private static final int TIME_OUT = 20000;

	public static String getUrlData(final String url, JSONObject params) throws AppException {
		try {
			if (params != null) {
				StringBuilder builder = new StringBuilder();
				Iterator<?> it = params.keys();
				while (it.hasNext()) {
					final String key = it.next().toString();
					builder.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), "UTF-8")).append("&");
				}
				builder.deleteCharAt(builder.length() - 1);
				byte[] entitydata = builder.toString().getBytes();
				URL _url = new URL(url);
				LogUtil.e("url", builder.toString());
				HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
				connection.setRequestMethod("POST");
				connection.setConnectTimeout(TIME_OUT);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
				OutputStream outStream = connection.getOutputStream();
				outStream.write(entitydata);
				outStream.flush();
				outStream.close();
				if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					byte[] data = readInputStream(connection.getInputStream());
					connection.disconnect();
					return new String(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw AppException.http(e);
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/** 将图片转成base64 **/
	public static String getBitmapBase64(Bitmap bitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 40, bos);
		byte[] bytes = bos.toByteArray();
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(bytes, 0);
	}

	/** 将图片路径转base64 **/
	public static String getBitmapBase64(Context context,String url) {
		String _url=PickPhoto.scalePicture(context, url, 480, 800);
		File file = new File(_url);
		if (!file.exists()) {
			return null;
		}
		ByteArrayOutputStream bos = null;
		FileInputStream fis = null;
		byte[] data = null;
		try {
			bos = new ByteArrayOutputStream();
			fis = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = fis.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
			}
			data = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data == null ? null : Base64.encodeToString(data, 0);
	}
	
	/**
	 * GET数据
	 */

	public static String getURLData(String url) {
		HttpResponse httpResponse = null;
		String result = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity(), "GB2312");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
