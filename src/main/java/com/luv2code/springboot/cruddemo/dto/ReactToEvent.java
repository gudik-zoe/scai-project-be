package com.luv2code.springboot.cruddemo.dto;

public class ReactToEvent {

	private int idEvent;

	private int status;

	public ReactToEvent() {

	}

	public ReactToEvent(int idEvent, int status) {
		this.idEvent = idEvent;
		this.status = status;
	}

	public int getIdEvent() {
		return idEvent;
	}

	public void setIdEvent(int idEvent) {
		this.idEvent = idEvent;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
