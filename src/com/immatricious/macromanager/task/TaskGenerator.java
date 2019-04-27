package com.immatricious.macromanager.task;

import java.io.File;

/**
 * Generates tasks to be put in the taskpool of the taskHandler passed as constructor argument
 * @author June
 *
 */
public class TaskGenerator {
	
	private TaskHandler th;
	private TaskReader tr;
	
	public TaskGenerator(TaskHandler th)
	{
		this.th = th;
		
		this.tr = th.getTaskReader();
	}
	
	public void generateAll()
	{
		File taskScriptFolder = new File("assets/tasksscripts");
		
		for(File file :taskScriptFolder.listFiles())
		{
			Task t = tr.read(file.getName());
			System.out.println(t.getMessageString());
			th.addTask(t, TaskPriority.UNLISTED);
		}
	}
}
