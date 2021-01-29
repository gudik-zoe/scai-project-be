package com.luv2code.utility;

import org.springframework.web.multipart.MultipartFile;

public class Prova {
	public  MultipartFile eventPhoto;
	
	public String eventName;
	
	public String desctiption;
	
	public String location;
	
	public String date;
	
	public Prova() {
		
	}

	public Prova(MultipartFile eventPhoto, String eventName, String desctiption, String location, String date) {
		this.eventPhoto = eventPhoto;
		this.eventName = eventName;
		this.desctiption = desctiption;
		this.location = location;
		this.date = date;
	}

	public MultipartFile getEventPhoto() {
		return eventPhoto;
	}

	public void setEventPhoto(MultipartFile eventPhoto) {
		this.eventPhoto = eventPhoto;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDesctiption() {
		return desctiption;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
