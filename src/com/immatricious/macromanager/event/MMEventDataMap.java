package com.immatricious.macromanager.event;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MMEventDataMap implements MMEventData
{

	private Map<String,MMEventDataPair> dataMap = new HashMap<String,MMEventDataPair>();
	
	
	public MMEventDataMap( MMEventDataPair... data )
	{
		for(MMEventDataPair d : data)
		{
			dataMap.put(d.key, d);
		}
	}
	
	public MMEventDataPair getData(String key) { return dataMap.get(key); }

	@Override
	public Object[] getData() {
		// TODO Auto-generated method stub
		return null;
	}
}
