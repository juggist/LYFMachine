package com.icofficeapp.activity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.icoffice.library.activity.BaseActivity;
import com.icoffice.library.backactivity.BackstageActivity;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.bean.network.StatusAddChangeLogStatus;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.bean.network.StatusQuitConvert;
import com.icoffice.library.callback.ConnectNetCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.AdTimerTaskControl;
import com.icoffice.library.moudle.control.AdTimerTaskControl.ShowAdViewInterface;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.moudle.control.BasePayControl;
import com.icoffice.library.moudle.control.CardPayControl;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.GoodsWayUtil;
import com.icoffice.library.utils.ParseJSON;
import com.icoffice.library.widget.CommentDialogUtils;
import com.icofficeapp.R;
import com.icofficeapp.activity.AdFragment.ChangeViewInterface;
import com.icofficeapp.app.ICofficeApplication;
import com.icofficeapp.control.LYMachineControl;
import com.icofficeapp.handler.ActivityHandler;
import com.icofficeapp.handler.BaseHandler;
import com.icofficeapp.handler.ExchangeDetailHandler;
import com.icofficeapp.handler.ExchangeHandler;
import com.icofficeapp.handler.MainFragmentHandler;
import com.icofficeapp.handler.ShopHandler;
import com.icofficeapp.handler.WeChatHandler;
import com.icofficeapp.util.AnimaUtils;
import com.icofficeapp.widget.WaveView;

@SuppressLint({ "CommitTransaction", "HandlerLeak" })
public class BaseFragmentActivity extends BaseActivity implements
		ChangeViewInterface,ShowAdViewInterface {

	public BaseMachineControl mMachineControl;
	public ICofficeApplication iCofficeApplication;
	public AdTimerTaskControl mAdTimerTaskControl;

	protected int currentView;// 0_广告位 1_内页
	private int currentAd = 0;//当前播放的广告页面（断点播放）

	private MyReceiver receiver;// 接受开关门的广播
//	private boolean isAlive;// 判断是否在展示
	

	protected AdFragment mAdFragment;
	protected MainFragment mMainFragment;

	private CommentDialogUtils dialog;
	private int dialogReturnCount = 20;
	private ScheduledExecutorService mExecutorService;
	private TextView tv_returnTask;

	
	public Handler mMainFragmentHandler;
	private ConnectNetCallBack mConnectNetCallBack;//检测网络链接状态接口
	
	public ShopFragment mShopFragment;
	public ActivityFragment mActivityFragment;
	public WeChatFragment mWeChatFragment;
	public ExchangeFragment mExchangeFragment;
	public ExchangeDetailFragment mExchangeDetailFragment;
	
	/**
	 * 购买
	 */
	private Runnable shopRunnale;//维护所有操作到runnable
	private ShopHandler shopHandler;//维护所有操作hanlder
	private int shopTimer;//维护所有操作的倒计时
	/**
	 * 活动
	 * @param mContext
	 */
	private Runnable activityRunnable;//维护活动操作runnable
	private ActivityHandler activityHandler;//维护活动操作hanlder
	private int activityTimer;//维护活动操作的倒计时
	/**
	 * 微商城
	 * @param mContext
	 */
	private Runnable weChatShopRunnable;//维护微商城操作runnable
	private WeChatHandler weChatShopHandler;//维护微商城操作hanlder
	private int weChatShopTimer;//维护微商城操作的倒计时
	/**
	 * 兑换
	 * @param mContext
	 */
	private Runnable exchangeRunnable;//维护兑换操作runnable
	private ExchangeHandler exchangeHandler;//维护兑换操作hanlder
	private int exchangeTimer;//维护兑换操作的倒计时
	
	private  int icofficeState = -1;//用于判断是否是客非卡支付方式 5时是客非卡   可进行延伸
	
	private ShowExchangeInterface mShowExchangeInterface;
	/**
	 * 兑换详情
	 * @param mContext
	 */
	private Runnable exchangeDetailRunnable;//维护兑换详情操作runnable
	private ExchangeDetailHandler exchangeDetailHandler;//维护兑换详情操作hanlder
	private int exchangeDetailTimer;//维护兑换详情操作的倒计时
	
	private String icofficePayfail;//客非卡支付情况失败情况说明
	private String icofficeBalnace;//客非卡支付情况失败情况说明
	private String icofficeStute;//客非卡支付情况状态说明
	
	private String service_outgoods_tv;//服务器出货信息
	
	private CardPayControl cardPayControl;//客非卡传递数据对象
	public BaseViewHandler mViewHandler = new BaseHandler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RefreshShopTimer:
				mBundle = msg.getData();
				shopTimer = mBundle.getInt("shopTimer");
				break;
			case BaseHandler.DIALOG_FLAG:
				tv_returnTask.setText(dialogReturnCount + "秒");
				break;
			case BaseViewHandler.OutGoodsSuccess://出货成功
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "出货成功提示");
				mBundle = msg.getData();
