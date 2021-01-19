package com.luv2code.springboot.cruddemo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.service.EventService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ReactToEvent;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventRestController {

	private EventService eventService;

	public EventRestController(EventService theEventService) {
		eventService = theEventService;
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
