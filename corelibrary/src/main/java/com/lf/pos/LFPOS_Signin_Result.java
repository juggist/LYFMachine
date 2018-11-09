package com.lf.pos;

//签到的结果
public class LFPOS_Signin_Result {
	private int SigninResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private boolean fEnd; // TRUE 表示所有商户都已处理，没有更多的待执行的签到
	private String szShopId; // 商户号
	private String szPosId;   // 终端号
	
	public LFPOS_Signin_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		SigninResultObject = createSigninResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroySigninResultObject(SigninResultObject);
		super.finalize();
	}
	
	public String getShopId()
	{
		return this.szShopId;
	}
	
	public String getPosId()
	{
		return this.szPosId;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadSigninResultObject(this, SigninResultObject);
	}
	

	private static native void loadSigninResultObject(LFPOS_Signin_Result param, int address);
	private native static int createSigninResultObject();
	private native static void destroySigninResultObject(int object);
}
