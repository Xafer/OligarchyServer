package com.immatricious.macromanager.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.character.CharacterManager;
import com.immatricious.macromanager.environment.Environment;
import com.immatricious.macromanager.environment.MapTileManager;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventDispatcher;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.net.ConnectionManager;
import com.immatricious.macromanager.net.Interpret;
import com.immatricious.macromanager.net.NetworkHandler;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.util.CommandWrapper;
import com.immatricious.macromanager.util.DataParser;
import com.immatricious.macromanager.character.Character;

/**
 * Handles the task to be sent to the custom clients
 * @author June
 *
 */
public class TaskHandler {
	private NetworkHandler nh;
	private CharacterManager cm;
	private MapTileManager mm;
	private Interpret interpret;
	private Environment environment;
	private TaskGenerator taskGenerator;
	
	static public CommandWrapper log;
	
	private TaskReader taskReader;
	
	private MMEventDispatcher dispatcher;
	private static MMEventDispatcher currentDispatcher;
	
	//A task pool with priorities
	//Tasks will be executed in order of priority
	//1: Prioritized over all tasks. If there is context compatible one (e.g. required character is available), will be returned
	//2: Passive tasks that must done on interval, won't be sent unless tasks in 1 are empty
	private Map<TaskPriority, List<Task>> taskPool = new HashMap<TaskPriority, List<Task>>();
	private Map<String,Task> taskByName = new HashMap<String,Task>();
	
	public TaskHandler()
	{
		dispatcher = new MMEventDispatcher();
		currentDispatcher = dispatcher;
		
		TaskHandler.log = new CommandWrapper(this);
		
		for(int i = 0 ; i < 3; i++)
			taskPool.put(TaskPriority.getFromInt(i), new ArrayList<Task>());
	}
	
	public void read()
	{
		cm.read();
		environment.read();
		generateTasks();
	}
	
	public void generateTasks() {taskGenerator.generateAll(); }
	
	public void init()
	{
		interpret = new Interpret(this);
		
		nh = new NetworkHandler();
		nh.init(this);
		
		this.cm = new CharacterManager(this);
		
		this.mm = new MapTileManager();
		
		this.environment = new Environment();
		
		taskReader = new TaskReader(environment);
		taskGenerator = new TaskGenerator(this);
	}
	
	public void addTask(Task task, TaskPriority priority)
	{
		taskByName.put(task.getName(), task);

		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Task>("task",task));
		dispatcher.dispatchEvent(new MMEvent(MMEventType.TASK_INSERT,data));
	}
	
	public void poolTask(Task task, TaskPriority priority)
	{
		if(!taskPool.containsKey(priority))
			taskPool.put(priority, new ArrayList<Task>());
		
		if(priority != TaskPriority.UNLISTED)taskPool.get(priority).add(task);
		
		System.out.println("Pooling " + task.getName() + " to " + priority.toString());
		
		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<Task>("task",task),
												new MMEventDataPair<TaskPriority>("priority",priority));
		dispatcher.dispatchEvent(new MMEvent(MMEventType.TASK_POOLED,data));
	}
	
	public Task findAndQueueTask(Character character)
	{
		for(int i = 0; i < 2; i++)
		{
			TaskPriority p = TaskPriority.getFromInt(i);
			List<Task> pool = taskPool.get(p);
			
			for(Task task : pool)
			{
				if(cm.isFit(task, character))
				{
					character.queueTask(false, task);
					pool.remove(task);
					
					MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<Task>("task",task),
															new MMEventDataPair<TaskPriority>("priority",p));
					TaskHandler.dispatchEvent(new MMEvent(MMEventType.TASK_UNPOOLED,data));
					
					return task;
				}
			}
		}
		return null;
	}
	
	/**
	 * Fetch a task for character if available, or log in available character
	 * @param character
	 */
	public Task assignTasks(Character character)
	{
		Iterator<Character> i = cm.getCharacters().iterator();
		Character c = character;
		Task t = null;
		boolean first = true;
		
		do
		{
			if(first) first = false;
			else c = i.next();
			
			if(c.isLogged() && !first)
				continue;
			
			t = findAndQueueTask(c);
			if(t != null)
				break;
		}while(i.hasNext());
		
		if(	t != null &&
			c != character)
		{
			ThreadedConnection tc = character.getConnection();
			tc.login(c);
		}
		
		return t;
	}
	
	public synchronized Task fetchTask(Character character)
	{	
		Task t;

		if((t = character.nextTask()) == null)//
			t = assignTasks(character);
		
		MMEventDataMap data = new MMEventDataMap(	new MMEventDataPair<Task>("task",t),
												new MMEventDataPair<Character>("character",character));
		dispatcher.dispatchEvent(new MMEvent(MMEventType.TASK_FETCHED, data));
			
		return t;
	}
	
	public Interpret getInterpret() { return this.interpret; }
	
	public TaskReader getTaskReader() { return this.taskReader; }
	public CharacterManager getCharacterManager() { return this.cm; }
	public Environment getEnvironment() { return this.environment; }
	
	public Collection<Task> getTasks() { return taskByName.values(); }
	public Map<TaskPriority, List<Task>> getTaskPool() { return this.taskPool; }
	public Task getTaskByName(String task) { return taskByName.get(task); }
	
	/**
	 * Returns task priority from the task pool
	 * @param task task to check priority from
	 * @return task priority, or -1 if it's not in the pool
	 */
	public int getTaskPriority(Task task)
	{
		int priority = -1;
		
		
		
		return priority;
	}
	
	public MMEventDispatcher getDispatcher()
	{
		 return this.dispatcher;
	}
	
	public void close()
	{
		environment.close();
	}
	
	public ConnectionManager getConnectionManager() { return nh.getConnectionManager(); }
	
	public MapTileManager getMapManager() { return this.mm; }
	
	public static MMEventDispatcher getCurrentDispatcher() { return currentDispatcher; }
	public static void dispatchEvent(MMEvent e) { currentDispatcher.dispatchEvent(e); }
}
