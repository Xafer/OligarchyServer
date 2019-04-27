package com.immatricious.macromanager.character;

import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventListener;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;

public class Character {
	
	private Account account;
	
	private String characterName;
	private CharacterStat stats;
	
	private List<Task> taskQueue = new ArrayList<Task>();
	private Task ongoingTask;
	
	private ThreadedConnection currentConnection;
	
	private double[] localPos;
	
	/**
	 * Character object, usually created from reading character files or recieving character data
	 * @param abilities an array of integers representing character abilities(13 of them)
	 * @param attributes and array of integers representing character attributes(9 of them)
	 */
	public Character(Account account, String characterName, int[] abilities, int[] attributes)
	{
		this.account = account;
		this.characterName = characterName;
		this.stats = new CharacterStat(abilities, attributes);
	}
	
	private void setAbOrAt(int[] levels,boolean ab)
	{
		if(ab) stats.setAbilities(levels);
		else stats.setAtttributes(levels);
		
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Character>("character",this));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.CHARACTER_UPDATED, data));
	}
	
	public void setAbilities(int[] abilities){ setAbOrAt(abilities,true); }
	public void setAttributes(int[] attributes){ setAbOrAt(attributes,false); }
	
	public Account getAccount() { return this.account; }
	public String getName() { return this.characterName; }
	public double[] getPos() { return this.localPos; }
	
	public void setPos(double x, double y) { this.localPos = new double[]{x,y}; }
	
	public int getAbility(Ability a) { return stats.getAbility(a); }
	public int getAttribute(Attribute a) { return stats.getAttribute(a); }
	
	public void queueTask(boolean first, Task task)
	{
		if(first)
		{
			List<Task> newList = (new ArrayList<Task>());
			newList.add(task);
			newList.addAll(taskQueue);
			taskQueue = newList;
		}
		else
			taskQueue.add(task);

		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<Task>("task",task),
												new MMEventDataPair<Character>("character",this));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.TASK_ASSIGNED,data));
	}
	
	public Task getTask(int i) { return (i >= taskQueue.size())? null : taskQueue.get(i); }
	
	public Task nextTask()
	{
		if(taskQueue.size() == 0)
			return null;
		
		ongoingTask = taskQueue.remove(0);
		
		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<Task>("task",ongoingTask),
												new MMEventDataPair<Character>("character",this));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.CHARACTER_NEXT_TASK, data));
		
		return ongoingTask;
		
	}
	
	public Task getOngoingTask() { return ongoingTask; }
	
	public boolean isLogged() { return currentConnection != null; }
	
	public void setConnection(ThreadedConnection tc)
	{
		this.currentConnection = tc;
		MMEventType et = tc != null?MMEventType.CHARACTER_LOGGED_ON:MMEventType.CHARACTER_LOGGED_OFF;

		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Character>("character",this));
		TaskHandler.dispatchEvent(new MMEvent(et,data));
	}
	public ThreadedConnection getConnection() { return this.currentConnection; }
}
