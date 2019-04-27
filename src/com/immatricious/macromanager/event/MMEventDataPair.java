package com.immatricious.macromanager.event;

public class MMEventDataPair<T> {
	public final String key;
	public final T value;
	
	public MMEventDataPair(String key, T value)
	{
		this.key = key;
		this.value = value;
	}
}
