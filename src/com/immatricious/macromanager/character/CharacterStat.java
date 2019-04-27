package com.immatricious.macromanager.character;

import java.util.HashMap;
import java.util.Map;

public class CharacterStat {
	private Map<Ability,Integer> abilities = new HashMap<Ability,Integer>();
	private Map<Attribute,Integer> attributes = new HashMap<Attribute,Integer>();
	
	public CharacterStat(int[] abilities, int[] attributes)
	{
		for(int i = 0, l = abilities.length; i < l; i++)
		{
			Ability a = Ability.values()[i];
			this.abilities.put(a, abilities[i]);
		}
		
		for(int i = 0, l = attributes.length; i < l; i++)
		{
			Attribute a = Attribute.values()[i];
			this.attributes.put(a, attributes[i]);
		}
	}

	public int getAbility(Ability a) {
		return abilities.get(a);
	}

	public int getAttribute(Attribute a) {
		return attributes.get(a);
	}

	public void setAbilities(int[] abilities) {
	}

	public void setAtttributes(int[] attributes) {
	}
}
