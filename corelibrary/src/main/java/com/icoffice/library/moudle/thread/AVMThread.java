package com.icoffice.library.moudle.thread;

import android.os.Looper;

import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;

/**
 * 开机自检测是否连通单片机thread
 * @author lufeisong
 *
 */
public class AVMThread extends Thread{
	private BaseMachineControl mBaseMachineControl;
	private int _waitTime = 1000;//持续时长，ms
	private int _interval = 1000;//循环间隔时长，ms
	public AVMThread(BaseMachineControl baseMachineControl){
		super();
		mBaseMachineControl = baseMachineControl;
	}
	boolean checkAVMStatus(){
		return mBaseMachineControl.getAvmStatus();
	}

	public void setDuration(int seconds) {
		_waitTime = seconds * 1000;
	}
	@Override
	public void run() {
		int maxLoopCount = _waitTime / _interval;
		int currentCount = 0;
		while(!checkAVMStatus()){
			if(currentCount < maxLoopCount){
				currentCount++;
				try {
					Thread.sleep(_interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				mBaseMachineControl.exitApp(false);
				break;
			}
		}
		super.run();
	}

}
