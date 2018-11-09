package com.leiyuntop.Refrigerator;

class 写加热控制温度 extends IRefrigeratorData
{
	public 写加热控制温度(int value)
	{
		m_Key = ECommand.写加热控制温度;
		m_Direction = EDirection.写ROM;
		m_Address = 0x025A;
		m_Items = new int[] { IntToBCD(value) };
	}
}