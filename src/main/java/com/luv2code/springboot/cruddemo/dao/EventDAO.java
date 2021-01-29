package com.luv2code.springboot.cruddemo.dao;

import java.text.ParseException;
import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.utility.ReactToEvent;
import com.luv2code.utility.UpdateEvent;

public interface EventDAO {

	public List<Event> getEvents() throws ParseException;

	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent);

	public List<Event> getEvents(int accounId);

	public List<EventReact> getLinkedEvents(int accountId);

	public Event getEventById(int eventId);

	public Event createEvent(Event event);

	public Event updateEvent(UpdateEvent newEvent , Event originalEvent) throws Exception;

}
