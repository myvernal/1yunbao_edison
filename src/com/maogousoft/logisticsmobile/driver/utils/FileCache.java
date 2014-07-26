package com.maogousoft.logisticsmobile.driver.utils;

import java.io.File;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class FileCache {

	private File cacheDir;

	private File fileCacheDir;

	private File audioCacheDir;

	private File imageCacheDir;

	private File chatImageCacheDir;

	// 缓存目录
	public static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/driver/";

	public File getCacheDir() {
		return cacheDir;
	}

	public File getFileCacheDir() {
		fileCacheDir = new File(cacheDir, "/file/");
		if (!fileCacheDir.exists())
			fileCacheDir.mkdirs();
		return fileCacheDir;
	}

	public File getAudioCacheDir() {
		audioCacheDir = new File(cacheDir, "/audio/");
		if (!audioCacheDir.exists())
			audioCacheDir.mkdirs();
		return audioCacheDir;
	}

	public File getImageCacheDir() {
		imageCacheDir = new File(cacheDir, "/image/");
		if (!imageCacheDir.exists())
			imageCacheDir.mkdirs();
		return imageCacheDir;
	}

	public File getImageCacheDirPath() {
		chatImageCacheDir = new File(cacheDir, "/image/chat/");
		if (!chatImageCacheDir.exists())
			chatImageCacheDir.mkdirs();
		return chatImageCacheDir;
	}

	/** 生成图片路径 **/
	public Uri makeImageUrl() {
		final File file = getImageCacheDir();
		final String fileName = System.currentTimeMillis() + ".jpg";
		return Uri.fromFile(new File(file, fileName));
	}

	/** 生成录音文件url **/
	public String makeAudioUrl() {
		final File file = getAudioCacheDir();
		final String fileName = file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".amr";
		return fileName;
	}

	public FileCache(Context context) {

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

			cacheDir = new File(DIR, "/cache");
		} else {

			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}
}
