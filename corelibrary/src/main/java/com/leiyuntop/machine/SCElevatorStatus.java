package com.leiyuntop.machine;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 提升机构状态
 * 
 * @author MENGQINGYU
 */
public class SCElevatorStatus extends ISCInfo
{
	public Hashtable<String, SCState> Param1;

	/**
	 * 提升机构状态
	 * 
	 * @param param1
	 *            提升机构状态
	 */
	public SCElevatorStatus(Hashtable<String, SCState> param1)
	{
		packageCode = 9;

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

		return "9-提升机构状态:      " + rtn;
	}
}
