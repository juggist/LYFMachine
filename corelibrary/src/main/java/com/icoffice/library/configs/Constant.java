package com.icoffice.library.configs;

import android.os.Environment;

public class Constant {
	public final static String BASE_ROOT_FILE = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String APP_FILE = BASE_ROOT_FILE + "/iCoffice";
	public final static String IMAGE_FILE = APP_FILE + "/images";
	public final static String APK_DOWNLOAD_FILE = APP_FILE + "/apk_download";
	public final static String MEDIA_FILE = APP_FILE + "/media";
	public final static String URLS = "http://machine.coffice.so/files";
	
	public final static CofficeServer.MachineType mt_id = CofficeServer.MachineType.Machine; // 机器类型
	public final static String APP_TYPE = "20"; //2.0APP标识，用于升级APP
	public final static String WXORDER_PREFIX = "t"; //微信订单号测试开头，便于区分正式跟测试
	

	// 服务器地址
	public final static String SVR_HOST = "dev-machine.coffice.so";
	// 微商城地址
	public final static String SVN_EXCHANGE_HOST = "dev-m.coffice.so";

	// case 1:黄钧磊接口
	public final static String BASE_URL = "http://"+SVR_HOST+"/index.php/api/";// 测试接口url
	// case 2:微商城接口
	public final static String WECHAT_BASE_URL = "http://"+SVN_EXCHANGE_HOST+"/api/";// 测试兑换码访问接口
	// case 3:活动接口
	public final static String ACTIVITY_BASE_URL = "http://"+SVR_HOST+"/index.php/music_valley_api/";// 测试兑换码访问接口

	public final static String DOWNLOAD_IMG_URL = "http://" + SVR_HOST + "/files";//
	// 银联服务器地址（POS）
	public final static String UNIONPAY_SVR_HOST = "110.83.63.198";
	public final static boolean VERSION_CONTROL = false;//true-正式版本  false－测试版本
	
}
