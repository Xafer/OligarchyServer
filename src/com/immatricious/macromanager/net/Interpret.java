package com.immatricious.macromanager.net;

import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.util.DataParser;
import com.immatricious.macromanager.character.Account;
import com.immatricious.macromanager.character.Character;
import com.immatricious.macromanager.character.CharacterManager;
import com.immatricious.macromanager.environment.HashedLocation;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;

public class Interpret {
	private TaskHandler th;

	private String lastMessage;
	
	public Interpret(TaskHandler th)
	{
		this.th = th;
	}
	
	public void processClientMessage(ThreadedConnection connection)
	{
		
		String msg = connection.getLastMessage();
		
		lastMessage = msg;
		
		CharacterManager cm = th.getCharacterManager();
		
		Character character = connection.getCharacter();
		
		Task t = null;
		
		String msgData = msg.substring(1, msg.length());
		MMEventDataMap cdata = new MMEventDataMap(new MMEventDataPair<ThreadedConnection>("connection",connection));
		
		switch(msg.charAt(0))//0:a, 1:character name, 2: character abilities, 3: character attributes
		{
		case '?':
			connection.login(th.getCharacterManager().getTaskableCharacter());
			break;
		
		case 'p':
			if(character != null)
			{
				String[] spl = msgData.split(":");
				character.setPos(Double.parseDouble(spl[0]),Double.parseDouble(spl[1]));
			}
			TaskHandler.dispatchEvent(new MMEvent(MMEventType.RCV_UPDATE_CHAR, cdata));
			break;
			
		case 'f':
			
			if(character == null)
				break;
			
			t = th.fetchTask(character);
			
			th.getDispatcher().dispatchEvent(new MMEvent(MMEventType.RCV_FETCH, cdata));
			
			if(t!= null)
				connection.sendMessage(t.getMessageString());
			break;
			
		case 'o'://0:a, 1:character name, 2: character abilities, 3: character attributes
			HashedLocation h = th.getEnvironment().getHashedLocation().getHashedLocation(Integer.parseInt(msgData));
			System.out.println("sending " + h);
			
			th.getDispatcher().dispatchEvent(new MMEvent(MMEventType.RCV_LOCATE, cdata));
			
			if(h != null)
				connection.sendMessage(h.getMessageString());
			break;
			
		case 'c':
			th.getDispatcher().dispatchEvent(new MMEvent(MMEventType.RCV_CHAT, cdata));
			break;
		
		case 'b':
			th.getCharacterManager().updateBuddies(msgData);
			th.getDispatcher().dispatchEvent(new MMEvent(MMEventType.RCV_UPDATE_BUDDIES, cdata));
			break;
		case 'm':
			th.getMapManager().recieveMapData(msgData);
			th.getDispatcher().dispatchEvent(new MMEvent(MMEventType.RCV_MAPDATA, cdata));
			break;
		}
	}
	
	public String getLastMessage() { return this.lastMessage; }
}
