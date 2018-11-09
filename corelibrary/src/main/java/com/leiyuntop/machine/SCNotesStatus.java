package com.leiyuntop.machine;

/**
 * 纸币器状态
 * 
 * @author MENGQINGYU
 */
public class SCNotesStatus extends ISCInfo
{
	/**
	 * 状态代码
	 */
	public SCState Param1;

	/**
	 * 待收纸币金额
	 */
	public int Param2;

	/**
	 * 压入纸币金额
	 */
	public int Param3;

	/**
	 * 开关状态
	 */
	public boolean Param4;

	/**
	 * 纸币器状态
	 * 
	 * @param param1
	 *            状态代码
	 * @param param2
	 *            待收纸币金额
	 * @param param3
	 *            压入纸币金额
	 * @param param4
	 *            开关状态, true开
	 */
	public SCNotesStatus(SCState param1, int param2, int param3, boolean param4)
	{
		packageCode = 12;

		Param1 = param1;
		Param2 = param2;
		Param3 = param3;
		Param4 = param4;
	}

	@Override
	public String toString()
	{
		return "12-纸币器状态:" + Param1 + "|" + Param2 + "|" + Param3 + "|" + Param4;
	}
}
