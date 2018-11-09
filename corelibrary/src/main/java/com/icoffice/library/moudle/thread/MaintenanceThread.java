package com.icoffice.library.moudle.thread;

import android.content.pm.PackageManager.NameNotFoundException;

import com.icoffice.library.moudle.control.BaseMachineControl;
import com.icoffice.library.utils.CommonUtils;

public class MaintenanceThread extends Thread {
	
	private final int LOOP_COUNT = 6000; // 6000次*100ms=10分钟执行一次
	
	private BaseMachineControl _machineControl;
	private boolean _exit = false;
	
	public MaintenanceThread(BaseMachineControl machineControl) {
		super();
		_machineControl = machineControl;
	}
	
	// 升级APK
	private void upgradeApk() {
		try {
			_machineControl.upgradeApk(null);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// 刷新出货队列，释放垃圾数据
	private void refreshOutGoodsPayControl() {
		_machineControl.refreshOutGoodsBasePayControl();
	}
	
	// 下载广告
	private void downloadAds() {
		
	}
	//凌晨app自启动
	private void checkAppRestar(){
		_machineControl.checkAppRestart();
	}
	//删除缓存log
	private void deleteCach(){
		_machineControl.deleteCache();
	}
	// 退出线程
	public void exit() {
		_exit = true;
	}
	// 上传未提交的订单
	public void postUnCommitOrder(){
		_machineControl.netOrderComplete();
		_machineControl.cashOrderComplete();
		_machineControl.cofficeShopOrderComplete();
	}
	@Override
	public void run() {
		int loopCount = 1;
		while (!_exit) {
			if (loopCount % LOOP_COUNT == 0) {
				CommonUtils.showLog(CommonUtils.TAG, "维护线程启动");
				upgradeApk();
				downloadAds();
				refreshOutGoodsPayControl();
				checkAppRestar();
				deleteCach();
				if(_machineControl.isFrontView()){
					postUnCommitOrder();
					CommonUtils.showLog(CommonUtils.TAG, "app在前台，维护线程里的功能启动");
				}else{
					CommonUtils.showLog(CommonUtils.TAG, "app在后台，维护线程里的功能暂停");
				}
				loopCount = 0; // 重置循环计数
			}

			loopCount++; // 增加循环计数
			
			try {
				Thread.sleep(100); // 休眠100ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
