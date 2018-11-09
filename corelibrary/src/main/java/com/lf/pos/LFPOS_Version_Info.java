package com.lf.pos;


//POS版本信息
public class LFPOS_Version_Info {
	private int VersionInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	// 0.POS或者1.应用程序版本
	private int bType;
    private int num;
	private String type;
	private String version[] = new String[10];
	
	public LFPOS_Version_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		VersionInfoObject = createVersionInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyVersionInfoObject(VersionInfoObject);
		super.finalize();
	}

	public int getTypet()
	{
		return this.bType;
	}
	public int getnum()
	{
		return this.num;
	}
	public String gettype()
	{
		return this.type;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadVersionInfoObject(this, VersionInfoObject);
	}
	

	private static native void loadVersionInfoObject(LFPOS_Version_Info param, int address);
	private native static int createVersionInfoObject();
	private native static void destroyVersionInfoObject(int object);
}
