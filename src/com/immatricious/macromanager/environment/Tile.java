package com.immatricious.macromanager.environment;

public class Tile {
	private int id;
	private String tileName;
	private int[] color;

	public Tile(int id, String tileName, int[] color)
	{
		this.id = id;
		this.tileName = tileName;
		this.color = color;
	}
	
	public int getID() { return this.id; }
	public String getName() { return this.tileName; }
	
	public int getr() { return color[0]; }
	public int getg() { return color[1]; }
	public int getb() { return color[2]; }
}
