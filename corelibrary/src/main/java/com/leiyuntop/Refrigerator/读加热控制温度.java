package com.leiyuntop.Refrigerator;

class 读加热控制温度 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public int 加热控制温度;

	public 读加热控制温度()
	{
		m_Key = ECommand.读加热控制温度;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0258;
		m_Length = 3;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			最小值 = BCDToInt(m_Receive[0]);
			最大值 = BCDToInt(m_Receive[1]);
			加热控制温度 = BCDToInt(m_Receive[2]);
		}
		catch (Exception ex)
		{

		}
	}
}