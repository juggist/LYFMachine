package com.lf.pos;


//圈存时提示读银行卡时的信息
public class LFPOS_Load_Ask_Card_Info {
	private int LoadAskCardInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private int bReader; // 某bit为1表示允许对应的读卡方式
	private int bInOrOut; // 0 - 提示刷转入卡, 1 - 转出卡
	
	public LFPOS_Load_Ask_Card_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		LoadAskCardInfoObject = createLoadAskCardInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyLoadAskCardInfoObject(LoadAskCardInfoObject);
		super.finalize();
	}

	public int getReader()
	{
		return this.bReader;
	}
	public int getInOrOut()
	{
		return this.bInOrOut;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadLoadAskCardInfoObject(this, LoadAskCardInfoObject);
	}
	

	private static native void loadLoadAskCardInfoObject(LFPOS_Load_Ask_Card_Info param, int address);
	private native static int createLoadAskCardInfoObject();
	private native static void destroyLoadAskCardInfoObject(int object);
}
