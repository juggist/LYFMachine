package com.leiyuntop.Refrigerator;

class 读冷凝器高温报警温度 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public int 冷凝器高温报警温度;

	public 读冷凝器高温报警温度(Refrigerator refrigerator)
	{
		m_Key = ECommand.读冷凝器高温报警温度;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0250;
		m_Length = 3;
		m_Refrigerator = refrigerator;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			boolean version = m_Refrigerator.getInterfaceVersion() > 16;

			最小值 = BCDToInt(m_Receive[0], version);
			最大值 = BCDToInt(m_Receive[1], version);
			冷凝器高温报警温度 = BCDToInt(m_Receive[2], version);
		}
		catch (Exception ex)
		{

		}
	}
}