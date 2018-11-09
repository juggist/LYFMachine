package com.icofficeapp.app;

import com.icoffice.library.app.BaseApplication;
import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icofficeapp.activity.MainActivity;
import com.icofficeapp.control.LYMachineControl;

public class ICofficeApplication extends BaseApplication {
	private BaseMachineControl mMachineControl;
	public static String ETOUCH_DOOR_STATE = "com.avm.serialport.door_state";// 开关门广播
	public static String ETOUCH_OUT_GOODS = "com.avm.serialport.OUT_GOODS";// 出货判断广播
	public static String ETOUCH_SELECT_GOODS = "com.avm.serialport.SELECT_GOODS";// 物理按钮选货广播
	public static String ETOUCH_RECEIVE_MONEY = "com.avm.serialport.RECEIVE_MONEY";// 收币广播
	public static String ETOUCH_AVM_DISCONNECT = "com.avm.serialport.NOTICE_AVM_DISCONNECT";// 通讯中断
	public static String NOTICE_UPDATE_ADAPTER = "com.icofficeapp.refreshAdapter";// 刷新商品展示页

	@Override
	public void onCreate() {
		setC();
		setClassName();
		mMachineControl = LYMachineControl.getInstance(this);
		initBaseMachineControl(mMachineControl);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
 

	@Override
	public void setC() {
		c = MainActivity.class;
		
	}

	@Override
	public void setClassName() {
		className = "com.icofficeapp.activity.MainActivity";
		
	}
}
