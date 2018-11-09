package com.leiyuntop.Refrigerator;

class 写冷凝器风扇启动温度 extends IRefrigeratorData
{
	public 写冷凝器风扇启动温度(int value, Refrigerator refrigerator)
	{
		m_Key = ECommand.写冷凝器风扇启动温度;
		m_Direction = EDirection.写ROM;
		m_Address = 0x024A;
		m_Items = new int[] { IntToBCD(value, refrigerator.getInterfaceVersion() > 16) };
	}
}