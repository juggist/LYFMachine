package com.leiyuntop.Refrigerator;

class 写机组运行模式 extends IRefrigeratorData
{
	public 写机组运行模式(ERunModel model)
	{
		m_Key = ECommand.写机组运行模式;
		m_Direction = EDirection.写ROM;
		m_Address = 0x0202;
		m_Items = new int[] { model.ordinal() };
	}
}