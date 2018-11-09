package com.icoffice.library.bean.network;
/**
 * 获取微信支付状态
 * @author lufeisong
 *
 */
public class StatusWeChatStateBean {
	private String status;
	private String msg;
	private String number;
	private String clientOrderNo;
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String o_number) {
		this.number = o_number;
	}
	public String getClientOrderNo() {
		return clientOrderNo;
	}
	public void setClientOrderNo(String clientOrderNo) {
		this.clientOrderNo = clientOrderNo;
	}
	
}