//				int paye_typeSuccess = bundleSuccess.getInt("pay_type");
				mBasePayControl = (BasePayControl) mBundle.getSerializable("PayControl");
				paye_type = mBasePayControl.getPayType();
				icofficeState = -1;
				Log.e("", ""+paye_type);
				if(paye_type == 6){
					
					try{
						mExchangeDetailFragment.refresh();
					}catch(Exception e){
						
					}
					setExchangeDetailFinishTimer( ExchangeDetailHandler.EXCHANGEDETAIL_2_SHOP_TIME);
				}else if (paye_type == 5) {
					icofficeState = 5;
					 cardPayControl = (CardPayControl) mBasePayControl;
					SoldGoodsBean soldGoodsBean = mMachineControl.selectSoldGoodsStatus8g_code(cardPayControl.getmOrderBean().getG_code());
					if(mMachineControl.normalSold(soldGoodsBean) && mMainFragment != null){
						mMainFragment.payAgain(mBasePayControl,soldGoodsBean);
					}else{
						shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.INIT_DETAIL, 0);
					}
				}else{
					SoldGoodsBean soldGoodsBean = mMachineControl.selectSoldGoodsStatus8g_code(mBasePayControl.getmOrderBean().getG_code());
					if(mMachineControl.normalSold(soldGoodsBean) && mMainFragment != null){
						mMainFragment.payAgain(mBasePayControl,soldGoodsBean);
					}else{
						shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.INIT_DETAIL, 0);
					}
				}
				startDialogTimerTask(CommentDialogUtils.DIALOG_BUYSUCCESS_FLAG,5);
				break;
			case BaseViewHandler.OutGoodsFail://出货失败
				CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "出货失败提示");
				mBundle = msg.getData();
