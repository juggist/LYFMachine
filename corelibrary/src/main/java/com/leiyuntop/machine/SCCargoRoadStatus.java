package com.leiyuntop.machine;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 电机货道状态
 * 
 * @author MENGQINGYU
 */
public class SCCargoRoadStatus extends ISCInfo
{
	/**
	 * 电机货道状态(货道号，A1-A8|B1-B8|C1-C8|D1-D8|E1-E8|F1-F8)
	 */
	public LinkedHashMap<String, SCState> Param1;

	/**
	 * 电机货道状态
	 * 
	 * @param param1
	 *            电机货道状态
	 */
	public SCCargoRoadStatus(LinkedHashMap<String, SCState> param1)
	{
		packageCode = 7;
		
		Param1 = param1;
	}

	@Override
	public String toString()
	{
		String rtn = "";

		Iterator<Entry<String, SCState>> iter = Param1.entrySet().iterator();

		while (iter.hasNext())
		{
			Entry<String, SCState> entry = iter.next();

			rtn += entry.getKey() + "|" + entry.getValue() + "        ";
		}

		return "7-货道状态:      " + rtn;
	}
}
