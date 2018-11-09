package com.icoffice.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;

public class MachineUtil {
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
		return Mac.trim();
	}
	public static void SystemReboot() {
		new Thread(){

			@Override
			public void run() {
				super.run();
				try {
					FileUtil.saveCrashInfo2File(System.currentTimeMillis() + "");
					Runtime.getRuntime().exec("su -c /system/bin/reboot");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}
}
