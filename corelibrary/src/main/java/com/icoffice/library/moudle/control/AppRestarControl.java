package com.icoffice.library.moudle.control;

import com.icoffice.library.bean.db.AppRestarTime;
import com.icoffice.library.utils.CommonUtils;

/**
 * app自启动control
 * 
 * @author lufeisong
 * 
 */
public class AppRestarControl {
	private BaseMachineControl mBaseMachineControl;
	private static AppRestarControl mAppRestarControl;
	private final static String startResetTime = "02:30:00";

	private AppRestarControl(BaseMachineControl baseMachineControl) {
		mBaseMachineControl = baseMachineControl;
	}

	public static AppRestarControl getInstance(
			BaseMachineControl baseMachineControl) {
		if (mAppRestarControl == null)
			mAppRestarControl = new AppRestarControl(baseMachineControl);
		return mAppRestarControl;
	}

	public void check() {
		String timeCurrentDay = CommonUtils.currentTime_ymd();
		String timeLastDay = mBaseMachineControl.getAutoRestartTime();
		double betweenTime = CommonUtils.currentSecondsTime(CommonUtils
				.currentTime_hms())
				- CommonUtils.currentSecondsTime(startResetTime);
		if (betweenTime > 0 && !timeCurrentDay.equals(timeLastDay)) {
			if (timeLastDay.isEmpty()) {
				// 这是首次安装APP，从未自动重启过的情况
				AppRestarTime appRestartTime = new AppRestarTime();
				appRestartTime.setMode(1);
				appRestartTime.setTime(timeCurrentDay);
				mBaseMachineControl.insertAppRestartTime(appRestartTime);
			} else {
				if (mBaseMachineControl.lock(true)) {
					AppRestarTime appRestartTime = new AppRestarTime();
					appRestartTime.setMode(1);
					appRestartTime.setTime(timeCurrentDay);
					mBaseMachineControl.insertAppRestartTime(appRestartTime);
					mBaseMachineControl.lock(false);
//					mBaseMachineControl.exitApp(false);
					mBaseMachineControl.SystemReboot();
				}
			}
		}

	}
}
