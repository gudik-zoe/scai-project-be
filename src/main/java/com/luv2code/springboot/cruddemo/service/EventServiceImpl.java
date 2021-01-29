package com.luv2code.springboot.cruddemo.service;

import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.dao.EventDAO;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.utility.ReactToEvent;
import com.luv2code.utility.UpdateEvent;

@Service
@Transactional
public class EventServiceImpl implements EventService{

	
	private EventDAO eventDAO;
	
	@Autowired
	 public EventServiceImpl(EventDAO theEventDAO) {
		 eventDAO = theEventDAO;
	}
	
	
	@Override
	public List<Event> getEvents() throws ParseException {
		return eventDAO.getEvents();
			
	}


	@Override
	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent) {
		return eventDAO.reactToEvent(accountId , reactToEvent);
	}


	@Override
	public List<Event> getMyEvents(int accountId) {
		return eventDAO.getEvents(accountId);
	}


	@Override
	public List<EventReact> getLinkedEvents(int accountId) {
		return eventDAO.getLinkedEvents(accountId);
	}


	@Override
	public Event getEventById(int eventId) {
		return eventDAO.getEventById(eventId);
	}


	@Override
	public Event createEvent(Event event) {
		return eventDAO.createEvent(event);
	}


	@Override
	public Event updateEvent(UpdateEvent newEvent , Event originalEvent) throws Exception {
		return eventDAO.updateEvent(newEvent , originalEvent);
		
	}
	
	

}
