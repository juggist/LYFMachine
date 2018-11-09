package com.leiyuntop.machine;

/**
 * 售货机事件
 * 
 * @author MENGQINGYU
 */
public interface IMachineEventHandler
{
	/**
	 * 售货机事件
	 * 
	 * @param packageCode
	 *            包代码
	 * @param obj
	 *            数据
	 */
	void InfoEventHandler(int packageCode, ISCInfo obj);

	/**
	 * 售货机故障
	 * 
	 * @param etype
	 *            故障代码
	 * @param message
	 *            故障描述
	 */
	void ErrorEventHandler(int etype, String message);
}
