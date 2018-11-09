package com.icoffice.library.bean.db;
/**
 * 货道对应的信息bean
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class WayBean extends BaseDbBean{
	private String way_id;//货道id
	private String g_code;//商品g_code
	private Integer maxNum;//最大库存量
	private Integer storeNum;//库存量
	private Integer status;//0_货道异常，1_货道正常
	private String w_code;//显示货道id
	private Integer w_type;//0_非履带，1_履带
	public String getWay_id() {
		return way_id;
	}
	public void setWay_id(String way_id) {
		this.way_id = way_id;
	}
	public String getG_code() {
		return g_code;
	}
	public void setG_code(String g_code) {
		this.g_code = g_code;
	}
	public Integer getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}
	public Integer getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(Integer storeNum) {
		this.storeNum = storeNum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getW_code() {
		return w_code;
	}
	public void setW_code(String w_code) {
		this.w_code = w_code;
	}
	public Integer getW_type() {
		return w_type;
	}
	public void setW_type(Integer w_type) {
		this.w_type = w_type;
	}
		
}
