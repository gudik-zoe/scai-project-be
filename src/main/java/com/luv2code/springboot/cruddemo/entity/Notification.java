package com.luv2code.springboot.cruddemo.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_notification")
	private int idNotification;

	@Column(name = "not_creator")
	private int notCreator;

	@Column(name = "not_receiver")
	private int notReceiver;
	

	@Column(name = "action")
	private String action;

	@Column(name = "date")
	private Date date;

	@Column(name = "related_post_id")
	private int relatedPostId;

	@Column(name = "seen")
	private boolean seen;

	public Notification() {

	}

	public Notification(int notCreator, int notReceiver, String action, Date date, int relatedPostId, boolean seen) {
		this.notCreator = notCreator;
		this.notReceiver = notReceiver;
		this.action = action;
		this.date = date;
		this.relatedPostId = relatedPostId;
		this.seen = seen;
	}

	public int getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(int idNotification) {
		this.idNotification = idNotification;
	}

	public int getNotCreator() {
		return notCreator;
	}

	public void setNotCreator(int notCreator) {
		this.notCreator = notCreator;
	}

	public int getNotReceiver() {
		return notReceiver;
	}

	public void setNotReceiver(int notReceiver) {
		this.notReceiver = notReceiver;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRelatedPostId() {
		return relatedPostId;
	}

	public void setRelatedPostId(int relatedPostId) {
		this.relatedPostId = relatedPostId;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	
	
	

	

}
