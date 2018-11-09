package com.icoffice.library.bean;

import com.icoffice.library.bean.db.GoodsBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 单个分类下 商品集合详情
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class GoodsSortBean implements Serializable{
	private String gt_id;
	private String gt_name;
	private String gt_ename;
	private ArrayList<GoodsBean> goodsBean = new ArrayList<GoodsBean>();
	
	public String getGt_ename() {
		return gt_ename;
	}

	public void setGt_ename(String gt_ename) {
		this.gt_ename = gt_ename;
	}


	public String getGt_id() {
		return gt_id;
	}

	public void setGt_id(String gt_id) {
		this.gt_id = gt_id;
	}

	public String getGt_name() {
		return gt_name;
	}

	public void setGt_name(String gt_name) {
		this.gt_name = gt_name;
	}

	public ArrayList<GoodsBean> getGoodsBean() {
		return goodsBean;
	}

	public void setGoodsBean(ArrayList<GoodsBean> goodsBean) {
		this.goodsBean = goodsBean;
	}
	
	
}
