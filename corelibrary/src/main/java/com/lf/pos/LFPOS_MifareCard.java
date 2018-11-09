package com.lf.pos;

public class LFPOS_MifareCard {
private int MifareCardParam;
	
	private int sectorindex;   //扇区号
	private int blockindex;     // 扇区中的块号
	private String sectorkey;       // 扇区密钥
	private String writesector;       // 需写入块的数据
	
	public LFPOS_MifareCard() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		MifareCardParam = createMifareCardParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyMifareCardParamObject(MifareCardParam);
		super.finalize();
	}
	
	public void setsectorindex(int sectorindex) {
		this.sectorindex = sectorindex;
	}
	
	public void setblockindex(int blockindex) {
		this.blockindex = blockindex;
	}
	
	public void setsectorkey(String sectorkey) {
		this.sectorkey = sectorkey;
	}
	
	public void setwritesector(String writesector) {
		this.writesector = writesector;
	}
	
	public int setNativeObject() {
		saveMifareCardParamObject(this, MifareCardParam);
		return MifareCardParam;
	}
	
	private static native void saveMifareCardParamObject(LFPOS_MifareCard param, int address);
	private native static int createMifareCardParamObject();
	private native static void destroyMifareCardParamObject(int object);
}
