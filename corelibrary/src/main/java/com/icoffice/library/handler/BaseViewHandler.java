package com.icoffice.library.handler;


public class BaseViewHandler extends BaseHttpHandler {
	public final static int AcceptMoney = 			5;
	public final static int AdvanceMoney = 			6;
	public final static int OutGoods = 				7;
	public final static int OutGoodsSuccess = 		8;
	public final static int OutGoodsFail = 			-8;
	public final static int OutGoodsTimeOut = 		9;
	
	// 用于升级APK的事件
	public final static int UpgradeSuccess = 		10;
	public final static int UpgradeFail = 			-10;
	public final static int UpgradeNothingToDo = 	11;
	public final static int UpgradeDownloading = 	12;
	public final static int UpgradeInstalling = 	13;
	
	// 用于设置货道的事件
	public final static int SetRoadSuccess = 		14;
	public final static int SetRoadFail = 			-14;
	
	//后台
	public final static int CONNECT_FAIL = 30;//后台网络连接失败
	public final static int WAY_SUCCESS = 31;//
	
	
	public final static String Title = "正在提交数据中...";
	public final static String Download_Title = "版本跟新";
	public final static String Download_version = "正在检测版本中...";
	public final static String Download_Info = "正在下载中...";
	public final static String Install_Title = "正在安装中...";
	public final static String Update_Success = "更新成功...";
	public final static String Update_Fail = "更新失败...";
	public final static String Update_Nothig = "不需要更新...";
	
	//前台
	public final static int CONNECT_NET_FAIL = 40;//链接网路失败
	public final static int GOODS_SOLD_SELL_OUT = 41;//商品售罄
	public final static int AVM_STATUS_FAIL = 42;//机器故障
	public final static int IS_LOCKED = 43;//被锁住
	public final static int GOODS_RCODE_OVER = 44;//商品领取完
	public final static int WAIT = 45;//等待开放
	public final static int CONNECT_NET_SUCCESS = 46;//链接网络成功
	
}

