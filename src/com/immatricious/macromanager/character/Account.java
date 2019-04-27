package com.immatricious.macromanager.character;

import java.util.ArrayList;
import java.util.List;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;

public class Account {
	private String username;
	private String password;
	private List<String> characters = new ArrayList<String>();
	
	private boolean logged;
	
	public Account(String username, String password)
	{
		this.username = username;
		this.password = password;
		
		this.logged = false;
	}
	
	public void add(String character) { if(!characters.contains(character))characters.add(character); }
	
	public String getUsername() { return this.username; }
	public String getPassword() { return this.password; }

	public List<String> getCharacters() { return this.characters; }

	public boolean isLogged() { return this.logged; }
	public void setLogged(boolean b)
	{
		this.logged = b;
		
		MMEventType type = b?MMEventType.ACCOUNT_LOGGED_ON:MMEventType.ACCOUNT_LOGGED_OFF;
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Account>("account",this));
		TaskHandler.dispatchEvent(new MMEvent(type,data));
	}
}
