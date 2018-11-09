package com.icoffice.library.bean.db;
/**
 * 数据备份
 * 辅助
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class RecoverAssistWayBean extends RecoveryWayBean{
	private Integer store_differ;//补货，减货，换货，库库存差

	public Integer getStore_differ() {
		return store_differ;
	}

	public void setStore_differ(Integer store_differ) {
		this.store_differ = store_differ;
	}
	
}
