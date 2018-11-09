package com.leiyuntop.machine;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 硬币数量
 * 
 * @author MENGQINGYU
 */
public class SCCoinQty extends ISCInfo
{
	public Hashtable<Double, Integer> Param1;

	/**
	 * 硬币数量
	 */
	public SCCoinQty(Hashtable<Double, Integer> param1)
	{
		packageCode = 18;
		
		Param1 = param1;
	}

	@Override
	public String toString()
	{
		String rtn = "";

		Iterator<Entry<Double, Integer>> iter = Param1.entrySet().iterator();

		while (iter.hasNext())
		{
			Entry<Double, Integer> entry = iter.next();

			rtn += entry.getKey() + "|" + entry.getValue() + "        ";
		}

		return "18-硬币数量:" + rtn;
	}
}