//				int paye_typeFail = bundleFail.getInt("pay_type");
				mBasePayControl = (BasePayControl) mBundle.getSerializable("PayControl");
				paye_type = mBasePayControl.getPayType();
				if(paye_type == 6){
					try{
						mExchangeDetailFragment.refresh();
					}catch(Exception e){
						
					}
					setExchangeDetailFinishTimer( ExchangeDetailHandler.EXCHANGEDETAIL_2_SHOP_TIME);
				}else{
					shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.INIT_DETAIL, 0);
					
				}
				startDialogTimerTask(CommentDialogUtils.DIALOG_BUYFAIL_FLAG, 5);
				break;
			case BaseViewHandler.CONNECT_NET_FAIL://链接网络失败
				startDialogTimerTask(
						CommentDialogUtils.DIALOG_CONNECT_NET_FAIL_FLAG, 5);
				break;
			case BaseViewHandler.CONNECT_NET_SUCCESS://网络链接成功
				mBundle = msg.getData();
				mConnectNetCallBack.connectSuccess(mBundle.getInt("payType"));
				break;
			case BaseViewHandler.GOODS_SOLD_SELL_OUT://商品出售完
				startDialogTimerTask(
						CommentDialogUtils.DIALOG_GOODS_SELL_OUT_FLAG, 5);
				break;
			case BaseViewHandler.AVM_STATUS_FAIL://单片机状态error
				startDialogTimerTask(
						CommentDialogUtils.DIALOG_AVM_STATUS_FAIL_FLAG, 5);
				break;
			case BaseViewHandler.IS_LOCKED://被锁定
				startDialogTimerTask(CommentDialogUtils.DIALOG_IS_LOCKED, 5);
				break;
			case POST_CHECK_RCODE_SUCCESS://检测微商城兑换码成功
				result = (String) msg.getData().getString("result");
				StatusCheckRcode statusCheckRcode = ParseJSON.parseStatusCheckRcode(result);
				if(statusCheckRcode.getCode().equals("1") || statusCheckRcode.getCode().equals("2")){
					dismissExchangeDialog();
					mShowExchangeInterface.showExchange(statusCheckRcode);
				}else {
					showExchangeInfoDialog(Integer.parseInt(statusCheckRcode.getCode()),statusCheckRcode.getMsg(), 5);
				}
				break;
			case POST_CHECK_RCODE_FAIL://检测微商城兑换码失败
				startDialogTimerTask(CommentDialogUtils.DIALOG_CONNECT_NET_FAIL_FLAG, 5);
				result = (String) msg.getData().getString("result");
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"POST_CHECK_RCODE_FAIL = " + result);
				break;
			case POST_ADD_CHANGE_LOG_SUCCESS://检测微商城,增加兑换记录成功
				result = (String) msg.getData().getString("result");
				StatusAddChangeLogStatus statusAddChangeLogStatus = ParseJSON.parseStatusAddChangeLogStatus(result);
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"add_log_msg success = " + statusAddChangeLogStatus.getMsg());
				break;
			case POST_ADD_CHANGE_LOG_FAIL://检测微商城,增加兑换记录失败
				result = (String) msg.getData().getString("result");
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"add_log_msg fail = " + result);
				break;
			case POST_QUIT_CONVERT_SUCCESS://检测微商城,退出兑换（解除锁定）成功
				result = (String) msg.getData().getString("result");
				StatusQuitConvert statusQuitConvert = ParseJSON.parseStatusQuitConvert(result);
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"quit_convert : code = " + statusQuitConvert.getCode() + ";msg = " + statusQuitConvert.getMsg());
				break;
			case POST_QUIT_CONVERT_FAIL://检测微商城,退出兑换（解除锁定）失败
				CommonUtils.showLog(CommonUtils.EXCHANGE_TAG,"quit_convert :connect Fail ");
				break;
			case BaseViewHandler.GOODS_RCODE_OVER://商品领取完
				startDialogTimerTask(
						CommentDialogUtils.DIALOG_GOODS_RCODE_OVER_FLAG, 5);
				break;
			case BaseViewHandler.WAIT:
				startDialogTimerTask(
						CommentDialogUtils.DIALOG_WAIT_FLAG, 5);
				break;
			case POST_TEST_CHECK_RCODE_SUCCESS://测试_检测微商城兑换码成功
				StatusCheckRcode test_statusCheckRcode = ParseJSON.test_parseStatusCheckRcode("");
				if(test_statusCheckRcode.getCode().equals("1")){
					dismissExchangeDialog();
					mShowExchangeInterface.showExchange(test_statusCheckRcode);
				}else {
					showExchangeInfoDialog(Integer.parseInt(test_statusCheckRcode.getCode()),test_statusCheckRcode.getMsg(), 5);
				}
				break;
				
			case ICOFFICECARD_PAY_FAIL:
				 mBundle = msg.getData();
				 
				icofficePayfail =  mBundle.getString("msg");
				icofficeBalnace  = mBundle.getString("balance");
				icofficeStute  = mBundle.getString("status");
				
				startDialogTimerTask(CommentDialogUtils.DIALOG_PAY_FAIL, 5);
				break;
			case CommentDialogUtils.DIALOG_PAY_SUCC:
				
				break;
			case CommentDialogUtils.DIALOG_SERVICE_OUTGOODS:
				 mBundle = msg.getData();
				service_outgoods_tv = mBundle.getString("result");
				startDialogTimerTask(CommentDialogUtils.DIALOG_SERVICE_OUTGOODS, 5);
				break;
			case LEIYUN_DOOROPEN_FLAG:
				if (mMachineControl.isFrontView())
					startActivity(new Intent(BaseFragmentActivity.this,BackstageActivity.class));
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	public BaseFragmentActivity() {
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {

		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		iCofficeApplication = (ICofficeApplication) getApplication();
		mMachineControl = LYMachineControl.getInstance(this);
		mMachineControl.initViewHandler(mViewHandler);
		mMachineControl.mRootUtil.hideStatusBar();
		mMachineControl.startAudionService();
		mAdTimerTaskControl = AdTimerTaskControl.getInstance(this);
	}

	@Override
	protected void onDestroy() {
		mMachineControl.mRootUtil.showStatusBar();
		mMachineControl.destoryAudioService();
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
//		isAlive = true;
		mMachineControl.refreshCloseDoorStatus();
		mMachineControl.setFrontView(true);
		if (receiver == null) {
			IntentFilter doorFilter = new IntentFilter();
			receiver = new MyReceiver();
			doorFilter.addAction(ICofficeApplication.ETOUCH_DOOR_STATE);
			doorFilter.addAction(ICofficeApplication.ETOUCH_OUT_GOODS);
			doorFilter.addAction(ICofficeApplication.ETOUCH_SELECT_GOODS);
			doorFilter.addAction(ICofficeApplication.ETOUCH_RECEIVE_MONEY);
			doorFilter.addAction(ICofficeApplication.ETOUCH_AVM_DISCONNECT);
			registerReceiver(receiver, doorFilter);
		}
		super.onResume();
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	protected void onStop() {
//		isAlive = false;
		mMachineControl.setFrontView(false);
		super.onStop();
	}
	
	public void showAdView() {
		mMachineControl.setRespondButton(true);
		mMachineControl.lock(false);
		currentView = 0;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mAdFragment = new AdFragment(this,this);
		ft.replace(R.id.container, mAdFragment);
		try {
			ft.commit();
		} catch (Exception ex) {

		}

	}
	public void showMainView(int BUY_TYPE){
		if(BUY_TYPE == ChangeViewInterface.PAY_SCREEN)
			mMachineControl.startSound(6);
		currentView = 1;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		mMainFragment = new MainFragment(this, mMachineControl, BUY_TYPE);
		ft.replace(R.id.container, mMainFragment);
		ft.commit();
	}
	public void showMainViewInterface(int BUY_TYPE) {
		if(currentView == 1){
			showMainView(BUY_TYPE);
		}else if(currentView == 0){
			if(isUnlocked()){
				showMainView(BUY_TYPE);
			}
		}
	}


	/**
	 * dialog展示
	 * 
	 * @author lufeisong
	 * 
	 */

	void startDialogTimerTask(int DIALOG_FLAG, int time) {
		dialogReturnCount = time;
		dismissExchangeDialog();
		switch (DIALOG_FLAG) {
		case CommentDialogUtils.DIALOG_BUYFAIL_FLAG:
			mMachineControl.startSound(10);
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_buyfail, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_BUYSUCCESS_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_buysuccess, R.style.MyDialog);
			
			if(icofficeState==5){
				TextView tv_balance = (TextView) dialog.findViewById(R.id.dialog_tv_outgoods_success);
				if(cardPayControl!=null){
					if(cardPayControl.getBalance()!=null&&!"".equals(cardPayControl.getBalance())){
						String privce = CommonUtils.priceExchange(cardPayControl.getBalance());
						tv_balance.setText("客非卡当前余额："+privce+"元");
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "----客非卡支付成功--显示余额");
					}
				}
			}
			
			break;
		case CommentDialogUtils.DIALOG_CONNECT_NET_FAIL_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_connect_net_fail, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_GOODS_SELL_OUT_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_goods_sold_finish, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_GOODS_PRICE_ERROR_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_goods_price_error, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_AVM_STATUS_FAIL_FLAG:
			mMachineControl.startSound(17);
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_avm_status_fail, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_IS_LOCKED:
			mMachineControl.startSound(17);
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_is_locked, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_GOODS_RCODE_OVER_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_rcode_over, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_WAIT_FLAG:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_wait, R.style.MyDialog);
			break;
		case CommentDialogUtils.DIALOG_PAY_SUCC:
			
			break;
			
		case CommentDialogUtils.DIALOG_PAY_FAIL:
			dialog = new CommentDialogUtils(this, 384, 477,
					R.layout.dialog_icoffice_buy_fail, R.style.MyDialog);
			TextView failMegTv = (TextView) dialog.findViewById(R.id.dialog_icoffice_buy_fail);
			
			String yuan_icofficeBalnace  = "";//装换单位
			if("605".equals(icofficeStute)){
				
				if(icofficeBalnace!=null&&!"".equals(icofficeBalnace)){
					 yuan_icofficeBalnace = CommonUtils.priceExchange(icofficeBalnace);
					failMegTv.setText(icofficePayfail+"，当前余额"+yuan_icofficeBalnace);
				}
				
			}if("608".equals(icofficeStute)){
				failMegTv.setText("已超出每日可支付额度");
			}if("609".equals(icofficeStute)){
				if(icofficeBalnace!=null&&!"".equals(icofficeBalnace)){
					 yuan_icofficeBalnace = CommonUtils.priceExchange(icofficeBalnace);
				}
				failMegTv.setText("余额不足，当前剩余金额"+yuan_icofficeBalnace);
			}else {
				failMegTv.setText(icofficePayfail);	
			}
			shopTimerTask(ShopHandler.PAY_ICOFFICE_FLAG,ShopHandler.PAYAGAIN_2_DETAIL, 0);
			break;
			
		case CommentDialogUtils.DIALOG_SERVICE_OUTGOODS:
			dialog = new CommentDialogUtils(this, 384, 477,R.layout.dialog_service_outgoods, R.style.MyDialog);
			TextView serviceOutgoodsTv = (TextView) dialog.findViewById(R.id.dialog_service_outgoods_tip);
			serviceOutgoodsTv.setText(service_outgoods_tv + "");
			break;
		default:
			break;
		}
		ImageButton iBtn_exit = (ImageButton) dialog
				.findViewById(R.id.dialog_ibtn_exit);
		tv_returnTask = (TextView) dialog.findViewById(R.id.dialog_tv_return);
		dialog.show();
		iBtn_exit.setOnClickListener(dialogExitListener());
		if (mExecutorService != null)
			mExecutorService.shutdownNow();
		mExecutorService = Executors.newSingleThreadScheduledExecutor();
		mExecutorService.scheduleAtFixedRate(new DialogTask(), 0, 1,
				TimeUnit.SECONDS);

	}
	/**
	 * 微商城 兑换 dialog
	 */
