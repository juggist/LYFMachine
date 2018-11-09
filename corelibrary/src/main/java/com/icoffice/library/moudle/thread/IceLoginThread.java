package com.icoffice.library.moudle.thread;

import com.icoffice.library.ice.Coordinator;
import com.icoffice.library.moudle.control.BaseMachineControl;

public class IceLoginThread extends Thread {

	private final int LOOP_COUNT = 100; // 100次*100ms=10s执行一次

	private BaseMachineControl _machineControl;
	private Coordinator _coordinator;
	private boolean _exit = false;

	public IceLoginThread(BaseMachineControl machineControl,
			Coordinator coordinator) {
		super();
		_machineControl = machineControl;
		_coordinator = coordinator;
	}

	// 退出线程
	public void exit() {
		_exit = true;
	}

	@Override
	public void run() {

		int loopCount = 1;
		while (!_exit) {
			if (loopCount % LOOP_COUNT == 0) {
				_coordinator.ping();
				if (_coordinator.getState() == Coordinator.ClientState.Disconnected) {
					_machineControl.loginIceServer();
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
