package com.icoffice.library.moudle.control;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;

import CofficeServer.PayType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.icoffice.library.app.BaseApplication;
import com.icoffice.library.bean.GoodsStateBean;
import com.icoffice.library.bean.UserBean;
import com.icoffice.library.bean.db.AdStatusBean;
import com.icoffice.library.bean.db.AppRestarTime;
import com.icoffice.library.bean.db.GoodsBean;
import com.icoffice.library.bean.db.LYMachineSetBean;
import com.icoffice.library.bean.db.MachineBean;
import com.icoffice.library.bean.db.OrderBean;
import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.bean.db.RcodeBean;
import com.icoffice.library.bean.db.RecoveryRecordsBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.bean.db.SoldWayBean;
import com.icoffice.library.bean.db.WayBean;
import com.icoffice.library.bean.network.GoodsAllBean;
import com.icoffice.library.bean.network.StatusAliPayStateBean;
import com.icoffice.library.bean.network.StatusAlipaySoniceBean;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.bean.network.StatusExchangeOrder;
import com.icoffice.library.bean.network.StatusGoodsStatus;
import com.icoffice.library.bean.network.StatusLocalOrder;
import com.icoffice.library.bean.network.StatusNetOrder;
import com.icoffice.library.bean.network.StatusRegisterBean;
import com.icoffice.library.bean.network.StatusTemplateBean;
import com.icoffice.library.bean.network.StatusUserBean;
import com.icoffice.library.bean.network.StatusWayAndGoodsBean;
import com.icoffice.library.bean.network.StatusWayBean;
import com.icoffice.library.bean.network.StatusWeChatStateBean;
import com.icoffice.library.callback.ConnectNetCallBack;
import com.icoffice.library.callback.GoodsStatusCallBack;
import com.icoffice.library.callback.RegisterMachineCallBack;
import com.icoffice.library.callback.TemplateCallBack;
import com.icoffice.library.callback.UserEnterCallBack;
import com.icoffice.library.callback.WayAndGoodsCallBack;
import com.icoffice.library.callback.WayStatusCallBack;
import com.icoffice.library.configs.Constant;
import com.icoffice.library.db.DbHelper;
import com.icoffice.library.handler.BaseHttpHandler;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.ice.Coordinator;
import com.icoffice.library.moudle.thread.AVMThread;
import com.icoffice.library.moudle.thread.HeartBeatThread;
import com.icoffice.library.moudle.thread.IceLoginThread;
import com.icoffice.library.moudle.thread.MaintenanceThread;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.MachineUtil;
import com.icoffice.library.utils.Mutex;
import com.icoffice.library.utils.NetWork;
import com.icoffice.library.utils.ParseICEJSON;
import com.icoffice.library.utils.ParseJSON;
import com.icoffice.library.utils.ParseUtil;
import com.icoffice.library.utils.RootUtil;
import com.icoffice.library.utils.download.DownloadService;
import com.icoffice.library.utils.download.DownloadService.DownloadStateListener;
import com.leiyuntop.Refrigerator.Refrigerator;

public abstract class BaseMachineControl {
	protected Context mContext;
	protected BaseApplication _application;
	protected BaseViewHandler mViewHandler, mBackViewHandler;
	
	private ArrayList<BasePayControl> listRollBasePayControl = new ArrayList<BasePayControl>();//轮询中的订单集合
	private ArrayList<BasePayControl> listOutGoodsBasePayControl = new ArrayList<BasePayControl>();//出货中的订单结合
	
	protected BasePayControl mCurrentPayControl;//当前的订单
	public boolean isFrontView = true;//true在前台页面  false后台页面
	public boolean respondButton = true;// 判断是否是相应物理按钮跳转内页
	
	private boolean _isLoadConfig = false;
	protected Mutex _mutex = new Mutex();
	protected boolean _isSinglechipStartSuccess = false; // 单片机初始化
	protected boolean _isIceStartSuccess = false; // ICE登录成功
//	protected boolean _coinStatus = true; // 默认为正常
//	protected boolean _notesStatus = true; // 默认为正常
	protected boolean _closeDoorStatus = true; // 默认为关门
	protected String _networkMode = ""; // 网络模式
	protected int _signalStrength = 0; // 网络信号
	protected int _receivedMoney = 0; // 收币金额
	
	protected SIMControl mSIMControl;//sim卡控制对象
	protected MachineBean mMachineBean;//机器状态对象
	public DbHelper mDbHelper;//数据库操作对象
	protected NetWork mNetWork;//网络操作对象
	public RootUtil mRootUtil;//权限操作对象
	public AudioControl mAudioControl;//音频操作对象
	public FileUtil mFileUtil;

	
	private SoldGoodsBean mSoldGoodsBean;

	protected AppRestarControl mAppRestarControl;//APP自启动对象
	protected UpgradeControl _upgradeControl; // APK更新对象
	protected Coordinator _coordinator; // 与服务器进行Ice通讯的协助对象
	protected MaintenanceThread _maintenanceThread; // 维护线程对象（进行apk更新，广告下载等）
	protected HeartBeatThread _heartBeatThread; // 心跳线程对象
	protected IceLoginThread _iceLoginThread; // ICE状态监测及重连线程
	protected AVMThread mAVMThread;//开机自启动 检测与单片机的连通状态
	protected PosControl _posControl; // POS控制对象
	protected SupervisorControl _supervisorControl; // 守护进程
	
	/**
	 * 接口定义
	 */
	protected ConnectNetCallBack mConnectNetCallBack;
	protected GoodsStatusCallBack mGoodsStatusCallBack;
	protected WayStatusCallBack mWayStatusCallBack;
	protected WayAndGoodsCallBack mWayAndGoodsCallBack;
	protected UserEnterCallBack mUserEnterCallBack;
	protected RegisterMachineCallBack mRegisterMachineCallBack;
	protected TemplateCallBack mTemplateCallBack;
	
	@SuppressLint("HandlerLeak")
	public BaseHttpHandler mHttpHandler = new BaseHttpHandler() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BaseHttpHandler.CONNECT_NET_FAIL_BACKSTAGE:
				result = (String) msg.getData().getString("result");
				mConnectNetCallBack.connectFail(result);
				break;
			case BaseHttpHandler.ICE_ROLL_WX_SUCCESS: // 微信轮询
				StatusWeChatStateBean mStatusWeChatStateBean = ParseICEJSON
						.getWeChatState((HashMap) msg.obj);
				mWechatPayControl = (WechatPayControl) getRollBasePayControl(mStatusWeChatStateBean
						.getClientOrderNo());
				
