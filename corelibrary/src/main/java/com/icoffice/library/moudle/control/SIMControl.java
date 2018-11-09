package com.icoffice.library.moudle.control;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * sim卡控制类
 * @author lufeisong
 *
 */
public class SIMControl {
	private static SIMControl mSIMControl;
	private TelephonyManager tm;
	private SIMControl(Context context){
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	public static SIMControl getInstance(Context context){
		if(mSIMControl == null)
			mSIMControl = new SIMControl(context);
		return mSIMControl;
	}
	//取出IMEI
	public String getDeviceId(){
		return tm.getDeviceId();      
	}
	//取出MSISDN，很可能为空
	public String getLine1Number(){
		return tm.getLine1Number();       
	}
	//取出ICCID
	public String getSimSerialNumber(){
		return tm.getSimSerialNumber();       
	}
	public String getSubscriberId(){
		return tm.getSubscriberId();     
	}
}
