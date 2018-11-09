package com.icoffice.library.bean.db;
/**
 * 机器状态设置
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class MachineSetBean extends BaseDbBean{
	private String coinAndnoteStatus;//0_关闭  1_打开
	private String coinWaitTime;
	private String physicsButtonStatus;//物理按钮 0_关闭  1_打开
	public String getCoinAndnoteStatus() {
		return coinAndnoteStatus;
	}
	public void setCoinAndnoteStatus(String coinAndnoteStatus) {
		this.coinAndnoteStatus = coinAndnoteStatus;
	}
	public String getCoinWaitTime() {
		return coinWaitTime;
	}
	public void setCoinWaitTime(String coinWaitTime) {
		this.coinWaitTime = coinWaitTime;
	}
	public String getPhysicsButtonStatus() {
		return physicsButtonStatus;
	}
	public void setPhysicsButtonStatus(String physicsButtonStatus) {
		this.physicsButtonStatus = physicsButtonStatus;
	}
	
}
