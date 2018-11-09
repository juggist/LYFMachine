package com.lf.pos;


//读银行卡失败时的信息
public class LFPOS_Badcard_Info {
	private int BadcardInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private int eHow; // 用户操作是刷卡/插卡/挥卡	, 取值为 LFPOS_READER
	 // 根据用户是插IC卡还是刷磁卡还是挥卡，应提示用户重新操作。
	// 读卡失败，对读卡超时的计数没有影响。
	
	public LFPOS_Badcard_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		BadcardInfoObject = createBadcardInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyBadcardInfoObject(BadcardInfoObject);
		super.finalize();
	}

	public int geteHow()
	{
		return this.eHow;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadBadcardInfoObject(this, BadcardInfoObject);
	}
	

	private static native void loadBadcardInfoObject(LFPOS_Badcard_Info param, int address);
	private native static int createBadcardInfoObject();
	private native static void destroyBadcardInfoObject(int object);
}
