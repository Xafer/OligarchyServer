package com.immatricious.macromanager.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.immatricious.macromanager.character.Account;
import com.immatricious.macromanager.character.CharacterManager;
import com.immatricious.macromanager.task.TaskHandler;

public class ConnectionManager extends Thread {
	private Map<ThreadedConnection,Socket> connections = new HashMap<ThreadedConnection,Socket>();
	private Interpret interpret;
	private TaskHandler th;
	
	public ConnectionManager(TaskHandler th)
	{
		this.th = th;
		this.interpret = th.getInterpret();
	}
	
	public void run()
	{
		System.out.println("Awaiting connections");
		try {
			ServerSocket ss = new ServerSocket(2123);
			
			while(!ss.isClosed())
			{
				Socket socket = ss.accept();
				
				System.out.println("Connected to " + socket.getInetAddress());
				
				ThreadedConnection tc = new ThreadedConnection(this);
				
				connections.put(tc, socket);
				
				tc.start();
			}
				
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocketFromConnection(ThreadedConnection tc) { return connections.get(tc); }
	
	public void broadCast(String msg)
	{
		for(ThreadedConnection s : connections.keySet())
		{
			s.sendMessage(msg);
		}
	}
	
	public void sendMessageToConnection(int connectionID, String msg) {
		for(ThreadedConnection s : connections.keySet())
		{
			if(s.getId() != connectionID)
				continue;
			s.sendMessage(msg);
		}
	}
	
	public void sendChatMessageToConnection(int connectionID, String msg, String channel) {
		for(ThreadedConnection s : connections.keySet())
		{
			if(s.getId() != connectionID)
				continue;
			s.sendChatMessage(msg, channel);
		}
	}

	public void processClientMessage(ThreadedConnection c)
	{
		interpret.processClientMessage(c);
	}
	
	public void endConnection(ThreadedConnection c)
	{
		connections.remove(c);
		th.getMapManager().writeMaps();
	}
	
	public ThreadedConnection getConnectionFromCharacterName(String characterName)
	{
		for(ThreadedConnection c : connections.keySet())
			if(c.getCharacterName().equals(characterName))
				return c;
		
		return null;
	}
	
	public List<ThreadedConnection> getConnections() {
		return new ArrayList<ThreadedConnection>(connections.keySet());
	}
}
