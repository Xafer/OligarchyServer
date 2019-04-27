package com.immatricious.macromanager.task;

public enum ActionTargetDataType {
	FLOAT('f'),
	VECTOR('v'),
	STRING('s'),
	PLAYER('p');
	
	private char dataType;
	
	private ActionTargetDataType(char dataType)
	{
		this.dataType = dataType;
	}
	
	public char getDataType() { return this.dataType; }
}
