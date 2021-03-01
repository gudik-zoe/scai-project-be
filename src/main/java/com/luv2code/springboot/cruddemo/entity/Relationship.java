package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "relationship")
public class Relationship {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_relationship")
	private int idRelationship;

	@Column(name = "user_one_id")
	private int userOneId;

	@Column(name = "user_two_id")
	private int userTwoId;

	@Column(name = "status")
	private int status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_one_id", insertable = false, updatable = false, nullable = false)
	private Account account;
	public Relationship() {

	}

	public Relationship(int userOneId, int userTwoId, int status) {
		this.userOneId = userOneId;
		this.userTwoId = userTwoId;
		this.status = status;
	}

	public int getIdRelationship() {
		return idRelationship;
	}

	public void setIdRelationship(int idRelationship) {
		this.idRelationship = idRelationship;
	}

	public int getUserOneId() {
		return userOneId;
	}

	public void setUserOneId(int userOneId) {
		this.userOneId = userOneId;
	}

	public int getUserTwoId() {
		return userTwoId;
	}

	public void setUserTwoId(int userTwoId) {
		this.userTwoId = userTwoId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

	


}
