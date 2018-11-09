package com.lf.pos;

//设备状态数据
public class LFPOS_Out_State {
	private int OutStateParamObject;
	
	// 当前状态	取值从 LFPOS_STATE 中取
	private int sta = 10;
	// 当前银联操作，仅当 sta 为 LFPOS_S_BUSY 时有效，	取值从 LFPOS_EVENT 中取
	// 有效值 > LFPOS_EVT_NONE 并且 < LFPOS_EVT_FROM_POS
	private int op = 11;
	
	public LFPOS_Out_State() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		OutStateParamObject = createOutStateParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyOutStateParamObject(OutStateParamObject);
		super.finalize();
	}
	
	public int getsta() {
		return sta;
	}
	
	public int getop() {
		return op;
	}
	
	public int getLocalObject() {
		return OutStateParamObject;
	}
	
	public void getNativeObject() {
		loadOutStateParamObject(this, OutStateParamObject);
	}
	
	private static native void loadOutStateParamObject(LFPOS_Out_State param, int address);
	private native static int createOutStateParamObject();
	private native static void destroyOutStateParamObject(int object);
}