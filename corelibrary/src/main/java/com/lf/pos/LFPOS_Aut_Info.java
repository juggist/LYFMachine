package com.lf.pos;


//预授权应答
public class LFPOS_Aut_Info {
	private int AutInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String description;
	private int result;
	
	public LFPOS_Aut_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		AutInfoObject = createAutInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyAutInfoObject(AutInfoObject);
		super.finalize();
	}
	
	public String getdescription()
	{
		return this.description;
	}
	
	public int getresult()
	{
		return this.result;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadAutInfoObject(this, AutInfoObject);
	}
	

	private static native void loadAutInfoObject(LFPOS_Aut_Info param, int address);
	private native static int createAutInfoObject();
	private native static void destroyAutInfoObject(int object);
}
