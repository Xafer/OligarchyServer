package com.immatricious.macromanager.event;

public interface MMEventListener {
	public void act(MMEvent event);
	public MMEventType getEventType();
}
