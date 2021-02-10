package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.springboot.cruddemo.service.EventService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ReactToEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class EventRestController {

	@Autowired
	private EventService eventService;

	public EventRestController() {

	}

	@GetMapping("/events")
	public List<Event> getEvents() throws ParseException {
		return eventService.getEvents();
	}

	@PostMapping("/reactToEvent")
	public EventReact reactToEvent(@RequestBody ReactToEvent reactToEvent,
			@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);

		return eventService.reactToEvent(idExtractor.getIdFromToken(), reactToEvent);

	}

	@GetMapping("/myEvents")
	public List<Event> getMyEvents(@RequestHeader("Authorization") String authHeader) throws ParseException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return eventService.getMyEvents(idExtractor.getIdFromToken());
	}

	@GetMapping("/linkedEvents")
	public List<EventReact> getLinkedEvents(@RequestHeader("Authorization") String authHeader) throws ParseException {
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
		return eventService.createEvent(idExtractor.getIdFromToken(), name, eventPhoto, when, where, description);
	}

	@PutMapping("/editEvent")
	public Event updateEvent(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "eventPhoto", required = false) MultipartFile eventPhoto,
			@RequestPart(value = "name", required = false) String name,
			@RequestPart(value = "eventId", required = true) String eventId,
			@RequestPart(value = "when", required = false) String when,
			@RequestPart(value = "where", required = false) String where,
			@RequestPart(value = "description", required = false) String description) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return eventService.updateEvent(idExtractor.getIdFromToken(), eventId, name, when, where, description,
				eventPhoto);

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
