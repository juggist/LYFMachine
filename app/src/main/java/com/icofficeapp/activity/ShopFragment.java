package com.icofficeapp.activity;

import java.util.ArrayList;

import CofficeServer.PayType;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icoffice.library.bean.db.PayTypeBean;
import com.icoffice.library.bean.db.SoldGoodsBean;
import com.icoffice.library.callback.ConnectNetCallBack;
import com.icoffice.library.handler.BaseViewHandler;
import com.icoffice.library.moudle.control.AdTimerTaskControl;
import com.icoffice.library.moudle.control.AliPayControl;
import com.icoffice.library.moudle.control.BasePayControl;
import com.icoffice.library.moudle.control.CardPayControl;
import com.icoffice.library.moudle.control.WechatPayControl;
import com.icoffice.library.moudle.wechat.SDKRuntimeException;
import com.icoffice.library.moudle.wechat.WxPayHelper;
import com.icoffice.library.utils.BitmapUtil;
import com.icoffice.library.utils.CommonUtils;
import com.icoffice.library.utils.FileUtil;
import com.icoffice.library.utils.QRService;
import com.icoffice.library.widget.CommentDialogUtils;
import com.icofficeapp.R;
import com.icofficeapp.activity.AdFragment.ChangeViewInterface;
import com.icofficeapp.adapter.ShopAdapter;
import com.icofficeapp.handler.ShopHandler;

/**
 * 购买商品的流程
 * 
 * @author lufeisong
 * 
 */
@SuppressLint({ "ValidFragment", "HandlerLeak" })
public class ShopFragment extends BaseChildFragment implements OnClickListener,ConnectNetCallBack{
	private BaseFragmentActivity mContext;
	private ShopAdapter adapter;
	
	private SoldGoodsBean mSoldGoodsBean;
	//客非卡操作对象
	private CardPayControl cardPayControl;
	private FrameLayout rl_shop_detail;
	private LinearLayout ll_shop_buy;
	// buy view
	private GridView gv;
	private EditText cardNumEt;
	private ImageButton iBtn_up, iBtn_down;
	private ImageView iv_buy_goodsPic;
	private TextView tv_buy_goodsName_CN, tv_buy_goodsName_EN,
			tv_buy_goodsPrice,tv_buy_icoffice_card_pay_price;
	private ImageButton iBtn_back;
	private LinearLayout ll_up,ll_down;

	// paytype
	private LinearLayout ll_typechoose, ll_typecurrent;
	private ImageButton iBtn_cash_changePaytype, iBtn_icoffice_changePaytype,
			iBtn_wechat_changePaytype, iBtn_alipay_changePaytype;
	
	private ImageButton iBtn_cash, iBtn_icoffice, iBtn_wechat, iBtn_alipay,iBtn_yee;
	private LinearLayout ll_cash, ll_icoffice, ll_wechat, ll_alipay,ll_yee;
	private LinearLayout ll_paytype_cash, ll_paytype_icoffice,
			ll_paytype_wechat, ll_paytype_ailpay, ll_paytype_again;
	private Button btn_payagain;
	private ImageView iv_erweima;
	private TextView tv_price_cash_CN, tv_price_cash_EN,tv_price_wechat_CN, tv_price_wechat_EN,tv_price_alipay_CN, tv_price_alipay_EN;

	private int BUY_TYPE;

	private final int VIEW_DETAIL_FLAG = 0;// 详细商品列表
	private final int VIEW_BUY_FLAG = 1;// 购买流程

	private int PAYTYPE_CURRENT_FLAG = 0;// 当前支付方式
	
	private final int TYPE_CHOOSE_FLAG = 0;// 选择支付方式
	private final int TYPE_CURRENT_FLAG = 1;// 确定支付方式

	private int page_count = 0;
	private int page_size = 0;
	private boolean isCofficeCardConnSuccess = false;

	private ArrayList<SoldGoodsBean> mGoodsBean_total_list;
	private ArrayList<SoldGoodsBean> mGoodsBean_current_list = new ArrayList<SoldGoodsBean>();
	private ArrayList<View> view_list = new ArrayList<View>();// 操作流程list
	private ArrayList<View> paytype_list = new ArrayList<View>();// 支付方式list
	private ArrayList<View> typeaction_list = new ArrayList<View>();// 支付action list
	// wechat
	private WxPayHelper mWxPayHelper;
	// alipay
	private AliPayControl mAliPayControl;
	
