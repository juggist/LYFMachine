package com.leiyuntop.Refrigerator;

class 写化霜时间 extends IRefrigeratorData
{
	public 写化霜时间(int value)
	{
		m_Key = ECommand.写化霜时间;
		m_Direction = EDirection.写ROM;
		m_Address = 0x0232;
		m_Items = new int[] { IntToBCD(value) };
	}
}