package com.immatricious.macromanager.event;

public class MMEvent {

	private MMEventType eventType;
	private MMEventData eventData;
	
	public MMEvent(MMEventType eventType, MMEventData eventData)
	{
		this.eventType = eventType;
		this.eventData = eventData;
	}
	
	public MMEventType getEventType() { return this.eventType; }
	public MMEventData getEventData() { return this.eventData; }

}
