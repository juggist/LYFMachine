package com.leiyuntop.Refrigerator;

class 写化霜间隔 extends IRefrigeratorData
{
	public 写化霜间隔(int value)
	{
		m_Key = ECommand.写化霜间隔;
		m_Direction = EDirection.写ROM;
		m_Address = 0x023A;
		m_Items = new int[] { IntToBCD(value) };
	}
}