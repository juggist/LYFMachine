package com.leiyuntop.machine;

/**
 * 出货状态
 * 
 * @author MENGQINGYU
 */
public class SCSale extends ISCInfo
{
	/**
	 * 货道号
	 */
	public String Param1;

	/**
	 * 状态
	 */
	public SCSaleState Param2;

	/**
	 * 红外传感器状态
	 */
	public String Param3;

	/**
	 * 电机状态
	 */
	public String Param4;

	/**
	 * 出货状态
	 * 
	 * @param param1
	 *            货道号
	 * @param param2
	 *            状态
	 */
	public SCSale(String param1, SCSaleState param2, String param3, String param4)
	{
		packageCode = 10;

		Param1 = param1;
		Param2 = param2;
		Param3 = param3;
		Param4 = param4;
	}

	@Override
	public String toString()
	{
		return "10-出货:  " + Param1 + "|" + Param2 + "|" + Param3 + "|" + Param4;
	}
}
