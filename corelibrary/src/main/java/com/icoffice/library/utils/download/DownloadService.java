package com.icoffice.library.utils.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

import com.icoffice.library.configs.Constant;
import com.icoffice.library.utils.CommonUtils;

public class DownloadService {
	// private static String CommonUtils.DOWNLOAD_TAG = "DownloadService" ;
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	private static final String CACHE_FILENAME_PREFIX = "cache_";
	private static ExecutorService SINGLE_TASK_EXECUTOR = null;
	private static ExecutorService LIMITED_TASK_EXECUTOR = null;
	private static final ExecutorService FULL_TASK_EXECUTOR = null;
	private static final ExecutorService DEFAULT_TASK_EXECUTOR;
	private static Object lock = new Object();
	static {
		LIMITED_TASK_EXECUTOR = (ExecutorService) Executors
				.newFixedThreadPool(1);
		DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
	};
	// 下载状态监听，提供回调
	DownloadStateListener listener;
	// 下载目录
	private static String downloadPath;

	// 下载链接集合
	private List<String> listURL;
	// 下载个数
	private int size = 0;
	private int failSize = 0;
	private static String separatorPath;//分目录

	// 下载完成回调接口
	public interface DownloadStateListener {
		public void onSuccess();

		public void onFinish();

		// code 0拒绝执行异常,1下载图片失败异常
		public void onFailed(int code, Exception e, String errorMsg);
	}

	public DownloadService(String downloadPath,String _separatorPath ,List<String> listURL,
			DownloadStateListener listener) {
		this.downloadPath = downloadPath;
		this.listURL = listURL;
		this.listener = listener;
		separatorPath = _separatorPath;
	}

	public void setDefaultExecutor() {

	}

	/**
	 * 开始下载
	 */
	public void startDownload() {
		// 首先检测path是否存在
		File downloadDirectory = new File(downloadPath + separatorPath);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}
		for (final String url : listURL) {
			CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "下载的路径为 " + url);
			// 捕获线程池拒绝执行异常
			try {
				// 线程放入线程池
				DEFAULT_TASK_EXECUTOR.execute(new Runnable() {

					@Override
					public void run() {
						downloadBitmap(Constant.DOWNLOAD_IMG_URL + url);
					}
				});
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				getImgNum();
				listener.onFailed(0, e, null);
				CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG,
						"thread pool rejected error");

			} catch (Exception e) {
				e.printStackTrace();
				getImgNum();
				listener.onFailed(0, e, null);
			}

		}

	}

	/**
	 * 下载图片
	 * 
	 * @param urlString
	 * @return
	 */
	int i = 0;

	private File downloadBitmap(String urlString) {
		String fileName = urlString;
		// 图片命名方式
		final File file = new File(createFilePath(new File(downloadPath + separatorPath),
				fileName));
		final File cachFile = new File(file.toString() + "_tmp");
		if (file.exists()) {
			getImgNum();
			CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "文件已存在");
		} else {
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;
			try {
				final URL url = new URL(createUrl(urlString));
				urlConnection = (HttpURLConnection) url.openConnection();
				final InputStream in = new BufferedInputStream(
						urlConnection.getInputStream(), IO_BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(cachFile),
						IO_BUFFER_SIZE);

				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				
				// 每下载成功一个，统计一下图片个数
				CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "download " + urlString + " success");
				cachFile.renameTo(file);
				statDownloadNum();
				getImgNum();
				return file;

			} catch (final IOException e) {
				// 有一个下载失败，则表示批量下载没有成功
				CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "download "
						+ urlString + " error " + e.toString());
				getImgNum();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (out != null) {
					try {
						out.close();
					} catch (final IOException e) {
						CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG,
								"download " + urlString + " error");
					}
				}
			}
		}
		return null;
	}

	/**
	 * Creates a constant cache file path given a target cache directory and an
	 * image key.
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {
//		try {
     	   key = key.replace(Constant.DOWNLOAD_IMG_URL + separatorPath, "");
			return cacheDir.getAbsolutePath() + File.separator
					+ key;
//		} catch (final UnsupportedEncodingException e) {
//			CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG,
//					"createFilePath - error " + e.toString());
//		}
//		return null;
	}
	public static String createUrl(String url){
		int index = url.lastIndexOf("/");
		try {
			return url.substring(0, index + 1) + URLEncoder.encode(url.substring(index + 1, url.length()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 统计下载个数
	 */
	private void statDownloadNum() {
		synchronized (lock) {
			size++;
			if (size == listURL.size()) {
				CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG,
						"download finished total" + size);
				// 释放资源
				// DEFAULT_TASK_EXECUTOR.shutdownNow();
				// 如果下载成功的个数与列表中 url个数一致，说明下载成功
				listener.onSuccess(); // 下载成功回调
			}
		}
	}

	/**
	 * 统计下载成功和下载失败的数量
	 */
	private void getImgNum() {
		synchronized (lock) {
			failSize++;
			if (failSize == listURL.size()) {
				// 释放资源
				// 下载失败 与 成功相同 说明结束下载成功了
				CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "图片下载结束");
				listener.onFinish(); // 下载结束回调
			}
		}

	}
}
