package com.leiyuntop.Refrigerator;

class 写制冷控制回差 extends IRefrigeratorData
{
	public 写制冷控制回差(int value)
	{
		m_Key = ECommand.写制冷控制回差;
		m_Direction = EDirection.写ROM;
		m_Address = 0x022A;
		m_Items = new int[] { IntToBCD(value) };
	}
}