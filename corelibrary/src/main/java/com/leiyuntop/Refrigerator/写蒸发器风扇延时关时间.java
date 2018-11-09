package com.leiyuntop.Refrigerator;

class 写蒸发器风扇延时关时间 extends IRefrigeratorData
{
	public 写蒸发器风扇延时关时间(int value)
	{
		m_Key = ECommand.写蒸发器风扇延时关时间;
		m_Direction = EDirection.写ROM;
		m_Address = 0x0212;
		m_Items = new int[] { IntToBCD(value) };
	}
}