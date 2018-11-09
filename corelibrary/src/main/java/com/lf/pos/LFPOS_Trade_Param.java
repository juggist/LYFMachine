package com.lf.pos;

/**
 * 非线程安全的数据
 * @author 
 *
 */
public class LFPOS_Trade_Param {// implements LFPOSNativeData {
	private int TradeParamObject;
	
	private String szShopId; // 商户号
	private String szPosId; // 终端号
	private int uPayment;       // 消费金额，取值不能为0，以人民币分为单位
	private int bNoReader;      // 某bit为1表示“不允许”对应的读卡方式
	// 例如，bNoReader等于0表示读卡方式无限制，而等于
	// (LFPOS_R_SWIPE|LFPOS_R_PLUG)表示不允许刷磁卡或插IC卡，只允许挥卡
	private int bType; // 卡片类型
	private String szCardNo;// 卡号
	
	public LFPOS_Trade_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		TradeParamObject = createTradeParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyTradeParamObject(TradeParamObject);
		super.finalize();
	}
	
	public String getShopId() {
		return szShopId;
	}
	
	public void setShopId(String shopId) {
		this.szShopId = shopId;
	}
	
	public String getPosId() {
		return szPosId;
	}
	
	public void setPosId(String posId) {
		this.szPosId = posId;
	}
	
	public int getPaymentAmount() {
		return uPayment;
	}
	
	public void setPaymentAmount(int paymentAmount) {
		this.uPayment = paymentAmount;
	}
	
	public int isNoReader() {
		return bNoReader;
	}
	
	public void setNoReader(int isNoReader) {
		this.bNoReader = isNoReader;
	}
	
	public int getCardType() {
		return bType;
	}
	
	public void setCardType(int cardType) {
		this.bType = cardType;
	}
	
	public String getCardNo() {
		return szCardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.szCardNo = cardNo;
	}
	
	//将交易的结构体数据传到C层
	public int setNativeObject() {
		saveTradeParamObject(this, TradeParamObject);
		return TradeParamObject;
	}
	private static native void saveTradeParamObject(LFPOS_Trade_Param param, int address);
	private native static int createTradeParamObject();
	private native static void destroyTradeParamObject(int object);
}
