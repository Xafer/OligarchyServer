package com.immatricious.macromanager.task;

public enum ActionType {
	HEARTH('h'),
	MOVE('m'),
	RELATIVEMOVE('n'),
	RIGHTCLICK('r'),
	FLOWERCHOOSE('c'),
	FOLLOW('l');
	
	private char actionChar;
	
	private ActionType(char c)
	{
		actionChar = c;
	}
	
	public char getActionChar() { return this.actionChar; }
}
