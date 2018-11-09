package com.leiyuntop.Refrigerator;

class 读蒸发器风扇延时关时间 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public int 蒸发器风扇延时关时间;

	public 读蒸发器风扇延时关时间()
	{
		m_Key = ECommand.读蒸发器风扇延时关时间;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0210;
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
			蒸发器风扇延时关时间 = BCDToInt(m_Receive[2]);
		}
		catch (Exception ex)
		{

		}
	}
}