	private ArrayList<PayTypeBean> payTypeBean_list = new ArrayList<PayTypeBean>();//所有支付方式集合

	public ShopFragment() {

	}

	public ShopFragment(BaseFragmentActivity context,
			ArrayList<SoldGoodsBean> mGoodsBean_total_list, int BUY_TYPE) {
		mContext = context;
		this.BUY_TYPE = BUY_TYPE;
		setGoodsBean_total_list(mGoodsBean_total_list);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shop, null);
		initView(view);
		initListener();
		initData();
		return view;
	}

	void initView(View view) {
		rl_shop_detail = (FrameLayout) view
				.findViewById(R.id.fragment_shop_rl_detail);
		ll_shop_buy = (LinearLayout) view
				.findViewById(R.id.fragment_shop_ll_buy);
		// buy view
		gv = (GridView) view.findViewById(R.id.fragment_shop_gv);
		iBtn_up = (ImageButton) view.findViewById(R.id.fragment_shop_up);
		iBtn_down = (ImageButton) view.findViewById(R.id.fragment_shop_down);
		iv_buy_goodsPic = (ImageView) view
				.findViewById(R.id.fragment_shop_buy_goodsPic);
		tv_buy_goodsName_CN = (TextView) view
				.findViewById(R.id.fragment_shop_buy_goodsName_CN);
		tv_buy_goodsName_EN = (TextView) view
				.findViewById(R.id.fragment_shop_buy_goodsName_EN);
		tv_buy_goodsPrice = (TextView) view
				.findViewById(R.id.fragment_shop_buy_goodsPrice);
		iBtn_back = (ImageButton) view
				.findViewById(R.id.fragment_shop_buy_back);
		ll_up = (LinearLayout) view.findViewById(R.id.fragment_shop_up_parent);
		ll_down = (LinearLayout) view.findViewById(R.id.fragment_shop_down_parent);
		// paytype
		ll_typechoose = (LinearLayout) view
				.findViewById(R.id.fragment_shop_typechoose);
		ll_typecurrent = (LinearLayout) view
				.findViewById(R.id.fragment_shop_typecurrent);
		iBtn_cash_changePaytype = (ImageButton) view
				.findViewById(R.id.paytype_cash_ibtn_others);
		iBtn_icoffice_changePaytype = (ImageButton) view
				.findViewById(R.id.paytype_icoffice_ibtn_others);
		iBtn_wechat_changePaytype = (ImageButton) view
				.findViewById(R.id.paytype_wechat_ibtn_others);
		iBtn_alipay_changePaytype = (ImageButton) view
				.findViewById(R.id.paytype_alipay_ibtn_others);
		iBtn_cash = (ImageButton) view.findViewById(R.id.fragment_shop_cash);
		iBtn_icoffice = (ImageButton) view
				.findViewById(R.id.fragment_shop_icoffice);
		iBtn_wechat = (ImageButton) view
				.findViewById(R.id.fragment_shop_wechat);
		iBtn_alipay = (ImageButton) view
				.findViewById(R.id.fragment_shop_alipay);
		iBtn_yee = (ImageButton) view.findViewById(R.id.fragment_shop_yee);
		ll_cash = (LinearLayout) view.findViewById(R.id.fragment_shop_ll_cash);
		ll_icoffice = (LinearLayout) view
				.findViewById(R.id.fragment_shop_ll_icoffice);
		ll_wechat = (LinearLayout) view
				.findViewById(R.id.fragment_shop_ll_wechat);
		ll_alipay = (LinearLayout) view
				.findViewById(R.id.fragment_shop_ll_alipay);
		ll_yee = (LinearLayout) view.findViewById(R.id.fragment_shop_ll_yee);
		ll_paytype_cash = (LinearLayout) view.findViewById(R.id.paytype_cash);
		ll_paytype_icoffice = (LinearLayout) view
				.findViewById(R.id.paytype_icoffice);
		ll_paytype_wechat = (LinearLayout) view
				.findViewById(R.id.paytype_wechat);
		ll_paytype_ailpay = (LinearLayout) view
				.findViewById(R.id.paytype_alipay);
		ll_paytype_again = (LinearLayout) view.findViewById(R.id.pay_again);
		btn_payagain = (Button) view.findViewById(R.id.pay_again_btn);
		iv_erweima = (ImageView) view.findViewById(R.id.paytype_wechat_iv);
		tv_price_cash_CN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_cash_CN);
		tv_price_cash_EN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_cash_EN);
		tv_price_wechat_CN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_wechat_CN);
		tv_price_wechat_EN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_wechat_EN);
		tv_price_alipay_CN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_alipay_CN);
		tv_price_alipay_EN = (TextView) view
				.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_alipay_EN);
		cardNumEt = (EditText) view.findViewById(R.id.paytype_icoffice_card_num);
		tv_buy_icoffice_card_pay_price = (TextView) view.findViewById(R.id.fragment_shop_currenttype_tv_goodsprice_CN);
		//初始化 使输入框不可写入
		CommonUtils.setEditInput(cardNumEt, false);
	
	}

	void initListener() {
		iBtn_up.setOnClickListener(this);
		iBtn_down.setOnClickListener(this);
		iBtn_back.setOnClickListener(this);
		iBtn_cash_changePaytype.setOnClickListener(this);
		iBtn_icoffice_changePaytype.setOnClickListener(this);
		iBtn_wechat_changePaytype.setOnClickListener(this);
		iBtn_alipay_changePaytype.setOnClickListener(this);
		iBtn_cash.setOnClickListener(this);
		iBtn_icoffice.setOnClickListener(this);
		iBtn_wechat.setOnClickListener(this);
		iBtn_alipay.setOnClickListener(this);
		iBtn_yee.setOnClickListener(this);
		btn_payagain.setOnClickListener(this);
		
		gv.setOnItemClickListener(getGvListener());
		getIcofficeCardNum();
	}

	private void getIcofficeCardNum() {
		cardNumEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				//保存的到的数据 同事清空输入框 //(限制 对输入的获取 使用换行符 ? ？)
				String cardNum = arg0.toString();
				int currentIndex = -1;
				//这里索引客非卡号对客非卡的结尾进行判断
				currentIndex = cardNum.indexOf("?");
				if(currentIndex==-1){
					currentIndex = cardNum.indexOf("？");
					if(currentIndex==-1){
						currentIndex = cardNum.indexOf("\n");
						if(currentIndex>0){
							cardNum = cardNum = cardNum.replace("\n", "");
						}
					}else {
						cardNum = cardNum.replace("？", "");
					}
				}else {
					cardNum = cardNum.replace("?", "");
				}
				if(currentIndex>0){
					if(cardNum!=null&&!"".equals(cardNum)){
						if(isCofficeCardConnSuccess){
							CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "刷卡---success");
							cardPayControl.set_cardNO(cardNum);
							cardPayControl.start();
					//不可输入
						}
					}
					CommonUtils.setEditInput(cardNumEt, false);
					cardNumEt.setText("");
				}
				
			}
		});

	}
	
	void initData() {
		mMachineControl.setRespondButton(true);
		mContext.setmShopFragment(this);
		mContext.setConnectNetCallBack(this);
		if(mGoodsBean_total_list.size() == 0){
			ll_up.setVisibility(View.INVISIBLE);
			ll_down.setVisibility(View.INVISIBLE);
			BitmapUtil.displayBackground2Drawable(R.drawable.goods_empty, gv);
		}
			
		view_list.add(rl_shop_detail);
		view_list.add(ll_shop_buy);
		typeaction_list.add(ll_typechoose);
		typeaction_list.add(ll_typecurrent);
		paytype_list.add(null);
		paytype_list.add(ll_paytype_cash);
		paytype_list.add(null);
		paytype_list.add(ll_paytype_wechat);
		paytype_list.add(ll_paytype_ailpay);
		paytype_list.add(ll_paytype_icoffice);
		paytype_list.add(null);
		paytype_list.add(null);
		paytype_list.add(ll_paytype_again);

		mWxPayHelper = new WxPayHelper();
		adapter = new ShopAdapter(mContext, mGoodsBean_current_list);
		gv.setAdapter(adapter);
		if (BUY_TYPE == ChangeViewInterface.PAY_BUTTON) {
			button2shop();
		} else if (BUY_TYPE == ChangeViewInterface.PAY_SCREEN) {
			screen2shop();
		}
		
		
	}

	// 屏幕购买跳转购买页面
	void screen2shop() {
		detail2choosePaytype(VIEW_DETAIL_FLAG,ShopHandler.DETAIL_2_FINISH_TIMER);
		notifyDataSetChanged();
	}

	// 物理按钮购买跳转购买页面
	public void button2shop() {
		detail2choosePaytype(VIEW_BUY_FLAG, ShopHandler.DETAIL_2_FINISH_TIMER);

		notifyDataSetChanged();
		choosePaytype2paytype(TYPE_CHOOSE_FLAG);
		SoldGoodsBean soldGoodsBean = mContext.mMachineControl
				.getSoldGoodsBean();
		mSoldGoodsBean = soldGoodsBean;
//		setSoldGoodsBean(soldGoodsBean);
		initBuyData();
	}

	// 初始化购买界面的数据
	void initBuyData() {
		payTypeBean_list = mMachineControl.getPayTypeBean();
		for(int i = 0;i < payTypeBean_list.size();i++){
			PayTypeBean payTypeBean = payTypeBean_list.get(i);
			if( payTypeBean.getPayType() == PayType.cash.value() ){
				if( payTypeBean.getPayStatus() == 1)
					ll_cash.setVisibility(View.VISIBLE);
				else
					ll_cash.setVisibility(View.GONE);
			}
				
			if( payTypeBean.getPayType() == PayType.ali.value() ){
				if(payTypeBean.getPayStatus() == 1)
					ll_alipay.setVisibility(View.VISIBLE);
				else
					ll_alipay.setVisibility(View.GONE);
			}
				
			if( payTypeBean.getPayType() == PayType.card.value() ){
				if(payTypeBean.getPayStatus() == 1)
					ll_icoffice.setVisibility(View.VISIBLE);
				else
					ll_icoffice.setVisibility(View.GONE);
			}
				
			if( payTypeBean.getPayType() == PayType.wechat.value() ){
				if( payTypeBean.getPayStatus() == 1)
					ll_wechat.setVisibility(View.VISIBLE);
				else
					ll_wechat.setVisibility(View.GONE);
			}
				
			if( payTypeBean.getPayType() == PayType.yeepay.value()){
				if( payTypeBean.getPayStatus() == 1)
					ll_yee.setVisibility(View.VISIBLE);
				else
					ll_yee.setVisibility(View.GONE);
			}
				
		}
		mContext.mMachineControl.startSound(18);
		mImageLoaderControl.displayImageBig(
				FileUtil.ROOT_PATH + mSoldGoodsBean.getZ_detail_image(),
				iv_buy_goodsPic, R.drawable.empty_detail);
		tv_buy_goodsName_CN.setText(mSoldGoodsBean.getG_name());
		tv_buy_goodsName_EN.setText(mSoldGoodsBean.getG_ename());
		tv_buy_goodsPrice.setText("￥ "
				+ Float.parseFloat(mSoldGoodsBean.getUnit_price()) / 100 + "元");
		
	}

	// 切换购买列表与商品详情列表
	void detail2choosePaytype(int VIEW_FLAG, int delay_time) {
		mContext.shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.DETAIL_2_FINISH, delay_time);
		if (VIEW_DETAIL_FLAG == VIEW_FLAG){
			mContext.mAdTimerTaskControl.setAdTimer(AdTimerTaskControl.AdStartTimer);
		}
		else{
			mContext.mAdTimerTaskControl.setAdTimer(AdTimerTaskControl.AdLastTimer);
		}
		for (int i = 0; i < view_list.size(); i++) {
			if (i == VIEW_FLAG)
				view_list.get(i).setVisibility(View.VISIBLE);
			else
				view_list.get(i).setVisibility(View.INVISIBLE);
		}
	}
	 Animation animation = null;
	 View view= null;
	// 切换支付方式与当前支付
	void choosePaytype2paytype(int TYPE_FLAG) {
		mContext.shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.CHOOSEPAYTYPE_2_DETAIL,ShopHandler.CHOOSEPAYTYPE_2_DETAIL_TIMER);
		if(TYPE_FLAG == 0){
			mMachineControl.setRespondButton(true);
		}else if(TYPE_FLAG == 1){
			mMachineControl.setRespondButton(false);
		}
		
		for (int i = 0; i < typeaction_list.size(); i++) {
			if (i == TYPE_FLAG){
				typeaction_list.get(i).setVisibility(View.VISIBLE);
				if(TYPE_FLAG!=0){
					if(animation==null){
						animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
					}
					view = typeaction_list.get(i);
					animation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation arg0) {
							view.clearAnimation();
							
						}
					});
					typeaction_list.get(i).startAnimation(animation);
					
				}
				
