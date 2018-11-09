package com.leiyuntop.Refrigerator;

class 读温度 extends IRefrigeratorData
{
	public double[] 温度 = new double[4];

	public 读温度()
	{
		m_Key = ECommand.读温度;
		m_Direction = EDirection.读RAM;
		m_Address = 0x0078;
		m_Length = 8;
	}

	@Override
	public void GetReceive(Byte[] val)
	{
		try
		{
			super.GetReceive(val);

			温度[0] = m_Receive[0] == 0xFF || m_Receive[1] == 0xFF ? Double.NaN
					: (BCDToInt(m_Receive[0]) + BCDToInt(m_Receive[1] & 0xF0) / 100.0)
							* ((m_Receive[1] & 0x0F) == 0 ? 1 : -1);
			温度[1] = m_Receive[2] == 0xFF || m_Receive[3] == 0xFF ? Double.NaN
					: (BCDToInt(m_Receive[2]) + BCDToInt(m_Receive[3] & 0xF0) / 100.0)
							* ((m_Receive[3] & 0x0F) == 0 ? 1 : -1);
			温度[2] = m_Receive[4] == 0xFF || m_Receive[5] == 0xFF ? Double.NaN
					: (BCDToInt(m_Receive[4]) + BCDToInt(m_Receive[5] & 0xF0) / 100.0)
							* ((m_Receive[5] & 0x0F) == 0 ? 1 : -1);
			温度[3] = m_Receive[6] == 0xFF || m_Receive[7] == 0xFF ? Double.NaN
					: (BCDToInt(m_Receive[6]) + BCDToInt(m_Receive[7] & 0xF0) / 100.0)
							* ((m_Receive[7] & 0x0F) == 0 ? 1 : -1);
		}
		catch (Exception ex)
		{
			温度[0] = Double.NaN;
			温度[1] = Double.NaN;
			温度[2] = Double.NaN;
			温度[3] = Double.NaN;
		}
	}
}