				if (null != mWechatPayControl) {
					if (mStatusWeChatStateBean.getStatus().equals("1")) {
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "支付成功; _isNoticeOutGoods = " + mWechatPayControl._isNoticeOutGoods);
						mWechatPayControl.mOrderBean
								.setO_number(mStatusWeChatStateBean.getNumber());
						if (!mWechatPayControl._isNoticeOutGoods) {
							mWechatPayControl.noticeOutGoods();
						}
					} else if (mStatusWeChatStateBean.getStatus().equals("81")) {
						// 支付失败
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "还未生成订单");
					} else if (mStatusWeChatStateBean.getStatus().equals("89")) {
						// 支付失败
						if(mWechatPayControl.isRollFrist()){
							CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "倒计时重置为90");
							startSound(1);
							mWechatPayControl.setRollFrist(false);
							mWechatPayControl.setRollTime(1 * 90 * 1000);
							mWechatPayControl.setTimeStampFirst(System.currentTimeMillis());
							Bundle mBundle = new Bundle();
							mBundle.putInt("shopTimer", 90);
							CommonUtils.sendMessage(mViewHandler, RefreshShopTimer, mBundle);
						}
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "订单已经生成，还未支付");
					}
				} else {
					// 没有该订单
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "客户端原因造成没有该微信订单");
				}
				break;
			case BaseHttpHandler.ICE_ROLL_WX_FAIL:
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "weChat: " + "支付通讯失败");
				break;
			case BaseHttpHandler.ICE_ROLL_ALI_SONICE_SUCCESS:
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "声波通讯成功");
				startSound(1);
				StatusAlipaySoniceBean mStatusAlipaySoniceBean = ParseICEJSON
						.getAlipaySoniceBean((HashMap) msg.obj);
				mAliPayControl = (AliPayControl) getRollBasePayControl(mStatusAlipaySoniceBean
						.getClientOrderNo());
				if (null != mAliPayControl) {
					if (mStatusAlipaySoniceBean.getStatus().equals("101")) {// 快捷支付成功
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "快捷支付成功");
						if (!mAliPayControl._isNoticeOutGoods) {

							mAliPayControl.noticeOutGoods();
						}
					} else if (mStatusAlipaySoniceBean.getStatus()
							.equals("104")) {// 支付中轮询
//						mAliPayControl.rollRequest();
//						Bundle mBundle = new Bundle();
//						mBundle.putInt("shopTimer", 60);
//						CommonUtils.sendMessage(mViewHandler, RefreshShopTimer, mBundle);
					} else if (mStatusAlipaySoniceBean.getStatus()
							.equals("103")
							|| mStatusAlipaySoniceBean.getStatus()
									.equals("102")
							|| mStatusAlipaySoniceBean.getStatus()
									.equals("105")) {// 支付失败 交易取消
						mAliPayControl.UnNormalInterrupt();
					} else {
						mAliPayControl.UnNormalInterrupt();
					}
				} else {
					// 没有该订单
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "客户端原因造成没有该支付宝订单");
				}
				break;
			case BaseHttpHandler.ICE_ROLL_ALI_SONICE_FAIL:
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "订单声波通讯失败");
				mBundle = msg.getData();
				client_order_no = mBundle.getString("client_order_no");
				mAliPayControl = (AliPayControl) getRollBasePayControl(client_order_no);
				mAliPayControl.UnNormalInterrupt();
				break;
			case BaseHttpHandler.ICE_ROLL_ALI_SUCCESS:
				StatusAliPayStateBean mStatusAliPayStateBean = ParseICEJSON
						.getStatusAliPayStateBean((HashMap) msg.obj);
				mAliPayControl = (AliPayControl) getRollBasePayControl(mStatusAliPayStateBean
						.getClientOrderNo());
				if (null != mAliPayControl) {
					
					if (mStatusAliPayStateBean.getStatus().equals("1")) {
						mAliPayControl.mOrderBean
								.setO_number(mStatusAliPayStateBean.getNumber());
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "支付宝订单生成; 服务器返回的status = " + mStatusAliPayStateBean.getStatus() + ";_isNoticeOutGoods" + mAliPayControl._isNoticeOutGoods);
						if (!mAliPayControl._isNoticeOutGoods) {
							mAliPayControl.noticeOutGoods();
						}
					} else if (mStatusAliPayStateBean.getStatus().equals("89")
							|| mStatusAliPayStateBean.getStatus().equals("81")) {
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "支付宝还未生成订单; 服务器返回的status = " + mStatusAliPayStateBean.getStatus());
					} else {
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "支付宝还未生成订单; 服务器返回的status = " + mStatusAliPayStateBean.getStatus());
					}
				} else {
					// 没有该订单
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "客户端原因造成没有该支付宝订单");
				}
				break;
			case BaseHttpHandler.ICE_ROLL_ALI_FAIL:
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay: " + "订单声波通讯失败");
				break;
			case BaseHttpHandler.ICE_NET_ORDER_SUCCESS:// 网络订单提交成功
				StatusNetOrder mStatusNetOrder = ParseICEJSON
						.parseStatusNetOrder((HashMap) msg.obj);
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "net 订单 成功: " + "order = " + (HashMap) msg.obj);
				for (int i = 0; i < mStatusNetOrder.getSuccessarr().size(); i++) {
					updateP_state(mStatusNetOrder.getSuccessarr().get(i));
				}
				break;
			case BaseHttpHandler.ICE_NET_ORDER_FAIL:// 网络订单提交失败
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "net 订单 失败: " );
				break;
			case BaseHttpHandler.ICE_ROLL_ETOUCH_CASH_SUCCESS:// cash订单提交成功
				StatusLocalOrder statusLocalOrder = ParseICEJSON
						.parseStatusLocalOrder((HashMap) msg.obj);
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"cash订单提交成功，返回的data = " + (HashMap) msg.obj);
				for (int i = 0; i < statusLocalOrder.getSuccessarr().size(); i++) {
					updateP_state(statusLocalOrder.getSuccessarr().get(i));
				}
				break;
			case BaseHttpHandler.ICE_ROLL_ETOUCH_CASH_FAIL:// cash订单提交失败
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"cash订单提交失败");
				break;
			case BaseHttpHandler.ICE_CREAT_ORDER_COFFICESHOP_SUCCESS:
				StatusExchangeOrder statusExchangeOrder = ParseICEJSON.parseStatusExchangeOrder((HashMap) msg.obj);
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"order = " + (HashMap) msg.obj);

				for (int i = 0; i < statusExchangeOrder.getSuccessarr().size(); i++) {
					CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"statusExchangeOrder.getSuccessarr() = "
							+ statusExchangeOrder.getSuccessarr().get(i));
					updateP_state(statusExchangeOrder.getSuccessarr().get(i));
				}
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"兑换订单提交成功");
				break;
			case BaseHttpHandler.ICE_CREAT_ORDER_COFFICESHOP_FAIL:
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"兑换订单提交失败");
				break;
			case BaseHttpHandler.ICE_CONNECTED_SUCCESS: // ice登录成功
				CommonUtils.showLog(CommonUtils.ICE_TAG,"ice登录成功");
				_isIceStartSuccess = true;
				socketGetGoodsBean();
				socketAd();
				//初次连接上ICE时，获取服务端配置
				if (!_isLoadConfig) {
					reloadConfig();
					_isLoadConfig = true;
				}
				// checkInitStatus();
				break;
			case BaseHttpHandler.ICE_CONNECTED_FAIL:
				break;
			case BaseHttpHandler.ICE_GET_GOODSBEAN_SUCCESS: // ice获取全部货品信息成功
				resultMap = (Map<String, String>) msg.obj;
				if ("1".equals(resultMap.get("status"))) {
//					Log.i("ice", "queryMachineGoods: " + resultMap.get("list"));
					try {
						JSONArray list = new JSONArray(resultMap.get("list"));
						GoodsAllBean mGoodsAllBean = ParseICEJSON
								.parseGoodListAll(list,
										resultMap.get("mt_timestamp"));
						dbInsertGoodsBean(mGoodsAllBean);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				break;
			case BaseHttpHandler.ICE_GET_GOODSBEAN_FAIL:
				break;
			case BaseHttpHandler.ICE_QUERYMACHINEAD_SUCCESS://获取广告成功
				resultMap = (Map<String, String>) msg.obj;
				if("1".equals(resultMap.get("status"))){
					try {
						JSONArray list = new JSONArray(resultMap.get("list"));
						ArrayList<AdStatusBean> adStatusBean_list = ParseICEJSON.parseAdStatusBeanList(list);
						insertAdStatusBean(adStatusBean_list);
						downLoadAd(selectAllAdStatusBean());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				CommonUtils.showLog(CommonUtils.TAG, "获取机器广告 = " + resultMap);
				break;
			case BaseHttpHandler.ICE_QUERYMACHINEAD_FAIL://获取广告失败
				break;
			case POST_CHECK_M_NO_SUCCESS://获取机器编码成功
				result = (String) msg.getData().getString("result");
				StatusRegisterBean check_mRegisterStatusBean = ParseJSON
						.parseRegister(result);
				if (check_mRegisterStatusBean.getStatus().equals("1")
						|| check_mRegisterStatusBean.getStatus().equals("32")) {
					mRegisterMachineCallBack.success(check_mRegisterStatusBean);
				} else {
					// TODO 注册失败
				}
				break;
			case POST_CHECK_M_NO_FAIL://获取机器编码失败
				break;
			case POST_REGISTER_SUCCESS://机器注册成功
				result = (String) msg.getData().getString("result");
				StatusRegisterBean mRegisterStatusBean = ParseJSON
						.parseRegister(result);
				if (mRegisterStatusBean.getStatus().equals("1")
						|| mRegisterStatusBean.getStatus().equals("32")) {
					mDbHelper.insertMachine(mRegisterStatusBean);
					mMachineBean = ParseUtil
							.getMachineDbbean(mRegisterStatusBean);
					mRegisterMachineCallBack.success(mRegisterStatusBean);
					
					ArrayList<String> drink_list = GoodsWayUtil.getDrinkWCode();
					ArrayList<String> drink_2_list = GoodsWayUtil.getDrink2WCode();
					ArrayList<String> food_list = GoodsWayUtil.getFoodWCode();
					ArrayList<String> grid_list = GoodsWayUtil.getGridWCode();
					ArrayList<String> leiyun_list = GoodsWayUtil.getLeiyunWCode();
					mDbHelper.deleteWayBean();
					if(mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.FOODS_BOX_ID)){
						mDbHelper.insertWayBean(food_list);
					}
					if(mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.DRINKS_BOX_ID)){
						mDbHelper.insertWayBean(drink_list);
					}
					if(mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.DRINKS_2_BOX_ID)){
						mDbHelper.insertWayBean(drink_2_list);
					}
					if(mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.GRID_1_BOX_ID)
							||mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.GRID_2_BOX_ID)){
						mDbHelper.insertWayBean(grid_list);
					}
					if(mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.LEIYUN_BOX_ID)
							||mRegisterStatusBean.getBox_map().containsKey(GoodsWayUtil.LEIYUN_BOX_ID)){
						mDbHelper.insertWayBean(leiyun_list);
					}
					mDbHelper.updateMachineBean(mMachineBean);
					mDbHelper.updateRefreshWayBean(mRegisterStatusBean.getmWayBean_list());
					mDbHelper.updateGoodsStatusBean(mRegisterStatusBean.getmGoodsStatusBean_list());
					mMachineBean = initMachineBean();
					mNetWork.mMachineBean = mMachineBean;
				} else {
					// TODO 注册失败
					mRegisterMachineCallBack.fail(mRegisterStatusBean.getMsg());
				}
				break;
			case POST_REGISTER_FAIL://机器注册失败
				result = (String) msg.getData().getString("result");
				mRegisterMachineCallBack.fail(result);
				break;
			case POST_GOODSSTATE_SUCCESS:;//修改商品状态成功
				result = (String) msg.getData().getString("result");
				StatusGoodsStatus mStatusGoodsStatus = ParseJSON
						.parseGoodsStatus(result);
				if (mStatusGoodsStatus.getStatus().equals("1")) {
					mGoodsStatusCallBack.success(mStatusGoodsStatus.getMsg());
				} else {
					mGoodsStatusCallBack.fail(mStatusGoodsStatus.getMsg());
				}
				break;
			case POST_GOODSSTATE_FAIL:;//修改商品状态失败
				result = (String) msg.getData().getString("result");
				mGoodsStatusCallBack.fail(result);
				break;
			case POST_WAY_STATE_SUCCESS://修改货道状态成功
				result = (String) msg.getData().getString("result");
				StatusWayBean mStatusWayBean = ParseJSON.parseWayStatus(result);
				if (mStatusWayBean.getStatus().equals("1")) {
					mWayStatusCallBack.success(mStatusWayBean.getMsg());
				} else {
					mWayStatusCallBack.fail(mStatusWayBean.getMsg());
				}
				break;
			case POST_WAY_STATE_FAIL://修改货道状态失败
				result = (String) msg.getData().getString("result");
				mWayStatusCallBack.fail(result);
				break;
			case POST_WAYANDGOODSSTATE_SUCCESS://补货成功
				result = (String) msg.getData().getString("result");
				StatusWayAndGoodsBean mStatusWayAndGoodsBean = ParseJSON
						.parseWayAndGoodsStatus(result);
				if (mStatusWayAndGoodsBean.getStatus().equals("1")) {
					mWayAndGoodsCallBack.success(mStatusWayAndGoodsBean
							.getMsg());
				} else {
					mWayAndGoodsCallBack.fail(mStatusWayAndGoodsBean.getMsg());
				}
				break;
			case POST_WAYANDGOODSSTATE_FAIL://补货失败
				result = (String) msg.getData().getString("result");
				mWayAndGoodsCallBack.connectFail(result);
				break;
			case POST_USERENTER_SUCCESS://用户登入成功
				result = (String) msg.getData().getString("result");
				StatusUserBean mStatusUserBean = ParseJSON
						.parseUserStatus(result);
				if (mStatusUserBean.getStatus().equals("1")) {
					mUserEnterCallBack.success(mStatusUserBean);
					UserBean mUserBean = ParseUtil.getUserBean(mStatusUserBean);
					mNetWork.setmUserBean(mUserBean);
				} else {
					mUserEnterCallBack.fail(mStatusUserBean.getMsg());
				}
				break;
			case POST_USERENTER_FAIL://用户登入失败
				result = (String) msg.getData().getString("result");
				mUserEnterCallBack.fail(result);
				break;
			case POST_CHECK_RCODE_SUCCESS://检测微商城兑换码成功
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_CHECK_RCODE_SUCCESS, result);
				break;
			case POST_CHECK_RCODE_FAIL://检测微商城兑换码失败
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_CHECK_RCODE_FAIL, result);
				break;
			case POST_ADD_CHANGE_LOG_SUCCESS://检测微商城,增加兑换记录成功
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_ADD_CHANGE_LOG_SUCCESS, result);
				break;
			case POST_ADD_CHANGE_LOG_FAIL://检测微商城,增加兑换记录失败
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_ADD_CHANGE_LOG_FAIL, result);
				break;
			case POST_QUIT_CONVERT_SUCCESS://检测微商城,退出兑换（解除锁定）成功
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_QUIT_CONVERT_SUCCESS, result);
				break;
			case POST_QUIT_CONVERT_FAIL://检测微商城,退出兑换（解除锁定）失败
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_QUIT_CONVERT_FAIL, result);
				break;
			case POST_TEST_CHECK_M_NO_SUCCESS://测试_检测微商城兑换码成功
				result = (String) msg.getData().getString("result");
				CommonUtils.sendMessage(mViewHandler, POST_TEST_CHECK_RCODE_SUCCESS, result);
				break;
			case POST_TEMPLATE_SUCCESS://获取模板成功
				result = (String) msg.getData().getString("result");
				StatusTemplateBean statusTemplateBean = ParseJSON.parseStatusTemplateBean(result);
				mTemplateCallBack.TemplateSuccess(statusTemplateBean);
				break;
			case POST_TEMPLATE_FAIL://获取模板失败
				result = (String) msg.getData().getString("result");
				mTemplateCallBack.TemplateConnectFail(result);
				break;
			case DOWNLOAD_AD_SUCCESS:
				ArrayList list = msg.getData().getCharSequenceArrayList("list");
				ArrayList<AdStatusBean> fail_adStatusBean_list = (ArrayList<AdStatusBean>) list.get(0);
				for(int i = 0;i < fail_adStatusBean_list.size();i++){
				AdStatusBean fail_AdStatusBean = fail_adStatusBean_list.get(i);
				if(mFileUtil.isFileExist(FileUtil.ROOT_PATH + fail_AdStatusBean.getFile()) && CommonUtils.md5sum(FileUtil.ROOT_PATH + fail_AdStatusBean.getFile()).equals(fail_AdStatusBean.getFileMd5()))
				{
					CommonUtils.showLog(CommonUtils.AD_TAG, "下载成功 存在的广告 " + FileUtil.ROOT_PATH + fail_AdStatusBean.getFile());
					updateAdStatusBean(fail_AdStatusBean);
				}else
				{
					CommonUtils.showLog(CommonUtils.AD_TAG, "下载成功 不存在的广告 " + FileUtil.ROOT_PATH + fail_AdStatusBean.getFile());
				}
				}
				break;
			default:
				break;
			}
		}
	};

	protected BaseMachineControl(Context context) {
		mContext = context;
		_application = (BaseApplication) context.getApplicationContext();
		mDbHelper = DbHelper.getInstance(mContext); // dbHelper初始化
		mNetWork = NetWork.getInstance();
		mRootUtil = RootUtil.getInstance();
		mSIMControl = SIMControl.getInstance(mContext);
		mFileUtil = FileUtil.getInstance();
		
		
		mMachineBean = initMachineBean();
		loadMachineSet();
		//获取支付方式
		selectPayTypeBean();
		
		//插入app启动时间
		insertStartAppTime();

		//初始化app自启动助手
		mAppRestarControl = AppRestarControl.getInstance(this);
		
		// 初始化ICE通讯助手
		String args[] = { ""/* , "Ice.LogFile=" */};
		_coordinator = new Coordinator(this, args);

		// 初始化升级器
		_upgradeControl = new UpgradeControl(_coordinator, mContext, this);

		// 初始化维护线程
		_maintenanceThread = new MaintenanceThread(this);

		// 初始化心跳线程，心跳间隔为60秒钟
		// Todo: 心跳间隔应从本地配置中读取
		_heartBeatThread = new HeartBeatThread(this);
		_heartBeatThread.setInterval(60);

		loginIceServer();

		// 初始化ICE状态监测线程
		_iceLoginThread = new IceLoginThread(this, _coordinator);
		_iceLoginThread.start();
		/**
		 * 线程阻塞bug
		 */
		//初始化检测单片机心跳线程
//		mAVMThread = new AVMThread(this);
//		mAVMThread.setDuration(60);
//		mAVMThread.start();
		
		//初始化守护程序
//		FileUtil.CopyAssetsBin(mContext, "bin");
//		_supervisorControl = SupervisorControl.getInstance(mContext, _application.getClassName());
//		_supervisorControl.start();
		
		// 初始化POS
		// _posControl = new PosControl();
	}
	
	public Context getContext() {
		return mContext;
	}

	/**
	 * 注册接口
	 * 
	 * @param mGoodsStatusCallBack
	 */
	public void setGoodsStatusCallBack(GoodsStatusCallBack goodsStatusCallBack) {
		mGoodsStatusCallBack = goodsStatusCallBack;
	}

	public void setWayStatusCallBack(WayStatusCallBack wayStatusCallBack) {
		mWayStatusCallBack = wayStatusCallBack;
	}

	public void setWayAndGoodsCallBack(WayAndGoodsCallBack wayAndGoodsCallBack) {
		mWayAndGoodsCallBack = wayAndGoodsCallBack;
	}

	public void setConnectNetCallBack(ConnectNetCallBack connectNetCallBack) {
		mConnectNetCallBack = connectNetCallBack;
	}

	public void setUserEnterCallBack(UserEnterCallBack userEnterCallBack) {
		mUserEnterCallBack = userEnterCallBack;
	}

	public void setRegisterMachineCallBack(RegisterMachineCallBack registerMachineCallBack) {
		mRegisterMachineCallBack = registerMachineCallBack;
	}
	public void setTemplateCallBack(TemplateCallBack templateCallBack){
		mTemplateCallBack = templateCallBack;
	}
	/**
	 * machineBean初始化
	 * 
	 * @param
	 */
	public MachineBean initMachineBean() {
		MachineBean machineBean = mDbHelper.selectMachineBean();
		if (null == machineBean) {
			mDbHelper.insertMachineBean();
			mMachineBean = new MachineBean();
		} else {
			mMachineBean = machineBean;
		}
		return mMachineBean;
	}
	/**
	 * 初始化机器设置
	 */
	public void loadMachineSet(){
		_application.setCoinAndnoteStatus(getMachineSetStatus().get(0));
		_application.setCoinWaitTime(getMachineSetStatus().get(1));
		_application.setPhysicsButtonStatus(getMachineSetStatus().get(2));
	}
	/**
	 * 插入app启动时间
	 */
	public void insertStartAppTime(){
		String timeCurrentDay = CommonUtils.currentTime();
		AppRestarTime appRestartTime = new AppRestarTime();
		appRestartTime.setMode(0);
		appRestartTime.setTime(timeCurrentDay);
		insertAppRestartTime(appRestartTime);
	}
	/**
	 * 启动app自启动检测
	 */
	public void checkAppRestart(){
		mAppRestarControl.check();
	}
	/**
	 * netWork初始化
	 * 
	 * @param
	 */
	public void initNetWork(String BASE_URL, String WECHAT_BASE_URL,String ACTIVITY_BASE_URL) {
		mNetWork.mMachineBean = mMachineBean;
		mNetWork.BASE_URL = BASE_URL;
		mNetWork.WECHAT_BASE_URL = WECHAT_BASE_URL;
		mNetWork.ACTIVITY_BASE_URL = ACTIVITY_BASE_URL;
		mNetWork.mHttpHandler = mHttpHandler;
	}

	/**
	 * Ice建立服务器连接
	 * 
	 * @param
	 */
	public void loginIceServer() {

		// 登录服务器
		String machineCode = mMachineBean.getM_code();
		if (null == mMachineBean.getM_code()
				|| mMachineBean.getM_code().isEmpty()) {
			// 机器没有注册成功，无法登录ICE
			return;
		}
		_coordinator.login(machineCode, "", Constant.SVR_HOST);
	}

	/**
	 * viewHandler初始化
	 * 
	 * @param
	 */
	public void initViewHandler(BaseViewHandler mViewHandler) {
		this.mViewHandler = mViewHandler;
	}
	/**
	 * 后台viewHandler初始化
	 * 
	 * @param mBackViewHandler
	 */
	public void initBackViewHandler(BaseViewHandler mBackViewHandler) {
		this.mBackViewHandler = mBackViewHandler;
	}
	public CashLeiYunPayControl startCashLeiYunPay(SoldGoodsBean soldGoodsBean) {
		mCurrentPayControl = new CashLeiYunPayControl(this, soldGoodsBean);
		mCurrentPayControl.start();
		return (CashLeiYunPayControl) mCurrentPayControl;
	}
	public ExchangePayControl startExchangePay(RcodeBean rcodeBean,String rid,String oid) {
		ExchangePayControl payControl = new ExchangePayControl(this, rcodeBean,rid,oid);
		mCurrentPayControl = payControl;
		mCurrentPayControl.start();
		return (ExchangePayControl) mCurrentPayControl;
	}
	public CashETPayControl startCashETPay(SoldGoodsBean soldGoodsBean) {
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"开始现金payControl");
		startSound(11);
		CashETPayControl payControl = new CashETPayControl(this,soldGoodsBean);
		mCurrentPayControl = payControl;
		mCurrentPayControl.start();
		payControl.cashIn(_receivedMoney);
		return (CashETPayControl) mCurrentPayControl;
	}

	public CardPayControl startCardPay(SoldGoodsBean soldGoodsBean) {
		startSound(15);
		mCurrentPayControl = new CardPayControl(this,soldGoodsBean);
//		mCurrentPayControl.start();
		return (CardPayControl) mCurrentPayControl;
	}

	public WechatPayControl startWecatPay(SoldGoodsBean soldGoodsBean) {
		startSound(8);//语音播报
		mCurrentPayControl = new WechatPayControl(this, soldGoodsBean);
		mCurrentPayControl.start();
		return (WechatPayControl) mCurrentPayControl;
	}
	//支付宝支付
	public AliPayControl startAliPay(SoldGoodsBean soldGoodsBean) {
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "alipay:" + "点击购买");
		mCurrentPayControl = new AliPayControl(this,soldGoodsBean);
		mCurrentPayControl.start();
		return (AliPayControl) mCurrentPayControl;
	}

	public UnionPayControl startUnionPay(SoldGoodsBean soldGoodsBean) {
		mCurrentPayControl = new UnionPayControl(this,soldGoodsBean);
		mCurrentPayControl.start();
		return (UnionPayControl) mCurrentPayControl;
	}
	public ServicePayControl startServicePay(SoldGoodsBean soldGoodsBean,OrderBean orderBean) {
		mCurrentPayControl = new ServicePayControl(this,soldGoodsBean,orderBean);
		return (ServicePayControl) mCurrentPayControl;
	}
	public BasePayControl getCurrentPayControl() {
		return mCurrentPayControl;
	}

	public void addListRollBasePayControl(BasePayControl basePayControl) {
		listRollBasePayControl.add(basePayControl);
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "添加basePayControl");
	}

	public void addListOutGoodsBasePayControl(BasePayControl basePayControl,
			boolean useQueue) {
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, " 是否出货指令放入队列 useQueue = " + useQueue);
		if (useQueue) {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "出货指令放入队列");
			listOutGoodsBasePayControl.add(basePayControl);
			if (listOutGoodsBasePayControl.size() == 1) {
				outGoods(basePayControl);
			}else{
				for(int i = 0;i < listOutGoodsBasePayControl.size();i++){
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "出货指令队列 当前" + i + " = " + listOutGoodsBasePayControl.get(i).getClient_order_no());
				}
			}
		} else {
			outGoods(basePayControl);
		}
	}

	/**
	 * 根据订单号从listBasePayControl获取BasePayControl对象
	 * 
	 * @param
	 */
	private BasePayControl getRollBasePayControl(String client_order_no) {
		BasePayControl basePayControl = null;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"根据订单号从listBasePayControl获取BasePayControl对象:getRollBasePayControl.listRollBasePayControl:"+ listRollBasePayControl.size());
						
		for (int i = 0; i < listRollBasePayControl.size(); i++) {
			BasePayControl tBasePayControl = listRollBasePayControl.get(i);
			if (tBasePayControl.mOrderBean.getClient_order_no().equals(
					client_order_no)) {
				basePayControl = tBasePayControl;
				break;
			}
		}
		return basePayControl;
	}

	/**
	 * 根据订单号从outGoodsBasePayControl获取BasePayControl对象
	 * 
	 * @param
	 */
	public BasePayControl getOutGoodsBasePayControl(String tradeTrace) {
		BasePayControl basePayControl = null;
		int size = listOutGoodsBasePayControl.size();
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"根据订单号从outGoodsBasePayControl获取BasePayControl对象:getOutGoodsBasePayControl.listOutGoodsBasePayControl:" + size);
		if (size > 0) {
			if (tradeTrace.isEmpty()) {
				basePayControl = listOutGoodsBasePayControl.get(0);
			} else {
				synchronized (this) {
					for (int i = 0; i < listOutGoodsBasePayControl.size(); i++) {
						BasePayControl tBasePayControl = listOutGoodsBasePayControl
								.get(i);
						if (tBasePayControl.getTradeTrace().equals(tradeTrace)) {
							basePayControl = tBasePayControl;
							break;
						}
					}
				}
			}
		}
		return basePayControl;
	}

	/**
	 * 根据订单号从listBasePayControl删除BasePayControl对象
	 * 
	 * @param
	 */
	public void deleteRollBasePayControl(String client_order_no) {
		for (int i = 0; i < listRollBasePayControl.size(); i++) {
			BasePayControl tBasePayControl = listRollBasePayControl.get(i);
			if (tBasePayControl.mOrderBean.getClient_order_no().equals(
					client_order_no)) {
				listRollBasePayControl.remove(i);
				break;
			}
		}
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "根据订单号从listBasePayControl删除BasePayControl对象 client_order_no = " + client_order_no + ";剩余的 轮询数组长度 = " + listRollBasePayControl.size());
	}

	/**
	 * 根据订单号从outGoodsBasePayControl删除BasePayControl对象
	 * 
	 * @param
	 */
	private void deleteOutGoodsBasePayControl(String tradeTrace) {
		synchronized (this) {
			if (tradeTrace.isEmpty()) {
				// 没有流水号，适用于非易触机型
				listOutGoodsBasePayControl.remove(0);
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"");
			} else {
				for (int i = 0; i < listOutGoodsBasePayControl.size(); i++) {
					BasePayControl tBasePayControl = listOutGoodsBasePayControl
							.get(i);
					if (tBasePayControl.getTradeTrace().equals(tradeTrace)) {
						listOutGoodsBasePayControl.remove(i);
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"根据订单号从outGoodsBasePayControl删除BasePayControl对象 ; tradeTrace = " + tradeTrace);
						break;
					}
				}
			}
		}
		BasePayControl basePayControl = getOutGoodsBasePayControl("");
		if (null != basePayControl && basePayControl.isUseQueue()) {
			outGoods(basePayControl);
		}
	}

	/**
	 * 更新出货队列，删除超时的对象
	 */
	public void refreshOutGoodsBasePayControl() {
		synchronized (this) {
			int size = listOutGoodsBasePayControl.size();
			List<Integer> indexList = new ArrayList<Integer>();
			for (int i = 0; i < listOutGoodsBasePayControl.size(); i++) {
				BasePayControl payControl = listOutGoodsBasePayControl.get(i);
				long timeStamp = payControl.getCreateTimeStamp();
				if (System.currentTimeMillis() > (timeStamp + 1000 * 60 * 10)) {
					// 订单创建时间大于10分钟，进行清理
					if (PayType.valueOf(payControl.getPayType()) == PayType.cash) {
						// ToDo : 只适用于易触现金形式，后续改进
						payControl.mOrderBean.setO_pay_state(0);
					}
//					payControl.mOrderBean.setO_state(3);

					payControl.finish();
					
					// 记录要删除的对象的索引
					indexList.add(i);
				}
			}

			// 清理对象
			for (int i = indexList.size(); i > 0; i--) {
				listOutGoodsBasePayControl.remove((int)indexList.get(i-1));
			}
			int currentSize = listOutGoodsBasePayControl.size();
			int freeSize = size - currentSize;

			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"refreshOutGoodsBasePayControl:共释放出货对象="+ freeSize + ";现对象数=" + currentSize);
							
		}
	}

	/**
	 * 出货信息处理函数，硬件厂商在获得出货结果时，都会提供回调函数。本函数封装在回调入口里做的处理
	 */
	public abstract void checkOutGoodsResult();
	public void outGoodsFinishByLY(int msgWhat,boolean isSuccess, String tradeTrace){
		BasePayControl basePayControl = getOutGoodsBasePayControl(tradeTrace);
		if (null != basePayControl) {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"雷云峰 出货状态 = " + isSuccess + ";流水号 tradeTrace = " + tradeTrace + ";支付方式 = " + basePayControl.getPayType());
			if (isSuccess) {
				if(PayType.valueOf(basePayControl.getPayType()) == PayType.cash)
					startSound(12);
				else
					startSound(9);
				basePayControl.mOrderBean.setO_state(1);
			} else {
				basePayControl.mOrderBean.setO_state(2);
			}
			updateWayBean(basePayControl.mOrderBean.getW_code());
			basePayControl.finish();
			deleteOutGoodsBasePayControl(tradeTrace);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PayControl", basePayControl);
			CommonUtils.sendMessage(mViewHandler, msgWhat, bundle);
		} else {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"雷云峰 出货状态 = listOutGoodsBasePayControl is wrong Or CashPay ;流水号 tradeTrace = " + tradeTrace);
		}
	}
	/**
	 * 出货完成通知
	 * 
	 * @param isSuccess
	 *            是否出货成功
	 * @param tradeTrace
	 *            本地流水号（售货机专用）
	 * 
	 */
	public boolean outGoodsFinish(int msgWhat,boolean isSuccess, String tradeTrace) {
		BasePayControl basePayControl = getOutGoodsBasePayControl(tradeTrace);
		if (null != basePayControl) {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"出货状态 = " + isSuccess + ";流水号 tradeTrace = " + tradeTrace + ";支付方式 = " + basePayControl.getPayType());
			if (isSuccess) {
				if(PayType.valueOf(basePayControl.getPayType()) == PayType.cash)
					startSound(12);
				                          
				else
					startSound(9);
				updateWayBean(basePayControl.mOrderBean.getW_code());
				basePayControl.mOrderBean.setO_state(1);
			} else {
				if (PayType.valueOf(basePayControl.getPayType()) == PayType.cash) {
					basePayControl.mOrderBean.setO_pay_state(0);
				}
				basePayControl.mOrderBean.setO_state(3);
			}

			basePayControl.finish();
			deleteOutGoodsBasePayControl(tradeTrace);
			Bundle bundle = new Bundle();
			bundle.putSerializable("PayControl", basePayControl);
//			bundle.putInt("pay_type", basePayControl.getPayType());
			CommonUtils.sendMessage(mViewHandler, msgWhat, bundle);
			return true;
		} else {
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"出货状态 = listOutGoodsBasePayControl is wrong Or CashPay ;流水号 tradeTrace = " + tradeTrace);
			return false;
		}
	}

	/**
	 * 物理键旁路出货完成通知
	 * 
	 * @param box
	 *            货柜
	 * @param road
	 *            货道
	 * @param price
	 *            价格
	 * @param payType
	 *            支付方式
	 * @param cardNo
	 *            卡号
	 */
	public void outGoodsBypass(int msgWhat,boolean isSuccess, String box, String road,
			String price, String payType, String cardNo) {
		// TODO 04-10 20:48:30.338: E/AndroidRuntime(23027): Caused by:
		// java.lang.NumberFormatException: Invalid int: ""
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"出货状态 = " + isSuccess + ";支付方式 = " + payType);
		PayType thePayType = PayType.valueOf(Integer.parseInt(payType));
		switch (thePayType) {
		case cash:
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "cashPay 补单");
			SoldGoodsBean soldGoodsBean = selectSoldGoodsBean(GoodsWayUtil.genWayId(box,road));
			CashETPayControl cashETPayControl = new CashETPayControl(this, soldGoodsBean, Integer.parseInt(price));
			//补单时，当前剩余币额加上单价才是之前的收币金额
			int o_pay_state = 0;
			int o_state = 0;
			if (isSuccess){
				updateWayBean(cashETPayControl.mOrderBean.getW_code());
				startSound(12);
				o_pay_state = 1;
				o_state = 1;
			}else{
				o_pay_state = 0;
				o_state = 0;
			}
			cashETPayControl.setO_state(o_state);
			cashETPayControl.setOutGoodsInfo(_receivedMoney,o_pay_state);
			cashETPayControl.finish();
			Bundle bundle = new Bundle();
			bundle.putSerializable("PayControl", cashETPayControl);
