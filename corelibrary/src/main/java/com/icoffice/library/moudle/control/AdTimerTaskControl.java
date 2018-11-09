package com.icoffice.library.moudle.control;

import android.os.Handler;
/**
 * 购买页面自动返回广告页面
 * @author lufeisong
 *
 */
public class AdTimerTaskControl {
	public static AdTimerTaskControl mAdTimerTask;
	private static ShowAdViewInterface _showAdViewInterface;
	private Handler adHandler;
	private Runnable adRunnable;// 广告runnable
	
	public int AdTimer;// 当前跳回广告为到倒计时
	public static int AdLastTimer = Integer.MAX_VALUE;// 广告回跳最大延时，默认为不跳回广告
	public static int AdStartTimer = 60;// 广告回跳开始倒计时
	
	private  AdTimerTaskControl(ShowAdViewInterface showAdViewInterface){
		_showAdViewInterface = showAdViewInterface;
		startAdTimerTask();
	}
	public static AdTimerTaskControl getInstance(ShowAdViewInterface showAdViewInterface){
		if(mAdTimerTask == null)
			mAdTimerTask = new AdTimerTaskControl(showAdViewInterface);
		return mAdTimerTask;
	}
	void startAdTimerTask(){
		setAdTimer(AdLastTimer);
		adHandler = new Handler();
		adRunnable = new Runnable() {

			@Override
			public void run() {
				int adTimer = getAdTimer();
				if (0 == getAdTimer()) {
					adTimer = AdLastTimer;
					_showAdViewInterface.showAd();
				} else {
					adTimer--;
				}
				setAdTimer(adTimer);
				adHandler.postDelayed(adRunnable, 1000);
			}
		};
		adHandler.postDelayed(adRunnable, 1000);
	}
	
	public void setAdTimer(int AdTimer) {
		this.AdTimer = AdTimer;
	}

	public int getAdTimer() {
		return AdTimer;
	}
	public interface ShowAdViewInterface{
		public void showAd();
	}
}
