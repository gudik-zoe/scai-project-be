package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.ReactToEvent;
import com.luv2code.springboot.cruddemo.dto.UpdateEvent;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.exceptions.BadRequestException;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.EventJpaRepo;
import com.luv2code.springboot.cruddemo.jpa.EventReactJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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
			throw new NotFoundException("no such id for an account");
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
	public Event updateEvent(int accountId, UpdateEvent newEvent) throws Exception {
		Event theEvent = getEventById(newEvent.getIdEvent());
		if (theEvent.getEventCreatorId() == accountId) {
			if (newEvent.getEventPhoto() != null) {
				String newImage = storageService.pushImage(newEvent.getEventPhoto()).getImageUrl();
				theEvent.setCoverPhoto(newImage);
			}
			theEvent.setName(newEvent.getName());
			theEvent.setDescription(newEvent.getDescription());
			theEvent.setLocation(newEvent.getLocation());
			theEvent.setTime(newEvent.getTime());
			eventJpaRepo.save(theEvent);
			return theEvent;
		} else {
			throw new BadRequestException("cannot update an event that is not yours");
		}

	}

	@Override
	public String checkDate(int eventId) {
//		System.out.println("here");
//		Event event = getEventById(eventId);
////		LocalDate eventData =	Instant.ofEpochMilli(Integer.parseInt(event.getTime())).atZone(ZoneId.systemDefault()).toLocalDate();
//		System.out.println(Period.between(LocalDateTime.now() , );

		return "hello world";
	}

	public boolean checkIfExpired(Event event) throws ParseException {
	LocalDate eventData =	Instant.ofEpochMilli(Integer.parseInt(event.getTime())).atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println(Period.between(eventData , LocalDate.now()));
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
