package com.immatricious.macromanager.environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathReader {
	
	private String filename;
	private List<PathPoint> points;
	
	public PathReader(String filename)
	{
		this.filename = filename;
	}
	
	public List<PathPoint> read()
	{
		points = new ArrayList<PathPoint>();
		
		String line;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			while((line = br.readLine()) != null && line.charAt(0) != 'e')
			{
				PathPoint path;
				String[] pointParts = line.split(":");
				
				//Parse ID
				int id = Integer.parseInt(pointParts[0]);
				
				//Parse Pos
				String[] pos = pointParts[1].split(",");
				float x = Float.parseFloat(pos[0]);
				float y = Float.parseFloat(pos[1]);
				
				//Parse Next Paths
				String[] nextU = pointParts[2].split(",");
				int[] next = new int[nextU.length];
				for(int i = 0, j = nextU.length; i < j; i++)
					next[i] = Integer.parseInt(nextU[i]);
				
				//Create path
				path = new PathPoint(x,y,next,id);
				
				points.add(path);
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return points;
	}

	public void write(List<PathPoint> points)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false));
			
			for(PathPoint point : points)
			{
				StringBuilder line = new StringBuilder();
				line.append(point.getID()).append(':').append(point.getX()).append(',').append(point.getY()).append(':');
				for(int next : point.getPaths())
				{
					if(next != point.getPaths()[0])
						line.append(',');
					
					line.append(next);
				}
				bw.write(line.toString());
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException e) {
			System.out.println("could not write to path file: " + filename);
		}
	}
}
