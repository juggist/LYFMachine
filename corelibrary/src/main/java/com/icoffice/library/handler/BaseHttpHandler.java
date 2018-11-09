package com.icoffice.library.handler;

import android.os.Bundle;
import android.os.Handler;

import com.icoffice.library.moudle.control.AliPayControl;
import com.icoffice.library.moudle.control.BasePayControl;
import com.icoffice.library.moudle.control.WechatPayControl;

import java.util.Map;

public class BaseHttpHandler extends Handler {
	protected String result;
	protected Map<String, String> resultMap;
	protected Bundle mBundle = null;
	protected String client_order_no;
	protected WechatPayControl mWechatPayControl;
	protected AliPayControl mAliPayControl;
	public static BasePayControl mBasePayControl = null;//当前的交易对象
	
	public final static int CONNECT_NET_FAIL = 0000;//网络连接失败
	

	public final static int ICE_CONNECTED_SUCCESS = 5;
	public final static int ICE_CONNECTED_FAIL = -5;
	public final static int ICE_GET_GOODSBEAN_SUCCESS = 6;
	public final static int ICE_GET_GOODSBEAN_FAIL = -6;
	public final static int ICE_ROLL_WX_SUCCESS = 7;
	public final static int ICE_ROLL_WX_FAIL = -7;
	public final static int ICE_ROLL_ALI_SONICE_SUCCESS = 8;
	public final static int ICE_ROLL_ALI_SONICE_FAIL = -8;
	public final static int ICE_ROLL_ALI_SUCCESS = 9;
	public final static int ICE_ROLL_ALI_FAIL = -9;
	public final static int ICE_ROLL_ETOUCH_CASH_SUCCESS = 10;
	public final static int ICE_ROLL_ETOUCH_CASH_FAIL = -10;
	public final static int ICE_NET_ORDER_SUCCESS = 11;
	public final static int ICE_NET_ORDER_FAIL = -11;
	public final static int ICE_LOCAL_ORDER_SUCCESS = 12;
	public final static int ICE_LOCAL_ORDER_FAIL = -12;
	public final static int ICE_CREAT_ORDER_COFFICESHOP_SUCCESS = 13;
	public final static int ICE_CREAT_ORDER_COFFICESHOP_FAIL = -13;
	public final static int ICE_QUERYMACHINEAD_SUCCESS = 14;//查询广告接口成功
	public final static int ICE_QUERYMACHINEAD_FAIL = -14;//查询广告接口失败
	
	public final static int RefreshShopTimer =      15;//刷新购买倒计时
	
	public final static int CONNECT_NET_FAIL_BACKSTAGE = 20;//后台网络连接失败
	public final static int POST_REGISTER_SUCCESS = 21;//机器注册成功
	public final static int POST_REGISTER_FAIL = -21;//机器注册成功
	public final static int POST_GOODSSTATE_SUCCESS = 22;//修改商品状态成功
	public final static int POST_GOODSSTATE_FAIL = -22;//修改商品状态成功
	public final static int POST_WAYANDGOODSSTATE_SUCCESS = 23;//补货成功
	public final static int POST_WAYANDGOODSSTATE_FAIL = -23;//补货成功
	public final static int POST_WAY_STATE_SUCCESS = 24;//修改货道状态
	public final static int POST_WAY_STATE_FAIL = -24;//修改货道状态
	public final static int POST_USERENTER_SUCCESS = 25;//用户登入成功
	public final static int POST_USERENTER_FAIL = -25;//用户登入失败
	public final static int POST_CHECK_RCODE_SUCCESS = 26;//检测微商城兑换码成功
	public final static int POST_CHECK_RCODE_FAIL = -26;//检测微商城兑换码失败
	public final static int POST_ADD_CHANGE_LOG_SUCCESS = 27;//检测微商城,增加兑换记录成功
	public final static int POST_ADD_CHANGE_LOG_FAIL = -27;//检测微商城,增加兑换记录失败
	public final static int POST_QUIT_CONVERT_SUCCESS = 28;//检测微商城,退出兑换（解除锁定）成功
	public final static int POST_QUIT_CONVERT_FAIL = -28;//检测微商城,退出兑换（解除锁定）失败
	public final static int POST_CHECK_M_NO_SUCCESS = 29;//获取机器编码成功
	public final static int POST_CHECK_M_NO_FAIL = -29;//获取机器编码失败
	public final static int POST_TEST_CHECK_M_NO_SUCCESS = 30;//测试__检测微商城兑换码成功
	public final static int POST_TEST_CHECK_M_NO_FAIL = -30;//测试__检测微商城兑换码失败
	public final static int POST_TEST_CHECK_RCODE_SUCCESS = 31;//测试_检测微商城兑换码成功
	public final static int POST_TEST_CHECK_RCODE_FAIL = -31;//测试_检测微商城兑换码失败
	public final static int POST_TEMPLATE_SUCCESS = 32;//获取模板成功
	public final static int POST_TEMPLATE_FAIL = -32;//获取模板失败
	public final static int DOWNLOAD_AD_SUCCESS = 40;//下载广告成功
	
	public final static int ICOFFICECARD_PAY_FAIL = 51;//客非卡支付失败
}
