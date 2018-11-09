package com.leiyuntop.machine;

/**
 * 红外传感器
 * 
 * @author MENGQINGYU
 */
public class SCPickStatusStatus extends ISCInfo
{
	/**
	 * 红外传感器状态， 0-挡;1-通
	 */
	public int[] Param1;

	/**
	 * 红外传感器状态
	 */
	public SCState Param2;

	/**
	 * 红外传感器状态
	 * 
	 * @param param1
	 *            传感器状态， 0-挡;1-通
	 * @param param2
	 *            传感器状态， 有无
	 */
	public SCPickStatusStatus(int[] param1, SCState param2)
	{
		packageCode = 8;

		Param1 = param1;
		Param2 = param2;
	}

	@Override
	public String toString()
	{
		String val = "";

		for (int i = 0; i < Param1.length; i++)
		{
			val += Param1[i] + "    ";
		}

		return "8-出货检测状态:      " + val + "|" + Param2;
	}
}
