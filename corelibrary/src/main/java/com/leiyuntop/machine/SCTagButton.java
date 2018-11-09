package com.leiyuntop.machine;

/**
 * 附加按钮按下
 * 
 * @author MENGQINGYU
 */
public class SCTagButton extends ISCInfo
{
	/**
	 * 按钮号
	 */
	public int Param1;

	/**
	 * 附加按钮按下
	 * 
	 * @param param1
	 *            按钮号
	 */
	public SCTagButton(int param1)
	{
		packageCode = 17;

		Param1 = param1;
	}

	@Override
	public String toString()
	{
		return "17-附加按钮按下";
	}
}
