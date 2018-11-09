package com.leiyuntop.Refrigerator;

class 写制冷控制温度 extends IRefrigeratorData
{
	public 写制冷控制温度(int value, Refrigerator refrigerator)
	{
		m_Key = ECommand.写制冷控制温度;
		m_Direction = EDirection.写ROM;
		m_Address = 0x0222;
		m_Items = new int[] { IntToBCD(value, refrigerator.getInterfaceVersion() > 16) };
	}
}