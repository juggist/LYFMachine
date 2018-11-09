package com.leiyuntop.Refrigerator;

class 读冷凝器风扇启动温度 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public int 冷凝器风扇启动温度;

	public 读冷凝器风扇启动温度(Refrigerator refrigerator)
	{
		m_Refrigerator = refrigerator;
		m_Key = ECommand.读冷凝器风扇启动温度;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0248;
		m_Length = 3;
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
			冷凝器风扇启动温度 = BCDToInt(m_Receive[2], version);
		}
		catch (Exception ex)
		{

		}
	}
}