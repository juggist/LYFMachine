package com.leiyuntop.Refrigerator;

class 读配置 extends IRefrigeratorData
{
	public int 开机保护时间;
	public int 蒸发器风扇延时关时间;
	public int 制冷控制温度;
	public int 制冷控制回差;
	public int 化霜时间;
	public int 化霜间隔;
	public int 冷凝器风扇启动温度;
	public int 冷凝器高温报警温度;
	public int 加热控制温度;
	public int 加热控制回差;

	public 读配置(Refrigerator refrigerator)
	{
		m_Key = ECommand.读配置;
		m_Direction = EDirection.读RAM;
		m_Address = 0x0090;
		m_Length = 12;
		m_Refrigerator = refrigerator;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			boolean version = m_Refrigerator.getInterfaceVersion() > 16;

			开机保护时间 = BCDToInt(m_Receive[0]);
			蒸发器风扇延时关时间 = BCDToInt(m_Receive[1]);
			制冷控制温度 = BCDToInt(m_Receive[3], version);
			制冷控制回差 = BCDToInt(m_Receive[4]);
			化霜时间 = BCDToInt(m_Receive[5]);
			化霜间隔 = BCDToInt(m_Receive[6]);
			冷凝器风扇启动温度 = BCDToInt(m_Receive[8], version);
			冷凝器高温报警温度 = BCDToInt(m_Receive[9], version);
			加热控制温度 = BCDToInt(m_Receive[10]);
			加热控制回差 = BCDToInt(m_Receive[11]);
		}
		catch (Exception ex)
		{

		}
	}
}