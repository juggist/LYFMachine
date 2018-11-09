package com.icoffice.library.widget;
/**
 * 自定义Dialog类
 */
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class CommentDialogUtils extends Dialog {
	public static final int DIALOG_BUYFAIL_FLAG = 0;//出货失败
	public static final int DIALOG_BUYSUCCESS_FLAG = 1;//出货成功
	public static final int DIALOG_CONNECT_NET_FAIL_FLAG = 2;//链接网络失败
	public static final int DIALOG_GOODS_SELL_OUT_FLAG = 3;//售罄
	public static final int DIALOG_GOODS_PRICE_ERROR_FLAG = 4;//单价小于1分的error
	public static final int DIALOG_AVM_STATUS_FAIL_FLAG = 5;//单片机通讯异常
	public static final int DIALOG_IS_LOCKED = 6;//状态被锁
//	public static final int DIALOG_RCODE_ISLOCKED = 7;//微商城兑换码状态被锁
//	public static final int DIALOG_RCODE_ERROR = 8;//微商城兑换码异常
	public static final int DIALOG_GOODS_RCODE_OVER_FLAG = 9;//微商城兑换商品领取完
	public static final int DIALOG_WAITOUTGOODS_FLAG = 10;//正在出货中
	public static final int DIALOG_WAIT_FLAG = 11;//等待开放
	
	public static final int DIALOG_PAY_FAIL = 12;//客非卡支付失败
	public static final int DIALOG_PAY_SUCC = 13;//客非卡支付成功
	public static final int DIALOG_SERVICE_OUTGOODS = 14;//服务器出货
	
	
	
	
	public CommentDialogUtils(Context context, int width, int height, int layout,
			int style) {
		super(context, style);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set content
		setContentView(layout);
		

		// set window params
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		// set width,height by density and gravity
		float density = getDensity(context);
		params.width = (int) (width * density);
		params.height = (int) (height * density);
		params.gravity = Gravity.CENTER;

		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}
}
