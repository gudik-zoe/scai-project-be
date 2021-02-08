package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "event_react")
public class EventReact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_event_react")
	private int idEventReact;

	@Column(name = "status")
	private Integer status;

	@Column(name = "related_event_id")
	private int relatedEventId;

	@Column(name = "event_react_creator_id")
	private int eventReactCreatorId;

	public EventReact() {

	}

	public EventReact(Integer status, int relatedEventId, int eventReactCreatorId) {
		this.status = status;
		this.relatedEventId = relatedEventId;
		this.eventReactCreatorId = eventReactCreatorId;
	}

	public int getIdEventReact() {
		return idEventReact;
	}

	public void setIdEventReact(int idEventReact) {
		this.idEventReact = idEventReact;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int getRelatedEventId() {
		return relatedEventId;
	}

	public void setRelatedEventId(int relatedEventId) {
		this.relatedEventId = relatedEventId;
	}

	public int getEventReactCreatorId() {
		return eventReactCreatorId;
	}

	public void setEventReactCreatorId(int eventReactCreatorId) {
		this.eventReactCreatorId = eventReactCreatorId;
	}
	
	

}
