package com.icoffice.library.utils;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

/**
 * apk 工具类
 * @author lufeisong
 *
 */
public class ApkUtil {
	/**
	 * 获取mac地址
	 */
	@SuppressWarnings("resource")
	public static String getLocalMacAdress(Context mContext) {
		String Mac = null;
		try {
			String path = "sys/class/net/wlan0/address";
			if ((new File(path)).exists()) {
				FileInputStream fis = new FileInputStream(path);
				byte[] buffer = new byte[8192];
				int byteCount = fis.read(buffer);
				if (byteCount > 0) {
					Mac = new String(buffer, 0, byteCount, "utf-8");
				}
			}
			if (Mac == null || Mac.length() == 0) {
				path = "sys/class/net/eth0/address";
				FileInputStream fis_name = new FileInputStream(path);
				byte[] buffer_name = new byte[8192];
				int byteCount_name = fis_name.read(buffer_name);
				if (byteCount_name > 0) {
					Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
				}
			}
			if (Mac.length() == 0 || Mac == null) {
				return "";
			}
		} catch (Exception io) {
		}
		Toast.makeText(mContext, Mac.trim(), Toast.LENGTH_LONG).show();
		return Mac.trim();
	}
	/**
	 * 获取本地apk版本号
	 * @param mContext
	 * @return
	 */
	public  static int getVerCode(Context mContext){
		int verCode = -1;
		try {
			verCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}
	/**
	 * 获取本地apk的名称
	 * @param mContext
	 * @return
	 */
	public static String getVerName(Context mContext){
		String verName = "";
		try {
			verName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}
	/**
	 * 获取本地apk版本号 v 1.0.0
	 * @param mContext
	 * @return
	 */
	public static String getVerNumber(Context mContext){
		final String SEPARATOR = "_";
		String verName = getVerName(mContext);
		int index = verName.indexOf(SEPARATOR);
		String verNumber = verName.substring(index + 1, verName.length());
		return "版本  v" + verNumber;
	}
}
