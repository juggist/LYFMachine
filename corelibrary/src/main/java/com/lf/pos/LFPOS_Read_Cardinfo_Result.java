package com.lf.pos;


//读卡号的结果
public class LFPOS_Read_Cardinfo_Result {
	private int ReadCardinfoResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szPan;    // 卡号
	private int eType;   // 卡的类型, 取值为 LFPOS_ETYPE
	
	public LFPOS_Read_Cardinfo_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		ReadCardinfoResultObject = createReadCardinfoResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyReadCardinfoResultObject(ReadCardinfoResultObject);
		super.finalize();
	}
	
	public String getszPan() {
		return szPan;
	}
	
	public int geteType() {
		return eType;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadReadCardinfoResultObject(this, ReadCardinfoResultObject);
	}
	

	private static native void loadReadCardinfoResultObject(LFPOS_Read_Cardinfo_Result param, int address);
	private native static int createReadCardinfoResultObject();
	private native static void destroyReadCardinfoResultObject(int object);
}
