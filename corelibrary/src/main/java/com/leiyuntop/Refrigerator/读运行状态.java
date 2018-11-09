package com.leiyuntop.Refrigerator;

class 读运行状态 extends IRefrigeratorData
{
	public ERunModel 运行模式;
	public ERunState 运行状态;
	public EAlarm 报警;

	public 读运行状态()
	{
		m_Key = ECommand.读运行状态;
		m_Direction = EDirection.读RAM;
		m_Address = 0x0030;
		m_Length = 4;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			运行模式 = ERunModel.values()[m_Receive[0]];
			运行状态 = ERunState.values()[m_Receive[1]];
			报警 = new EAlarm(m_Receive[3]);
		}
		catch (Exception ex)
		{

		}
	}
}