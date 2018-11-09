package com.lf.pos;


//读卡的数据
public class LFPOS_Read_Cardno_Param {
	private int ReadCardnoParamObject;
	
	private int eType;   // 卡的类型 , 例如：eType = (LFPOSEtype).LFPOS_T_ALL 值为LFPOSEtype里面的值
	private int bNoReader;      // 某bit为1表示“不允许”对应的读卡方式
	// 例如，bNoReader等于0表示读卡方式无限制，而等于
	// (LFPOS_R_SWIPE|LFPOS_R_PLUG)表示不允许刷磁卡或插IC卡，只允许挥卡
	
	public LFPOS_Read_Cardno_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		ReadCardnoParamObject = createReadCardnoParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyReadCardnoParamObject(ReadCardnoParamObject);
		super.finalize();
	}
	
	public int getEtype() {
		return eType;
	}
	
	public void setEtype(int eType) {
		this.eType = eType;
	}
	
	public int getNoReader() {
		return bNoReader;
	}
	
	public void setNoReader(int bNoReader) {
		this.bNoReader = bNoReader;
	}
	
	public int getLocalObject() {
		return ReadCardnoParamObject;
	}
	
	public void getNativeObject() {
		loadReadCardnoParamObject(this, ReadCardnoParamObject);
	}
	
	public int setNativeObject() {
		saveReadCardnoParamObject(this, ReadCardnoParamObject);
		return ReadCardnoParamObject;
	}
	
	private static native void loadReadCardnoParamObject(LFPOS_Read_Cardno_Param param, int address);
	private static native void saveReadCardnoParamObject(LFPOS_Read_Cardno_Param param, int address);
	private native static int createReadCardnoParamObject();
	private native static void destroyReadCardnoParamObject(int object);
}