package com.leiyuntop.machine;

/**
 * 收币
 * 
 * @author MENGQINGYU
 */
public class SCCoinCollection extends ISCInfo
{
	/**
	 * 设备
	 */
	public CSNotesCoins Param1;

	/**
	 * 金额
	 */
	public int Param2;

	/**
	 * 收币
	 * 
	 * @param param1
	 *            设备
	 * @param param2
	 *            金额
	 */
	SCCoinCollection(CSNotesCoins param1, int param2)
	{
		packageCode = 15;

		Param1 = param1;
		Param2 = param2;
	}

	@Override
	public String toString()
	{
		return "15-收币:  " + Param1 + "|" + Param2;
	}
}
