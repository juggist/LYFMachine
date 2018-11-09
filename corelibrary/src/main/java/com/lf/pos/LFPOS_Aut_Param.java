package com.lf.pos;

//预授权申请
public class LFPOS_Aut_Param {
	private int AutParam;
	
	private String szShopId;   // 商户号
	private String szPosId;     // 终端号
	private int uPayment;       // 消费金额，取值不能为0，以人民币分为单位
	
	public LFPOS_Aut_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		AutParam = createAutParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyAutParamObject(AutParam);
		super.finalize();
	}
	
	public void setShopId(String shopId) {
		this.szShopId = shopId;
	}
	
	public void setPosId(String posId) {
		this.szPosId = posId;
	}
	
	public void setPaymentAmount(int paymentAmount) {
		this.uPayment = paymentAmount;
	}
	
	public int setNativeObject() {
		saveAutParamObject(this, AutParam);
		return AutParam;
	}
	
	private static native void saveAutParamObject(LFPOS_Aut_Param param, int address);
	//private static native void loadNativeObject(LFPOSTradeParam param, int address);
	private native static int createAutParamObject();
	private native static void destroyAutParamObject(int object);
}
