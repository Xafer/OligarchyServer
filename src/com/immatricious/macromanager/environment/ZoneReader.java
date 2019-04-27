package com.immatricious.macromanager.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.util.DataParser;

public class ZoneReader {
	private String filename;
	private List<Zone> zones;
	
	public ZoneReader(String filename)
	{
		this.filename = filename;
	}
	
	public List<Zone> read()
	{
		if(zones != null)
			return zones;
		
		zones = new ArrayList<Zone>();
		
		String line;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			boolean readingZone = true;//0: reading zone, 1: reading areas
			
			//Create variables containing zone building data
			String nameId = "";
			int[] offset = new int[2];
			List<Area> areas = new ArrayList<>();
			
			//Read zone file
			while((line = br.readLine()) != null)
			{
				//Zone data end
				if(line.charAt(0) == 'e')
				{
					Zone zone = new Zone(nameId, offset, areas);
					
					zones.add(zone);
					
					areas = new ArrayList<>();
					offset = new int[2];
					nameId = "";
					
					readingZone = true;
				}
				//If reading zone header
				else if(readingZone)
				{
					String[] zoneData = line.split(":");
					nameId = zoneData[0];
					
					offset = DataParser.parseIntegers(zoneData[1]);
				}
				//If reading zone areas
				else if(!readingZone)
				{
					String[] areaData = line.split(":");
					int[] tr = DataParser.parseIntegers(areaData[0]);
					int[] bl = DataParser.parseIntegers(areaData[1]);
					boolean safe = areaData[2].charAt(0) == 't';
					
					Area area = new Area(tr[0],tr[1],bl[0],bl[1],safe);
					
					areas.add(area);
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return zones;
	}

	public void write(List<Zone> zones)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
			
			for(Zone zone : zones)
			{
				StringBuilder line = new StringBuilder();
				line.append(zone.getNameId()).append(':');
				
				List<Area> areas = zone.getAreas();
				
				for(Area area : zone.getAreas())
				{
					line.append(area.getTopLeftX()).append(',').append(area.getTopLeftY()).append(',');
					line.append(area.getDownRightX()).append(',').append(area.getDownRightY());
					
					if(areas.indexOf(area) != areas.size()-1);
						line.append('&');
				}
				
				bw.write(line.toString());
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
