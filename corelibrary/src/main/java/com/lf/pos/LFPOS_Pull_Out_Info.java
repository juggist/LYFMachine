package com.lf.pos;


//提示拔出卡片时的信息
public class LFPOS_Pull_Out_Info {
	private int PullOutInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private int eType; // 卡片类型, 取值为 LFPOS_READER
	// 当卡片类型为磁卡时，应提示用户快速拔出卡片
	// (因为此款POS终端型号，读磁卡只发生在拔出的过程中)
	// 其它类型时，仅仅提示用户拔出卡片
	
	public LFPOS_Pull_Out_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		PullOutInfoObject = createPullOutInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyPullOutInfoObject(PullOutInfoObject);
		super.finalize();
	}

	public int getType()
	{
		return this.eType;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadPullOutInfoObject(this, PullOutInfoObject);
	}
	

	private static native void loadPullOutInfoObject(LFPOS_Pull_Out_Info param, int address);
	private native static int createPullOutInfoObject();
	private native static void destroyPullOutInfoObject(int object);
}
