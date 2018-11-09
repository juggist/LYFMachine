package com.icoffice.library.bean.network;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.icoffice.library.bean.db.RecoverAssistWayBean;

/**
 * 用户登入bean
 */
public class StatusUserBean extends BaseStatusBean{
	private String u_id;
	private String u_name;
	private HashMap<String,RecoverAssistWayBean> wayBean_map = new LinkedHashMap<String, RecoverAssistWayBean>();
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public HashMap<String, RecoverAssistWayBean> getWayBean_map() {
		return wayBean_map;
	}
	public void setWayBean_map(HashMap<String, RecoverAssistWayBean> wayBean_map) {
		this.wayBean_map = wayBean_map;
	}
		
}
