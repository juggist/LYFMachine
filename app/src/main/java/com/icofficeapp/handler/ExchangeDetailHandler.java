package com.icofficeapp.handler;

import android.os.Handler;
/**
 * 微商城 
 * 兑换详情
 * @author lufeisong
 *
 */
public class ExchangeDetailHandler extends Handler{
	public static final int EXCHANGEDETAIL_2_EXCHANGE = 1;//兑换_2_购买
	public static final int EXCHANGEDETAIL_REFRESH = 2;//刷新兑换详情页面
	
	public static final int EXCHANGEDETAIL_2_SHOP_TIME = 60;//兑换详情_2_购买 时间
	public static final int RESEET_EXCHANGEDETAIL_TIME = 0;//重置 兑换详情时间

}
