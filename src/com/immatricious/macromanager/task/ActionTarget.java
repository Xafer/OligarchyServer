package com.immatricious.macromanager.task;

public class ActionTarget {
	private String asMsg;
	private final ActionTargetDataType dataType;
	
	public ActionTarget(String asMsg)
	{
		this.asMsg = asMsg;
		dataType = readDataType();
	}
	
	private ActionTargetDataType readDataType()
	{
		char dataTypeChar = this.asMsg.charAt(0);
		
		for(ActionTargetDataType at : ActionTargetDataType.values())
		{
			if(at.getDataType() == dataTypeChar)
				return at;
		}
		
		return null;
	}
	
	public String getMessageString(){ return asMsg; }
	
	public ActionTargetDataType getDataType() { return this.dataType; }
}
