package com.lf.pos;

public class LFPOS_Mifare_Result {
	private int MifareResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String readsector;
	private int SectorReadLen;
	
	public LFPOS_Mifare_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		MifareResultObject = createMifareResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyMifareResultObject(MifareResultObject);
		super.finalize();
	}
	
	public String getreadsector()
	{
		return this.readsector;
	}
	
	public int getSectorReadLen()
	{
		return this.SectorReadLen;
	}
	
	public void getNativeObject() {
		//hdr.getNativeObject();
		loadMifareResultObject(this, MifareResultObject);
	}
	

	private static native void loadMifareResultObject(LFPOS_Mifare_Result param, int address);
	private native static int createMifareResultObject();
	private native static void destroyMifareResultObject(int object);
}
