package com.icoffice.library.bean.network;
/**
 * 轮回 alipay支付情况
 * @author lufeisong
 *
 */
public class StatusAliPayStateBean {
	private String status;
	private String msg;
	private String clientOrderNo;
	private String number;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getClientOrderNo() {
		return clientOrderNo;
	}
	public void setClientOrderNo(String clientOrderNo) {
		this.clientOrderNo = clientOrderNo;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
}
