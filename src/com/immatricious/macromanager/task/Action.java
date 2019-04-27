package com.immatricious.macromanager.task;

public class Action {
	
	private ActionType at;
	private ActionTarget target;
	private String asMsg;//As message
	
	public Action(ActionType at, ActionTarget target)
	{
		this.at = at;
		this.target = target;
		getMessageString();
	}
	
	public String getMessageString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(at.getActionChar());
		if(target != null)sb.append(target.getMessageString());
		
		asMsg = sb.toString();
		
		return asMsg;
	}
	
	public ActionType getActionType() { return this.at; }
}
