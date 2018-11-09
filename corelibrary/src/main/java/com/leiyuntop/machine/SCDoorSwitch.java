package com.leiyuntop.machine;

/**
 * 开关门状态
 * 
 * @author MENGQINGYU
 */
public class SCDoorSwitch extends ISCInfo
{
	/**
	 * 开关门状态
	 */
	public boolean Param1;

	/**
	 * 开关门状态
	 * 
	 * @param param1
	 *            开关门状态
	 */
	public SCDoorSwitch(boolean param1)
	{
		packageCode = 3;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "3-开关门:    " + Param1;
	}
}
