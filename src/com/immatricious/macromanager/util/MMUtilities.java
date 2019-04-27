package com.immatricious.macromanager.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.environment.MapTile;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventData;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.TaskHandler;

public class MMUtilities {
	
	private static List<ClientLogThread> clThreads;
	
	private static class ClientLogThread extends Thread{
		
		private InputStream is;
		private boolean errorReader;
		
		private static int ids = 0;
		public final int id;
		
		public ClientLogThread(InputStream is, boolean errorReader)
		{
			clThreads = new ArrayList<ClientLogThread>();
			this.is = is;
			this.id = ids++;
			
			this.errorReader = errorReader;
		}
		
		@Override
		public void run()
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			try {
				while((line = br.readLine()) != null)
				{
					MMEventType et = (errorReader)?MMEventType.LOADED_APP_ERROR:MMEventType.LOADED_APP_INPUT;
					TaskHandler.dispatchEvent(new MMEvent(et,new MMEventDataMap(new MMEventDataPair<String>("input",this.id + " >> " + line))));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Map<Integer,Process> startedProcesses = new HashMap<Integer,Process>(); 
	public static Process runOligarchyClient()
	{
		String path = "";
		String command = "";
		Process p = null;
		try {
			System.out.println("trying to open assets/clientpath.txt");
			BufferedReader br = new BufferedReader(new FileReader("assets/clientpath.txt"));
			path = br.readLine();
			br.close();
			System.out.println(path + "/run.bat");
			br = new BufferedReader(new FileReader(path + "/run.bat"));
			command = br.readLine();
			br.close();
			int connectionId = ThreadedConnection.getNextConnectionID();
			p = Runtime.getRuntime().exec(command,null,new File(path));
			startedProcesses.put(connectionId, p);
			ClientLogThread t = new ClientLogThread(p.getInputStream(),false);
			ClientLogThread te = new ClientLogThread(p.getErrorStream(),true);
			clThreads.add(t);
			clThreads.add(te);
			t.start();
			te.start();
			return p;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void stopOligarchyClient(Process p)
	{
		startedProcesses.remove(p);
		p.destroyForcibly();
	}
	
	public static void stopOligarchyClient(int pid)
	{
		Process p = startedProcesses.get(pid);
		if(p != null)stopOligarchyClient(p);
	}
}
