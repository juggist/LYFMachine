package com.leiyuntop.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件 工具类
 * @author lufeisong
 *
 */
public class FileUtil {
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	/**
	 * 保存信息到文件中
	 * 
	 * @param infoStr log信息
	 * 
	 */
	public static void saveCrashInfo2File(String infoStr) {
		try {
			String time = formatter.format(new Date());
			String fileName = "serial.txt";
//			if (Environment.getExternalStorageState().equals(
//					Environment.MEDIA_MOUNTED)) {
				String path = "/storage/emulated/0/coffice_machine/cach/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName,true);
				infoStr = time + " " + infoStr + "\n";
				fos.write(infoStr.getBytes());
				fos.close();
//			}
		} catch (Exception e) {
			// Log.e(TAG, "an error occured while writing file...", e);

		}
	}
}