//	private ImageView iv_video, iv_one,iv_two,iv_three,iv_four,iv_five,iv_six,iv_seven;
	private ImageView iv_video;
	private WaveView waveView;
	AnimaUtils animaUtils;
	void showExchangeDialog(){
		dismissExchangeDialog();
		dialog = new CommentDialogUtils(this,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				R.layout.dialog_exchange, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(false);
		exchangeDialogViewInit(dialog);
		dialog.show();
	}
	private void exchangeDialogViewInit(CommentDialogUtils dialog) {
		waveView = (WaveView) dialog.findViewById(R.id.dialog_exchange_music_ani);
		iv_video = (ImageView) dialog.findViewById(R.id.dialog_exchange_video);
		waveView.start();
		AnimaUtils animaUtils = new AnimaUtils();
		animaUtils.startVideoAnimal(iv_video);
		
	}

	/**  
	 * 微商城 兑换 反馈信息dialog
	 */
	void showExchangeInfoDialog(int DIALOG_FLAG,String dialog_str,int time){
		dialogReturnCount = time;
		dismissExchangeDialog();
		dialog = new CommentDialogUtils(this, 384, 477,R.layout.dialog_exchangedetail, R.style.MyDialog);
		ImageButton iBtn_exit = (ImageButton) dialog.findViewById(R.id.dialog_ibtn_exit);
		TextView tv_03 = (TextView) dialog.findViewById(R.id.dialog_exchangedetail_exchange_03);
		tv_returnTask = (TextView) dialog.findViewById(R.id.dialog_tv_return);
		tv_03.setText(dialog_str);
		dialog.show();
		iBtn_exit.setOnClickListener(dialogExitListener());
		if (mExecutorService != null)
			mExecutorService.shutdownNow();
		mExecutorService = Executors.newSingleThreadScheduledExecutor();
		mExecutorService.scheduleAtFixedRate(new DialogTask(), 0, 1,TimeUnit.SECONDS);
	}
	void dismissExchangeDialog(){
		try{
			if (dialog != null){
				if(waveView!=null){
					waveView.stop();
				}
				dialog.dismiss();
			}
		}catch(Exception e){
			
		}
		
	}
	
	class DialogTask implements Runnable {

		@Override
		public void run() {
			if (dialogReturnCount <= 0) {
				dialog.dismiss();
				mExecutorService.shutdownNow();
			} else {
				dialogReturnCount--;
				CommonUtils.sendMessage(mViewHandler, BaseHandler.DIALOG_FLAG, "");
			}
		}
	}

	View.OnClickListener dialogExitListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialogReturnCount = 0;
			}
		};
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			synchronized (this) {
				CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "接受到广播");
				String action = intent.getAction();
				if (ICofficeApplication.ETOUCH_DOOR_STATE.equals(action)) {
					// 获得开关门信息
					mMachineControl.refreshCloseDoorStatus();
					if (mMachineControl.getCloseDoorStatus()) {
						// 关门
						CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "单片机发送广播 = 关门");
					} else {
						// 开门
						CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "单片机发送广播 = 开门");
						if (mMachineControl.isFrontView())
							startActivity(new Intent(BaseFragmentActivity.this,
									BackstageActivity.class));
					}
				} else if (ICofficeApplication.ETOUCH_OUT_GOODS.equals(action)) {
					// 获取出货判断
					synchronized (this) {
						CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "单片机发送广播 = 出货完成，判断出货状态");
						mMachineControl.checkOutGoodsResult();
					}
				} else if (ICofficeApplication.ETOUCH_SELECT_GOODS.equals(action)) {
					// 物理按钮选货 如果有投入的钱，那么不会发送广播。
					List<String> info = mMachineControl.checkSelectInfo();

					String box = info.get(0);
					String road = info.get(1);
					String price = info.get(2);
					CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "单片机发送广播 = 物理按钮 " + "select_info: box=" + box + ";road=" + road + ";price=" + price);
					// TODO
					if (mMachineControl.isRespondButton()) {
						String wayId = GoodsWayUtil.genWayId(box, road);
						SoldGoodsBean soldGoodsBean = mMachineControl.selectSoldGoodsStatus8w_code(wayId);
						if(mMachineControl.normalSold(soldGoodsBean)){
							try{
								CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "物理按钮 way_id = " + soldGoodsBean.getW_code() + " 剩余库存为 : " + mMachineControl.getStore8Way_id(soldGoodsBean.getW_code()));
								showMainViewInterface(ChangeViewInterface.PAY_BUTTON);
							}catch(Exception e){
								
							}
						}else{
							
						}
					}
				} else if (ICofficeApplication.ETOUCH_RECEIVE_MONEY.equals(action)) {
					// 纸硬币器收币
					int total = intent.getIntExtra("receive_total_money", 0);
					int notes = intent.getIntExtra("receive_paper_money", 0);
					int coins = intent.getIntExtra("receive_coin_money", 0);
					synchronized (this) {
						CommonUtils.showLog(CommonUtils.BROADCAST_TAG, "单片机发送广播 = 接受现金 " + total);
						mMachineControl.refreshReceivedMoney(total);
					}
				} else if (ICofficeApplication.ETOUCH_AVM_DISCONNECT.equals(action)) {
					// 通讯中断
				}
			}
		}
	}
	/**
	 * 点击网络支付方式
	 * 判断网络链接状态
	 * 
	 * @param payType 
	 */
	public void isConnectNet(final int payType) {
		final int mPayType = payType;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int payType = mPayType;
				if (!mMachineControl.getIceServerStatus()) {
					mMachineControl.startSound(7);
					CommonUtils.sendMessage(mViewHandler,
							BaseViewHandler.CONNECT_NET_FAIL, "");
				} else{
					Bundle mBundle = new Bundle();
					mBundle.putInt("payType", payType);
					CommonUtils.sendMessage(mViewHandler,
							BaseViewHandler.CONNECT_NET_SUCCESS, mBundle);
				}
			}
		}).start();
	}

	public boolean isDoorOpen() {
		if (mMachineControl.isDoorOpen()) {
			CommonUtils.sendMessage(mViewHandler,
					BaseViewHandler.AVM_STATUS_FAIL, "");
			CommonUtils.showLog(CommonUtils.TAG,"Door is open");
			return true;
		} else
			return false;
	}

	public boolean isAvmRunning() {
		if (!mMachineControl.getAvmStatus()) {
			CommonUtils.sendMessage(mViewHandler,
					BaseViewHandler.AVM_STATUS_FAIL, "");
			return false;
		} else
			return true;
	}
	public boolean isUnlocked() {
		if (!mMachineControl.tryLock(0)) {
			CommonUtils.showLog(CommonUtils.TAG,"the view unable to lock machine");
			CommonUtils.sendMessage(mViewHandler,BaseViewHandler.IS_LOCKED, "");
			return false;
		} else{
			CommonUtils.showLog(CommonUtils.TAG,"the view locked machine");
			return true;
		}
			
	}
	public void isWait(){
		CommonUtils.sendMessage(mViewHandler,BaseViewHandler.WAIT, "");
	}
	public void setmMainFragmentHandler(Handler mainFragmentHandler) {
		mMainFragmentHandler = mainFragmentHandler;
	}
	public void setmMainFragment(MainFragment mainFragment) {
		mMainFragment = mainFragment;
	}
	public void setmAdFragment(AdFragment adFragment) {
		mAdFragment = adFragment;
	}
	public void setmShopFragment(ShopFragment shopFragment) {
		this.mShopFragment = shopFragment;
	}
	public void setmActivityFragment(ActivityFragment activityFragment) {
		mActivityFragment = activityFragment;
	}
	public void setmWeChatFragment(WeChatFragment weChatFragment) {
		mWeChatFragment = weChatFragment;
	}
	public void setmExchangeFragment(ExchangeFragment exchangeFragment) {
		mExchangeFragment = exchangeFragment;
	}
	public void setmExchangeDetailFragment(ExchangeDetailFragment exchangeDetailFragment) {
		mExchangeDetailFragment = exchangeDetailFragment;
	}
	public void setConnectNetCallBack(ConnectNetCallBack connectNetCallBack){
		mConnectNetCallBack = connectNetCallBack;
	}
	/**
	 * 维护购买操作计时器
	 */
	public void shopTimerTask(final int PAY_ACTION,final int ACTION_FLAG,int timer){
		cancleShopTask();
		shopTimer = timer;
		shopHandler = new ShopHandler();
		shopRunnale = new Runnable() {
			
			@Override
			public void run() {
				if(shopTimer > 0){
					if(PAY_ACTION == ShopHandler.PAY_ICOFFICE_FLAG || PAY_ACTION == ShopHandler.PAY_WECHAT_FLAG || PAY_ACTION == ShopHandler.PAY_ALIPAY_FLAG)
						if(shopTimer == 15)
							mMachineControl.startSound(19);
					shopTimer--;
					Bundle bundle = new Bundle();
					bundle.putInt(MainFragmentHandler.TASK_TIMER, shopTimer);
					CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.TASK_FLAG, bundle);
					shopHandler.postDelayed(shopRunnale, 1000);
				}else{
					switch(ACTION_FLAG){
					case ShopHandler.INIT_DETAIL:
						try{
							CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.REFRESH_FLAG, "");
							cancleShopTask();
						}catch(Exception e){
							CommonUtils.showLog(CommonUtils.TAG,"exception = " + e.toString());
						}
						break;
					case ShopHandler.DETAIL_2_FINISH:
						CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.SHOP_DETAIL_FLAG, "");
						cancleShopTask();
						break;
					case ShopHandler.CHOOSEPAYTYPE_2_DETAIL:
						mShopFragment.task2detail();
						break;
					case ShopHandler.PAYTYPE_2_DETAIL:
						mShopFragment.task2detail();
						break;
					case ShopHandler.PAYAGAIN_2_DETAIL:
						mShopFragment.task2detail();
						break;
					default:
						break;
					}
				}
			}
		};
		shopHandler.postDelayed(shopRunnale, 1000);
	}
	public void setShopFinishTimer() {
		this.shopTimer = 0;
	}
	public void cancleShopTask(){
		if(shopHandler != null && shopRunnale != null)
			shopHandler.removeCallbacks(shopRunnale);
	}
	/**
	 * 维护活动操作计时器
	 */
	public void activityTimerTask(final int ACTIVITY_FLAG,int timer){
		cancleActivityTask();
		activityTimer = timer;
		activityHandler = new ActivityHandler();
		activityRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(activityTimer > 0){
					activityTimer--;
					Bundle bundle = new Bundle();
					bundle.putInt(MainFragmentHandler.TASK_TIMER, activityTimer);
					CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.TASK_FLAG, bundle);
					activityHandler.postDelayed(activityRunnable, 1000);
				}else{
					switch(ACTIVITY_FLAG){
					case ActivityHandler.ACTIVITY_2_SHOP:
						mMainFragment.Return2Shop();
						break;
					default:
						break;
					}
				}
				
			}
		};
		activityHandler.postDelayed(activityRunnable, 1000);
	}
	public void setActivityFinishTimer() {
		this.activityTimer = 0;
	}
	public void cancleActivityTask(){
		if(activityHandler != null && activityRunnable != null)
			activityHandler.removeCallbacks(activityRunnable);
	}
	/**
	 * 维护微商城操作计时器
	 */
	public void weChatShopTimerTask(final int ACTIVITY_FLAG,int timer){
		cancleWeChatShopTask();
		weChatShopTimer = timer;
		weChatShopHandler = new WeChatHandler();
		weChatShopRunnable = new Runnable() {
			@Override
			public void run() {
				if(weChatShopTimer > 0){
					weChatShopTimer--;
					Bundle bundle = new Bundle();
					bundle.putInt(MainFragmentHandler.TASK_TIMER, weChatShopTimer);
					CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.TASK_FLAG, bundle);
					weChatShopHandler.postDelayed(weChatShopRunnable, 1000);
				}else{
					switch(ACTIVITY_FLAG){
					case WeChatHandler.WECHATSHOP_2_SHOP:
						mMainFragment.Return2Shop();
						break;
					default:
						break;
					}
				}
				
			}
		};
		weChatShopHandler.postDelayed(weChatShopRunnable, 1000);
	}
	public void setWeChatShopFinishTimer() {
		this.weChatShopTimer = 0;
	}
	public void cancleWeChatShopTask(){
		if(weChatShopHandler != null && weChatShopRunnable != null)
			weChatShopHandler.removeCallbacks(weChatShopRunnable);
	}
	/**
	 * 维护兑换操作计时器
	 */
	public void exchangeTimerTask(final int ACTIVITY_FLAG,int timer){
		cancleActivityTask();
		exchangeTimer = timer;
		exchangeHandler = new ExchangeHandler();
		exchangeRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(exchangeTimer > 0){
					exchangeTimer--;
					Bundle bundle = new Bundle();
					bundle.putInt(MainFragmentHandler.TASK_TIMER, exchangeTimer);
					CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.TASK_FLAG, bundle);
					exchangeHandler.postDelayed(exchangeRunnable, 1000);
				}else{
					switch(ACTIVITY_FLAG){
					case ExchangeHandler.EXCHANGE_2_SHOP:
						mMainFragment.Return2Shop();
						break;
					default:
						break;
					}
				}
				
			}
		};
		exchangeHandler.postDelayed(exchangeRunnable, 1000);
	}
	public void setExchangeFinishTimer(int time) {
		this.exchangeTimer = time;
	}
	public void cancleExchangeTask(){
		if(exchangeHandler != null && exchangeRunnable != null)
			exchangeHandler.removeCallbacks(exchangeRunnable);
	}
	public void setShowExchangeInterface(ShowExchangeInterface showExchangeInterface){
		mShowExchangeInterface = showExchangeInterface;
	}
	/**
	 * 维护兑换详情操作计时器
	 */
	public void exchangeDetailTimerTask(final int ACTIVITY_FLAG,int timer){
		cancleActivityTask();
		exchangeDetailTimer = timer;
		exchangeDetailHandler = new ExchangeDetailHandler();
		exchangeDetailRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(exchangeDetailTimer > 0){
					exchangeDetailTimer--;
					Bundle bundle = new Bundle();
					bundle.putInt(MainFragmentHandler.TASK_TIMER, exchangeDetailTimer);
					CommonUtils.sendMessage(mMainFragmentHandler,MainFragmentHandler.TASK_FLAG, bundle);
					exchangeDetailHandler.postDelayed(exchangeDetailRunnable, 1000);
				}else{
					switch(ACTIVITY_FLAG){
					case ExchangeDetailHandler.EXCHANGEDETAIL_2_EXCHANGE:
						mMainFragment.Return2Exchange();
						break;
					default:
						break;
					}
				}
				
			}
		};
		exchangeDetailHandler.postDelayed(exchangeDetailRunnable, 1000);
	}
	public void setExchangeDetailFinishTimer(int time) {
		this.exchangeDetailTimer = time;
	}
	public void cancleExchangeDetailTask(){
		if(exchangeDetailHandler != null && exchangeDetailRunnable != null)
			exchangeDetailHandler.removeCallbacks(exchangeDetailRunnable);
	}

	public int getCurrentAd() {
		return currentAd;
	}

	public void setCurrentAd(int currentAd) {
		this.currentAd = currentAd;
	}
	
	/**
	 * 接口回调
	 * 展示广告模块
	 */
	@Override
	public void showAd() {
		showAdView();
	}
	/**
	 * 接口
	 * 切换到兑换模块
	 * @author lufeisong
	 *
	 */
	public interface ShowExchangeInterface {
		void showExchange(StatusCheckRcode statusCheckRcode);
	}

}
