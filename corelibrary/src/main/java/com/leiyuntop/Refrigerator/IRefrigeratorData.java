package com.leiyuntop.Refrigerator;

import java.util.*;

abstract class IRefrigeratorData
{
	protected ECommand m_Key;
	protected int m_Direction;
	protected int m_Address;
	protected int[] m_Items;
	protected int m_Length;
	protected byte[] m_Receive;
	protected Refrigerator m_Refrigerator;

	public ECommand getKey()
	{
		return m_Key;
	}

	public boolean getWrite()
	{
		return m_Direction == EDirection.写RAM || m_Direction == EDirection.写扩展RAM || m_Direction == EDirection.写ROM;
	}

	public boolean getState()
	{
		return m_Receive != null && m_Receive.length > 0 && m_Receive[0] == m_Items[0];
	}

	public byte[] GetSend()
	{
		List<Integer> send = new ArrayList<Integer>();

		send.add(0xAA); // 头
		send.add(0x09); // 长度
		send.add(0x04); // 板号
		send.add(m_Direction); // 读写
		send.add(m_Address >> 8); // 址址1
		send.add(m_Address); // 址址2

		if (m_Direction == EDirection.写RAM || m_Direction == EDirection.写ROM)
		{
			send.set(1, send.get(1) + m_Items.length);
			send.add(m_Items.length); // 数据长度

			for (int i = 0; i < m_Items.length; i++)
			{
				send.add(m_Items[i]);
			}
		}
		else
		{
			send.add(m_Length);
		}

		int check = 0xBB;

		Iterator<Integer> itr = send.iterator();

		while (itr.hasNext())
		{
			check ^= itr.next();
		}

		send.add(check); // 校验码
		send.add(0xBB); // 结尾

		Integer[] val = new Integer[send.size()];
		send.toArray(val);
		
		byte[] rtn = new byte[val.length];
		
		for (int i = 0;i < val.length;i++)
		{
			rtn[i] = (byte)(int)val[i];
		}
		
		return rtn;
	}

	public void GetReceive(Byte[] val)
	{
		if (val.length < 5)
		{
			m_Receive = null;
		}

		int check = 0;

		for (int i = 0; i < val.length; i++)
		{
			check ^= val[i];
		}

		if (check != 0)
		{
			m_Receive = null;
		}

		m_Receive = new byte[val.length - 4];

		for (int i = 0, j = 2; i < val.length - 4; i++, j++)
		{
			m_Receive[i] = val[j];
		}
	}

	protected int BCDToInt(int val, boolean sign)
	{
		val &= 0xFF;
		
		if (sign)
		{
			return (((val & 0x7F) >> 4) * 10 + (val & 0xF)) * (val > 127 ? -1 : 1);
		}
		else
		{
			return (val >> 4) * 10 + (val & 0xF);
		}
	}

	protected int BCDToInt(int val)
	{
		return BCDToInt(val, false);
	}

	protected byte IntToBCD(int val, boolean sign)
	{
		if (sign)
		{
			int temp = Math.abs(val) & 0x7F;
			return (byte) ((((temp / 10) << 4) + temp % 10) | (val < 0 ? 0x80 : 0x00));
		}
		else
		{
			return (byte) (((val / 10) << 4) + val % 10);
		}
	}
	
	protected byte IntToBCD(int val)
	{
		return IntToBCD(val, false);
	}
}