package com.immatricious.macromanager.environment;

import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.task.Action;
import com.immatricious.macromanager.task.ActionTarget;
import com.immatricious.macromanager.task.ActionTargetDataType;
import com.immatricious.macromanager.task.ActionType;
import com.immatricious.macromanager.util.DataParser;

public class Environment {

	private PrePath path;
	private ZoneManager zone;
	private HashedLocationManager locations;
	
	public Environment()
	{
		path = new PrePath();
		zone = new ZoneManager();
		locations = new HashedLocationManager();
	}
	
	public void read()
	{
		path.read();
		zone.read();
		locations.read();
	}
	
	public List<Action> pathFindTo(ActionTarget target)
	{
		List<Action> pathActions = new ArrayList<Action>();
		
		float[] end = DataParser.parseVector(target.getMessageString());
		
		List<PathPoint> pathPoints = path.getFastestPath(0, 0, end[0], end[1]);
		
		for(PathPoint point : pathPoints)
		{
			pathActions.add(new Action(ActionType.MOVE,new ActionTarget("v" + point.getX() + "," + point.getY())));
		}
		
		return pathActions;
	}

	public PrePath getPath() { return this.path; }
	public HashedLocationManager getHashedLocation() { return this.locations; }
	
	public void close()
	{
		path.close();
		zone.close();
		locations.close();
	}
}
