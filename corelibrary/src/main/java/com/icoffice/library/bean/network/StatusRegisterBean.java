package com.icoffice.library.bean.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.icoffice.library.bean.db.GoodsStatusBean;
import com.icoffice.library.bean.db.WayBean;
/**
 * 注册机器
 * 解析服务器返回的bean
 * 
 * @author lufeisong
 *
 */
public class StatusRegisterBean extends BaseStatusBean{
	private String m_id;
	private String m_code;
	private String mi_id;
	private String m_no;
	private HashMap<String, String> box_map = new LinkedHashMap<String, String>();//存在的货柜信息
	private ArrayList<GoodsStatusBean> mGoodsStatusBean_list = new ArrayList<GoodsStatusBean>();//商品状态bean集合
	private ArrayList<WayBean> mWayBean_list = new ArrayList<WayBean>();//货道bean集合
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_code() {
		return m_code;
	}
	public void setM_code(String m_code) {
		this.m_code = m_code;
	}
	public String getMi_id() {
		return mi_id;
	}
	public void setMi_id(String mi_id) {
		this.mi_id = mi_id;
	}
	public String getM_no() {
		return m_no;
	}
	public void setM_no(String m_no) {
		this.m_no = m_no;
	}
	public ArrayList<GoodsStatusBean> getmGoodsStatusBean_list() {
		return mGoodsStatusBean_list;
	}
	public void setmGoodsStatusBean_list(
			ArrayList<GoodsStatusBean> mGoodsStatusBean_list) {
		this.mGoodsStatusBean_list = mGoodsStatusBean_list;
	}
	public ArrayList<WayBean> getmWayBean_list() {
		return mWayBean_list;
	}
	public void setmWayBean_list(ArrayList<WayBean> mWayBean_list) {
		this.mWayBean_list = mWayBean_list;
	}
	public HashMap<String, String> getBox_map() {
		return box_map;
	}
	public void setBox_map(HashMap<String, String> box_map) {
		this.box_map = box_map;
	}
	
}
