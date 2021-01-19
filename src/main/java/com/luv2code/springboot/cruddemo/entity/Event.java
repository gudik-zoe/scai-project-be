package com.luv2code.springboot.cruddemo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	@Column(name = "when")
	private Date when;

	@Column(name = "where")
	private String where;

	@Column(name = "cover_photo")
	private String coverPhoto;

	@Column(name = "event_creator_id")
	private int eventCreatorId;

	@Column(name = "description")
	private String description;
	
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_event_id", referencedColumnName = "id_event", insertable = false, updatable = false, nullable = false)
	private List<EventReact> eventFollower;

	public Event(String name, Date when, String where, String coverPhoto, int eventCreatorId, String description) {
		this.name = name;
		this.when = when;
		this.where = where;
		this.coverPhoto = coverPhoto;
		this.eventCreatorId = eventCreatorId;
		this.description = description;
	}

	public Event() {

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

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
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
