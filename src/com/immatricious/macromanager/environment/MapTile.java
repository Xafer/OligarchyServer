package com.immatricious.macromanager.environment;

import java.awt.image.BufferedImage;

public class MapTile {
	private BufferedImage mapImage;
	private String mapData;
	private int[] tilePos;
	
	public MapTile(int x, int y, String mapData)
	{
		tilePos = new int[]{x,y};
		setMapData(mapData);
	}
	
	public void setMapData(String mapData)
	{
		this.mapData = mapData;
		this.mapImage = MapTileReader.interpretMapTileData(mapData);
	}
	
	public BufferedImage getImage() { return this.mapImage; }
	public int getx() { return tilePos[0]; }
	public int gety() { return tilePos[1]; }
	public String getMapData() { return this.mapData; }
}
