package com.leiyuntop.Refrigerator;

class 读设备状态 extends IRefrigeratorData
{
	public EUnitState 设备状态;

	public 读设备状态()
	{
		m_Key = ECommand.读设备状态;
		m_Direction = EDirection.读RAM;
		m_Address = 0x0022;
		m_Length = 1;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			设备状态 = new EUnitState(m_Receive[0]);
		}
		catch (Exception ex)
		{

		}
	}
}