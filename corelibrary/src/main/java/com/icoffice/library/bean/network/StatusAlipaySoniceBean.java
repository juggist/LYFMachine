package com.icoffice.library.bean.network;
/**
 * 支付宝返回bean
 * @author lufeisong
 *
 */
public class StatusAlipaySoniceBean {
	private String status;
	private String msg	;	
	private String result_code	;
	private String number;
	private String clientOrderNo;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
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
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getClientOrderNo() {
		return clientOrderNo;
	}
	public void setClientOrderNo(String clientOrderNo) {
		this.clientOrderNo = clientOrderNo;
	}
	

}
