package com.icoffice.library.bean.db;
/**
 * app启动 关闭时间
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class AppRestarTime extends BaseDbBean{
	private String time;
	private Integer mode;//0-app启动时间 1—凌晨自动重启
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
}
