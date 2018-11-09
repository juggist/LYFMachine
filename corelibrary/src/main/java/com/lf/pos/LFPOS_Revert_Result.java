package com.lf.pos;


//冲正的结果
public class LFPOS_Revert_Result {
	private int RevertResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szShopId;   // 商户号
	private String szPosId;     // 终端号
	private String szPan;      // 银行卡号
	private String szAmount;   // 消费金额
	private String szType;     // 交易类型
	private String szVoucherNo; // 交易的凭证号
	private String szBatch;     // 交易的批次号
	
	public LFPOS_Revert_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		RevertResultObject = createRevertResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyRevertResultObject(RevertResultObject);
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
	public String getPan()
	{
		return this.szPan;
	}
	public String getAmount()
	{
		return this.szAmount;
	}
	public String getType()
	{
		return this.szType;
	}
	public String getVoucherNo()
	{
		return this.szVoucherNo;
	}
	public String getBatch()
	{
		return this.szBatch;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadRevertResultObject(this, RevertResultObject);
	}
	

	private static native void loadRevertResultObject(LFPOS_Revert_Result param, int address);
	private native static int createRevertResultObject();
	private native static void destroyRevertResultObject(int object);
}
