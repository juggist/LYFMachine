package com.leiyuntop.machine;

/**
 * 版本号
 * 
 * @author MENGQINGYU
 */
public class SCVersion extends ISCInfo
{
	/**
	 * 硬件版本号
	 */
	public String Param1;

	/**
	 * 软件版本号
	 */
	public String Param2;

	/**
	 * 版本号
	 * 
	 * @param param1
	 *            硬件版本号
	 * @param param2
	 *            软件版本号
	 */
	public SCVersion(String param1, String param2)
	{
		packageCode = 22;

		Param1 = param1;
		Param2 = param2;
	}

	@Override
	public String toString()
	{
		return "22-版本号:    " + Param1 + "|" + Param2;
	}
}
