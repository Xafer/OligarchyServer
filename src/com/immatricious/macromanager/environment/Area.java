package com.immatricious.macromanager.environment;

public class Area
{
	private int topLeftX;
	private int topLeftY;
	
	private int downRightX;
	private int downRightY;
	
	private boolean safe;
	
	/**
	 * Define a sub area for a zone
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 */
	public Area(int a1, int a2, int b1, int b2, boolean safe)
	{
		topLeftX = a1;
		topLeftY = a2;
		downRightX = b1;
		downRightY = b2;
		
		this.safe = safe;
	}
	
	/**
	 * Returns the area of this area 
	 * @return (b1-a1)*(b2-a2)
	 */
	public int getArea()
	{
		return (downRightX-topLeftX)*(downRightY-topLeftY);
	}
	
	public int[] getRandomPoint()
	{
		int[] res = new int[2];

		res[0] = (int) (Math.random()*(downRightX-topLeftX) + topLeftX);
		res[1] = (int) (Math.random()*(downRightY-topLeftY) + topLeftY);
		
		return res;
	}

	public int getTopLeftX() { return topLeftX; }
	public int getTopLeftY() { return topLeftY; }

	public int getDownRightX() { return downRightX; }
	public int getDownRightY() { return downRightY; }
}
