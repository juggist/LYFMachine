package com.lf.pos;


//消费的结果
//消费成功时，根据 desc 字段等于 LFPOS_A_NORMAL 还是等于
//LFPOS_A_ECASH 决定返回的是以下哪个结构
//以下返回的字段都为字符串，并且包含结束符'\0'

//当 desc 字段等于 LFPOS_A_NORMAL 时，返回在线交易(借贷记账户)的以下结构
public class LFPOS_Trade_Normal_Result {
	private int TradeNormalResultObject;
	
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
	// 以下是借贷记账户交易返回的数据
	private String szISS;      // 发卡行
	private String szACQ;      // 收单行
	private String szRefNum;   // 交易的系统参考号
	private String szDateTime; // 交易时间 (YYYYMMDDHHMMSS)
	private String AuthNo;      // 授权码
	
	public LFPOS_Trade_Normal_Result() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		TradeNormalResultObject = createTradeNormalResultObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyTradeNormalResultObject(TradeNormalResultObject);
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
	public String getISS()
	{
		return this.szISS;
	}
	public String getACQ()
	{
		return this.szACQ;
	}
	public String getRefNum()
	{
		return this.szRefNum;
	}
	public String getDateTime()
	{
		return this.szDateTime;
	}
	public String getAuthNo()
	{
		return this.AuthNo;
	}
	
	//将交易的结果读取回来
	public void getNativeObject() {
		hdr.getNativeObject();
		loadTradeNormalResultObject(this, TradeNormalResultObject);
	}
	

	private static native void loadTradeNormalResultObject(LFPOS_Trade_Normal_Result param, int address);
	private native static int createTradeNormalResultObject();
	private native static void destroyTradeNormalResultObject(int object);
}
