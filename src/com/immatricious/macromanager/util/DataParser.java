package com.immatricious.macromanager.util;

public class DataParser {
	public static float[] parseVector(String msg)
	{
		float[] r = new float[2];
		
		String d = msg.substring(1, msg.length());
		String[] strValues = d.split(",");

		r[0] = Float.parseFloat(strValues[0]);
		r[1] = Float.parseFloat(strValues[1]);
		
		return r;
	}
	
	//Parses integers in a string separated by commas
	public static int[] parseIntegers(String v)
	{
		String[] values = v.split(",");
		int l = values.length;
		
		int[] parsed = new int[l];
		
		for(int i = 0; i < l; i++)
			parsed[i] = Integer.parseInt(values[i]);
		
		return parsed;
	}
	
	//Parses Numbers in a string separated by commas. Pass in array type for return type
	/*public static Number[] parseNumbers(Number[] a, String data)
	{
		String[] values = data.split(",");
		int l = values.length;
		
		T[] parsed = a;
		
		for(int i = 0; i < l; i++)
		{
			switch(a[i].)
			{
			case 
			}
			parsed[i] = Integer.parseInt(values[i]);
		}
		
		return parsed;
	}*/
}
