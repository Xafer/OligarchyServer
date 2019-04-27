package com.immatricious.macromanager.task;

public enum TaskPriority {
	UNLISTED(-1),
	PRIMARY (0),
	RECURRENT (1),
	STANDBY (2);
	
	private final int priorityNumber;
	
	private TaskPriority(int p) { this.priorityNumber = p; }
	
	public int getPriority() { return this.priorityNumber; }
	
	public static TaskPriority getFromInt(int priority) { for(TaskPriority p : TaskPriority.values())if(priority == p.getPriority())return p;return null; }
}
