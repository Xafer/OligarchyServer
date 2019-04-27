package com.immatricious.macromanager.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;

//Should have been named PathManager, Oh well
public class PrePath {
	
	private PathReader pathReader;
	private Map<Integer,PathPoint> paths = new HashMap<Integer,PathPoint>();
	
	public PrePath()
	{
		pathReader = new PathReader("assets/path.txt");
	}
	
	public void read()
	{
		List<PathPoint> pathList = pathReader.read();
		
		for(PathPoint path : pathList)
		{
			addPoint(path);
		}
	}
	
	public void addPoint(PathPoint p)
	{
		paths.put(p.getID(), p);
		
		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<PathPoint>("point",p));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.PATH_ADDED,data));
	}
	
	public void removePoint(PathPoint p)
	{
		paths.remove(p.getID());
		
		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<PathPoint>("point",p));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.PATH_REMOVED,data));
	}
	
	public PathPoint getClosestPath(float x, float y)
	{
		PathPoint closestPath = null;
		float d = Float.MAX_VALUE;
		
		for(PathPoint path : paths.values())
		{
			float rx = (path.getX() - x);
			float ry = (path.getY() - y);
			
			float nd =(rx*rx + ry*ry);
			
			if(nd < d)
			{
				d = nd;
				closestPath = path;
			}
		}
		
		return closestPath;
	}
	
	public List<PathPoint> getFastestPath(float xs, float ys, float xe, float ye)
	{
		PathPoint start = getClosestPath(xs,ys);
		PathPoint end = getClosestPath(xe,ye);
		
		List<PathPoint> shortestPath = null;
		float shortestDist = Float.MAX_VALUE;
		
		//Storing points that have a;ready been passed over
		Map<Integer,Integer> readPoints = new HashMap<Integer,Integer>();
		Map<Integer,Float> readPointsDist = new HashMap<Integer,Float>();
		//Points to examine during next iteraion
		List<List<Integer>> nextPoints = new ArrayList<List<Integer>>();
		
		List<Integer> first = new ArrayList<Integer>();
		
		if(start != null)
		{
			first.add(start.getID());
			nextPoints.add(first);
		}
		
		//List to add to next points once built in loop
		List<Integer> nPoints;
		
		do
		{
			nPoints = new ArrayList<Integer>();
			List<Integer> lPoint = nextPoints.get(nextPoints.size()-1);
			
			List<PathPoint> currentPath = new ArrayList<PathPoint>();
			
			for(int l : lPoint)
			{	
				PathPoint p = paths.get(l);
				for(int n : p.getPaths())
				{
					PathPoint pn = paths.get(n);
					
					if(!readPoints.containsKey(n))
					{
						nPoints.add(n);
						readPoints.put(n, l);
						readPointsDist.put(n, pn.dist2(p));
					}
					
					if(pn == end)
					{
						PathPoint prev = pn;
						float d = 0;
						do 
						{
							currentPath.add(prev);
							d += readPointsDist.get(prev.getID());
						}while((prev = paths.get(readPoints.get(prev.getID()))) != start);
						currentPath.add(start);
						if(d < shortestDist)
						{
							shortestPath = currentPath;
							shortestDist = d;
						}
					}
				}
			}
			
			nextPoints.add(nPoints);
		}
		while(nPoints.size() > 0);
		
		List<PathPoint> flippedPath = new ArrayList<PathPoint>();
		
		//Flipping around the arraylist
		while(shortestPath.size() > 0)
			flippedPath.add(shortestPath.remove(shortestPath.size()-1));
		
		return flippedPath;
	}
	
	public Map<Integer, PathPoint> getPathPoints() { return paths; }

	public void close() {
		pathReader.write(new ArrayList<PathPoint>(paths.values()));
	}
}
