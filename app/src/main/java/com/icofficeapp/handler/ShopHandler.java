package com.icofficeapp.handler;

import android.os.Handler;

public class ShopHandler extends Handler{
	public final static int DETAIL_2_FINISH = 30;//回到购买列表 取消task
	public final static int CHOOSEPAYTYPE_2_DETAIL = 31;//选择支付方式_2_购买列表
	public final static int PAYTYPE_2_DETAIL = 32;//支付_2_购买列表
	public final static int PAYAGAIN_2_DETAIL = 33;//再次购买_2_购买列表
	public final static int INIT_DETAIL = 34;//初始化购买界面
	
	public final static int DETAIL_2_FINISH_TIMER = 0;//选择支付方式_2_购买列表 倒计时
	public final static int CHOOSEPAYTYPE_2_DETAIL_TIMER = 20;//选择支付方式_2_购买列表 倒计时
	public final static int PAYTYPE_2_DETAIL_TIMER = 20;//支付_2_购买列表 倒计时
	public final static int PAYAGAIN_2_DETAIL_TIMER = 20;//再次购买_2_购买列表 倒计时
	public final static int ALIPAY_TIME = 60;//支付宝返回60秒
	public final static int WECHAT_TIME = 60;//微信返回60秒
//	public final static int CASH_TIME = 12;//现金返回20秒
	public final static int CARD_TIME = 60;//科菲卡返回20秒
	
	public static final int PAY_UNKNOW_FLAG = 0;// UnknownPay
	public static final int PAY_CASH_FLAG = 1;// cash
	public static final int PAY_UNION_FLAG = 2;// unionpay
	public static final int PAY_WECHAT_FLAG = 3;// wechat
	public static final int PAY_ALIPAY_FLAG = 4;// ali
	public static final int PAY_ICOFFICE_FLAG = 5;// card
	public static final int PAY_EXCHANGE_FLAG = 6;// exchangepay
	public static final int PAY_YEE_FLAG = 7;// yeepay
	public static final int PAY_OTHER = 8;// 非支付操作
	
	
}
