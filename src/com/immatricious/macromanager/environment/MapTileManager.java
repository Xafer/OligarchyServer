package com.immatricious.macromanager.environment;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.TaskHandler;

public class MapTileManager {
	private MapTileReader mr;
	private List<MapTile> availableMapTiles;
	
	public MapTileManager()
	{
		mr = new MapTileReader();
		
		availableMapTiles = new ArrayList<MapTile>();
		loadMaps();
	}
	
	private void loadMaps()
	{
		Path mapPath = Paths.get("assets/map");
		File[] mapFiles = mapPath.toFile().listFiles();
		
		for(File mf : mapFiles)
		{
			String mp = mf.getName();
			MapTile mapTile = mr.read(mp);
			availableMapTiles.add(mapTile);
			
			MMEventDataMap cdata = new MMEventDataMap(new MMEventDataPair<MapTile>("maptile",mapTile));
			TaskHandler.dispatchEvent(new MMEvent(MMEventType.MAP_LOADED,cdata));
		}
	}
	
	public void writeMaps()
	{
		for(MapTile mt : availableMapTiles)
			mr.write(mt);
	}
	
	public void recieveMapData(String mapData)
	{
		String[] loc = mapData.split(":")[0].split(",");
		int x = Integer.parseInt(loc[0]);
		int y = Integer.parseInt(loc[1]);
		
		MapTile m = null;
		
		for(MapTile mt : availableMapTiles)
		{
			if(mt.getx() != x || mt.gety() != y)
				continue;
			mt.setMapData(mapData);
			m = mt;
			break;
		}
		
		if(m == null)
		{
			m = new MapTile(x, y, mapData);
			availableMapTiles.add(m);
		}
		
		MMEventDataMap cdata = new MMEventDataMap(new MMEventDataPair<MapTile>("maptile",m));

		TaskHandler.dispatchEvent(new MMEvent(MMEventType.RCV_MAPDATA,cdata));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.MAP_LOADED,cdata));
	}
	
	public List<MapTile> getAvailableMapTiles() { return this.availableMapTiles; }
}
