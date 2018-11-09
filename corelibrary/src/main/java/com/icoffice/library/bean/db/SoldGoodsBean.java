package com.icoffice.library.bean.db;
/**
 * 在售商品的信息
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class SoldGoodsBean extends GoodsBean{
	private Integer storeNum;
	private String w_code;

	public Integer getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}

	public String getW_code() {
		return w_code;
	}

	public void setW_code(String w_code) {
		this.w_code = w_code;
	}
	
}
