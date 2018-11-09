package com.leiyuntop.machine;

/**
 * 升降机位置
 * 
 * @author MENGQINGYU
 */
public class SCElevatorLocation extends ISCInfo
{
	/**
	 * 升降机位置
	 */
	public String Param1;

	/**
	 * 升降机位置
	 * 
	 * @param param1
	 *            升降机位置
	 */
	public SCElevatorLocation(String param1)
	{
		packageCode = 20;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "20-升降机位置:    " + Param1;
	}
}
