package com.immatricious.macromanager.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneManager {
	private Map<String,Zone> zones = new HashMap<String,Zone>();
	private ZoneReader reader;
	
	public ZoneManager()
	{
		reader = new ZoneReader("assets/zones.txt");
	}
	
	public void read()
	{
		List<Zone> zoneList = reader.read();
		for(Zone zone : zoneList)
			zones.put(zone.getNameId(),zone);
	}

	public void close() {
		reader.write(new ArrayList<Zone>(zones.values()));
	}
}
