package com.icoffice.library.bean.network;
/**
 * 所有商品详情集合
 */
import com.icoffice.library.bean.GoodsSortBean;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GoodsAllBean implements Serializable{
	private String version;
	
	private ArrayList<GoodsSortBean> goodsListAllBean = new ArrayList<GoodsSortBean>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ArrayList<GoodsSortBean> getGoodsListAllBean() {
		return goodsListAllBean;
	}

	public void setGoodsListAllBean(ArrayList<GoodsSortBean> goodsListAllBean) {
		this.goodsListAllBean = goodsListAllBean;
	}
	
	
}
