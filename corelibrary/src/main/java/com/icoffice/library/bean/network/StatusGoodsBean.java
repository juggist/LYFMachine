package com.icoffice.library.bean.network;

import java.util.ArrayList;

import com.icoffice.library.bean.db.GoodsBean;

public class StatusGoodsBean extends BaseStatusBean{
	private ArrayList<GoodsBean> list = new ArrayList<GoodsBean>();

	public ArrayList<GoodsBean> getList() {
		return list;
	}

	public void setList(ArrayList<GoodsBean> list) {
		this.list = list;
	}
	
}
