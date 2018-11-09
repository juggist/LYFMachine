package com.leiyuntop.machine;

/**
 * 硬币器状态
 * 
 * @author MENGQINGYU
 */
public class SCCoinStatus extends ISCInfo
{
	/**
	 * 状态代码
	 */
	public SCState Param1;

	/**
	 * 可找零金额
	 */
	public int Param2;

	/**
	 * 上次收币金额
	 */
	public int Param3;

	/**
	 * 开关状态
	 */
	public boolean Param4;

	/**
	 * 硬币器状态
	 * 
	 * @param param1
	 *            状态代码
	 * @param param2
	 *            可找零金额
	 * @param param3
	 *            上次收币金额
	 * @param param4
	 *            开关状态, true开
	 */
	public SCCoinStatus(SCState param1, int param2, int param3, boolean param4)
	{
		packageCode = 11;

		Param1 = param1;
		Param2 = param2;
		Param3 = param3;
		Param4 = param4;
	}

	@Override
	public String toString()
	{
		return "11-硬币器状态:" + Param1 + "|" + Param2 + "|" + Param3 + "|" + Param4;
	}
}
