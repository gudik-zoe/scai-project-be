package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_message")
	private int idMessages;

	@Column(name = "id_sender")
	private int idSender;

	@Column(name = "id_receiver")
	private int idReceiver;

	@Column(name = "message")
	private String message;

	@Column(name = "date")
	private Date date;

	@Column(name = "seen")
	private boolean seen;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_sender", insertable = false, updatable = false, nullable = false)
//	private Account account;

	public Message() {

	}

	public Message(int idSender, int idReceiver, String message, Date date, boolean seen) {
		this.idSender = idSender;
		this.idReceiver = idReceiver;
		this.message = message;
		this.date = date;
		this.seen = seen;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getIdMessages() {
		return idMessages;
	}

	public void setIdMessages(int idMessages) {
		this.idMessages = idMessages;
	}

	public int getIdSender() {
		return idSender;
	}

	public void setIdSender(int idSender) {
		this.idSender = idSender;
	}

	public int getIdReceiver() {
		return idReceiver;
	}

	public void setIdReceiver(int idReceiver) {
		this.idReceiver = idReceiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

}
