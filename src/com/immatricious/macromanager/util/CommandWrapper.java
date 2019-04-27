package com.immatricious.macromanager.util;

import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.net.ThreadedConnection;
import com.immatricious.macromanager.task.TaskHandler;

public class CommandWrapper {
	
	private List<CommandListener> commandListeners;
	private TaskHandler th;
	
	public CommandWrapper(TaskHandler th)
	{
		commandListeners = new ArrayList<CommandListener>();
		this.th = th;
	}
	
	public void addCommandListener(CommandListener listener){ commandListeners.add(listener); }
	public void removeCommandListner(CommandListener listener) { commandListeners.remove(listener); }
	
	public void dispatchCommand(String cmd, CommandType cmdType)
	{
		for(CommandListener l : commandListeners)
		{
			l.read(cmd, cmdType);
		}
	}
	
	public void parseCommand(String cmd)
	{
		String[] args = cmd.split(" ");
		
		boolean parsed = false;
		
		switch(args[0])
		{
		case "login":
			List<ThreadedConnection> tcl = th.getConnectionManager().getConnections();
			for(ThreadedConnection tc : tcl)
			{
				if(tc.getConnectionId()!= Integer.parseInt(args[1])) continue;
				
				if(args.length < 4)tc.login(th.getCharacterManager().getAccount(args[2]));
				else tc.login(th.getCharacterManager().getCharacter(args[3]));
				
				parsed = true;
			}
			break;
		default:
			break;
		}
		
		dispatchCommand((parsed?"":"error: could not parse command ")+cmd, CommandType.CMD_INPUT);
	}
}
