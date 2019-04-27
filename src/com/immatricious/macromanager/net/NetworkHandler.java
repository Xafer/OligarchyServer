package com.immatricious.macromanager.net;

import java.util.Scanner;

import com.immatricious.macromanager.task.TaskHandler;

public class NetworkHandler {

	private ConnectionManager manager;
	
	public NetworkHandler()
	{
		
	}
	
	public void init(TaskHandler th)
	{
		manager = new ConnectionManager(th);
		manager.start();
	}
	
	public ConnectionManager getConnectionManager() { return this.manager; }
}
