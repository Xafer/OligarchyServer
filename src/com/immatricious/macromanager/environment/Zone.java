package com.immatricious.macromanager.environment;

import java.util.List;

public class Zone {
	private final String nameId;
	
	private int totalArea;
	private int[] offset;
	private List<Area> areas;
	
	public Zone(String nameId, int[] offset, List<Area> areas)
	{
		this.nameId = nameId;
		this.offset = offset;
		this.areas = areas;
		
		totalArea = 0;
		
		for(Area area : areas)
			totalArea += area.getArea();
	}
	
	public String getNameId() { return this.nameId; }
	public int[] getOffset() { return this.offset; }
	public int getTotalArea() { return this.totalArea; }
	
	public int[] getRandomPoint()
	{
		int totalArea = 0;
		
		double r = Math.random();
		
		for(Area area : areas)
		{
			int w = area.getArea();
			totalArea += w;
			
			if(((double)totalArea) / ((double)this.totalArea) >= r)
				return area.getRandomPoint();
		}
		
		return null;
	}

	public List<Area> getAreas() {
		return areas;
	}
}
