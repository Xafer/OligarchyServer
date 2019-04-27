package com.immatricious.macromanager.environment;

import java.util.HashMap;
import java.util.Map;

public class HashedLocationManager {
	private Map<Integer,HashedLocation> locations = new HashMap<Integer,HashedLocation>();
	private HashedLocationReader reader;
	
	public HashedLocationManager()
	{
		reader = new HashedLocationReader("assets/hashloc.txt");
	}
	
	public void read()
	{
		locations = reader.read();
	}
	
	public HashedLocation getHashedLocation(int hash) { return locations.get(hash); }

	public void close() {
		reader.write(locations);
	}
}
