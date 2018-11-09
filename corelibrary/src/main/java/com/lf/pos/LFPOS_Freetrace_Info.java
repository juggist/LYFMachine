package com.lf.pos;


//POS剩余流水数的提示
public class LFPOS_Freetrace_Info {
	private int FreetraceInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	// POS还能存储流水的条数
	private int uFreeTraceNum;
	
	public LFPOS_Freetrace_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		FreetraceInfoObject = createFreetraceInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyFreetraceInfoObject(FreetraceInfoObject);
		super.finalize();
	}
	
	public int getFreeTraceNum()
	{
		return this.uFreeTraceNum;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadFreetraceInfoObject(this, FreetraceInfoObject);
	}
	

	private static native void loadFreetraceInfoObject(LFPOS_Freetrace_Info param, int address);
	private native static int createFreetraceInfoObject();
	private native static void destroyFreetraceInfoObject(int object);
}
