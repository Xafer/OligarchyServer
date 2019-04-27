package com.immatricious.macromanager.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.TaskHandler;

public class HashedLocationReader {

	private String filename;
	private Map<Integer,HashedLocation> hashes;
	
	public HashedLocationReader(String filename)
	{
		this.filename = filename;
	}
	
	public void write(Map<Integer,HashedLocation> hashes)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
			
			for(HashedLocation h : hashes.values())
			{
				String line = h.getMessageString();
				bw.write(line.substring(1,line.length()));
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<Integer,HashedLocation> read()
	{
		Map<Integer,HashedLocation> locations = new HashMap<Integer,HashedLocation>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			String line;
			
			while((line = br.readLine()) != null)
			{
				String[] data = line.split(":");
				int hash = Integer.parseInt(data[0]);
				
				String[] loc = data[1].split(",");
				double x = Double.parseDouble(loc[0]);
				double y = Double.parseDouble(loc[1]);
				
				HashedLocation h = new HashedLocation(hash,x,y);
				locations.put(hash,h);
				MMEventDataMap eventData = new MMEventDataMap(new MMEventDataPair<HashedLocation>("hashedLocation",h));
				TaskHandler.dispatchEvent(new MMEvent(MMEventType.HASHLOC_ADDED,eventData));
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hashes = locations;
		return locations;
	}
}
