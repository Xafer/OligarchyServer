package com.immatricious.macromanager.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.immatricious.macromanager.task.TaskHandler;
import com.immatricious.macromanager.util.CommandType;

public class MMEventDispatcher {
	private Map<MMEventType,List<MMEventListener>> listeners = new HashMap<MMEventType,List<MMEventListener>>();
	
	public MMEventDispatcher()
	{
		for(MMEventType type : MMEventType.values())
			listeners.put(type, new ArrayList<MMEventListener>());
	}
	
	public void dispatchEvent(MMEvent event)
	{
		List<MMEventListener> typeListeners = listeners.get(event.getEventType());
		
		TaskHandler.log.dispatchCommand(event.getEventType().toString(), CommandType.EVENT);
		
		for(MMEventListener l : typeListeners)
			l.act(event);
	}
	
	public void addEventListener(MMEventType type, MMEventListener listener){ listeners.get(type).add(listener); }
	public void removeEventListener(MMEventListener listener)
	{
		List<MMEventListener> l = listeners.get(listener.getEventType());
		
		if(l.contains(listener))
			l.remove(listener);
	}
}
