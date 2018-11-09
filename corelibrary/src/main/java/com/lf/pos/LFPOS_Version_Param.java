package com.lf.pos;


//获取版本的数据
public class LFPOS_Version_Param {
	private int VersionParamObject;
	
	private int bType;          // 获取版本的类型 0-POS终端的版本, 1-程序文件的版本（程序文件的路径由szLocalPak给出)
	private String szLocalPkg;   // 文件的本地路径
	
	public LFPOS_Version_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		VersionParamObject = createVersionParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyVersionParamObject(VersionParamObject);
		super.finalize();
	}
	
	public int getbType() {
		return bType;
	}
	
	public void setbType(int bType) {
		this.bType = bType;
	}
	
	public String getszLocalPkg() {
		return szLocalPkg;
	}
	
	public void setszLocalPkg(String szLocalPkg) {
		this.szLocalPkg = szLocalPkg;
	}
	
	public int getLocalObject(){
		return VersionParamObject;
	}
	
	public void getNativeObject() {
		loadVersionParamObject(this, VersionParamObject);
	}
	
	public int setNativeObject() {
		saveVersionParamObject(this, VersionParamObject);
		return VersionParamObject;
	}

	private static native void loadVersionParamObject(LFPOS_Version_Param param, int address);
	private static native void saveVersionParamObject(LFPOS_Version_Param param, int address);
	private native static int createVersionParamObject();
	private native static void destroyVersionParamObject(int object);
}