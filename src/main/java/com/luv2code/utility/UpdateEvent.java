package com.luv2code.utility;

import org.springframework.web.multipart.MultipartFile;

public class UpdateEvent {
	
	private int idEvent;

	private String name;

	private MultipartFile eventPhoto;

	private String time;

	private String location;

	private String description;

	public UpdateEvent() {

	}

	public UpdateEvent(int idEvent, String name, MultipartFile eventPhoto, String time, String location,
			String description) {
		this.idEvent = idEvent;
		this.name = name;
		this.eventPhoto = eventPhoto;
		this.time = time;
		this.location = location;
		this.description = description;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getEventPhoto() {
		return eventPhoto;
	}

	public void setEventPhoto(MultipartFile eventPhoto) {
		this.eventPhoto = eventPhoto;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
