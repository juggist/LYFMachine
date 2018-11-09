package com.leiyuntop.Refrigerator;

class 读制冷控制温度 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public int 制冷控制温度;

	public 读制冷控制温度(Refrigerator refrigerator)
	{
		m_Key = ECommand.读制冷控制温度;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0220;
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
			制冷控制温度 = BCDToInt(m_Receive[2], version);
		}
		catch (Exception ex)
		{

		}
	}
}