package com.leiyuntop.machine;

/**
 * 货道故障接口
 * 
 * @author MENGQINGYU
 */
public class SCCargoRoadFault extends ISCInfo
{
	/**
	 * 货道号
	 */
	public String Param1;
	
	/**
	 * 故障代码
	 */
	public SCLayState Param2;

	/**
	 * 构造货道故障
	 * 
	 * @param param1
	 *            货道号
	 *            
	 * @param param2
	 *            故障代码
	 */
	public SCCargoRoadFault(String param1, SCLayState param2)
	{
		packageCode = 5;
		
		Param1 = param1;
		Param2 = param2;
	}

	@Override
	public String toString()
	{
		return "5-货道故障:  " + Param1 + "|" + Param2;
	}
}
