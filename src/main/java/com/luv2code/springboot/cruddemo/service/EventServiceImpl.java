package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.dao.EventDAO;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.utility.ReactToEvent;

@Service
@Transactional
public class EventServiceImpl implements EventService{

	
	private EventDAO eventDAO;
	
	@Autowired
	 public EventServiceImpl(EventDAO theEventDAO) {
		 eventDAO = theEventDAO;
	}
	
	
	@Override

	public List<Event> getEvents() {
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
	
	

}
