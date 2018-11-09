package com.icoffice.library.bean.db;
/**
 * 数据备份表
 * 补货 提交到服务器的补货信息
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class RecoveryRecordsBean extends BaseDbBean{
	private String version;//补货单号
	private String records0;//退货记录(可传)
	private String records1;//补货记录(可传)
	private String records2;//出货记录(可传)
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRecords0() {
		return records0;
	}
	public void setRecords0(String records0) {
		this.records0 = records0;
	}
	public String getRecords1() {
		return records1;
	}
	public void setRecords1(String records1) {
		this.records1 = records1;
	}
	public String getRecords2() {
		return records2;
	}
	public void setRecords2(String records2) {
		this.records2 = records2;
	}
	
	
}
