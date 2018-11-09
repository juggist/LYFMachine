package com.icoffice.library.bean.db;
/**
 * 广告信息详情
 * @author lufeisong
 *
 */
public class AdStatusBean {
	private Integer aid;//a_id					广告id
	private Integer atId;//at_id				类型1广告 2 活动
	private String file;//a_file				广告文件
	private String smallImage;//a_small_image	缩略图
	private String startDate;//a_start_date		开始时间
	private String endDate;//a_end_date			结束时间
	private Integer interval;//a_interval		轮播间隔 秒为单位
	private String gCode;//g_code(联表)			对应商品
	private String fileMd5;//					商品md5
	private String smallImageMd5;//				商品缩略图md5
	private Integer download;//                 是否下载（0-未下载； 1-下载）
	public Integer getAid() {
		return aid;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public Integer getAtId() {
		return atId;
	}
	public void setAtId(Integer atId) {
		this.atId = atId;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSmallImage() {
		return smallImage;
	}
	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getgCode() {
		return gCode;
	}
	public void setgCode(String gCode) {
		this.gCode = gCode;
	}
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	public String getSmallImageMd5() {
		return smallImageMd5;
	}
	public void setSmallImageMd5(String smallImageMd5) {
		this.smallImageMd5 = smallImageMd5;
	}
	public Integer getDownload() {
		return download;
	}
	public void setDownload(Integer download) {
		this.download = download;
	}
	
}
