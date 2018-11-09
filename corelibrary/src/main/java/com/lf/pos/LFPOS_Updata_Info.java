package com.lf.pos;


//POS更新信息
public class LFPOS_Updata_Info {
	private int UpdataInfoObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String mode;//0- 不需要更新 1-需要更新 2-更新数据中 3-更新包出错 4-等待POS应答包
	private int  sumnum;//需要下载的总帧数;
	private int  currentnum;//当前帧数;
	
	public LFPOS_Updata_Info() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		UpdataInfoObject = createUpdataInfoObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyUpdataInfoObject(UpdataInfoObject);
		super.finalize();
	}

	public String getmode()
	{
		return this.mode;
	}
	public int getsumnum()
	{
		return this.sumnum;
	}
	public int getcurrentnum()
	{
		return this.currentnum;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadUpdataInfoObject(this, UpdataInfoObject);
	}
	

	private static native void loadUpdataInfoObject(LFPOS_Updata_Info param, int address);
	private native static int createUpdataInfoObject();
	private native static void destroyUpdataInfoObject(int object);
}
