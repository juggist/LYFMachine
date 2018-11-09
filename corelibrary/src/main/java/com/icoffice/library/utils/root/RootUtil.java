package com.icoffice.library.utils.root;
/**
 * root权限下的 操作
 */
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

import com.icoffice.library.utils.CommonUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


public class RootUtil {
	public static String HIDE_STATUSBAR_CMD = "su -c service call activity 42 s16 com.android.systemui";
	private static String SLIENT_INSTALL_CMD = "pm install -r ";
	private static String SLIENT_UNINSTALL_CMD = "pm uninstall ";
	/*
	 * 静默安装
	 */
	public static void install(String apkLocation,String packageName,Context mContext) {
		String cmd = SLIENT_INSTALL_CMD + apkLocation + packageName;
				
		File file = new File(apkLocation,packageName);
		if(file.exists()){
			CommonUtils.showLog(CommonUtils.UPGRADE_TAG, "file exist：" + cmd);
		}else{
			CommonUtils.showLog(CommonUtils.UPGRADE_TAG, "file !exist：" + cmd);
		}
		CommonUtils.showLog(CommonUtils.UPGRADE_TAG, "静默安装命令：" + cmd);
		excuteSuCMD(cmd,mContext);
	}
	/*
	 * 静默卸载
	 */
	public static void uninstall(String apkLocation,String packageName,Context mContext) {
		String cmd = SLIENT_UNINSTALL_CMD + packageName;
		System.out.println("静默卸载命令：" + cmd);
		excuteSuCMD(cmd,mContext);
	}
	protected static int excuteSuCMD(String cmd,final Context mContext) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream dos = new DataOutputStream(
					(OutputStream) process.getOutputStream());
			// 部分手机Root之后Library path 丢失，导入path可解决该问题
			dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
			cmd = String.valueOf(cmd);
			dos.writeBytes((String) (cmd + "\n"));
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			process.waitFor();
			int result = process.exitValue();
			CommonUtils.showLog(CommonUtils.UPGRADE_TAG,"安装请求：" + result);
			return (Integer) result;
		} catch(Exception exception){
			exception.printStackTrace();
			return -1;
		} 
	}
	/*
	 * showStatusBar
	 */
	public static void showStatusBar() {
		try {
			Process proc = Runtime.getRuntime().exec(new String[] {"su", "-c", "am", "startservice", "-n","com.android.systemui/.SystemUIService" });
			proc.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * hideStatusBar
	 */
	 public static int hideStatusBar(final Context mContext,String cmd,final Handler mHandler) {
			try {
				Process process = Runtime.getRuntime().exec("su");
				DataOutputStream dos = new DataOutputStream(process.getOutputStream());
				// 部分手机Root之后Library path 丢失，导入path可解决该问题
				dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
				cmd = String.valueOf(cmd);
				dos.writeBytes((String) (cmd + "\n"));
				dos.flush();
				dos.writeBytes("exit\n");
				dos.flush();
				process.waitFor();
				int result = process.exitValue();
				
				return (Integer) result;
			} catch (Exception localException) {
				localException.printStackTrace();
				return -1;
			}
		}
}
