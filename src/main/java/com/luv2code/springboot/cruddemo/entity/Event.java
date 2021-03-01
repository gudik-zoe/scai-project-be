package com.luv2code.springboot.cruddemo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_event")
	private int idEvent;

	@Column(name = "name")
	private String name;

	@Column(name = "time")
	private String time;

	@Column(name = "location")
	private String location;

	@Column(name = "cover_photo")
	private String coverPhoto;

	@Column(name = "event_creator_id")
	private int eventCreatorId;

	@Column(name = "description")
	private String description;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_event_id", referencedColumnName = "id_event", insertable = false, updatable = false, nullable = false)
	private List<EventReact> eventFollower;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "event_creator_id", insertable = false, updatable = false, nullable = false)
	private Account account;
	
	public Event() {

	}

	public Event(String name, String time, String location, String coverPhoto, int eventCreatorId, String description) {
		this.name = name;
		this.time = time;
		this.location = location;
		this.coverPhoto = coverPhoto;
		this.eventCreatorId = eventCreatorId;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String place) {
		this.location = place;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public int getEventCreatorId() {
		return eventCreatorId;
	}

	public void setEventCreatorId(int eventCreatorId) {
		this.eventCreatorId = eventCreatorId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<EventReact> getEventFollower() {
		return eventFollower;
	}

	public void setEventFollower(List<EventReact> eventFollower) {
		this.eventFollower = eventFollower;
	}
	
	

}
