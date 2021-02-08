package com.luv2code.springboot.cruddemo.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import com.luv2code.utility.ReactToEvent;

public interface EventService {

	public List<Event> getEvents() throws ParseException;

	public List<Event> getMyEvents(int accounId) throws ParseException;

	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent);

	public List<EventReact> getLinkedEvents(int accountId) throws ParseException;

	public Event getEventById(int eventId);

	public Event createEvent(int accountId, String name, MultipartFile eventPhoto, String when, String where,
			String description) throws Exception;

	public Event updateEvent(int accountId, String eventId, String name, String when, String where, String description,
			MultipartFile eventPhoto) throws Exception;

}
