package com.leiyuntop.machine;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 售货机状态
 * 
 * @author MENGQINGYU
 */
public class SCStatus extends ISCInfo
{
	/**
	 * 售货机状态
	 */
	public CSMachineState Param1;

	/**
	 * 有效电机数
	 */
	public int Param2;

	/**
	 * 出货检测传感器状态
	 */
	public SCState Param3;

	/**
	 * 出货升降平台状态
	 */
	public SCState Param4;

	/**
	 * 电机货道状态(货道号，A1-A8|B1-B8|C1-C8|D1-D8|E1-E8|F1-F8)
	 */
	public LinkedHashMap<String, SCState> Param5;

	/**
	 * 硬币器状态
	 */
	public SCState Param6;

	/**
	 * 纸币器状态
	 */
	public SCState Param7;

	/**
	 * 升降机在底部
	 */
	public boolean Param8;

	/**
	 * 升降机位置
	 */
	public String Param9;

	/**
	 * 售货机状态
	 * 
	 * @param param1
	 *            售货机状态
	 * @param param2
	 *            有效电机数
	 * @param param3
	 *            出货检测传感器状态
	 * @param param4
	 *            出货升降平台状态
	 * @param param5
	 *            电机货道状态
	 * @param param6
	 *            硬币器状态
	 * @param param7
	 *            纸币器状态
	 * @param param8
	 *            升降机在底部
	 * @param param9
	 *            升降机位置
	 */
	public SCStatus(CSMachineState param1, int param2, SCState param3, SCState param4,
			LinkedHashMap<String, SCState> param5, SCState param6, SCState param7, boolean param8, String param9)
	{
		packageCode = 6;

		Param1 = param1;
		Param2 = param2;
		Param3 = param3;
		Param4 = param4;
		Param5 = param5;
		Param6 = param6;
		Param7 = param7;
		Param8 = param8;
		Param9 = param9;
	}

	@Override
	public String toString()
	{
		String rtn = "";

		Iterator<Entry<String, SCState>> iter = Param5.entrySet().iterator();

		while (iter.hasNext())
		{
			Entry<String, SCState> entry = iter.next();

			rtn += entry.getKey() + "|" + entry.getValue() + "        ";
		}

		return "6-售货机状态:      " + Param1 + "|" + Param2 + "|" + Param3 + "|" + Param4 + "|" + rtn + "|"
				+ Param6 + "|" + Param7 + "|" + Param8 + "|" + Param9;
	}
}