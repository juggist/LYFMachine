package com.icoffice.library.bean.db;
/**
 * 数据备份表
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class RecoveryWayBean extends BaseDbBean {
		private String way_id;//货道id
		private String g_code;//商品g_code
		private Integer maxNum;//最大库存量
		private Integer storeNum;//库存量
		private Integer status;//0_货道异常，1_货道正常
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
		
	}
