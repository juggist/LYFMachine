package com.lf.pos;


//结算时间配置
public class LFPOS_Settle_Mode {
	private int SettleModeObject;
	
	// 结算模式
	//   0－不自动结算
	//   1－自动结算
	private int settle_mode;
	// 自动结算时间
	// 结算模式为1-自动结算时有效
	// 格式：BCD码 (HHMMSS)
	private int[] settle_time = new int[3];
	
	public LFPOS_Settle_Mode() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		SettleModeObject = createSettleModeObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroySettleModeObject(SettleModeObject);
		super.finalize();
	}
	
	public void setSettleMode(int settle_mode) {
		this.settle_mode = settle_mode;
	}
	
	public void setSettleTime(int[] settle_time) {
		this.settle_time[0] = settle_time[0];
		this.settle_time[1] = settle_time[1];
		this.settle_time[2] = settle_time[2];
	}
	
	public int setNativeObject() {
		saveSettleModeObject(this, SettleModeObject);
		return SettleModeObject;
	}
	
	private static native void saveSettleModeObject(LFPOS_Settle_Mode param, int address);
	private native static int createSettleModeObject();
	private native static void destroySettleModeObject(int object);
}
