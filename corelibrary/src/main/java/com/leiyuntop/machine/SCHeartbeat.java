package com.leiyuntop.machine;

/**
 * 心跳包
 * 
 * @author MENGQINGYU
 */
public class SCHeartbeat extends ISCInfo
{
	/**
	 * 心跳包
	 */
	public SCHeartbeat()
	{
		packageCode = 2;
	}

	@Override
	public String toString()
	{
		return "2-心跳包";
	}
}