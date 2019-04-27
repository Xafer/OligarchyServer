package com.immatricious.macromanager.environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTileReader {
	private String dirName;
	private Map<Integer,Tile> tdat;
	
	public static final int MAPTILESIZE = 25;
	
	//Map tile data structure:
	// used tile refs & colors:tilerefbyte,tilrefbyte
	//x,y:feldspar;colbyte,colbyte,colbyte!porphyry;colbyte,colbyte,colbyte!...:indexByte,heightbyte,heightbyte2; indexByte,heightbyte,heightbyte2...
	static public BufferedImage interpretMapTileData(String data)
	{
		class TileAssemblyData {
			public String name;
			public char[] color;
			public TileAssemblyData(String name, char[] color)
			{
				this.name = name;
				this.color = new char[3];
				
				switch(name)
				{
				case "porphyry":
					this.color = new char[]{64,0,0};
					break;
				case "cinnabar":
					this.color = new char[]{200,160,200};
					break;
				case "flint":
					this.color = new char[]{128,128,128};
					break;
				case "deep":
					this.color = new char[]{0,126,186};
					break;
				case "water":
					this.color = new char[]{0,164,200};
					break;
				case "odeep":
					this.color = new char[]{0,26,86};
					break;
				case "owater":
					this.color = new char[]{0,64,128};
					break;
				case "beach":
					this.color = new char[]{200,200,128};
					break;
				case "grass":
					this.color = new char[]{80,200,30};
					break;
				case "dirt":
					this.color = new char[]{100,80,30};
					break;
				case "field":
					this.color = new char[]{128,76,8};
					break;
				default:
					char colorcrop = 8;
					
					this.color[0] = (char) ((color[0]-'a')*colorcrop);
					this.color[1] = (char) ((color[1]-'a')*colorcrop);
					this.color[2] = (char) ((color[2]-'a')*colorcrop);
					break;
				}
			}
		}
		BufferedImage bi = new BufferedImage(25,25,BufferedImage.TYPE_INT_RGB);
		
		String[] dataSplit = data.split(":");
		
		String[] loc = dataSplit[0].split(",");
		
		String[] headerData = dataSplit[1].split(",");
		
		List<TileAssemblyData> tileData = new ArrayList<TileAssemblyData>();
		
		//Parse header
		for(String tileType : headerData)
		{
			String[] tileTypeData = tileType.split("!");
			TileAssemblyData tad = new TileAssemblyData(tileTypeData[0],tileTypeData[1].toCharArray());
			tileData.add(tad);
		}
		
		char[] charDataArray = dataSplit[2].toCharArray();
		
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		
		//Parse 25x25 char of tile data
		for(int i = 0, l = charDataArray.length/3; i < l; i++)
		{
			int n = i*3;
			char tileChar = (char) (charDataArray[n]-'A');
			int height = (int)(((int)charDataArray[n+1])*(Character.MAX_VALUE - 'A')) + (int)charDataArray[n+2];
			TileAssemblyData tad = tileData.get(tileChar);
			int x = i % MAPTILESIZE;
			int y = (i - x) / MAPTILESIZE;
			
			char[] colortemp = new char[] {tad.color[0],tad.color[1],tad.color[2]};
			
			int heightMod = 0;
			
			if(!tad.name.equals("porphyry"))
			{
				if(((height/4)%2 != 0))
				{
					colortemp[0]-= colortemp[0]/10;
					colortemp[1]-= colortemp[1]/10;
					colortemp[2]-= colortemp[2]/10;
				}
				
				heightMod = (height%128)/2;
			}
			
			Color c = new Color(colortemp[0]+heightMod,colortemp[1]+heightMod,colortemp[2]/2+heightMod);
			
			g2.setColor(c);
			g2.fillRect(x, y, 1, 1);
		}
		
		return bi;
	}
	
	public MapTileReader()
	{
		tdat = readTileData("assets/tiles.txt");
	}
	
	//Read file that contains tile data
	//Format: tilename:color r,g,b
	public Map<Integer,Tile> readTileData(String filename)
	{
		String line;
		
		Map<Integer,Tile> tileData = new HashMap<Integer,Tile>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			int i = 0;
			
			while((line = br.readLine()) != null)
			{
				String[] tileString = line.split(":");
				String[] colorString = tileString[1].split(",");
				int[] color = new int[]{Integer.parseInt(colorString[0]),
										Integer.parseInt(colorString[1]),
										Integer.parseInt(colorString[2])};
				Tile t = new Tile(i,tileString[0],color);
				tileData.put(i, t);
				i++;
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tileData;
			
	}
		
	public void writeTileData()
	{
		String filename = "assets/tiles.txt";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
			for(int i = 0, l = tdat.size(); i < l; i++)
			{
				Tile t = tdat.get(i);
				bw.write(t.getName()+":"+t.getr()+","+t.getg()+","+t.getb());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
		System.out.println("could not write to tile file: " + filename);
		}
	}
	
	public MapTile read(String filename)
	{
		String[] parsedCoords = filename.split("\\.")[0].split("x");
		int x = Integer.parseInt(parsedCoords[0]);
		int y = Integer.parseInt(parsedCoords[1]);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("assets/map/"+filename));
			
			String mapdata = br.readLine();
			MapTile m = new MapTile(x,y,mapdata);
			
			br.close();
			
			return m;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public MapTile read(int x, int y)
	{
		return read("assets/map/"+x+"x"+y+".hmt");
	}
	
	public void write(MapTile map)
	{
		String filename = "assets/map/" + map.getx() + "x" + map.gety() + ".hmt";
	try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
		bw.write(map.getMapData());
		bw.close();
	} catch (IOException e) {
		System.out.println("could not write to map file: " + filename);
	}
}
}
