//package com.luv2code.springboot.cruddemo.dao;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import com.luv2code.exception.error.handling.CustomeException;
//import com.luv2code.springboot.cruddemo.entity.Event;
//import com.luv2code.springboot.cruddemo.entity.EventReact;
//import com.luv2code.springboot.cruddemo.service.StorageService;
//import com.luv2code.utility.ImageUrl;
//import com.luv2code.utility.ReactToEvent;
//import com.luv2code.utility.UpdateEvent;
//
//@Repository
//public class EventDAOHibernateImpl implements EventDAO {
//
//	private EntityManager entityManager;
//
//	private StorageService storageService;
//
//	@Autowired
//	public EventDAOHibernateImpl(EntityManager theEntityManager, StorageService theStorageService) {
//		entityManager = theEntityManager;
//		storageService = theStorageService;
//	}
//
//	@Override
//	public List<Event> getEvents() throws ParseException {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Event> theQuery = currentSession.createQuery("from Event", Event.class);
//		List<Event> events = new ArrayList<Event>();
//		events = theQuery.getResultList();
//		for (int i = 0; i < events.size(); i++) {
//			if (checkIfExpired(events.get(i))) {
//				events.remove(i);
//			}
//		}
//		return events;
//	}
//
//	@Override
//	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Event event = currentSession.get(Event.class, reactToEvent.getIdEvent());
//		if (event != null && event.getEventCreatorId() != accountId) {
//			Query<EventReact> theQuery = currentSession.createQuery("from EventReact where related_event_id = "
//					+ reactToEvent.getIdEvent() + " and event_react_creator_id = " + accountId, EventReact.class);
//			EventReact theEventReact;
//			try {
//				theEventReact = theQuery.getSingleResult();
//				if (theEventReact.getStatus() == reactToEvent.getStatus()) {
//					currentSession.delete(theEventReact);
//					return null;
//				} else {
//					theEventReact.setStatus(reactToEvent.getStatus());
//					currentSession.update(theEventReact);
//					return theEventReact;
//				}
//			} catch (Exception e) {
//				EventReact eventReact = new EventReact(reactToEvent.getStatus(), event.getIdEvent(), accountId);
//				currentSession.save(eventReact);
//				return eventReact;
//			}
//		} else {
//			throw new CustomeException("there is no such event or this is event is yours");
//		}
//	}
//
//	@Override
//	public List<Event> getEvents(int accounId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<Event> theQuery = currentSession.createQuery("from Event where event_creator_id = " + accounId,
//				Event.class);
//		try {
//			List<Event> myEvents = theQuery.getResultList();
//			return myEvents;
//		} catch (Exception e) {
//			return null;
//		}
//
//	}
//
//	@Override
//	public List<EventReact> getLinkedEvents(int accountId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<EventReact> theQuery = currentSession
//				.createQuery("from EventReact where event_react_creator_id = " + accountId, EventReact.class);
//		try {
//			List<EventReact> linkedEvents = theQuery.getResultList();
//			for (int i = 0; i < linkedEvents.size(); i++) {
//				if (checkIfExpired(currentSession.get(Event.class, linkedEvents.get(i).getRelatedEventId()))) {
//					linkedEvents.remove(i);
//				}
//			}
//			return linkedEvents;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	@Override
//	public Event getEventById(int eventId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Event theEvent = currentSession.get(Event.class, eventId);
//		if (theEvent != null) {
//			return theEvent;
//		} else {
//			throw new CustomeException("there is no such event");
//		}
//
//	}
//
//	@Override
//	public Event createEvent(Event event) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		currentSession.save(event);
//		return event;
//	}
//
//	@Override
//	public Event updateEvent(UpdateEvent newEvent, Event originalEvent) throws Exception {
//		Session currentSession = entityManager.unwrap(Session.class);
//		if (newEvent.getEventPhoto() != null) {
//			ImageUrl image = storageService.pushImage(newEvent.getEventPhoto());
//			originalEvent.setCoverPhoto(image.getImageUrl());
//		}
//		originalEvent.setDescription(newEvent.getDescription());
//		originalEvent.setName(newEvent.getName());
//		originalEvent.setLocation(newEvent.getLocation());
//		originalEvent.setTime(newEvent.getTime());
//		currentSession.update(originalEvent);
//		return originalEvent;
//
//	}
//
//	public boolean checkIfExpired(Event event) throws ParseException {
//		Session currentSession = entityManager.unwrap(Session.class);
//		String jsDate = event.getTime() + ":00";
//		Date javaDate = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(jsDate);
//		if (javaDate.getTime() - new Date(System.currentTimeMillis()).getTime() < 0) {
//			currentSession.delete(event);
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//}
