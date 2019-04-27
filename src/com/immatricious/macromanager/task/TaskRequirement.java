package com.immatricious.macromanager.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.character.Ability;
import com.immatricious.macromanager.character.Attribute;
import com.immatricious.macromanager.character.CharacterStat;

public class TaskRequirement {
	//Requirements relate to level
	//Though, if -1, fetch highest level character for task
	private Map<Ability, Integer> abilities = new HashMap<Ability, Integer>();
	private Map<Attribute, Integer> attributes = new HashMap<Attribute, Integer>();

	private boolean onlyUseBest;
	
	public TaskRequirement()
	{
		onlyUseBest = false;
	}
	
	public Map<Ability, Integer>  getAbilityRequirements() { return abilities; }
	public Map<Attribute, Integer>  getAttributeRequirement() { return attributes; }
	
	public int getAbilityRequirement(Ability a) { return abilities.getOrDefault(a,0); }
	public int getAttributeRequirement(Attribute a) { return attributes.getOrDefault(a,0); }

	public void setAbility(Ability a, int n)
	{ 
		abilities.put(a,n);
		if(n == -1)onlyUseBest = true;
	}
	
	public void setAttribute(Attribute a, int n)
	{
		attributes.put(a,n);
		if(n == -1)onlyUseBest = true;
	}
	
	public void setAttributes(int[] attributes)
	{
		for(int i = 0, l = attributes.length; i < l; i++)
		{
			int attributeValue = attributes[i];
			Attribute attributeType = Attribute.values()[i];
			
			if(attributeValue != 0)
				this.attributes.put(attributeType,attributeValue);
		}
	}
	
	
	public void setAbilities(int[] abilities) {
		
		for(int i = 0, l = abilities.length; i < l; i++)
		{
			int abilityValue = abilities[i];
			Ability abilityType = Ability.values()[i];
			
			if(abilityValue != 0)
				this.abilities.put(abilityType,abilityValue);
		}
	}
	
	public boolean onlyUseBestCharacter() { return this.onlyUseBest; }
	public void setOnlyUserBestCharacter(boolean o) { this.onlyUseBest = o; }
}
