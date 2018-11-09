package com.lf.pos;

public class LFPOS_Ask_Card_Info {
	private int AskCardInfoObject;
	
	public LFPOS_RTNHDR hdr;
	private int bReader; // 某bit为1表示允许对应的读卡方式
	
	public LFPOS_Ask_Card_Info() {
		hdr = new LFPOS_RTNHDR();
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		AskCardInfoObject = createAskCardInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyAskCardInfoObject(AskCardInfoObject);
		super.finalize();
	}

	public int getbReader()
	{
		return this.bReader;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadAskCardInfoObject(this, AskCardInfoObject);
	}
	

	private static native void loadAskCardInfoObject(LFPOS_Ask_Card_Info param, int address);
	private native static int createAskCardInfoObject();
	private native static void destroyAskCardInfoObject(int object);
}
