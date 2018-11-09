package com.icoffice.library.bean.db;
/**
 * 雷云峰机器 门与云台的状态设置
 * @author lufeisong
 *
 */
@SuppressWarnings("serial")
public class LYMachineSetBean extends BaseDbBean{
	private Integer doorStatus;//0 门不关闭， 1  门关闭
	private Integer elevatorStatus;//0 不使用云台， 1 使用云台
	public Integer getDoorStatus() {
		return doorStatus;
	}
	public void setDoorStatus(Integer doorStatus) {
		this.doorStatus = doorStatus;
	}
	public Integer getElevatorStatus() {
		return elevatorStatus;
	}
	public void setElevatorStatus(Integer elevatorStatus) {
		this.elevatorStatus = elevatorStatus;
	}
	
	
}
