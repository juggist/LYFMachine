package com.icofficeapp.activity;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.bean.network.StatusCheckRcode;
import com.icoffice.library.handler.ClockHandler;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.moudle.control.BasePayControl;
import com.icoffice.library.moudle.control.ColckControl;
import com.icoffice.library.utils.ApkUtil;
import com.icoffice.library.utils.DisplayUtil;
import com.icofficeapp.R;
import com.icofficeapp.activity.AdFragment.ChangeViewInterface;
import com.icofficeapp.activity.BaseFragmentActivity.ShowExchangeInterface;
import com.icofficeapp.app.ICofficeApplication;
import com.icofficeapp.control.LYMachineControl;
import com.icofficeapp.handler.MainFragmentHandler;
import com.icofficeapp.util.AnimaUtils;
/**
 * 主界面
 * @author lufeisong
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class MainFragment extends BaseFragment implements OnClickListener,ShowExchangeInterface{
	private BaseFragmentActivity mContext;
	private BaseMachineControl mMachineControl;
	
	private AnimaUtils animaUtils;//动画工具类
	private ColckControl mColckUtil;
	private String DATE_FORMAT_BLANK = "%02d %02d";//时间显示
	private ShopFragment mShopFragment;
	
	//menu view
	private ImageView iv_all,iv_drink,iv_food,iv_others;
	//theme view
	private ImageView iv_buy,iv_activity,iv_wechat,iv_exchange;
	//return_task_tv
	private LinearLayout ll_task,ll_time;
	private TextView tv_task_01,tv_clock_01,tv_clock_02,tv_clock_03,tv_clock_04,tv_version;
	private TextView tv_clock_point;
	private boolean timePointShow = false;
	private int BUY_TYPE;
	private ImageView topView,smokeView;
	
	private final int IV_ALL_FLAG = 0;//全部
	private final int IV_DRINK_FLAG = 1;//饮料
	private final int IV_FOOD_FLAG = 2;//食品
	private final int IV_OTHERS_FLAG = 3;//其他
	
	private final int FRAGMENT_SHOP_FLAG = 0;//购买
	private final int FRAGMENT_ACTIVITY_FLAG = 1;//活动
	private final int FRAGMENT_WECHAT_FLAG = 2;//微商城
	private final int FRAGMENT_EXCHANGE_FLAG = 3;//兑换
	private int clockFlag = 0;//闹钟标示
	private boolean isShowTime = false;
	
//	private ArrayList<ImageView> menu_list = new ArrayList<ImageView>();//menu list
	private ArrayList<SoldGoodsBean> mGoodsBean_list = new ArrayList<SoldGoodsBean>();//当前选择的商品分类集合
	
	private Handler mMainFragmentHandler = new MainFragmentHandler(){

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MainFragmentHandler.INIT_FLAG:
				int THEME_FLAG_INIT = msg.getData().getInt("THEME_FLAG");
				mGoodsBean_list.clear();
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
				changeFragment(THEME_FLAG_INIT);
				break;
			case MainFragmentHandler.REFRESH_FLAG:
				BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
				int THEME_FLAG_REFRESH = msg.getData().getInt("THEME_FLAG");
				mGoodsBean_list.clear();
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
				mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
				changeFragment(THEME_FLAG_REFRESH);
				break;
			case MainFragmentHandler.SHOP_DETAIL_FLAG:
				ll_task.setVisibility(View.INVISIBLE);
				ll_time.setVisibility(View.VISIBLE);
				break;
			case MainFragmentHandler.TASK_FLAG:
				ll_task.setVisibility(View.VISIBLE);
				ll_time.setVisibility(View.INVISIBLE);
				bundle = msg.getData();
				int task_timer = bundle.getInt(MainFragmentHandler.TASK_TIMER);
				tv_task_01.setText(task_timer + "");
				break;
//			case MainFragmentHandler.COLCK_FLAG:
//				Bundle bundle = msg.getData();
//				tv_clock_01.setText(bundle.getString("AM_PM"));
//				tv_clock_03.setText(bundle.getString("day"));
//				tv_clock_04.setText(bundle.getString("type"));
//				tv_clock_02.setText(String.format(DATE_FORMAT, bundle.getInt("hour"),bundle.getInt("minutes")));
//				break;
			}
			super.handleMessage(msg);
		}
	};
	private Handler clockHandler = new ClockHandler(){

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case COLCK_FLAG:
				isShowTime = true;
				Bundle bundle = msg.getData();
				tv_clock_01.setText(bundle.getString("AM_PM"));
				tv_clock_03.setText(bundle.getString("day"));
				tv_clock_04.setText(bundle.getString("type"));
				tv_clock_02.setText(String.format(DATE_FORMAT_BLANK, bundle.getInt("hour"),bundle.getInt("minutes")));
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	public MainFragment(){
		
	}
	public MainFragment(BaseFragmentActivity context,BaseMachineControl mMachineControl,int BUY_TYPE){
		mContext = context;
		this.mMachineControl = mMachineControl;
		this.BUY_TYPE = BUY_TYPE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main, null);
		initView(view);
		timePiontJump();
		initListener();
		startFristAnimation();
		mMainFragmentHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				initData();
			}
		}, 800);
	
		return view;
	}
	private void startFristAnimation() {
		animaUtils = new AnimaUtils();
		float height = DisplayUtil.dip2px(mContext, 258);
		animaUtils.translateDown(topView, 500, -height,0f,-30,0f);
		animaUtils.smokeDiffusionAnima(smokeView, 300, 300);
		initMenuSelected(IV_ALL_FLAG);
		
	}
	void initView(View view){
		tv_version = (TextView) view.findViewById(R.id.fragment_shop_version_tv);
		//menu view
		iv_all = (ImageView) view.findViewById(R.id.fragment_shop_iv_all);
		iv_drink = (ImageView) view.findViewById(R.id.fragment_shop_iv_drink);
		iv_food = (ImageView) view.findViewById(R.id.fragment_shop_iv_food);
		iv_others = (ImageView) view.findViewById(R.id.fragment_shop_iv_others);
		//theme view
		iv_buy = (ImageView) view.findViewById(R.id.fragment_shop_theme_iv_buy);
		iv_activity = (ImageView) view.findViewById(R.id.fragment_shop_theme_iv_activity);
		iv_wechat = (ImageView) view.findViewById(R.id.fragment_shop_theme_iv_wechat);
		iv_exchange = (ImageView) view.findViewById(R.id.fragment_shop_theme_iv_exchange);
		//return_task view
		ll_task = (LinearLayout) view.findViewById(R.id.fragment_shop_theme_task);
		ll_time = (LinearLayout) view.findViewById(R.id.fragment_shop_theme_time);
		
		tv_task_01 = (TextView) view.findViewById(R.id.return_task_tv_01);
		tv_clock_01 = (TextView) view.findViewById(R.id.clock_tv_01);
		tv_clock_02 = (TextView) view.findViewById(R.id.clock_tv_02);
		tv_clock_point = (TextView) view.findViewById(R.id.clock_tv_point);
		tv_clock_03 = (TextView) view.findViewById(R.id.clock_tv_03);
		tv_clock_04 = (TextView) view.findViewById(R.id.clock_tv_04);
		
		topView = (ImageView) view.findViewById(R.id.fragment_shop_top_view);
		smokeView = (ImageView) view.findViewById(R.id.fragment_shop_smoke);
		
	}
	//时间点的跳动
	private void timePiontJump() {
		tv_clock_point.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(timePointShow){
					timePointShow  = false;
					tv_clock_point.setVisibility(View.VISIBLE);
				}else {
					timePointShow = true;
					tv_clock_point.setVisibility(View.GONE);
				}
				timePiontJump();
			}
		}, 500);
		
	}
	
	void initListener(){
		iv_all.setOnClickListener(this);
		iv_drink.setOnClickListener(this);
		iv_food.setOnClickListener(this);
		iv_others.setOnClickListener(this);
		iv_buy.setOnClickListener(this);
		iv_activity.setOnClickListener(this);
		iv_wechat.setOnClickListener(this);
		iv_exchange.setOnClickListener(this);
	}
	void initData(){
		

		tv_version.setText(ApkUtil.getVerNumber(mContext));
		ll_task.setVisibility(View.INVISIBLE);
		ll_time.setVisibility(View.VISIBLE);

//		menu_list.add(iv_all);
//		menu_list.add(iv_drink);
//		menu_list.add(iv_food);
//		menu_list.add(iv_others);
		
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("THEME_FLAG", FRAGMENT_SHOP_FLAG);
		msg.setData(bundle);
		msg.what = MainFragmentHandler.INIT_FLAG;
		mMainFragmentHandler.sendMessage(msg);
		
		mColckUtil = ColckControl.getInstance(mContext);
		mColckUtil.start(clockHandler);
		
		mContext.setShowExchangeInterface(this);
		
	}
	@SuppressLint("NewApi")
	void changeFragment(final int THEME_FLAG){
				FragmentTransaction ft = null;
				ft = getChildFragmentManager().beginTransaction();
				if(FRAGMENT_SHOP_FLAG == THEME_FLAG){
					mShopFragment = new ShopFragment(mContext,mGoodsBean_list,BUY_TYPE);
					ft.replace(R.id.fragment_main_contain, mShopFragment);
				}else if(FRAGMENT_ACTIVITY_FLAG == THEME_FLAG){
					ft.replace(R.id.fragment_main_contain, new ActivityFragment(mContext));
				}else if(FRAGMENT_WECHAT_FLAG == THEME_FLAG){
					ft.replace(R.id.fragment_main_contain, new WeChatFragment(mContext));
				}else if(FRAGMENT_EXCHANGE_FLAG == THEME_FLAG){
					ft.replace(R.id.fragment_main_contain, new ExchangeFragment(mContext));
				}
				try{
					ft.commitAllowingStateLoss();
				}catch(Exception e){
					
				}
		
	}
	
	
	//初始化menu菜单
	void initMenuSelected(int IV_FLAG){
		stopMenuAnimation();
		if(IV_ALL_FLAG==IV_FLAG){
			if(oa_allAnima==null){
				oa_allAnima = animaUtils.menuShopView(iv_all, 800);
			}else {
				oa_allAnima.start();
			}
		}if(IV_DRINK_FLAG==IV_FLAG){
			if(oa_drinkAnima==null){
				oa_drinkAnima = animaUtils.menuShopView(iv_drink, 800);
			}else {
				oa_drinkAnima.start();
			}
		}if(IV_FOOD_FLAG==IV_FLAG){
			if(oa_foodAnima==null){
				oa_foodAnima = animaUtils.menuShopView(iv_food, 800);
			}else {
				oa_foodAnima.start();	
			}
			
		}if(IV_OTHERS_FLAG==IV_FLAG){
			if(oa_otherAnima==null){
				oa_otherAnima = animaUtils.menuShopView(iv_others, 800);
			}else {
				oa_otherAnima.start();
			}
		}
		
		
		
//		for(int i = 0;i < menu_list.size();i++){
//			if(i == IV_FLAG){
//				stopMenuAnimation();
//				animaUtils.menuShopView(menu_list.get(i), 800);
//				
//			}else{
////				menu_list.get(i).setSelected(false);
//			}
//		}
	}
	//从微商城，活动，兑换 跳转到 购买
	public void Return2Shop(){
		BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
		changeFragment(FRAGMENT_SHOP_FLAG);
	}
	//退回到兑换
	public void Return2Exchange(){
		changeFragment(FRAGMENT_EXCHANGE_FLAG);
	}
	//Theme 动画对象
	private ObjectAnimator oa_buyAnima,oa_activityAnima,oa_wechatAnima,oa_exchangeAnima;
	//menu 动画对象
	private ObjectAnimator oa_allAnima,oa_drinkAnima,oa_foodAnima,oa_otherAnima;
	/**
	 * 用于停止动画 向动画设置为初始值
	 */
	private void stopThemeAnimation(){
		if(oa_buyAnima!=null){
			iv_buy.clearAnimation();
			oa_buyAnima.setCurrentPlayTime(0);
			oa_buyAnima.cancel();
		}if(oa_activityAnima!=null){
			iv_activity.clearAnimation();
			oa_activityAnima.setCurrentPlayTime(0);
			oa_activityAnima.cancel();
		}if(oa_wechatAnima!=null){
			iv_wechat.clearAnimation();
			oa_wechatAnima.setCurrentPlayTime(0);
			oa_wechatAnima.cancel();
		}if(oa_exchangeAnima!=null){
			iv_exchange.clearAnimation();
			oa_exchangeAnima.setCurrentPlayTime(0);
			oa_exchangeAnima.cancel();
		}
	}
	/**
	 * 停止侧面动画
	 */
	private void stopMenuAnimation(){
		if(oa_allAnima!=null){
			iv_all.clearAnimation();
			oa_allAnima.setCurrentPlayTime(0);
			oa_allAnima.cancel();
		}if(oa_drinkAnima!=null){
			iv_drink.clearAnimation();
			oa_drinkAnima.setCurrentPlayTime(0);
			oa_drinkAnima.cancel();
		}if(oa_foodAnima!=null){
			iv_food.clearAnimation();
			oa_foodAnima.setCurrentPlayTime(0);
			oa_foodAnima.cancel();
		}if(oa_otherAnima!=null){
			iv_others.clearAnimation();
			oa_otherAnima.setCurrentPlayTime(0);
			oa_otherAnima.cancel();
		}
	}
		
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.fragment_shop_iv_all:
			initMenuSelected(IV_ALL_FLAG);
			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
			mGoodsBean_list.clear();
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
			changeFragment(FRAGMENT_SHOP_FLAG);
			break;
		case R.id.fragment_shop_iv_drink:
			initMenuSelected(IV_DRINK_FLAG);
			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
			mGoodsBean_list.clear();
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
			changeFragment(FRAGMENT_SHOP_FLAG);
					
			break;
		case R.id.fragment_shop_iv_food:
			initMenuSelected(IV_FOOD_FLAG);
			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
			mGoodsBean_list.clear();
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
			changeFragment(FRAGMENT_SHOP_FLAG);
			
			break;
		case R.id.fragment_shop_iv_others:
			initMenuSelected(IV_OTHERS_FLAG);
			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
			mGoodsBean_list.clear();
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
			changeFragment(FRAGMENT_SHOP_FLAG);
			break;
		case R.id.fragment_shop_theme_iv_buy:
			initMenuSelected(IV_ALL_FLAG);
			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
			mGoodsBean_list.clear();
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
			changeFragment(FRAGMENT_SHOP_FLAG);
			stopThemeAnimation();
			if (oa_buyAnima == null) {
				oa_buyAnima = AnimaUtils.WeiShopJitterAnimal(iv_buy);
			} else {
				oa_buyAnima.start();
			}
			mMachineControl.startSound(6);
			break;
		case R.id.fragment_shop_theme_iv_activity:
			stopMenuAnimation();
			mMachineControl.setRespondButton(true);
			changeFragment(FRAGMENT_ACTIVITY_FLAG);
			stopThemeAnimation();
			if(oa_activityAnima==null){
				oa_activityAnima = AnimaUtils.WeiShopJitterAnimal(iv_activity);
			}else {
				oa_activityAnima.start();
			}
			
			break;
		case R.id.fragment_shop_theme_iv_wechat:
			mMachineControl.setRespondButton(true);
			changeFragment(FRAGMENT_WECHAT_FLAG);
			stopMenuAnimation();
			stopThemeAnimation();
			if(oa_wechatAnima==null){
				oa_wechatAnima = AnimaUtils.WeiShopJitterAnimal(iv_wechat);
			}else {
				oa_wechatAnima.start();
			}
			break;
		case R.id.fragment_shop_theme_iv_exchange:
			mMachineControl.setRespondButton(false);
			changeFragment(FRAGMENT_EXCHANGE_FLAG);
			stopMenuAnimation();
			stopThemeAnimation();
			if(oa_exchangeAnima==null){
				oa_exchangeAnima = AnimaUtils.WeiShopJitterAnimal(iv_exchange);
			}else {
				oa_exchangeAnima.start();
			}
			break;
		}
	}
	@Override
	public void onStart() {
		mContext.setmMainFragment(this);
		mContext.setmMainFragmentHandler(mMainFragmentHandler);
		IntentFilter refreshFilter = new IntentFilter(ICofficeApplication.NOTICE_UPDATE_ADAPTER);
		mContext.registerReceiver(RefreshAdapterReceiver, refreshFilter);  
		super.onStart();
	}

	@Override
	public void onDetach() {
		mContext.setmMainFragment(null);
		super.onDetach();
	}
	/**
	 * 刷新商品展示页面的广播
	 */
	BroadcastReceiver RefreshAdapterReceiver = new BroadcastReceiver() {  
		  
        @Override  
        public void onReceive(Context context, Intent intent) { 
        	if(intent.getAction().equals(ICofficeApplication.NOTICE_UPDATE_ADAPTER)){
        		initMenuSelected(IV_ALL_FLAG);
    			BUY_TYPE = ChangeViewInterface.PAY_SCREEN;
    			mGoodsBean_list.clear();
    			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
    			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
    			mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
    			changeFragment(FRAGMENT_SHOP_FLAG);
        	}
        }  
    };
	@Override
	public void showExchange(StatusCheckRcode statusCheckRcode) {
		FragmentTransaction ft = null;
		ft = getChildFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_main_contain, new ExchangeDetailFragment(mContext,statusCheckRcode));
		try {
			ft.commitAllowingStateLoss();
		} catch (Exception e) {

		}
	}
	//二次购买
	public void payAgain(BasePayControl mBasePayControl,SoldGoodsBean soldGoodsBean){
		mGoodsBean_list.clear();
		mGoodsBean_list.addAll(mMachineControl.getGoodsBean_drink_list());
		mGoodsBean_list.addAll(mMachineControl.getGoodsBean_food_list());
		mGoodsBean_list.addAll(mMachineControl.getGoodsBean_others_list());
		mShopFragment.payAgain(mGoodsBean_list,mBasePayControl,soldGoodsBean);
		
	}
}
