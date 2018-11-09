package com.icoffice.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class CommonUtils {
	public static final String TAG = "iCoffice Library";
	public static final String BROADCAST_TAG = "广播";//广播的tag
	public static final String PURCHASE_TAG = "购买";//购买流程tag
	public static final String ICE_TAG = "ice";//ice tag
	public static final String TEST_TAG = "test";//测试tag
	public static final String APP_TAG = "app生命周期";//app
	public static final String DATABASE_TAG = "数据库";//数据库
	public static final String EXCHANGE_TAG = "兑换";//兑换
	public static final String UPGRADE_TAG = "升级";//升级
	public static final String NETWORK_TAG = "网络";//网络
	public static final String TEMPLATE_TAG = "模板";//模板
	public static final String SCM_TAG = "单片机";//模板
	public static final String WAYANDGOODS_TAG="补货";
	public static final String WAY_TAG="货道信息";
	public static final String GOODS_TAG="商品信息";
	public static final String AD_TAG="广告";
	public static final String DOWNLOAD_TAG = "下载";
	public static final String EXCEPTION_TAG = "异常";
	public static final String LEIYUN_TAG = "雷云峰";
	public static final String obligate_TAG = "预留接口";
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 打印log
	 * @param logStr
	 */
	public static void showLog(String TAG,String logStr){
		Log.i(TAG,logStr);
		FileUtil.saveCrashInfo2File(TAG + "_" + logStr);
	}
	
//	public static void showLog(String logStr){
//		Log.i(TAG,logStr);
//	}
	/**
	 * 发送message
	 * @param mHandelr
	 * @param msgWhat
	 * @param result
	 */
	public static void sendMessage(Handler mHandelr,int msgWhat,String result){
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("result", result);
		msg.what = msgWhat;
		msg.setData(bundle);
		mHandelr.sendMessage(msg);
	}
	/**
	 * 发送message
	 * @param mHandelr
	 * @param msgWhat
	 * @param bundle
	 */
	public static void sendMessage(Handler mHandelr,int msgWhat,Bundle bundle){
		Message msg = new Message();
		msg.what = msgWhat;
		msg.setData(bundle);
		mHandelr.sendMessage(msg);
	}
	/**
	 * 保存文件时创建文件夹
	 */
	public static boolean sdCardFileExist(String file) {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return false;
		File saveFile = new File(file);
		if (!saveFile.exists())
			saveFile.mkdirs();
		return true;
	}
	/**
	 * 正则表达式 
	 * 判断是否为正整形数字
	 * @param str
	 * @return
	 */
	public static boolean bPositiveIntgerNum(String str){
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
	 * 正则表达式 
	 * 判断是否为整形数字
	 * @param str
	 * @return
	 */
	public static boolean bIntgerNum(String str){
		Pattern pattern = Pattern.compile("^-?[0-9]+");
		Matcher isNum = pattern.matcher(str.trim());
		if(isNum.matches()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 兑换价格
	 * “分”转“元”
	 * @param price
	 * @return
	 */
	public static String priceExchange(String price){
		Float exchangePrice = Float.parseFloat(price);
		price = exchangePrice / 100 + "";
		return price;
	}
	/**
	 * 获取类名方法
	 * 
	 * 
	 * @return
	 */
	public static String getClassName(Class<?> c) {
		String tableName = c.getName();
		return tableName.substring(tableName.lastIndexOf('.') + 1);
	}
	/**
	 * 获取当前时间 转化为String
	 */
	@SuppressLint("SimpleDateFormat")
	public static String currentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}
	/**
	 * 获取当前时间 转化为String
	 */
	public static String currentTime_ymd() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return sdf.format(date);
	}
	/**
	 * 获取当前时间 转化为String
	 */
	public static String currentTime_hms() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}
	public static double currentSecondsTime(String currentTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		double time = 0;
		try {
			Date data2 = sdf.parse(currentTime);
			time = data2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}
	/**
	 * 吐司方法
	 */
	public static void showToast(Context mContext,String str){
		Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
	}
	/**
	 * 隐藏软键盘
	 * @param mContext
	 * @param view
	 */
	public static void hideKeyBoard(Context mContext,View view){
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	/**
	 * 显示软键盘
	 * @param context
	 * @param view
	 */
	public static void showKeyBoard(Context context,View view){
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	/**
	 * 防止疯狂点击事件
	 */
	private static long lastClickTime;
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void mapSort(Map map) {
		List arrayList = new ArrayList(map.entrySet());

		Collections.sort(arrayList, new Comparator() {
			public int compare(Object arg1, Object arg2) {
				Map.Entry obj1 = (Map.Entry) arg1;
				Map.Entry obj2 = (Map.Entry) arg2;
				return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
			}
		});
	}
	
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
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str == null)
			return true;
		if(str.isEmpty())
			return true;
		else 
			return false;
	}
	/**
	 * 是否包含敏感字
	 * @param bigStr
	 * @param smallStr
	 * @return
	 */
	public  static boolean isStrInString(String bigStr, String smallStr) {
		if (bigStr.toUpperCase().indexOf(smallStr.toUpperCase()) > -1)
			return true;
		return false;
	}
	/**
	 * 把map或者javabean转换成Json字符串
	 * @param object	map对象或者javabean
	 * @return
	 */
	public static String objectToJson(Object object){
		JSONObject json = JSONObject.fromObject(object);  
		return json.toString();
	}
	/**
	 * 把list转换成Json字符串
	 * @param object	list对象
	 * @return
	 */
	public static String arrayToJson(Object object){
		JSONArray json = JSONArray.fromObject(object);
		return json.toString();
	}
	public static String getTradeTrace(int sourceDate,int formatLength){
		String[] aZ_array = new String[]{
				"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		
		String str = frontCompWithZore(sourceDate, formatLength);
		int no = Integer.parseInt(str.substring(0, str.length() - 5));
		str = aZ_array[no] + str.substring(str.length() - 5,str.length());
		return str;
	}
	/** 
	  * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回 
	  * @param sourceDate 
	  * @param formatLength 
	  * @return 重组后的数据 
	  */  
	 public static String frontCompWithZore(int sourceDate,int formatLength)  
	 {  
	  /* 
	   * 0 指前面补充零 
	   * formatLength 字符总长度为 formatLength 
	   * d 代表为正数。 
	   */  
	  String newString = String.format("%0"+formatLength+"d", sourceDate);  
	  return  newString;  
	 }  
	 /**
	  *  解析文件md5
	  * @param b
	  * @return
	  */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5sum(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

	/**
	 * 控制编辑框是否可以写入
	 * @param editText
	 * @param canEdit
	 */
	public static void setEditInput(EditText editText,boolean canEdit) {
		if(canEdit){
			editText.setFocusable(true);
			editText.setFilters(new InputFilter[] { new InputFilter() {
				public CharSequence filter(CharSequence source, int start, int end,
						Spanned dest, int dstart, int dend) {
					
					return null;
					
			
				}
			} });
		}else {
			editText.setFocusable(false);
			editText.setFilters(new InputFilter[] { new InputFilter() {
				public CharSequence filter(CharSequence source, int start, int end,
						Spanned dest, int dstart, int dend) {
					return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
				}
			} });
		}
	}
}
