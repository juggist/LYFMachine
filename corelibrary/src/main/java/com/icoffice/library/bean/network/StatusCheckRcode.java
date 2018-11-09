package com.icoffice.library.bean.network;

import com.icoffice.library.bean.db.RcodeBean;

import java.util.ArrayList;

/**
 * 微信兑换 返回信息
 * @author lufeisong
 *
 */
public class StatusCheckRcode extends BaseStatusBean{
	private String code;////返回的状态代码：-1为非空错误,0为返回空值,1为返回成功,2活动的兑换码信息获取成功，-2提示兑换码和售货机已锁定，-3提示售货机5分钟内频繁对服务器端发请求信息
	private String rid; //兑换码ID
	private String oid;//兑换码号码
	private ArrayList<RcodeBean> list = new ArrayList<RcodeBean>();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public ArrayList<RcodeBean> getList() {
		return list;
	}
	public void setList(ArrayList<RcodeBean> list) {
		this.list = list;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
}
