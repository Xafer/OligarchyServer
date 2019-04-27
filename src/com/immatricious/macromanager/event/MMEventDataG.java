package com.immatricious.macromanager.event;

public class MMEventDataG<T> implements MMEventData {
	private T[] data;
	
	public MMEventDataG(T[] data)
	{
		this.data = data;	
	}
	
	public T[] getData() { return this.data; }
}
