package com.icoffice.library.bean.network;

import java.util.ArrayList;
/**
 * 网络订单提交bean
 * @author lufeisong
 *
 */
public class StatusNetOrder extends BaseStatusBean{
	private String o_count;
	private ArrayList<String> successarr = new ArrayList<String>();
	public String getO_count() {
		return o_count;
	}
	public void setO_count(String o_count) {
		this.o_count = o_count;
	}
	public ArrayList<String> getSuccessarr() {
		return successarr;
	}
	public void setSuccessarr(ArrayList<String> successarr) {
		this.successarr = successarr;
	}
	
	
}
