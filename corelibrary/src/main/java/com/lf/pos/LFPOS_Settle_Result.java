package com.lf.pos;


//结算的结果
//金额都以人民币分为单位
public class LFPOS_Settle_Result {
	private int SettleResultObject;

	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private boolean fEnd; // TRUE 表示所有商户都已处理，没有更多的待执行的结算
	private String szShopId; // 商户号
	private String szPosId;   // 终端号
	private String szDateTime;  // 结算时间 (YYYYMMDDHHMMSS)
	private int  iInEquivalent;     // 内卡对账结果(0表示内卡对账平，1表示内卡对账不平)
	private int uInSaleAmount;     // 内卡消费总金额
	private int uInSaleSum;        // 内卡消费总笔数
	private int uInRefundAmount;   // 内卡退货总金额
	private int uInRefundSum;      // 内卡退货总笔数
	private int uInOfflineAmount;  // 内卡离线交易总金额
	private int uInOfflineSum;     // 内卡离线交易总笔数
	private int uInECAmount;       // 内卡电子现金消费总金额
	private int uInECSum;          // 内卡电子现金总笔数
	private int uInLoadAmount;     // 内卡圈存总金额
	private int uInLoadSum;        // 内卡圈存总笔数
	private int uInRevokeAmount;   // 内卡交易撤销总金额
	private int uInRevokeSum;      // 内卡交易撤销总笔数
	private int  iOutEquivalent;    // 外卡对账结果(0表示外卡对账平，1表示外卡对账不平)
	private int uOutSaleAmount;    // 外卡消费总金额
	private int uOutSaleSum;       // 外卡消费总笔数
	private int uOutRefundAmount;  // 外卡退货总金额
	private int uOutRefundSum;     // 外卡退货总笔数
	private int uOutOfflineAmount; // 外卡离线交易总金额
	private int uOutOfflineSum;    // 外卡离线交易总笔数
	private int uOutECAmount;      // 外卡电子现金消费总金额
	private int uOutECSum;         // 外卡电子现金总笔数
	private int uOutLoadAmount;    // 外卡圈存总金额
	private int uOutLoadSum;       // 外卡圈存总笔数
	private int uOutRevokeAmount;  // 外卡交易撤销总金额
	private int uOutRevokeSum;     // 外卡交易撤销总笔数
	
	public LFPOS_Settle_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		SettleResultObject = createSettleResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroySettleResultObject(SettleResultObject);
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
	public String getDateTime()
	{
		return this.szDateTime;
	}
	public int getInEquivalent()
	{
		return this.iInEquivalent;
	}
	public int getInSaleAmount()
	{
		return this.uInSaleAmount;
	}
	public int getInSaleSum()
	{
		return this.uInSaleSum;
	}
	public int getInRefundAmount()
	{
		return this.uInRefundAmount;
	}
	public int getInRefundSum()
	{
		return this.uInRefundSum;
	}
	public int getInOfflineAmount()
	{
		return this.uInOfflineAmount;
	}
	public int getInOfflineSum()
	{
		return this.uInOfflineSum;
	}
	public int getInECAmount()
	{
		return this.uInECAmount;
	}
	public int getInECSum()
	{
		return this.uInECSum;
	}
	public int getInLoadAmount()
	{
		return this.uInLoadAmount;
	}
	public int getInLoadSum()
	{
		return this.uInLoadSum;
	}
	public int getInRevokeAmount()
	{
		return this.uInRevokeAmount;
	}
	public int getInRevokeSum()
	{
		return this.uInRevokeSum;
	}
	public int getOutEquivalent()
	{
		return this.iOutEquivalent;
	}
	public int getOutSaleAmount()
	{
		return this.uOutSaleAmount;
	}
	public int getOutSaleSum()
	{
		return this.uOutSaleSum;
	}
	public int getOutRefundAmount()
	{
		return this.uOutRefundAmount;
	}
	public int getOutRefundSum()
	{
		return this.uOutRefundSum;
	}
	public int getOutOfflineAmount()
	{
		return this.uOutOfflineAmount;
	}
	public int getOutOfflineSum()
	{
		return this.uOutOfflineSum;
	}
	public int getOutECAmount()
	{
		return this.uOutECAmount;
	}
	public int getOutECSum()
	{
		return this.uOutECSum;
	}
	public int getOutLoadAmount()
	{
		return this.uOutLoadAmount;
	}
	public int getOutLoadSum()
	{
		return this.uOutLoadSum;
	}
	public int getOutRevokeAmount()
	{
		return this.uOutRevokeAmount;
	}
	public int getOutRevokeSum()
	{
		return this.uOutRevokeSum;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadSettleResultObject(this, SettleResultObject);
	}
	

	private static native void loadSettleResultObject(LFPOS_Settle_Result param, int address);
	private native static int createSettleResultObject();
	private native static void destroySettleResultObject(int object);
}
