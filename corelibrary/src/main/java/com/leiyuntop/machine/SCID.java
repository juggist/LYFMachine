package com.leiyuntop.machine;

/**
 * 机器号
 * 
 * @author MENGQINGYU
 */
public class SCID extends ISCInfo
{
	public String Param1;

	/**
	 * 机器号
	 * 
	 * @param param1
	 *            机器号
	 */
	public SCID(String param1)
	{
		packageCode = 21;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "21-机器号:    " + Param1;
	}
}