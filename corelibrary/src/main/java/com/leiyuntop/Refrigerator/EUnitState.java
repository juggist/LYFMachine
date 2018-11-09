package com.leiyuntop.Refrigerator;

/**
 * 模块状态
 * 
 * @author MENGQINGYU
 * 
 */
public class EUnitState
{
	private byte m_Value;

	/**
	 * 得到除霜电磁阀状态
	 * 
	 * @return 值
	 */
	public boolean getDefrostValve()
	{
		return (m_Value & 0x01) != 0x01;
	}

	/**
	 * 得到冷凝器风扇状态
	 * 
	 * @return 值
	 */
	public boolean getCondenserFan()
	{
		return (m_Value & 0x02) != 0x02;
	}

	/**
	 * 得到蒸发器风扇状态
	 * 
	 * @return 值
	 */
	public boolean getEvaporatorFan()
	{
		return (m_Value & 0x04) != 0x04;
	}

	/**
	 * 得到蒸发器风扇状态
	 * 
	 * @return 值
	 */
	public boolean getCompressor()
	{
		return (m_Value & 0x08) != 0x08;
	}

	/**
	 * 加热PTC
	 * 
	 * @return 值
	 */
	public boolean getHeatPtc()
	{
		return (m_Value & 0x20) != 0x20;
	}

	/**
	 * 加热风扇
	 * 
	 * @return 值
	 */
	public boolean getHeatFan()
	{
		return (m_Value & 0x40) != 0x40;
	}

	/**
	 * 构造函数
	 * 
	 * @param val
	 *            值
	 */
	public EUnitState(byte val)
	{
		m_Value = val;
	}
}