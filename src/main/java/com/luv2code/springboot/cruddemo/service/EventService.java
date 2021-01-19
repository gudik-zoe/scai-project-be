package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.utility.ReactToEvent;

public interface EventService {

	public List<Event> getEvents();

	public List<Event> getMyEvents(int accounId);

	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent);

	public  List<EventReact> getLinkedEvents(int accountId);
	
	public Event getEventById(int eventId);

}
