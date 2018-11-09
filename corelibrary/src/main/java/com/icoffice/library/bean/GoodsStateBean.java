package com.icoffice.library.bean;

import com.icoffice.library.bean.db.GoodsBean;

/**
 * 后台：
 * 商品状态bean
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class GoodsStateBean extends GoodsBean{
//	private GoodsBean mGoodsBean;//该商品的详情
	private Integer sold_status;//修改后在售的状态 0:false 1:true
	private String price;//修改后的价格
//	public GoodsBean getmGoodsBean() {
//		return mGoodsBean;
//	}
//	public void setmGoodsBean(GoodsBean mGoodsBean) {
//		this.mGoodsBean = mGoodsBean;
//	}
	public Integer isSold_status() {
		return sold_status;
	}
	public void setSold_status(Integer sold_status) {
		this.sold_status = sold_status;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
