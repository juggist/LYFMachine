package com.icoffice.library.bean.network;
/**
 * 微商城兑换
 * 增加兑换记录
 * @author lufeisong
 *
 */
public class StatusAddChangeLogStatus extends BaseStatusBean{
	private String code ; //返回状态ID：-1为空值错误,0为返回空值,1为返回成功,2产品已兑换完

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
