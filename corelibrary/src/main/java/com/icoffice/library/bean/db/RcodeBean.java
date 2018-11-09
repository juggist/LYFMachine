package com.icoffice.library.bean.db;
/**
 * 微信兑换 各个商品详情
 * 
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class RcodeBean extends SoldGoodsBean{
	private String order_gl_id;  //订单中商品包含的产品ID
	private String g_id; 		 //兑换的产品ID
	private String amounts; 	 //可兑换的产品数量
//	private String way_id;//货道id
//	private Integer storeNum;//库存量
	public String getOrder_gl_id() {
		return order_gl_id;
	}
	public void setOrder_gl_id(String order_gl_id) {
		this.order_gl_id = order_gl_id;
	}
	public String getG_id() {
		return g_id;
	}
	public void setG_id(String g_id) {
		this.g_id = g_id;
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
//	public String getWay_id() {
//		return way_id;
//	}
//	public void setWay_id(String way_id) {
//		this.way_id = way_id;
//	}
//	public Integer getStoreNum() {
//		return storeNum;
//	}
//	public void setStoreNum(Integer storeNum) {
//		this.storeNum = storeNum;
//	}
	
}
