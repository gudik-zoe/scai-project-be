package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.utility.ReactToEvent;

@Repository
public class EventDAOHibernateImpl implements EventDAO {

	private EntityManager entityManager;

	public EventDAOHibernateImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public List<Event> getEvents() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Event> theQuery = currentSession.createQuery("from Event", Event.class);
		List<Event> events = theQuery.getResultList();
		return events;
	}

	@Override
	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent) {
		Session currentSession = entityManager.unwrap(Session.class);
		Event event = currentSession.get(Event.class, reactToEvent.getIdEvent());
		if (event != null && event.getEventCreatorId() != accountId) {
			Query<EventReact> theQuery = currentSession.createQuery("from EventReact where related_event_id = "
					+ reactToEvent.getIdEvent() + " and event_react_creator_id = " + accountId, EventReact.class);
			EventReact theEventReact;
			try {
				theEventReact = theQuery.getSingleResult();
				if (theEventReact.getStatus() == reactToEvent.getStatus()) {
					currentSession.delete(theEventReact);
					return null;
				} else {
					theEventReact.setStatus(reactToEvent.getStatus());
					currentSession.update(theEventReact);
					return theEventReact;
				}
			} catch (Exception e) {
				EventReact eventReact = new EventReact(reactToEvent.getStatus(), event.getIdEvent(), accountId);
				currentSession.save(eventReact);
				return eventReact;
			}
		} else {
			throw new CustomeException("there is no such event or this is event is yours");
		}
	}

	@Override
	public List<Event> getEvents(int accounId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Event> theQuery = currentSession.createQuery("from Event where event_creator_id = " + accounId,
				Event.class);
		try {
			List<Event> myEvents = theQuery.getResultList();
			return myEvents;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public List<EventReact> getLinkedEvents(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<EventReact> theQuery = currentSession
				.createQuery("from EventReact where event_react_creator_id = " + accountId, EventReact.class);

		try {
			List<EventReact> linkedEvents = theQuery.getResultList();
			return linkedEvents;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Event getEventById(int eventId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Event theEvent = currentSession.get(Event.class, eventId);
		if (theEvent != null) {
			return theEvent;
		} else {
			throw new CustomeException("there is no such event");
		}

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
