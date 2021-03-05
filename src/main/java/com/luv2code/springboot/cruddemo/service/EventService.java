package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.dto.ReactToEvent;
import com.luv2code.springboot.cruddemo.dto.UpdateEvent;
import com.luv2code.springboot.cruddemo.entity.Event;
import com.luv2code.springboot.cruddemo.entity.EventReact;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface EventService {

	public List<Event> getEvents() throws ParseException;

	public List<Event> getMyEvents(int accounId) throws ParseException;

	public EventReact reactToEvent(int accountId, ReactToEvent reactToEvent);

	public List<EventReact> getLinkedEvents(int accountId) throws ParseException;

	public Event getEventById(int eventId);

	public Event createEvent(int accountId, String name, MultipartFile eventPhoto, String when, String where,
			String description) throws Exception;

	public Event updateEvent(int accountId, UpdateEvent newEvent) throws Exception;

	public String checkDate(int eventId);

}
