package com.lf.pos;


//消费撤销的数据
public class LFPOS_Revoke_Param {
	private int RevokeParamObject;
	
	private String szShopId; // 商户号
	private String szPosId; // 终端号
	private String szVoucherNo; // 交易的凭证号(用以指定待撤销的交易)
	
	public LFPOS_Revoke_Param() {
		// 创建本地对象，可以是结构休或者是C++类，将地址保存
		RevokeParamObject = createRevokeParamObject();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// 销毁本地对象
		destroyRevokeParamObject(RevokeParamObject);
		super.finalize();
	}
	
	public void setShopId(String shopId) {
		this.szShopId = shopId;
	}
	
	public void setPosId(String posId) {
		this.szPosId = posId;
	}
	
	public void setszVoucherNo(String VoucherNo) {
		this.szVoucherNo = VoucherNo;
	}
	
	public int setNativeObject() {
		saveRevokeParamObject(this, RevokeParamObject);
		return RevokeParamObject;
	}
	
	private static native void saveRevokeParamObject(LFPOS_Revoke_Param param, int address);
	private native static int createRevokeParamObject();
	private native static void destroyRevokeParamObject(int object);
}
