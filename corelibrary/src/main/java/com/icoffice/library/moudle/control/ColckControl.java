package com.icoffice.library.moudle.control;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.icoffice.library.handler.ClockHandler;
import com.icoffice.library.utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时钟工具类
 * @author lufeisong
 *
 */
public class ColckControl implements Runnable{
	private static ColckControl instance;
	private Context mContext;
	private Handler mHandler;
	private boolean isFinish = false;
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(isFinish)
			return;
		final Date d = new Date();
		Bundle bundle = new Bundle();
		bundle.putInt("hour", d.getHours());
		bundle.putInt("minutes", d.getMinutes());
		bundle.putString("day", getWeekOfDate());
		bundle.putString("AM_PM", getAmOrPm());
		bundle.putString("type", "ALARM");
		CommonUtils.sendMessage(mHandler, ClockHandler.COLCK_FLAG, bundle);
		mHandler.postDelayed(this, 1000);
	}
	private ColckControl(Context context){
		mContext = context;
	}
	public static ColckControl getInstance(Context context){
		if(instance == null)
			instance = new ColckControl(context);
		return instance;
	}
	public void start(Handler handler){
		isFinish = false;
		mHandler = handler;
		mHandler.post(this);
	}
	public void finish(){
		isFinish = true;
	}
	
	public static String getWeekOfDate() {
		Date dt = new Date();
		String[] weekDays = { "SUN", "MON", "TUES", "WED", "THUR", "FRI", "SAT	" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	public static String getAmOrPm(){
		String AM_PM = "";
		GregorianCalendar ca = new GregorianCalendar();  
//		if(ca.get(GregorianCalendar.AM_PM) == 0){
//			AM_PM = "AM";
//		}else if(ca.get(GregorianCalendar.AM_PM) == 1){
//			AM_PM = "PM";
//		}
		return AM_PM;
	}
}
