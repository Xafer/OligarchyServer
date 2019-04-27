package com.immatricious.macromanager.character;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.util.DataParser;

public class CharacterReader {
	
	private String filename;
	
	public CharacterReader(String filename)
	{
		this.filename = filename;
	}
	
	public Map<Account,List<Character>> read()
	{
		Map<Account,List<Character>> charMap = new HashMap<Account,List<Character>>();
		
		String line;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			boolean header = true;
			Account account = null;
			List<Character> characters = new ArrayList<Character>();
			
			while((line = br.readLine()) != null)
			{
				if("e".equals(line))
				{
					charMap.put(account, characters);
					characters = new ArrayList<Character>();
					header = true;
				}
				else if(header)
				{
					String[] accData = line.split(":");
					account = new Account(accData[0],accData[1]);
					header = false;
				}
				else
				{
					String[] charData = line.split(":");
					String name = charData[0];
					int[] abilities = DataParser.parseIntegers(charData[1]);
					int[] attributes = DataParser.parseIntegers(charData[2]);
					
					Character character = new Character(account,name,abilities,attributes);
					account.add(name);
					
					characters.add(character);
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return charMap;
	}
}
