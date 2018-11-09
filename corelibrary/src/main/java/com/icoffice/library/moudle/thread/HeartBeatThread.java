package com.icoffice.library.moudle.thread;

import com.icoffice.library.moudle.control.BaseMachineControl;

public class HeartBeatThread extends Thread {
	
	private int _loopCount = 600; // 600次*100ms=1分钟执行一次
	
	private BaseMachineControl _machineControl;
	private boolean _exit = false;
	
	public HeartBeatThread(BaseMachineControl machineControl) {
		super();
		// TODO Auto-generated constructor stub
		_machineControl = machineControl;
	}
	
	// 心跳
	private void heartBeat() {
//		_machineControl.heartBeat();
	}

	// 退出线程
	public void exit() {
		_exit = true;
	}

	// 设置心跳间隔
	public void setInterval(int seconds) {
		_loopCount = seconds*10;
	}

	@Override
	public void run() {
		
		int loopCount = 1;
		while (!_exit) {
			if (loopCount % _loopCount == 0) {
				heartBeat();
				
				loopCount = 0; // 重置循环计数
			}

			loopCount++; // 增加循环计数
			
			try {
				Thread.sleep(100); // 休眠100ms
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
