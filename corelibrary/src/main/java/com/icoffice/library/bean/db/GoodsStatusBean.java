package com.icoffice.library.bean.db;
/**
 * 数据库：
 * 商品状态bean
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class GoodsStatusBean extends BaseDbBean{
	private String g_code;//商品code
	private String unit_price;//商品修改后的在售单价
	private Integer sold_status;//商品修改后的在售状态 0:false 1:true
	public String getG_code() {
		return g_code;
	}
	public void setG_code(String g_code) {
		this.g_code = g_code;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public Integer getSold_status() {
		return sold_status;
	}
	public void setSold_status(Integer sold_status) {
		this.sold_status = sold_status;
	}
	
}
