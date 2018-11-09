package com.leiyuntop.Refrigerator;

class 读版本号 extends IRefrigeratorData
{
	public int 接口版本;
	public int 硬件版本;

	public 读版本号()
	{
		m_Key = ECommand.读版本号;
		m_Direction = EDirection.读扩展RAM;
		m_Address = 0x00BC;
		m_Length = 4;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			接口版本 = BCDToInt(m_Receive[0]) * 100 + BCDToInt(m_Receive[1]);
			硬件版本 = BCDToInt(m_Receive[2]) * 100 + BCDToInt(m_Receive[3]);
		}
		catch (Exception ex)
		{

		}
	}
}