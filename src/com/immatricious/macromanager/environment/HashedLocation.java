package com.immatricious.macromanager.environment;

public class HashedLocation {

	private int hash;
	private double x;
	private double y;
	
	public HashedLocation(int hash, double x, double y) {
		this.hash = hash;
		this.x = x;
		this.y = y;
	}
	
	public int getHash() { return this.hash; }
	public double getX() { return this.x; }
	public double getY() { return this.y; }

	public String getMessageString() {
		return "o" + hash + ":" + x + "," + y;
	}

}
