package com.lf.pos;


//当 desc 字段等于 LFPOS_A_ECASH 时，返回离线交易(电子现金账户)的以下结构
public class LFPOS_Trade_Ecash_Result {
	private int TradeEcashResultObject;
	
	LFPOS_RTNHDR hdr = new LFPOS_RTNHDR();
	private String szAmount;   // 消费金额(对应银联报文字段)
	private String szPan;      // 卡号
	private String szShopId;   // 商户号
	private String szPosId;     // 终端号
	private String szShopName; // 商户名
	private String szOperatorNo;// 操作员
	private String szVoucherNo; // 交易的凭证号
	private String szBatch;     // 交易的批次号
	private String szType;     // 交易类型
	private int swiptype;         //刷卡类型
	private String szExpDate;   // 卡有效期 (YYMM)
	private String szDateTime; // 脱机交易发生的时间 (YYYYMMDDHHMMSS)，不是银联返回的交易时间
	// 以下是符合PBOC的小额支付交易返回的数据
	private String szBalance;  // 电子现金余额

	public LFPOS_Trade_Ecash_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		TradeEcashResultObject = createTradeEcashResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyTradeEcashResultObject(TradeEcashResultObject);
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
	public String getShopName()
	{
		return this.szShopName;
	}
	public String getOperatorNo()
	{
		return this.szOperatorNo;
	}
	public String getVoucherNo()
	{
		return this.szVoucherNo;
	}
	public String getBatch()
	{
		return this.szBatch;
	}
	public String getType()
	{
		return this.szType;
	}
	public String getExpDate()
	{
		return this.szExpDate;
	}
	public String getDateTime()
	{
		return this.szDateTime;
	}
	public String getBalance()
	{
		return this.szBalance;
	}
	
	public void getNativeObject() {
		hdr.getNativeObject();
		loadTradeEcashResultObject(this, TradeEcashResultObject);
	}
	

	private static native void loadTradeEcashResultObject(LFPOS_Trade_Ecash_Result param, int address);
	private native static int createTradeEcashResultObject();
	private native static void destroyTradeEcashResultObject(int object);
}
