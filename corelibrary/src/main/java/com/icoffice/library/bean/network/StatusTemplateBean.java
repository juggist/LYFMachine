package com.icoffice.library.bean.network;

import com.icoffice.library.bean.db.WayBean;

import java.util.ArrayList;

/**
 * 模板接口解析 bean
 * 
 *	g_list		可售货品列表
 *		g_code		货品编号
	mw_list		货道状态列表(包含货品及最大补货量)
		g_code		货品编号
		mw_code		货道编号
		mws_inventory	最大补货量

 * @author lufeisong
 *
 */
public class StatusTemplateBean extends BaseStatusBean{
	private ArrayList<String> g_list = new ArrayList<String>();
	private ArrayList<WayBean> mw_list = new ArrayList<WayBean>();
	public ArrayList<String> getG_list() {
		return g_list;
	}
	public void setG_list(ArrayList<String> g_list) {
		this.g_list = g_list;
	}
	public ArrayList<WayBean> getMw_list() {
		return mw_list;
	}
	public void setMw_list(ArrayList<WayBean> mw_list) {
		this.mw_list = mw_list;
	}
	
}
