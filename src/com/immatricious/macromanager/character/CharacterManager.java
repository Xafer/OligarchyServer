package com.immatricious.macromanager.character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.event.MMEventDataMap;
import com.immatricious.macromanager.event.MMEvent;
import com.immatricious.macromanager.event.MMEventType;
import com.immatricious.macromanager.event.MMEventDataPair;
import com.immatricious.macromanager.task.Task;
import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.task.TaskRequirement;

public class CharacterManager {
	private Map<String,Character> characters = new HashMap<String,Character>();
	private CharacterReader cr;
	private Map<String,Account> accounts = new HashMap<String, Account>();
	
	private Map<String,HavenBuddy> buddies = new HashMap<String,HavenBuddy>();
	
	private int getFitIndex;//Using this variable to rotate through characters evenly
	
	private TaskHandler th;
	
	public CharacterManager(TaskHandler th)
	{
		this.th = th;
		this.cr = new CharacterReader("assets/characters.txt");
		
		getFitIndex = 0;
	}
	
	public void read()
	{
		Map<Account,List<Character>> chars = cr.read();
		
		for(Account acc : chars.keySet())
			addAccount(acc);
		
		for(List<Character> cl : chars.values())
			for(Character c : cl)
				addCharacter(c);
	}
	
	public Character getFitCharacter(TaskRequirement requirements, Character character)
	{
		Character c = null;
		
		if(isFit(requirements,character))
			c = character;
		else
		{
			Character ch;
			int n = 0;
			int l = characters.size();
			
			do
			{
				ch = ((ArrayList<Character>)(characters.values())).get(getFitIndex);
				getFitIndex++;
				if(getFitIndex >= characters.size())
					getFitIndex = 0;
				n++;
			}while(!isFit(requirements,ch) && n < l);
				
			c = ch;
		}
		
		return c;
	}

	public boolean isFit(TaskRequirement requirements, Character character)
	{
		boolean o = requirements.onlyUseBestCharacter();
		
		for(int i = 0, l = Ability.values().length; i < l; i++)
		{
			Ability a = Ability.values()[i];
			
			//If highest stat character must be used
			if(requirements.onlyUseBestCharacter() && character != findHighestAbility(a))
				return false;
			//If stat is higher than required
			else if(requirements.getAbilityRequirement(a) > character.getAbility(a))
				return false;
		}
		
		for(int i = 0, l = Attribute.values().length; i < l; i++)
		{
			Attribute a = Attribute.values()[i];
			
			//If highest stat character must be used
			if(requirements.onlyUseBestCharacter() && character != findHighestAttribute(a))
				return false;
			//If stat is higher than required
			else if(requirements.getAttributeRequirement(a) > character.getAttribute(a))
				return false;
		}
		
		return true;
	}

	public boolean isFit(Task task, Character character) {
		return isFit(task.getTaskRequirements(),character);
	}
	
	public Character findHighestAbility(Ability a)
	{
		Character h = null;
		
		for(Character c : characters.values())
		{
			if(	h == null ||
				c.getAbility(a) > h.getAbility(a))
				h = c;
		}
		
		return h;
	}
	
	public Character findHighestAttribute(Attribute a)
	{
		Character h = null;
		
		for(Character c : characters.values())
		{
			if(	h == null ||
				c.getAttribute(a) > h.getAttribute(a))
				h = c;
		}
		
		return h;
	}
	
	public Character getCharacter(String characterName){ return characters.get(characterName); }
	public Account getAccount(String accountName) { return this.accounts.get(accountName); }
	
	public String[] getAccountNames()
	{
		String[] accountNames = new String[accounts.keySet().size()];
		int i = 0;
		for(String a : accounts.keySet())
		{
			accountNames[i] = a;
			i++;
		}
		
		return accountNames;
	}
	
	public void addCharacter(Character c)
	{
		characters.put(c.getName(), c);
		
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Character>("character",c));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.CHARACTER_ADDED,data));
	}
	public void addAccount(Account acc)
	{
		accounts.put(acc.getUsername(), acc);
		
		MMEventDataMap data = new MMEventDataMap(new MMEventDataPair<Account>("account",acc));
		TaskHandler.dispatchEvent(new MMEvent(MMEventType.ACCOUNT_ADDED,data));
	}
	
	public Collection<Character> getCharacters() { return characters.values(); }
	
	public void updateBuddies(String buddyData)
	{
		buddies.clear();
		String[] b = buddyData.split(":");
		
		for(String buddy : b)
		{
			String[] buddyInfo = buddy.split(",");
			HavenBuddy hb = new HavenBuddy(buddyInfo[0]);
			hb.setLogged(buddyInfo[1].equals("t"));
			buddies.put(buddyInfo[0], hb);
		}
		
	}

	/**
	 * reads all characters and tries to assign a task. Returns on first success
	 * @return Character that has a newly assigned task
	 */
	public Character getTaskableCharacter() {
		for(Character c : characters.values())
		{
			if(c.isLogged())
				continue;
			
			Task t = th.findAndQueueTask(c);
			
			if(t != null)
				return c;
		}
		return null;
	}
}
