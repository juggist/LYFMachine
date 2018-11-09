package com.leiyuntop.machine;
import java.util.LinkedHashMap;

/**
 * 初始化
 * 
 * @author MENGQINGYU
 */
public class SCInit extends ISCInfo
{
	/**
	 * 电机数量
	 */
	public int Param1;
	
	/**
	 * 机器状态
	 */
	public LinkedHashMap<String, SCState> Param2;
	
	/**
	 * 初始化
	 * 
	 * @param param1
	 *            电机数量
	 */
	SCInit(int param1, LinkedHashMap<String, SCState> param2)
	{
		packageCode = 1;
		
		Param1 = param1;
		Param2 = param2;
	}

	@Override
	public String toString()
	{
		return "1-初始化:    " + Param1;
	}
}
