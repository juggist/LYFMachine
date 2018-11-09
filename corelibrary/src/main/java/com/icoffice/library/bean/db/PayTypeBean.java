package com.icoffice.library.bean.db;
/**
 * 支付方式bean
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class PayTypeBean extends BaseDbBean{
	private Integer payType;//支付方式
	private Integer payStatus;//支付状态 0_关闭 1_打开

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	
}
