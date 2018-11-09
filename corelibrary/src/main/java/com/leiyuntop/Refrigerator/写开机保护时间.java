package com.leiyuntop.Refrigerator;

class 写开机保护时间 extends IRefrigeratorData
{
	public 写开机保护时间(int value)
	{
		m_Key = ECommand.写开机保护时间;
		m_Direction = EDirection.写ROM;
		m_Address = 0x020A;
		m_Items = new int[] { IntToBCD(value) };
	}
}