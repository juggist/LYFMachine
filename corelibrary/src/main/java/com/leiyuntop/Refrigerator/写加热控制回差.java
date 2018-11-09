package com.leiyuntop.Refrigerator;

class 写加热控制回差 extends IRefrigeratorData
{
	public 写加热控制回差(int value)
	{
		m_Key = ECommand.写加热控制回差;
		m_Direction = EDirection.写ROM;
		m_Address = 0x0262;
		m_Items = new int[] { IntToBCD(value) };
	}
}