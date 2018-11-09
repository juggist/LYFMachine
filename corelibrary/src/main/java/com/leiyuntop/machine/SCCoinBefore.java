package com.leiyuntop.machine;

/**
 * 纸币预收数据
 * 
 * @author MENGQINGYU
 */
public class SCCoinBefore extends ISCInfo
{
	/**
	 * 金额
	 */
	public int Param1;

	/**
	 * 构造纸币预收数据
	 * 
	 * @param param1
	 *            金额
	 */
	public SCCoinBefore(int param1)
	{
		packageCode = 14;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "14-纸币预收:  " + Param1;
	}
}