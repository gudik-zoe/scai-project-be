package com.luv2code.springboot.cruddemo.rest;

import java.util.Currency;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.service.EventService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;
import com.luv2code.utility.ReactToEvent;
import com.luv2code.utility.UpdateEvent;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventRestController {

	private EventService eventService;

	private StorageService storageService;

	private EntityManager entityManager;

	@Autowired
	public EventRestController(EntityManager theEntityManager, EventService theEventService,
			StorageService theStorageService) {
		eventService = theEventService;
		storageService = theStorageService;
		entityManager = theEntityManager;
	}

	@GetMapping("/events")
	public List<Event> getEvents() {
		return eventService.getEvents();
	}

	@PostMapping("/reactToEvent")
	public EventReact reactToEvent(@RequestBody ReactToEvent reactToEvent,
			@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);

		return eventService.reactToEvent(idExtractor.getIdFromToken(), reactToEvent);

	}

	@GetMapping("/myEvents")
	public List<Event> getMyEvents(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return eventService.getMyEvents(idExtractor.getIdFromToken());
	}

	@GetMapping("/linkedEvents")
	public List<EventReact> getLinkedEvents(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return eventService.getLinkedEvents(idExtractor.getIdFromToken());
	}

	@GetMapping("/getEventById/{eventId}")
	public Event getEventById(@PathVariable int eventId) {

		return eventService.getEventById(eventId);
	}

	@PostMapping("/createEvent")
	public Event createEvent(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "eventPhoto", required = true) MultipartFile eventPhoto,
			@RequestPart(value = "name", required = true) String name,
			@RequestPart(value = "when", required = true) String when,
			@RequestPart(value = "where", required = true) String where,
			@RequestPart(value = "description", required = true) String description) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		ImageUrl image = storageService.pushImage(eventPhoto);
		String imageString = image.getImageUrl();
		Event event = new Event(name, when, where, imageString, idExtractor.getIdFromToken(), description);
		return eventService.createEvent(event);
	}

	@PutMapping("/editEvent")
	public Event updateEvent(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "eventPhoto", required = false) MultipartFile eventPhoto,
			@RequestPart(value = "name", required = false) String name,
			@RequestPart(value = "eventId", required = true) String eventId,
			@RequestPart(value = "when", required = false) String when,
			@RequestPart(value = "where", required = false) String where,
			@RequestPart(value = "description", required = false) String description) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		int idEvent = Integer.parseInt(eventId);
		Event theOriginalEvent = currentSession.get(Event.class, idEvent);
		if ( theOriginalEvent.getEventCreatorId() == idExtractor.getIdFromToken()) {
			UpdateEvent newEvent = new UpdateEvent(idEvent, name, eventPhoto, when, where, description);
			return eventService.updateEvent(newEvent ,theOriginalEvent );
		} else {
			throw new CustomeException("cannot update an event that is not yours or this event doesn't exist");
		}
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
