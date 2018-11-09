package com.lf.pos;


//消费撤销的结果
public class LFPOS_Revoke_Result {
	private int RevokeResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szAmount;   // 消费金额(已被撤销)
	private String szPan;      // 卡号
	private String szShopId;   // 商户号
	private String szPosId;     // 终端号
	
	public LFPOS_Revoke_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		RevokeResultObject = createRevokeResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyRevokeResultObject(RevokeResultObject);
		super.finalize();
	}

	public String getAmount()
	{
		return this.szAmount;
	}
	public String getPan()
	{
		return this.szPan;
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
		loadRevokeResultObject(this, RevokeResultObject);
	}
	

	private static native void loadRevokeResultObject(LFPOS_Revoke_Result param, int address);
	private native static int createRevokeResultObject();
	private native static void destroyRevokeResultObject(int object);
}
