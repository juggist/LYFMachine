package com.icoffice.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {
	/**
	 * edittext 不弹出软键盘
	 */
	public static void disableShowSoftInput(EditText et) {
		et.setFocusable(true);
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			et.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method method;
			try {
				method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				method.setAccessible(true);
				method.invoke(et, false);
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				method = cls.getMethod("setSoftInputShownOnFocus",
						boolean.class);
				method.setAccessible(true);
				method.invoke(et, false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 获取当前时间 转化为String
	 */
	public static String currentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}
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
	 * 2个float相减 丢失精度
	 */
	public static float getFloat(float f1,float f2){
		BigDecimal b1 = new BigDecimal(Float.toString(f1));
		BigDecimal b2 = new BigDecimal(Float.toString(f2));
		return b1.subtract(b2).floatValue();
	}
	/**
	 * 防止疯狂点击事件
	 */
	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 检测 是否链接网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 补足0
	 * 
	 * @param
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左(前)补0
			// sb.append(str).append("0");//右(后)补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}

	/**
	 * byte数组转换成16进制字符串
	 * 
	 * @param
	 * @return
	 */
	public static String bytes2HexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * byte数组转换成16进制字符数组
	 * 
	 * @param
	 * @return
	 */
	public static String[] bytes2HexStrings(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		String[] str = new String[src.length];
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				str[i] = "0";
			}
			str[i] = hv;
		}
		return str;
	}
	
	/**
	 * int值转成2字节的byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2byteArray(int num) {
		byte[] result = new byte[2];
		result[0] = (byte)(num >>> 8); //取次低8位放到0下标 
		result[1] = (byte)(num );      //取最低8位放到1下标
		return result;
	}
	
	/**
	 * 将2字节的byte数组转成int值
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArray2int(byte[] b) {
		byte[] a = new byte[2];
		int i = a.length - 1, j = b.length - 1;
		for (; i >= 0; i--, j--) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= 0)
				a[i] = b[j];
			else
				a[i] = 0;// 如果b.length不足2,则将高位补0
		}
		int v0 = (a[0] & 0xff) << 8;
		int v1 = (a[1] & 0xff);
		return v0 + v1;
	}
	
	/**
	 * 正则表达式 
	 * 
	 * 判断是否为整形数字
	 * @param str
	 * @return
	 */
	public static boolean bIntgerNum(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str.trim());
		if(isNum.matches()){
			if (str != null && !"".equals(str.trim()))
				return str.matches("^[0-9]*$");
			else
				return false;  
		}else{
			return false;
		}
	}

	/**
	 *程序崩溃 自启动apk
	 */

//	public static void restartApp(Context mContext) {
//		 Intent intent = new Intent(mContext.getApplicationContext(),MainActivity.class);  
//         PendingIntent restartIntent = PendingIntent.getActivity(    
//        		 mContext.getApplicationContext(), 0, intent,    
//                 Intent.FLAG_ACTIVITY_NEW_TASK);
//         //退出程序                                          
//         AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);    
//         mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10,    
//                 restartIntent); // 1秒钟后重启应用
//        ((BaseApplication) mContext.getApplicationContext()).exit();
//	}
//	
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	 }
	
	/**
	 * ping包
	 */
	private static void pingPackage() {
		new Thread(){

			@Override
			public void run() {
				super.run();
		        Log.i("iCoffice", "lala");
		        List<String> strs = CommandUtil.execute("ping -c 4 www.baidu.com");
		        Log.i("iCoffice", "" + strs.size());
		        for(String str : strs) {
		        	if(str.contains("ping: unknown host ")) {
		        		Log.i("iCoffice", "网络不可达.");
		        	} else if (str.contains("PING")) {
		        		Log.i("iCoffice", str);
		        	} else if(str.contains("64 bytes from")) {
		        		int timeIndex = str.indexOf("time");
		        		Log.i("iCoffice", str.substring(timeIndex + 5));
		        	} else {
		        		Log.i("iCoffice", str);
		        	}
		        }
			}
			
		}.start();
	}

	
}
