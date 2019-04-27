package com.immatricious.macromanager.task;

import java.util.ArrayList;
import java.util.List;

/**
 * A task is an organized group of actions generated depending on the context
 * @author June
 *
 */
public class Task {
	
	private String taskName;
	
	private TaskReader taskReader;
	private List<Action> actions = new ArrayList<Action>();
	private boolean renewable;
	private TaskRequirement requirements;
	private long waitUntil;
	
	public Task(String taskName, List<Action> actions, boolean renewable, TaskRequirement requirements)
	{
		this.taskName = taskName;
		setActions(actions);
		this.renewable = renewable;
		this.requirements = requirements;
		this.waitUntil = -1;
	}
	
	public void addAction(Action action) { actions.add(action); }
	
	public void setActions(List<Action> actions)
	{
		this.actions = actions;
	}
	
	public String getName() { return this.taskName; }
	
	public String getMessageString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("t");
		for(Action action : actions)
		{
			sb.append(action.getMessageString());
			if(actions.indexOf(action) == actions.size()-1)
				break;
			sb.append('%');
		}
		
		return sb.toString();
	}
	
	public boolean isRenewable() { return renewable; }
	
	public TaskRequirement getTaskRequirements() { return this.requirements; }
}
