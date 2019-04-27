package com.immatricious.macromanager.environment;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.task.TaskHandler;

public class PathPoint {
	private float x;
	private float y;
	private int[] paths;
	private final int pathID;
	
	public PathPoint(float x, float y, int[] paths, int pathID)
	{
		this.x = x;
		this.y = y;
		this.paths = paths;
		this.pathID = pathID;
	}
	
	public int getID() { return this.pathID; }
	public float getX() { return this.x; }
	public float getY() { return this.y; }
	public int[] getPaths() { return this.paths; }

	public Float dist2(PathPoint pathPoint) {
		float dx = pathPoint.getX() - this.x;
		float dy = pathPoint.getY() - this.y;
		return dx*dx + dy*dy;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
		
		MMEventDataMap data = new MMEventDataMap( new MMEventDataPair<PathPoint>("point",this));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.PATH_UPDATED,data));
	}
}
