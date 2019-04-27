package com.immatricious.macromanager.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import com.immatricious.macromanager.character.Account;
import com.immatricious.macromanager.character.Character;
import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.task.TaskHandler;

public class ThreadedConnection extends Thread
{
	private ConnectionManager cm;
	private Socket socket;
	
	private DataOutputStream os;
	
	private Account account;
	private Character character;
	private String msg;
	
	private final int id;
	private static int ids = 0;
	public static int getNextConnectionID() { return ids; }
	
	private boolean readTasks = false;
	
	public ThreadedConnection(ConnectionManager cm)
	{
		this.cm = cm;
		id = ids++;
	}
	
	@Override
	public void run()
	{
		socket = cm.getSocketFromConnection(this);
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<ThreadedConnection>("connection",this));
		
		try {
			DataInputStream is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			
			msg = "";

			TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_STARTED,data));
			
			while((msg = is.readUTF()) != null)
			{
				if(msg.charAt(0)!='p')
					System.out.println("recieved: " + msg);
				cm.processClientMessage(this);
			}
			
		} catch (IOException e) {
			
			cm.endConnection(this);
			
			System.out.println("Connection interrupted with connection " + id + ":" +  socket.getInetAddress());
			TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_ENDED,data));
			
			if(character != null)
			{
				character.setConnection(null);
				account.setLogged(false);
				TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_LOGGED_OFF,data));
			}
		}
	}
	
	public synchronized void sendMessage(String msg)
	{

		try {
			System.out.println("sent: " + msg);
			os.writeUTF(msg);
			MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<String>("message",msg));
			TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.SENT_MSG,data));
		} catch (IOException e) {
			System.out.println("Could not send message to " + character.getName());
		}
	}
	
	public synchronized void sendChatMessage(String msg, String channel)
	{
		sendMessage("c"+channel+":"+msg);
	}
	
	public synchronized void login(Account acc)
	{
		account = acc;
		account.setLogged(true);
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<ThreadedConnection>("connection",this));
		TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_LOGGED_ON,data));
		sendMessage("?" + account.getUsername() + ":" + account.getPassword());
	}
	
	public synchronized void login(Character c)
	{
		if(c == null)
			return;
		
		this.character = c;
		this.account = character.getAccount();
		sendMessage("?" + account.getUsername() + ":" + account.getPassword() + ":" + character.getName());

		character.setConnection(this);
		
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<ThreadedConnection>("connection",this));
		TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_LOGGED_ON,data));
		TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CHARACTER_LOGGED_ON,data));
	}
	
	public synchronized void logoff()
	{
		Account acc = character.getAccount();

		sendMessage("?");
		
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<ThreadedConnection>("connection",this));
		TaskHandler.getCurrentDispatcher().dispatchEvent(new MMEvent(MMEventType.CONNECTION_LOGGED_OFF,data));
		
		acc.setLogged(false);
		character.setConnection(null);
		
		this.character = null;
	}
	
	public String getLastMessage() { return msg; }
	public String getCharacterName() { return character == null?null:character.getName(); }
	public Character getCharacter() { return this.character; }
	public Account getAccount() { return this.account; }
	public String getIP() { return this.socket.getInetAddress().getHostAddress(); }
	public int getConnectionId() { return this.id; }
}
