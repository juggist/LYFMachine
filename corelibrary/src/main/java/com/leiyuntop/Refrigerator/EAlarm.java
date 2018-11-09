package com.leiyuntop.Refrigerator;

/**
 * 报警
 * 
 * @author MENGQINGYU
 * 
 */
public class EAlarm
{
	private byte m_Value;

	/**
	 * 得到箱体报警
	 * 
	 * @return 值
	 */
	public boolean getBox()
	{
		return (m_Value & 0x02) == 0x02;
	}

	/**
	 * 得到冷凝器报警
	 * 
	 * @return 值
	 */
	public boolean getCondenser()
	{
		return (m_Value & 0x04) == 0x04;
	}

	/**
	 * 得到蒸发器报警
	 * 
	 * @return 值
	 */
	public boolean getEvaporator()
	{
		return (m_Value & 0x08) == 0x08;
	}

	/**
	 * 得到加热报警
	 * 
	 * @return 值
	 */
	public boolean getHeat()
	{
		return (m_Value & 0x10) == 0x10;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param val
	 *            值
	 */
	public EAlarm(byte val)
	{
		m_Value = val;
	}
}