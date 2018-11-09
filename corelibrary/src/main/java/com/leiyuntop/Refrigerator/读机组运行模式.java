package com.leiyuntop.Refrigerator;

class 读机组运行模式 extends IRefrigeratorData
{
	public int 最小值;
	public int 最大值;
	public ERunModel 运行模式;

	public 读机组运行模式()
	{
		m_Key = ECommand.读机组运行模式;
		m_Direction = EDirection.读ROM;
		m_Address = 0x0200;
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
			运行模式 = ERunModel.values()[m_Receive[2]];
		}
		catch (Exception ex)
		{

		}
	}
}