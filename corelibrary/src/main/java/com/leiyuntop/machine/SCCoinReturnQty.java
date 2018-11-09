package com.leiyuntop.machine;

/**
 * 退币
 * 
 * @author MENGQINGYU
 */
public class SCCoinReturnQty extends ISCInfo
{
	/**
	 * 退币额
	 */
	public int Param1;

	/**
	 * 退币
	 * 
	 * @param param1
	 *            退币额
	 */
	SCCoinReturnQty(int param1)
	{
		packageCode = 19;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "19-退币:      " + Param1;
	}
}