//				AnimaUtils animaUtils = new AnimaUtils();
//				animaUtils.translateDown(typeaction_list.get(i), 1000, -400f,0f);
			}
			else
				typeaction_list.get(i).setVisibility(View.INVISIBLE);
		}
	}
	

	// 切换支付方式界面（包含支付成功）
	void paytype2paytype(int PAYTYPE_FLAG) {
		mContext.shopTimerTask(ShopHandler.PAY_OTHER,ShopHandler.PAYTYPE_2_DETAIL,ShopHandler.PAYTYPE_2_DETAIL_TIMER);
		choosePaytype2paytype(TYPE_CURRENT_FLAG);
		for (int i = 0; i < paytype_list.size(); i++) {
			if (i == PAYTYPE_FLAG){
				if(paytype_list.get(i) != null)
					paytype_list.get(i).setVisibility(View.VISIBLE);
			}else{
				if(paytype_list.get(i) != null)
					paytype_list.get(i).setVisibility(View.INVISIBLE);
			}
				
		}
	}

	// 现金支付
	void initCashPay() {
		
		if (Integer.parseInt(mSoldGoodsBean.getUnit_price()) <= 1) {
			mContext.startDialogTimerTask(
					CommentDialogUtils.DIALOG_GOODS_PRICE_ERROR_FLAG, 5);
			return;
		}
		tv_price_cash_CN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		tv_price_cash_EN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
//		PAYTYPE_CURRENT_FLAG = PAYTYPE_CASH_FLAG;
		paytype2paytype(PAYTYPE_CASH_FLAG);
		mMachineControl.startCashETPay(mSoldGoodsBean);
		int coinWaitTime = 0;
		try{
			coinWaitTime = Integer.parseInt(mContext.iCofficeApplication.getCoinWaitTime());
		}catch(Exception e){
			 coinWaitTime = 15;
		}
		mContext.shopTimerTask(ShopHandler.PAY_CASH_FLAG,ShopHandler.CHOOSEPAYTYPE_2_DETAIL,coinWaitTime);
	}
	
	// 客非卡支付
	void initICofficePay() {
		tv_buy_icoffice_card_pay_price.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		paytype2paytype(PAYTYPE_ICOFFICE_FLAG);
		cardPayControl = mMachineControl.startCardPay(mSoldGoodsBean);
		
		mContext.shopTimerTask(ShopHandler.PAY_ICOFFICE_FLAG,ShopHandler.CHOOSEPAYTYPE_2_DETAIL,
				ShopHandler.CARD_TIME);
	}

	// 微信支付
	void initWeChatPay() {
		tv_price_wechat_CN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		tv_price_wechat_EN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		paytype2paytype(PAYTYPE_WECHAT_FLAG);
		String url = null;
		try {
			WechatPayControl wechatPayControl = mMachineControl
					.startWecatPay(mSoldGoodsBean);
			url = mWxPayHelper.CreateNativeUrl(wechatPayControl
					.getClient_order_no());
			Bitmap picBit = QRService.getInstance().createImage(url,
					iv_erweima.getWidth(), iv_erweima.getHeight());
			iv_erweima.setImageBitmap(picBit);
		} catch (SDKRuntimeException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO 二维码生成失败提醒
		}
		mContext.shopTimerTask(ShopHandler.PAY_WECHAT_FLAG,ShopHandler.CHOOSEPAYTYPE_2_DETAIL,
				ShopHandler.WECHAT_TIME);
	}

	// 支付宝支付
	void initAliPay() {
		tv_price_alipay_CN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		tv_price_alipay_EN.setText(Float.parseFloat(mSoldGoodsBean.getUnit_price())
				/ 100 + "");
		paytype2paytype(PAYTYPE_ALIPAY_FLAG);
		mMachineControl.startAliPay(mSoldGoodsBean);
		mAliPayControl = (AliPayControl) (mMachineControl
				.getCurrentPayControl());
		mContext.shopTimerTask(ShopHandler.PAY_ALIPAY_FLAG,ShopHandler.CHOOSEPAYTYPE_2_DETAIL,
				ShopHandler.ALIPAY_TIME);
	}

	// 支付成功
	void initPayAgain() {
		paytype2paytype(PAYTYPE_PAYAGAIN_FLAG);
	}

	void payAgain(int PAYTYPE_FLAG) {
		switch (PAYTYPE_FLAG) {
		case PAYTYPE_CASH_FLAG:
			initCashPay();
			break;
		case PAYTYPE_ICOFFICE_FLAG:
			CommonUtils.setEditInput(cardNumEt, true);
			initICofficePay();
			break;
		case PAYTYPE_WECHAT_FLAG:
			initWeChatPay();
			break;
		case PAYTYPE_ALIPAY_FLAG:
			initAliPay();
			break;
		}
	}

	// 计时器返回购买列表
	public void task2detail() {
		detail2choosePaytype(VIEW_DETAIL_FLAG,ShopHandler.DETAIL_2_FINISH_TIMER);
	}
	//更新数据
	public void setGoodsBean_total_list(ArrayList<SoldGoodsBean> mGoodsBean_total_list){
		this.mGoodsBean_total_list = mGoodsBean_total_list;
		if (mGoodsBean_total_list.size() % 9 == 0)
			page_size = mGoodsBean_total_list.size() / 9;
		else
			page_size = mGoodsBean_total_list.size() / 9 + 1;
	}
	//刷新适配器
	public void notifyDataSetChanged(){
		mGoodsBean_current_list.clear();
		for (int i = page_count * 9; i < (page_count + 1) * 9; i++) {
			if (i < mGoodsBean_total_list.size()) {
				mGoodsBean_current_list.add(mGoodsBean_total_list.get(i));
			}
		}
		adapter.notifyDataSetChanged();
	}
	//再次购买
	public void payAgain(ArrayList<SoldGoodsBean> mGoodsBean_total_list,BasePayControl mBasePayControl,SoldGoodsBean soldGoodsBean){
		setGoodsBean_total_list(mGoodsBean_total_list);
		notifyDataSetChanged();
		if(ll_shop_buy.getVisibility() == View.VISIBLE){
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "在购买内页，可以再次购买");
			mSoldGoodsBean = soldGoodsBean;
//			setSoldGoodsBean(soldGoodsBean);
			initPayAgain();
			PAYTYPE_CURRENT_FLAG = mBasePayControl.getPayType();
		}else{
			CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "在购买首页，无法再次购买");
		}
		
	}

	@Override
	public void onClick(View arg0) {
		if (com.icoffice.library.utils.CommonUtils.isFastDoubleClick())
			return;
		switch (arg0.getId()) {
		case R.id.fragment_shop_up:
			if (page_count > 0) {
				page_count--;
				screen2shop();
			}
			break;
		case R.id.fragment_shop_down:
			if (page_count < page_size - 1) {
				page_count++;
				screen2shop();
			}
			break;
		case R.id.fragment_shop_buy_back:
			mContext.setShopFinishTimer();
			break;
		case R.id.fragment_shop_cash:
			if (!mContext.isAvmRunning() || mContext.isDoorOpen())
				return;
			PAYTYPE_CURRENT_FLAG = PAYTYPE_CASH_FLAG;
			initCashPay();
			break;
		case R.id.fragment_shop_icoffice:
			if (!mContext.isAvmRunning()|| mContext.isDoorOpen())
				return;
			PAYTYPE_CURRENT_FLAG = PAYTYPE_ICOFFICE_FLAG;
			CommonUtils.showLog("支付方式",  "客非卡");
			mContext.isConnectNet(PAYTYPE_CURRENT_FLAG);
			break;
		case R.id.fragment_shop_wechat:
			if (!mContext.isAvmRunning() || mContext.isDoorOpen())
				return;
			PAYTYPE_CURRENT_FLAG = PAYTYPE_WECHAT_FLAG;
			CommonUtils.showLog("支付方式",  "微信");
			mContext.isConnectNet(PAYTYPE_CURRENT_FLAG);
			break;
		case R.id.fragment_shop_alipay:
			if ( !mContext.isAvmRunning() || mContext.isDoorOpen())
				return;
			PAYTYPE_CURRENT_FLAG = PAYTYPE_ALIPAY_FLAG;
			CommonUtils.showLog("支付方式",  "支付宝");
			mContext.isConnectNet(PAYTYPE_CURRENT_FLAG);
			break;
		case R.id.fragment_shop_yee:
			mContext.isWait();
			break;
		case R.id.pay_again_btn:
			payAgain(PAYTYPE_CURRENT_FLAG);
			break;
		case R.id.paytype_cash_ibtn_others:
			choosePaytype2paytype(TYPE_CHOOSE_FLAG);
			break;
		case R.id.paytype_icoffice_ibtn_others:
			choosePaytype2paytype(TYPE_CHOOSE_FLAG);
			break;
		case R.id.paytype_wechat_ibtn_others:
			choosePaytype2paytype(TYPE_CHOOSE_FLAG);
			break;
		case R.id.paytype_alipay_ibtn_others:
			mAliPayControl.closeSonicWaveNFC();
			choosePaytype2paytype(TYPE_CHOOSE_FLAG);
			break;

		}

	}

	// 点击商品进入支付选择界面
	GridView.OnItemClickListener getGvListener() {
		return new GridView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!mContext.isAvmRunning() || mContext.isDoorOpen()) {
					return;
				}
				SoldGoodsBean soldGoodsBean = mGoodsBean_current_list.get(arg2);
				 //TODO checkroad 记录到日志，作为该货道状态判断依据
				if(mMachineControl.normalSold(soldGoodsBean)){
					CommonUtils.showLog(CommonUtils.PURCHASE_TAG, "屏幕按钮 way_id = " + soldGoodsBean.getW_code() + " 剩余库存为 : " + mMachineControl.getStore8Way_id(soldGoodsBean.getW_code()));
					detail2choosePaytype(VIEW_BUY_FLAG,ShopHandler.DETAIL_2_FINISH_TIMER);
					choosePaytype2paytype(TYPE_CHOOSE_FLAG);
					mSoldGoodsBean = soldGoodsBean;
//					setSoldGoodsBean(soldGoodsBean);
					initBuyData();
				}else{
					CommonUtils.sendMessage(mContext.mViewHandler,BaseViewHandler.GOODS_SOLD_SELL_OUT, "");
				}
			}
		};
	}

	@Override
	public void onStart() {
		mContext.mAdTimerTaskControl.setAdTimer(AdTimerTaskControl.AdStartTimer);
		super.onStart();
	}

	@Override
	public void onDetach() {
		mContext.cancleShopTask();
		mContext.mAdTimerTaskControl.setAdTimer(AdTimerTaskControl.AdLastTimer);
		super.onDetach();
	}

	@Override
	public void onDestroy() {
		if (mAliPayControl != null)
			mAliPayControl.closeSonicWaveNFC();
		mContext.setmShopFragment(null);
		super.onDestroy();
	}


	@Override
	public void connectFail(String msg) {
		// TODO Auto-generated method stub
		isCofficeCardConnSuccess = false;
	}

	@Override
	public void connectSuccess(int payType) {
		CommonUtils.setEditInput(cardNumEt, false);
		isCofficeCardConnSuccess = false;
		if(PAYTYPE_CURRENT_FLAG == payType){
			switch(payType){
			case PAYTYPE_ICOFFICE_FLAG :
				isCofficeCardConnSuccess = true;
				CommonUtils.setEditInput(cardNumEt, true);
				//切换成功后
				initICofficePay();
				CommonUtils.showLog("支付方式", "当前支付方式 = 客非卡" );
				break;// 客非卡支付
			case PAYTYPE_WECHAT_FLAG:
				initWeChatPay();
				CommonUtils.showLog("支付方式", "当前支付方式 = 微信" );
				break;// 微信支付
			case PAYTYPE_ALIPAY_FLAG :
				initAliPay();
				CommonUtils.showLog("支付方式", "当前支付方式 = 支付宝" );
				break;// alipay支付
			}
		}else{
			isCofficeCardConnSuccess = false;
			CommonUtils.showLog("支付方式", "支付取消 = " + payType);
		}
	}
}