//			bundle.putInt("pay_type", cashETPayControl.getPayType());
			CommonUtils.sendMessage(mViewHandler, msgWhat, bundle);
			break;
		case unionpay:
			break;
		default:
			break;
		}
	}

	/**
	 * 数据库更新机器表
	 */
	public void dbUpdateMachineBean(MachineBean machineBean) {
		mDbHelper.updateMachineBean(machineBean);
	}

	/**
	 * 数据库插入货品表
	 */
	public void dbInsertGoodsBean(GoodsAllBean goodsAllBean) {
		mDbHelper.insertGoodsBean(goodsAllBean);
	}

	/**
	 * 数据库查询货品
	 */
	public ArrayList<GoodsBean> dbSelectGoodsBean() {
		return mDbHelper.selectGoodsBean();
	}

	/**
	 * socket获取所有的货品信息
	 */
	public void socketGetGoodsBean() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("mt_timestamp", "0");
		_coordinator.queryMachineGoods(m);
	}
	public void socketAd(){
		Map<String, String> m = new HashMap<String, String>();
//		m.put("mt_timestamp", "0");
		_coordinator.queryMachineAd(m);
	}
	/**
	 * 查询当前ICE与服务器连接状况
	 */
	public boolean getIceServerStatus() {
		_coordinator.ping();
		boolean iceServerStatus = (_coordinator.getState() == Coordinator.ClientState.Connected);
		CommonUtils.showLog(CommonUtils.ICE_TAG, iceServerStatus + "");
		return iceServerStatus;
	}

	/**
	 * 查询当前与单片机连接状况
	 */
	public abstract boolean getAvmStatus();

	/**
	 * 心跳
	 */
	public void heartBeat() {
		// options.put("apk_ver",
		// Integer.toString(ApkUtil.getVerCode(mContext)));
		Map<String, String> options = new HashMap<String, String>();
		options.put("scmState", getAvmStatus() ? "1" : "0");
		options.put("networkModel", getNetworkMode());
		options.put("network", Integer.toString(getSignalStrength()));
		// coinState 纸硬币器状态
		// coin05 0.5元硬币(可传)
		// coin1 1元硬币(可传)
		// paperMoney 纸币张数(可传)
		// doorState 开关门状态
		// setTempA A设定温度(可传)
		// setTempB B设定温度(可传)
		// setTempC C设定温度(可传)
		// tempA A温度(可传)
		// tempB B温度(可传)
		// tempC C温度(可传)
		List<String> coinStatus = getCoinNotesStatus();
		
		if(_application.getCoinAndnoteStatus().equals("1")){
			options.put("coinState", coinStatus.get(0));
			options.put("coin05", coinStatus.get(1));
			options.put("coin1", coinStatus.get(2));
			options.put("paperMoney", coinStatus.get(3));
		}
		// 门开关必须在获取纸硬币信息之后调用
		options.put("doorState", getCloseDoorStatus() ? "1" : "0");
		List<String> temperatures = getTemperature();
		if (temperatures.size() > 0) {
			options.put("setTempA", temperatures.get(0));
			options.put("tempA", temperatures.get(1));
		}
		if (temperatures.size() > 2) {
			options.put("setTempB", temperatures.get(2));
			options.put("tempB", temperatures.get(3));
		}
		if (temperatures.size() > 4) {
			options.put("setTempC", temperatures.get(4));
			options.put("tempC", temperatures.get(5));
		}
		_coordinator.heartBeat(options);
	}

	/**
	 * APK版本升级
	 * 
	 * @param
	 * @throws NameNotFoundException
	 */
	public void upgradeApk(final BaseViewHandler viewHandler)
			throws NameNotFoundException {

		int versionCode = ApkUtil.getVerCode(mContext);
		String appDirPath = mContext.getPackageManager().getApplicationInfo(
				mContext.getPackageName(), 0).sourceDir;

		_upgradeControl.checkUpgrade(viewHandler, appDirPath, versionCode,
				Constant.APP_TYPE);
	}

	/**
	 * 从服务器端加载配置
	 */
	public void reloadConfig() {
		Map<String, String> ret = _coordinator.getConfig("");
		

		// 读取心跳间隔，需要保存并应用
		// Todo: 间隔时间应该保存下来，后续每次启动心跳线程都按照新的间隔执行
		String heartBeatInterval = ret.get("heartbeat_interval");
		if (heartBeatInterval != null && !heartBeatInterval.isEmpty()) {
			_heartBeatThread.setInterval(Integer.parseInt(heartBeatInterval));
		}
		
		// 读取支付方式配置，存入数据库
		String payTypeList = ret.get("pay_type_list");
		if (payTypeList != null && !payTypeList.isEmpty()) {
			String[] payTypeArray = payTypeList.split(",");
			Arrays.sort(payTypeArray);//自动从小到大排序
			// 读取服务端要求开启的支付方式，存入到内存中的对象数组
			ArrayList<PayTypeBean> payTypeBean_list = new ArrayList<PayTypeBean>();
			for (PayType payType : PayType.values()) {
				PayTypeBean payTypeBean = new PayTypeBean();
				payTypeBean.setPayType(payType.value());
				String value = Integer.toString(payType.value());
				if (Arrays.binarySearch(payTypeArray, value) < 0) {
					payTypeBean.setPayStatus(0);//设置为禁用
				} else {
					payTypeBean.setPayStatus(1);//设置为启用
				}
				payTypeBean_list.add(payTypeBean);
			}
			
			// 存入数据库
			for(int i = 0;i < payTypeBean_list.size();i++){
				CommonUtils.showLog(CommonUtils.TAG,"支付方式 = " + payTypeBean_list.get(i).getPayType() + ";status = " + payTypeBean_list.get(i).getPayStatus());
				insertPayTypeBean(payTypeBean_list.get(i));
			}
			_application.setPayTypeBean_list(payTypeBean_list);
		}
	}

	/**
	 * 售货机锁定，界面和维护线程（例如重启、更新等）均可锁定。一旦被锁定，另一方不可获得锁
	 * 
	 * @param 锁或者解锁
	 */
	public boolean lock(boolean lock) {
		if (lock) {
			try {
				_mutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			_mutex.release();
			return true;
		}
	}

	/**
	 * 售货机锁定，非阻塞形式，等待时间可设定
	 * 
	 * @param 等待时间
	 */
	public synchronized boolean tryLock(long msecs) {
		try {
			boolean tryLock = _mutex.attempt(msecs);
			return tryLock;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 机器初始化开始
	 */
	public void start() {

		_maintenanceThread.start();
		_heartBeatThread.start();
		// _posControl.initPos(HttpUrl.UNIONPAY_SVR_HOST, 6000);
		// _posControl.signinPos();
	}

	/**
	 * 机器出货
	 * 
	 * @param
	 */
	protected abstract void outGoods(BasePayControl basePayControl);

	public void refreshReceivedMoney(int money) {
		_receivedMoney = money;
	}

	/**
	 * 机器纸硬币器初始化
	 */
	protected abstract void coinInit();

	/**
	 * 机器纸币收币确认
	 * 
	 * @param
	 */
	protected abstract void coinConfirm(boolean confirm);

	/**
	 * 机器纸硬币器状态
	 */
	protected abstract void coinStatus();

	/**
	 * 机器纸币器打开
	 * 
	 * @param
	 */
	public abstract void coinOpen(int CFlag, int BFlag);

	/**
	 * 机器硬币器找零
	 * 
	 * @param
	 */
	public abstract void coinReturn(double amount);

	/**
	 * 机器硬币数量
	 */
	public abstract void coinQty();

	/**
	 * 获取机器出厂编码
	 */
	public abstract String getFactoryCode();
	/**
	 * 获取机器m_no
	 */
	public abstract String getM_No();
	/**
	 * 获取机器Mac地址
	 */
	public abstract String getMachineMac();

	/**
	 * 获取压缩机温度
	 */
	public abstract List<String> getTemperature();

	public String getNetworkMode() {
		return _networkMode;
	}

	public void setNetworkMode(String networkMode) {
		_networkMode = networkMode;
	}

	/**
	 * 获取3G卡信号
	 */
	public int getSignalStrength() {
		return _signalStrength;
	}

	/**
	 * 设置网络信号
	 */
	public void setSignalStrength(int strength) {
		_signalStrength = strength;
	}

	/**
	 * 获取开关门状态
	 */
	public boolean getCloseDoorStatus() {
		return _closeDoorStatus;
	}

	/**
	 * 设置开关门状态
	 */
	public void setCloseDoorStatus(boolean closeDoorStatus) {
		_closeDoorStatus = closeDoorStatus;
	}

	/**
	 * 从售货机查询开关门状态
	 */
	public abstract void refreshCloseDoorStatus();

	/**
	 * 查询选货信息（适用于物理按键选货）
	 * 
	 * 依次返回货柜，货道，价格信息
	 */
	public abstract List<String> checkSelectInfo();

	/**
	 * 获取纸硬币器状态 返回信息：状态，硬币0.5数量，硬币1.0数量，纸币数量
	 */
	public abstract List<String> getCoinNotesStatus();

	/**
	 * 读取客非卡号
	 */
	public void readCofficeCardNo(Handler handler) {
		_posControl.readCardNo(handler);
	}
	/**
	 * 获取app是否在前台
	 * @return
	 */
	public boolean isFrontView() {
		return isFrontView;
	}
	/**
	 * 设置app是否在前台
	 * @return
	 */
	public void setFrontView(boolean isFrontView) {
		this.isFrontView = isFrontView;
	}
	/**
	 * 判断点击物理是否可以跳转商品详情页面
	 * @return
	 */
	public boolean isRespondButton() {
		return respondButton;
	}
	/**
	 * 设置点击物理是否可以跳转商品详情页面
	 * @return
	 */
	public void setRespondButton(boolean respondButton) {
		this.respondButton = respondButton;
	}
	/**
	 * 中断POS读卡操作
	 */
	public int cancelPos() {
		return _posControl.cancelPos();
	}

	/**
	 * 重启POS机
	 */
	public int resetPos() {
		return _posControl.resetPos();
	}

	/**
	 * 测试料道是否正常
	 */
	public abstract boolean checkRoad(String wayId);

	/**
	 * 设置料道信息（适用于物理键机型，对应的app应重载之）
	 */
	public abstract boolean setRoad(Handler handler, final String wayId,
			boolean isValid, int price, int quantity);

	public void checkSinglechipInitStatus() {
		if (_isSinglechipStartSuccess) {
			initNetWork(Constant.BASE_URL, Constant.WECHAT_BASE_URL,
					Constant.ACTIVITY_BASE_URL);
			start();
		}
	}

	/**
	 * 获取NetWork对象
	 * 
	 * @return
	 */
	public NetWork getNetWork() {
		return mNetWork;
	}

	/**
	 * 获取DbHelper对象
	 * 
	 * @return
	 */
	public DbHelper getDbHelper() {
		return mDbHelper;
	}

	/**
	 * 是否第一次注册
	 */
	public boolean isFristRegister() {
		MachineBean mMachineBean = mDbHelper.selectMachineBean();
		if (mMachineBean.getM_code() == null
				|| mMachineBean.getM_code().equals(""))
			return true;
		else
			return false;
	}

	/**
	 * 注册声音service
	 */
	public void startAudionService() {
		//初始化音频control
		mAudioControl = AudioControl.getInstance();
	}

	/**
	 * 摧毁声音service
	 */
	public void destoryAudioService() {
		mAudioControl.stopSound();
	}


	/**
	 * 物理按钮 商品的信息
	 * 
	 * @return
	 */
	public SoldGoodsBean getSoldGoodsBean() {
		return mSoldGoodsBean;
	}

	public void setmSoldGoodsBean(SoldGoodsBean mSoldGoodsBean) {
		this.mSoldGoodsBean = mSoldGoodsBean;
	}

	/**
	 * 获取商品分类信息 饮料分类
	 * 
	 * @return
	 */
	public ArrayList<SoldGoodsBean> getGoodsBean_drink_list() {
		return mDbHelper.selectDrinkGoodsBean();
	}

	/**
	 * 获取商品分类信息 食品分类
	 * 
	 * @return
	 */
	public ArrayList<SoldGoodsBean> getGoodsBean_food_list() {
		return mDbHelper.selectFoodGoodsBean();
	}

	/**
	 * 获取商品分类信息 其他分类
	 * 
	 * @return
	 */
	public ArrayList<SoldGoodsBean> getGoodsBean_others_list() {
		return mDbHelper.selectOthersGoodsBean();
	}

	/**
	 * 物理按钮购买 针对 现金购买
	 * 
	 * @param way_id
	 */
//	public void button2shop(String way_id) {
//		GoodsBean mGoodsBean = mDbHelper.selectShopGoodsBean(way_id);
//		if (mGoodsBean != null)
//			startCashETPay(mGoodsBean);
//	}

	/**
	 * 获取未提交的现金订单
	 */
	public OrderBean[] selectCashOrder() {
		return mDbHelper.selectCashOrder();
	}

	/**
	 * 获取未提交的网络订单
	 */
	public OrderBean[] selectNetOrder() {
		return mDbHelper.selectNetOrder();
	}
	/**
	 * 获取未提交的微商城兑换商品
	 */
	public OrderBean[] selectCofficeShopOrder() {
		return mDbHelper.selectCofficeShopOrder();
	}

	/**
	 * 兑换接口
	 * 根据g_code 获取货道way_id
	 * 随机获取
	 */
	public String selectWay_id8g_codeRandom(String g_code) {
		return mDbHelper.selectWay_id8g_codeRandom(g_code);
	}
	
	/**
	 * 完成订单流程
	 */
	public void updateOrderBean(OrderBean mOrderBean) {
		mDbHelper.updateOrderBean(mOrderBean);
	}

	/**
	 * 出货后 递减库存
	 */
	public void updateWayBean(String way_id) {
		int rest_storeNum = 0;
		if(GoodsWayUtil.parseBox(way_id).equals(GoodsWayUtil.DRINKS_BOX_NAME))
			rest_storeNum = 1;
		mDbHelper.updateWayBean(way_id,rest_storeNum);
	}
	/**
	 * 根据货道id 获取库存量
	 * @param way_id
	 */
	public String getStore8Way_id(String way_id){
		return mDbHelper.getStrore8Way_id(way_id);
	}
	/**
	 * 判断是否能修改商品售卖状态
	 */
	public boolean checkSoldStatus(String g_code) {
		return mDbHelper.selectSoldStatus(g_code);
	}

	/**
	 * update p_state状态
	 */
	public void updateP_state(String client_order_no) {
		mDbHelper.updateP_state(client_order_no);
	}

	/**
	 * select SoldGoodsBean 根据货道号获取 SoldGoodsBean
	 * 
	 * @param w_code
	 * @return
	 */
	public SoldGoodsBean selectSoldGoodsStatus8w_code(String w_code) {
		SoldGoodsBean soldGoodsBean = mDbHelper.selectSoldGoodsBean8w_code(w_code);
		setmSoldGoodsBean(soldGoodsBean);
		return soldGoodsBean;
	}
	/**
	 * select SoldGoodsBean 根据g_code号获取 SoldGoodsBean
	 * 
	 * @param g_code
	 * @return
	 */
	public SoldGoodsBean selectSoldGoodsStatus8g_code(String g_code) {
		SoldGoodsBean soldGoodsBean = mDbHelper.selectSoldGoodsBean8g_code(g_code);
		setmSoldGoodsBean(soldGoodsBean);
		return soldGoodsBean;
	}

	/**
	 * select GoodsBean 根据货道号获取 GoodsBean
	 * 
	 * @param box
	 * @param road
	 * @return
	 */
	public SoldGoodsBean selectSoldGoodsBean(String w_code) {
		SoldGoodsBean soldGoodsBean = mDbHelper.selectSoldGoodsBean8w_code(w_code);
		return soldGoodsBean;
	}

	/**
	 * select MachineBean 获取机器code
	 * 
	 * @return
	 */
	public String selectMachineCode() {
		return mDbHelper.selectMachineCode();
	}

	/**
	 * select wayBean
	 * 
	 * @param w_code_list
	 * @return
	 */
	public ArrayList<Integer> selectBox(ArrayList<String> w_code_list) {
		return mDbHelper.selectBox(w_code_list);
	}

	/**
	 * 删除 货柜
	 * 
	 * @param list
	 */
	public void deleteBox(ArrayList<String> list) {
		mDbHelper.deleteWCode(list);
	}

	/**
	 * 获取所有机柜信息
	 * 
	 * @return
	 */
	public HashMap<Integer, ArrayList<String>> getWCode() {
		return GoodsWayUtil.getWCode(this);
	}

	/**
	 * 根据g_code获取price
	 * 
	 * @param g_code
	 * @return
	 */
	public Integer selectPrice(String g_code) {
		return mDbHelper.selectPrice(g_code);
	}

	/**
	 * 判断是否有货柜被选择
	 * 
	 * @return
	 */
	public boolean isExistCrate() {
		HashMap<String, WayBean> map = mDbHelper.selectWayBean();
		if (map.size() > 0)
			return true;
		else
			return false;
	}

	/**
	 * 获取 在售的货道信息 以及 商品信息
	 * 
	 * @return
	 */
	public ArrayList<SoldWayBean> selectSoldWayBeanList() {
		ArrayList<SoldWayBean> list = mDbHelper.selectSoldWayBean();
		return list;
	}

	/**
	 * 检测微商城兑换码
	 * 
	 * @param rcode
	 * @param m_code
	 */
	public void post_chk_rcode(String rcode) {
		mNetWork.post_chk_rcode(rcode);
	}
	/**
	 * 测试_检测微商城兑换码
	 * 
	 * @param rcode
	 * @param m_code
	 */
	public void test_post_chk_rcode(String rcode) {
		mNetWork.test_post_chk_rcode(rcode);
	}
	/**
	 * 微商城 
	 * 获取兑换码 对应的商品所有信息
	 * @param statusCheckRcode
	 * @return
	 */
	public StatusCheckRcode selectStatusCheckRcode(StatusCheckRcode statusCheckRcode){
		return mDbHelper.selectStatusCheckRcode(statusCheckRcode);
	}
	/**
	 * 微商城兑换
	 * 增加兑换记录
	 * @param order_gl_id
	 * @param g_id
	 * @param rid
	 */
	public void post_add_change_log(String order_gl_id,String g_id,String rid,String onumber){
		mNetWork.post_add_change_log(order_gl_id, g_id, rid,onumber);
	}
	/**
	 * 微商城兑换
	 * 退出兑换（解除锁定）
	 * @param rid
	 */
	public void post_quit_convert(String rid){
		mNetWork.post_quit_convert(rid);
	}
	/**
	 * 获取 模板
	 * @param mw_codes
	 */
	public void post_template(){
		ArrayList<WayBean> list = mDbHelper.selectWayBeanList();
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i < list.size();i++){
			WayBean wayBean = list.get(i);
			String decollator = ",";
			if(i == list.size() - 1)
				decollator = "";
			sb.append(GoodsWayUtil.parseBox(wayBean.getWay_id()) + decollator);
		}
		mNetWork.post_template(sb.toString());
	}
	public boolean isDoorOpen() {
		refreshCloseDoorStatus();
		return !_closeDoorStatus;
	}
	/**
	 * 播放音频
	 * @param sound_position
	 */
	public void startSound(int sound_position){
		mAudioControl.startSound(sound_position);
	}
	/**
	 * 退出应用
	 */
	public void exitApp(boolean stopSupervisor){
		if (stopSupervisor) {
			_supervisorControl.stopSupervisor();
		}
		
		_application.exit();
	}
	/**
	 * select PayTypeBean
	 * 获取所有支付方式状态
	 * @return
	 */
	public ArrayList<PayTypeBean> selectPayTypeBean(){
		ArrayList<PayTypeBean> list = mDbHelper.selectPayTypeStatus();
		setPayTypeBean(list);
		return list;
	}
	/**
	 * insert PayTypeBean
	 * 如果存在就修改状态，如果不存在就插入新数据
	 * @param payTypeBean
	 */
	public void insertPayTypeBean(PayTypeBean payTypeBean){
		mDbHelper.insertPayTypeStatus(payTypeBean);
	}
	
	public void setPayTypeBean(ArrayList<PayTypeBean> list){
		_application.setPayTypeBean_list(list);
	}
	public ArrayList<PayTypeBean> getPayTypeBean(){
		return _application.getPayTypeBean_list();
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
	 * 获取最后一次自动关闭app的时间
	 * @return
	 */
	public String getAutoRestartTime(){
		return mDbHelper.selectAutoRestartTime();
	}
	/**
	 * 获取最后一次非自动关闭app的时间
	 * @return
	 */
	public String getRestartTime(){
		return mDbHelper.selectRestartTime();
	}
	/**
	 * 插入最后一次自动关闭app的时间
	 * @param appRestarTime
	 */
	public void insertAppRestartTime(AppRestarTime appRestarTime){
		mDbHelper.insertAppRestartTime(appRestarTime);
	}
	/**
	 * insert RecordsRecovery
	 * 数据备份
	 * @param recordsRecovery
	 */
	public void insertRecordsRecovery(RecoveryRecordsBean recordsRecovery){
		mDbHelper.insertRecordsRecovery(recordsRecovery);
	}
	/**
	 * insert WayBeanRecovery
	 * 数据备份
	 * @param list
	 */
	public void insertWayBeanRecovery(ArrayList<WayBean> list){
		mDbHelper.insertWayBeanRecovery(list);
	}
	/**
	 * select WayBeanRecovery
	 * 数据备份
	 * @return
	 */
	public HashMap<String, WayBean> selectWayBeanRecovery(){
		 return mDbHelper.selectWayBeanRecovery();
	}
	/**
	 * select RecordsRecovery
	 * 数据备份
	 * @return
	 */
	public RecoveryRecordsBean selectRecordsRecovery(){
		return mDbHelper.selectRecordsRecovery();
	}
	/**
	 * 判断是否有数据未提交
	 * false:有数据未提交
	 * true:数据为空
	 * 
	 * 数据备份
	 * @return
	 */
	public boolean isTemplateRecordsRecoveryClear(){
		if(selectRecordsRecovery().getVersion() != null)
			return false;
		else
			return true;
	}
	/**
	 * delete WayBeanRecovery
	 * 数据备份
	 */
	public void deleteWayBeanRecovery(){
		mDbHelper.deleteWayBeanRecovery();
	}
	/**
	 * delete RecordsRecovery
	 * 数据备份
	 */
	public void deleteRecordsRecovery(){
		mDbHelper.deleteRecordsRecovery();
	}
	/**
	 * select 在售商品
	 * @return
	 */
	public HashMap<String, GoodsStateBean> selectSoldGoodsBean(){
		 return mDbHelper.selectSoldGoodsBean();
	}
	/**
	 * select WayBean表
	 * @return
	 */
	public HashMap<String, WayBean> selectWayBean(){
		 return mDbHelper.selectWayBean();
	}
	/**
	 * 模板数据库是否为空
	 * 商品销售状态表
	 * @return
	 */
	public boolean isTemplateGoodsStatusBeanClear(){
		HashMap<String, GoodsStateBean> map = mDbHelper.selectSoldGoodsBean();
		if(map.size() > 0)
			return false;
		else
			return true;
	}
	/**
	 * 模板数据库是否为空
	 * 货道最大库存量状态表
	 * @return
	 */
	public boolean isTemplateWayBeanClear(){
		ArrayList<WayBean> list = mDbHelper.selectWayBean8maxNum();
		if(list.size() > 0)
			return false;
		else
			return true;
	}
	/**
	 * 模板数据库是否为空
	 * 货道对应的商品是否为空
	 * false:不为空
	 * true:所有货道对应的商品为空
	 * @return
	 */
	public boolean isTemplateWayAndGoodsBeanClear(){
		return mDbHelper.selectWayBean8g_code();
	}
//	/**
//	 * 得到所有货道对应的库存
//	 * 服务器问客户端要数据
//	 * @return
//	 */
//	//获取所有库存量
//	public String getAllStoreNum(){
//		String str = "";
//		HashMap<String, WayBean> wayBean_map = mDbHelper.selectWayBean();
//		HashMap<String, Integer> map = new LinkedHashMap<String, Integer>();//key:货道id value:库存量
//		Iterator<Entry<String, WayBean>> obj = wayBean_map.entrySet().iterator();
//		while(obj.hasNext()){
//			Entry<String, WayBean> item = obj.next();
//			map.put(item.getValue().getWay_id(), item.getValue().getStoreNum());
//		}
//		try{
//			net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(map);
//			str = array.toString();
//		}catch(Exception e){
//			
//		}
//		return str;
//	}
	/**
	 * 根据服务器的sql语句查询本地数据库的数据
	 * 服务器问客户端要数据
	 * @param sql
	 * @return
	 */
	public String analysisCursor(String sql){
		String str = "";
		if(CommonUtils.isStrInString(sql, "ALTER") || CommonUtils.isStrInString(sql, "INSERT") || CommonUtils.isStrInString(sql, "DELETE") || CommonUtils.isStrInString(sql, "UPDATE") || CommonUtils.isStrInString(sql, "DROP"))
		{
			CommonUtils.showLog(CommonUtils.ICE_TAG, "输入非select sql语句，禁止访问");
			return str;
		}
		try{
			ArrayList<HashMap<String,String>> list = mDbHelper.selectDataBase(sql);
			CommonUtils.showLog(CommonUtils.ICE_TAG, "sql查询结果 ArrayList<HashMap<String,String>> = " + list.toString());
			StringBuffer sb = new StringBuffer();
			for(int i = 0;i < list.size();i++){
				HashMap<String, String> map = list.get(i);
				Iterator<Entry<String, String>> obj = map.entrySet().iterator();
				if(i == 0)
					sb.append("[");
				sb.append("{");
				while(obj.hasNext()){
					Entry<String, String> item = obj.next();
					sb.append( "\"" + item.getKey() + "\":\"" + item.getValue() + "\",");
				}
				sb.replace(sb.length() - 1, sb.length(), "");
				sb.append("}");
				if(i < (list.size() - 1))
					sb.append(",");
				if(i == (list.size() - 1))
					sb.append("]");
			}
			str = sb.toString();
		}catch(Exception e){
			CommonUtils.showLog(CommonUtils.ICE_TAG, "sql查询结果 exception " + e.toString());
		}
		CommonUtils.showLog(CommonUtils.ICE_TAG, "sql查询结果 str = " + str);
		return str;
	}
	
	/**
	 * sim卡信息
	 * 服务器问客户端要数据
	 */
	public String analysisSIM(){
		String str = "";
		HashMap<String,String> map = new LinkedHashMap<String, String>();
		map.put("DeviceId", mSIMControl.getDeviceId());
		map.put("Line1Number", mSIMControl.getLine1Number());
		map.put("SimSerialNumber", mSIMControl.getSimSerialNumber());
		map.put("SubscriberId", mSIMControl.getSubscriberId());
		try{
			org.json.JSONObject obj =  new org.json.JSONObject(map);
			str = obj.toString();
		}catch(Exception e){
			
		}
		return str;
	}
	/**
	 * 删除缓存
	 * 本地日志缓存
	 */
	public void deleteCache(){
		CommonUtils.showLog(CommonUtils.TAG, "删除日志");
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String current_time = formatter.format(new Date());
		int current_year = Integer.parseInt(current_time.subSequence(0, 4).toString());
		int current_month = Integer.parseInt(current_time.subSequence(5, 7).toString());
		int current_day = Integer.parseInt(current_time.subSequence(8, 10).toString());
		ArrayList<String> list = new ArrayList<String>();
		list = mFileUtil.getTxtFromSDcard(list,  FileUtil.ROOT_PATH + FileUtil.SECOND_CACH_PATH);
		for(int i = 0;i < list.size();i++){
			String root_path = list.get(i);
			CommonUtils.showLog(CommonUtils.TAG, "日志 = " + root_path);
			if(root_path.contains("_log_cach.txt")){
				int path_index = root_path.lastIndexOf("/");
				String path = root_path.substring(path_index + 1, root_path.length());
				int index = path.indexOf("_");
				String time = path.substring(0, index);
				
				try {
					if((System.currentTimeMillis() - new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime()) > (7 * 24 * 60 * 60 * 1000)){
						boolean deleteFileStatus = mFileUtil.DeleteFolder(root_path);
						CommonUtils.showLog(CommonUtils.TAG,  deleteFileStatus + "");
					}else{
						
					}
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 重启工控机
	 */
	public void SystemReboot(){
		MachineUtil.SystemReboot();
	}
	/**
	 * 通过服务器重启app
	 */
	public void exitAppByNet(){
		new Thread(new Runnable() {
			boolean exit(){
				if (lock(true)) {
					exitApp(false);
					lock(false);
				}else{
					return false;
				}
				return true;
			}
			@Override
			public void run() {
				for(;;){
					if(exit()){
						break;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}).start();
	}
	/**
	 * 通过服务器重启app
	 */
	public void exitRebootByNet(){
		new Thread(new Runnable() {
			boolean exit(){
				if (lock(true)) {
					SystemReboot();
					lock(false);
				}else{
					return false;
				}
				return true;
			}
			@Override
			public void run() {
				for(;;){
					if(exit()){
						break;
					}else{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}).start();
	}
	/**
	 * 获取机器设置信息
	 * @return
	 */
	public ArrayList<String> getMachineSetStatus(){
		return mDbHelper.selectMachineSetStatus();
	}
	/**
	 * 获取雷云峰机器设置信息
	 * @return
	 */
	public ArrayList<Integer> getLYMachineSetStatus(){
		return mDbHelper.selectLYMachineSetStatus();
	}
	/**
	 * 修改
	 * CoinAndnoteStatus的状态
	 * @param status
	 */
	public void updateCoinAndnoteStatus(String status){
		mDbHelper.updateCoinAndnoteStatus(status);
	}
	/**
	 * 修改
	 * CoinWaitTime的状态
	 * @param status
	 */
	
	public void updateCoinWaitTime(String time){
		mDbHelper.updateCoinWaitTime(time);
	}
	/**
	 * 修改
	 * CoinWaitTime的状态
	 * @param status
	 */
	
	public void updatePhysicsButtonStatus(String physicsButtonStatus){
		mDbHelper.updatePhysicsButtonStatus(physicsButtonStatus);
	}
	/**
	 * 雷云峰
	 * 修改
	 * door的状态
	 * @param status
	 */
	
	public void updateDoorStatus(String doorStatus){
		mDbHelper.updateDoorStatus(doorStatus);
	}
	/**
	 * 雷云峰
	 * 修改
	 * elevator的状态
	 * @param status
	 */
	
	public void updateElevatorStatus(String elevatorStatus){
		mDbHelper.updateElevatorStatus(elevatorStatus);
	}
	/**
	 * 获取首页广告接口
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<AdStatusBean> getAdStatusBeanList(){
		ArrayList<AdStatusBean> success_adStatusBean_list = new ArrayList<AdStatusBean>();
		success_adStatusBean_list = selectAdStatusBean();
		if(success_adStatusBean_list.size() > 0)
		{
			ArrayList<AdStatusBean> existAdStatusBean_list = new ArrayList<AdStatusBean>();//本地已经存在的广告
			for(int i = 0; i < success_adStatusBean_list.size();i++){
				AdStatusBean mAdStatusBean = success_adStatusBean_list.get(i);
				if(mFileUtil.isFileExist(FileUtil.ROOT_PATH + mAdStatusBean.getFile()) && mAdStatusBean.getDownload() == 1)
				{
					CommonUtils.showLog(CommonUtils.AD_TAG, "存在的广告 " + FileUtil.ROOT_PATH + mAdStatusBean.getFile());
					existAdStatusBean_list.add(mAdStatusBean);
				}
			}
			downLoadAd(success_adStatusBean_list);
			return existAdStatusBean_list;
		}
		else
		{
			ArrayList<String> list = new ArrayList<String>();
			list = mFileUtil.getAdFromSDcard(list, FileUtil.ROOT_PATH + FileUtil.SECOND_AD_PATH);
			for(int i = 0;i < list.size();i++){
				AdStatusBean adStatusBean = new AdStatusBean();
				adStatusBean.setAid(1);
				adStatusBean.setAtId(1);
				adStatusBean.setFile(list.get(i).replace(FileUtil.ROOT_PATH, ""));
				adStatusBean.setSmallImage(list.get(i).replace(FileUtil.ROOT_PATH, ""));
				adStatusBean.setInterval(7);
				adStatusBean.setStartDate("");
				adStatusBean.setEndDate("");
				adStatusBean.setgCode("");
				success_adStatusBean_list.add(i, adStatusBean);
			}
			return success_adStatusBean_list;
		}
	}
	
	/**
	 * 下载活动图片
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<AdStatusBean> getSelectActivityAdStatusBean(){
		ArrayList<AdStatusBean> success_adStatusBean_list = new ArrayList<AdStatusBean>();
		success_adStatusBean_list = selectActivityAdStatusBean();
		if(success_adStatusBean_list==null){
			CommonUtils.showLog(CommonUtils.DOWNLOAD_TAG, "---活动下载-getSelectActivityAdStatusBean---list==null");
			return null; 
		}
		if(success_adStatusBean_list.size() > 0)
		{
			ArrayList<AdStatusBean> existAdStatusBean_list = new ArrayList<AdStatusBean>();//本地已经存在的广告
			for(int i = 0; i < success_adStatusBean_list.size();i++){
				AdStatusBean mAdStatusBean = success_adStatusBean_list.get(i);
				if(mFileUtil.isFileExist(FileUtil.ROOT_PATH + mAdStatusBean.getFile()) && mAdStatusBean.getDownload() == 1)
				{
					CommonUtils.showLog(CommonUtils.AD_TAG, "存在的活动 --- " + FileUtil.ROOT_PATH + mAdStatusBean.getFile());
					existAdStatusBean_list.add(mAdStatusBean);
				}
			}
			downLoadAd(success_adStatusBean_list);
			return existAdStatusBean_list;
		}
		else
		{
			ArrayList<String> list = new ArrayList<String>();
			list = mFileUtil.getAdFromSDcard(list, FileUtil.ROOT_PATH + FileUtil.SECOND_ACTICITY_AD_PATH);
			for(int i = 0;i < list.size();i++){
				AdStatusBean adStatusBean = new AdStatusBean();
				adStatusBean.setAid(1);
				adStatusBean.setAtId(1);
				adStatusBean.setFile(list.get(i).replace(FileUtil.ROOT_PATH, ""));
				adStatusBean.setSmallImage(list.get(i).replace(FileUtil.ROOT_PATH, ""));
				adStatusBean.setInterval(7);
				adStatusBean.setStartDate("");
				adStatusBean.setEndDate("");
				adStatusBean.setgCode("");
				success_adStatusBean_list.add(i, adStatusBean);
			}
			return success_adStatusBean_list;
		}
	}
	
	
	/**
	 *获取首页广告信息
	 * @return
	 */
	public ArrayList<AdStatusBean> selectAdStatusBean(){
		return mDbHelper.selectAdStatusBean();
	}
	/**
	 * 获取所有广告信息
	 * @return
	 */
	public ArrayList<AdStatusBean> selectAllAdStatusBean(){
		return mDbHelper.selectAllAdStatusBean();
	}
	/**
	 * 获取活动广告信息
	 * @return
	 */
	public ArrayList<AdStatusBean> selectActivityAdStatusBean(){
		return mDbHelper.selectActivityAdStatusBean();
	}
	
	/**
	 *  获取微信广告接口
	 */
	public ArrayList<String> getWechatAdList() {
		ArrayList<String> list = new ArrayList<String>();
		list = mFileUtil.getImgFromSDcard(list, FileUtil.ROOT_PATH
				+ FileUtil.SECOND_WECHAT_AD_PATH);

		return list;
	}
	
	/**
	 * 插入广告信息
	 * @param adStatusBean_list
	 */
	public void insertAdStatusBean(ArrayList<AdStatusBean> adStatusBean_list){
		mDbHelper.insertAdStatusBean(adStatusBean_list);
	}
	/**
	 * 下载广告
	 */
	public void downLoadAd(final ArrayList<AdStatusBean> adStatusBean_list){
		final ArrayList<String> listURL = new ArrayList<String>();
		final ArrayList<AdStatusBean> fail_adStatusBean_list = new ArrayList<AdStatusBean>();
		final ArrayList<AdStatusBean> success_adStatusBean_list = new ArrayList<AdStatusBean>();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0; i < adStatusBean_list.size();i++){
					AdStatusBean mAdStatusBean = adStatusBean_list.get(i);
					if(mFileUtil.isFileExist(FileUtil.ROOT_PATH + mAdStatusBean.getFile()) && CommonUtils.md5sum(FileUtil.ROOT_PATH + mAdStatusBean.getFile()).equals(mAdStatusBean.getFileMd5()))
					{
						CommonUtils.showLog(CommonUtils.AD_TAG, "下载 存在的广告 " + FileUtil.ROOT_PATH + mAdStatusBean.getFile());
						success_adStatusBean_list.add(mAdStatusBean);
					}else
					{
						CommonUtils.showLog(CommonUtils.AD_TAG, "下载 不存在的广告 " + FileUtil.ROOT_PATH + mAdStatusBean.getFile());
						listURL.add(mAdStatusBean.getFile());
						fail_adStatusBean_list.add(mAdStatusBean);
					}
					String smallImagePath = mAdStatusBean.getSmallImage();
					if(!smallImagePath.equals("") && smallImagePath != null && !smallImagePath.equals("null"))
					{
						if(mFileUtil.isFileExist(FileUtil.ROOT_PATH + mAdStatusBean.getSmallImage()) && CommonUtils.md5sum(FileUtil.ROOT_PATH + mAdStatusBean.getSmallImage()).equals(mAdStatusBean.getSmallImageMd5()))
						{
							CommonUtils.showLog(CommonUtils.AD_TAG, "下载 存在的缩略图 " + FileUtil.ROOT_PATH + mAdStatusBean.getSmallImage());
						}else
						{
							CommonUtils.showLog(CommonUtils.AD_TAG, "下载 不存在的缩略图 " + FileUtil.ROOT_PATH + mAdStatusBean.getSmallImage());
							listURL.add(mAdStatusBean.getSmallImage());
						}
					}
				}
				for(int i = 0;i < success_adStatusBean_list.size();i++){
					updateAdStatusBean(success_adStatusBean_list.get(i));
				}
				new DownloadService(FileUtil.ROOT_PATH,FileUtil.SECOND_AD_PATH, listURL, new DownloadStateListener() {
					
					@Override
					public void onSuccess() {
						CommonUtils.showLog(CommonUtils.AD_TAG, "下载成功");
					}
					
					@Override
					public void onFinish() {
						CommonUtils.showLog(CommonUtils.AD_TAG, "下载结束");
						ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
						list.add(fail_adStatusBean_list);
						Bundle mBundle = new Bundle();
						mBundle.putParcelableArrayList("list", list);
						CommonUtils.sendMessage(mHttpHandler, BaseHttpHandler.DOWNLOAD_AD_SUCCESS, mBundle);
					}
					
					@Override
					public void onFailed(int code, Exception e, String errorMsg) {
						CommonUtils.showLog(CommonUtils.AD_TAG, "下载异常 code = " + code + ";Exception = " + e.toString() + ";errorMsg = " + errorMsg);
						
					}
				}).startDownload();
				
			}
		}).start();
			
	}
	
	/**
	 * 服务器发送出货指令
	 * @param client_order_no
	 * @return
	 */
	public String serverOutGoods(String client_order_no){
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "服务器出货 client_order_no = " + client_order_no);
		OrderBean mOrderBean = mDbHelper.selectOrderBean(client_order_no);
		if(mOrderBean == null)
			return "商品订单不存在";
		else{
			if(mOrderBean.getO_state() == 1){
				return "商品已经出货";
			}else if(mOrderBean.getO_state() == -1 || mOrderBean.getO_state() == 3){
				SoldGoodsBean soldGoodsBean = selectSoldGoodsStatus8g_code(mOrderBean.getG_code());
				if(normalSold(soldGoodsBean)){
					try {
						CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"服务器出货 way_id = " + soldGoodsBean.getW_code() + " 剩余库存为 : " + getStore8Way_id(soldGoodsBean.getW_code()));
						mOrderBean.setW_code(soldGoodsBean.getW_code());
						startServicePay(soldGoodsBean,mOrderBean);
						return "商品准备出货";
					} catch (Exception e) {
						return "商品出货异常";
					}
				}else{
					return "商品出货失败\n库存还剩" + soldGoodsBean.getStoreNum();
				}
			}
			return "商品卡货";
		}
	}
	/**
	 * update adStatusBean里的下载状态为成功（1）
	 * @param adStatusBean
	 */
	public void updateAdStatusBean(AdStatusBean adStatusBean){
		mDbHelper.updateAdStatusBean(adStatusBean);
	}
	/**
	 * 商品是否能正常购买
	 * @param soldGoodsBean
	 * @return
	 */
	public boolean normalSold(SoldGoodsBean soldGoodsBean){
		 //TODO checkroad 记录到日志，作为该货道状态判断依据
		if(soldGoodsBean == null)
			return false;
		String w_code = GoodsWayUtil.getW_code(soldGoodsBean.getW_code());
		boolean checkRoad = checkRoad(w_code);
				
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"soldGoodsBean.getStoreNum() = "+ soldGoodsBean.getStoreNum() + ";checkRoad = "+ checkRoad);
		int rest_storeNum = 0;
		if(GoodsWayUtil.parseBox(soldGoodsBean.getW_code()).equals(GoodsWayUtil.DRINKS_BOX_NAME) || GoodsWayUtil.parseBox(soldGoodsBean.getW_code()).equals(GoodsWayUtil.DRINKS_2_BOX_NAME))
			rest_storeNum = 1;
		CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "rest_storeNum = " + rest_storeNum);
		if (soldGoodsBean.getStoreNum() <= rest_storeNum || !checkRoad) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 根据模糊查询 获取本地订单号
	 * @param shamClient_order_no
	 * @return
	 */
	public String selectClient_order_no(String shamClient_order_no){
		return mDbHelper.selectClient_order_no(shamClient_order_no);
	}
	/**
	 * 创建本地文件夹
	 */
	public void createFile(){
		mFileUtil.createFile();
	}
	/**
	 * 提交网络支付的订单
	 */
	public void netOrderComplete(){
		OrderBean[] array = selectNetOrder();
		
		if(array != null){
			Map[] map = new Map[array.length];
			for(int i = 0;i < array.length;i++){
				OrderBean mOrderBean = array[i];
				Map<String, String> m = new HashMap<String, String>();
				Integer o_state = mOrderBean.getO_state();
				if(o_state == 0)
					o_state = 3;
				m.put("clientOrderNo", mOrderBean.getClient_order_no());
				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
				m.put("completeTime", mOrderBean.getO_complete_time());
				m.put("state", o_state + "");
				map[i] = m;
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"支付方式: " + mOrderBean.getPay_type() + " 订单提交服务器 = " + "map." + i + " = " + m.toString());
			}
//			for(int i = 0;i < map.length;i++){
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"网络支付订单提交服务器 = " + "map." + i + " = " + map[i].toString());
//			}
			if(map.length > 0)
				_coordinator.orderComplete(map);
			else
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"网络支付订单提交服务器 = 0" );
		}
	}
	/**
	 * 提交微商城，兑换的订单
	 */
	public void cofficeShopOrderComplete(){
		OrderBean[] array = selectCofficeShopOrder();
		
		if(array != null){
			Map[] map = new Map[array.length];
			for(int i = 0;i < array.length;i++){
				OrderBean mOrderBean = array[i];
				Map<String, String> m = new HashMap<String, String>();
				Integer o_state = mOrderBean.getO_state();
				if(o_state == 0)
					o_state = 3;
				m.put("clientOrderNo", mOrderBean.getClient_order_no());
				m.put("gCode", mOrderBean.getG_code());
				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
				m.put("generateTime", mOrderBean.getO_complete_time());
				m.put("state", o_state + "");
				m.put("unitPrice", mOrderBean.getO_unit_price() + "");
				m.put("totalPrice", mOrderBean.getO_unit_price() + "");
				m.put("sNumber", mOrderBean.getExpand());
				map[i] = m;
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"微商城兑换订单提交服务器 = " + "map." + i + " = " + m.toString());
			}
//			for(int i = 0;i < map.length;i++){
//				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"微商城兑换订单提交服务器 = " + "map." + i + " = " + map[i].toString());
//			}
			if(map.length > 0 )
				_coordinator.createOrderCofficeShop(map);
			else
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"微商城，兑换的订单提交服务器 = 0" );
		}
	}
	/**
	 * 提交现金支付的订单
	 */
	public void cashOrderComplete(){
		OrderBean[] array = selectCashOrder();
		if(array != null){
			Map[] map = new Map[array.length];
			for(int i = 0;i < array.length;i++){
				OrderBean mOrderBean = array[i];
				Map<String, String> m = new HashMap<String, String>();
				Integer o_state = mOrderBean.getO_state();
				if(o_state == 0)
					o_state = 3;
				m.put("clientOrderNo", mOrderBean.getClient_order_no());
				m.put("gCode", mOrderBean.getG_code());
				m.put("wCode", GoodsWayUtil.getWayName(mOrderBean.getW_code()));
				m.put("generateTime", mOrderBean.getO_generate_time());
				m.put("unitPrice", mOrderBean.getO_unit_price() + "");
				m.put("totalPrice", mOrderBean.getTotal_price() + "");
				m.put("state", o_state + "");
				m.put("cashIn", mOrderBean.getCashIn() + "");
				m.put("cashOut", mOrderBean.getCashOut() + "");
				map[i] = m;
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金订单提交服务器 = " + "map." + i + " = " + m.toString());
			}
//			for(int i = 0;i < map.length;i++){
//				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金订单提交服务器 = " + "map." + i + " = " + map[i].toString());
//			}
			if(map.length > 0)
				_coordinator.createOrderCash(map);
			else
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG,"现金的订单提交服务器 = 0" );
		}
	}
	/**
	 * 获取雷云峰机器的 门和云台的设置状态
	 * @return
	 */
	public LYMachineSetBean selectLYMachineSetBean(){
		return mDbHelper.selectLYMachineSetBean();
	}
	/**
	 * 根据way_id获取货道类型
	 * @param way_id
	 * @return
	 */
	public String selectWayType(String way_id){
		return mDbHelper.selectWayType(way_id);
	}
	public abstract void VMC_Elevator(String location);
	public abstract void setAutoCloseDoor(boolean doorStatus);
	public abstract Refrigerator getmRefrigerator();
}
