package com.immatricious.macromanager.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.environment.Environment;
import com.immatricious.macromanager.util.DataParser;

/**
 * Reads tasks from files and loads them
 * @author June
 *
 */
public class TaskReader {
	
	private Environment environment;
	
	public TaskReader(Environment environment)
	{
		this.environment = environment;
	}
	
	private List<Action> parseHavenTaskScript(String s)
	{
		char a = s.charAt(0);//Action type char
	
		List<Action> actions = new ArrayList<Action>();
		
		if(a == 'e')
			return null;
		
		ActionTarget target = null;
		if(s.length() > 1)
			target = new ActionTarget(s.substring(1, s.length()));
		
		switch(a)
		{
		case 'h'://Hearth
			actions.add(new Action(ActionType.HEARTH,target));
			break;
		case 'p'://assemble moving action path with prepaths
			actions.addAll(environment.pathFindTo(target));
			break;
		case 'm'://move relative to local coordHelper location
			actions.add(new Action(ActionType.MOVE,target));
			break;
		case 'n'://move relative to player pos
			actions.add(new Action(ActionType.RELATIVEMOVE,target));
			break;
		case 'r'://right click at or on
			actions.add(new Action(ActionType.RIGHTCLICK,target));
			break;
		case 'c'://flowemenu choose
			actions.add(new Action(ActionType.FLOWERCHOOSE,target));
			break;
		case 'l'://follow game object
			actions.add(new Action(ActionType.FOLLOW,target));
			break;
		}
		
		return actions;
	}
	
	/**
	 * Reads and interpret
	 * @return newly generated actions
	 */
	public Task read(String filename)
	{
		List<Action> actions = new ArrayList<Action>();
		
		String line;
		String taskName = "";
		TaskRequirement tr = new TaskRequirement();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("assets/tasksscripts/"+filename));
			
			boolean readHeader = true;
			
			System.out.println("reading " + "assets/tasksscripts/"+filename);
			
			while((line = br.readLine()) != null && !(line.length() < 2 && line.charAt(0) == 'e'))
			{
				//If reader hasn't been read, parse header
				if(readHeader)
				{
					String[] headerStrings = line.split(":");
					taskName = headerStrings[0];
					int[] abilities = DataParser.parseIntegers(headerStrings[1]);
					int[] attributes = DataParser.parseIntegers(headerStrings[2]);
					
					tr.setAbilities(abilities);
					tr.setAttributes(attributes);
					
					readHeader = false;
				}
				//Otherwise parse haven task script commands
				else
				{
					List<Action> lineTask = parseHavenTaskScript(line);
					
					if(lineTask == null)
						break;
					
					actions.addAll(lineTask);
				}
			}
				
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Task task = new Task(taskName,actions,false, tr);
		
		return task;
	}
	
	public void setEnvironment(Environment environment) { this.environment = environment; }
}
