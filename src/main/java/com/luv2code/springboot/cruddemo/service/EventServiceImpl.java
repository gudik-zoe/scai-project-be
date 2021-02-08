package com.luv2code.springboot.cruddemo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;

import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.jpa.repositories.EventJpaRepo;
import com.luv2code.springboot.cruddemo.jpa.repositories.EventReactJpaRepo;
import com.luv2code.utility.ReactToEvent;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventJpaRepo eventJpaRepo;

	@Autowired
	private EventReactJpaRepo eventReactJpaRepo;

	@Autowired
	private StorageService storageService;

	public EventServiceImpl() {

	}

	@Override
	public List<Event> getEvents() throws ParseException {
		List<Event> events = eventJpaRepo.findAll();
		for (int i = 0; i < events.size(); i++) {
			if (checkIfExpired(events.get(i))) {
				events.remove(i);
			}
		}
		return events;

	}

	@Override
	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent) {
		EventReact eventReact = eventReactJpaRepo.getEventReact(accountId, reactToEvent.getIdEvent());
		if (eventReact == null) {
			eventReact = new EventReact(reactToEvent.getStatus(), reactToEvent.getIdEvent(), accountId);
			eventReactJpaRepo.save(eventReact);

		} else if (eventReact != null && eventReact.getStatus() == reactToEvent.getStatus()) {
			eventReactJpaRepo.delete(eventReact);
			return null;

		} else if (eventReact != null && eventReact.getStatus() != reactToEvent.getStatus()) {
			eventReact.setStatus(reactToEvent.getStatus());
			eventReactJpaRepo.save(eventReact);
		}
		return eventReact;
	}

	@Override
	public List<Event> getMyEvents(int accountId) throws ParseException {
		List<Event> myEvents = eventJpaRepo.getMyEvents(accountId);
		for (int i = 0; i < myEvents.size(); i++) {
			if (checkIfExpired(myEvents.get(i))) {
				myEvents.remove(i);
			}
		}
		return myEvents;

	}

	@Override
	public List<EventReact> getLinkedEvents(int accountId) throws ParseException {
		List<EventReact> myLinkedEvents = eventReactJpaRepo.getMyLinkedEvents(accountId);
		for (int i = 0; i < myLinkedEvents.size(); i++) {
			if (checkIfExpired(getEventById(myLinkedEvents.get(i).getRelatedEventId()))) {
				myLinkedEvents.remove(i);
			}
		}
		return myLinkedEvents;
	}

	@Override
	public Event getEventById(int eventId) {
		Optional<Event> result = eventJpaRepo.findById(eventId);
		Event theEvent = null;
		if (result.isPresent()) {
			theEvent = result.get();
		} else {
			throw new CustomeException("no such id for an account");
		}
		return theEvent;
	}

	@Override
	public Event createEvent(int accountId, String name, MultipartFile eventPhoto, String when, String where,
			String description) throws Exception {
		String eventImage = storageService.pushImage(eventPhoto).getImageUrl();
		Event theEvent = new Event(name, when, where, eventImage, accountId, description);
		eventJpaRepo.save(theEvent);
		return theEvent;
	}

	@Override
	public Event updateEvent(int accountId, String eventId, String name, String when, String where, String description,
			MultipartFile eventPhoto) throws Exception {
		int idEvent = Integer.parseInt(eventId);
		Event theEvent = getEventById(idEvent);
		if (theEvent.getEventCreatorId() == accountId) {
			if (eventPhoto != null) {
				String newImage = storageService.pushImage(eventPhoto).getImageUrl();
				theEvent.setCoverPhoto(newImage);
			}
			theEvent.setName(name);
			theEvent.setDescription(description);
			theEvent.setLocation(where);
			theEvent.setTime(when);
			eventJpaRepo.save(theEvent);
			return theEvent;
		} else {
			throw new CustomeException("cannot update an event that is not yours");
		}

	}

	public boolean checkIfExpired(Event event) throws ParseException {
		String jsDate = event.getTime() + ":00";
		Date javaDate = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(jsDate);
		if (javaDate.getTime() - new Date(System.currentTimeMillis()).getTime() < 0) {
			eventJpaRepo.delete(event);
			return true;
		} else {
			return false;
		}
	}

}
