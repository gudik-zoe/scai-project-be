package com.luv2code.springboot.cruddemo.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_account")
	private int idAccount;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "gender")
	private String gender;

	@Column(name = "lives_in")
	private String livesIn;

	@Column(name = "study")
	private String study;

	@Column(name = "went_to")
	private String wentTo;

	@Column(name = "profile_photo")
	private String profilePhoto;

	@Column(name = "cover_photo")
	private String coverPhoto;

	@Column(name = "temporary_password")
	private String temporaryPassword;

	@Column(name = "temporary_password_creation_ts")
	private Date temporaryPasswordExpiryDate;


	public Account() {

	}

	public Account(String firstName, String lastName, String email, String password, String gender, String livesIn,
			String study, String wentTo, String profilePhoto, String coverPhoto) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.livesIn = livesIn;
		this.study = study;
		this.wentTo = wentTo;
		this.profilePhoto = profilePhoto;
		this.coverPhoto = coverPhoto;
	}

		public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public int getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLivesIn() {
		return livesIn;
	}

	public void setLivesIn(String livesIn) {
		this.livesIn = livesIn;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public String getWentTo() {
		return wentTo;
	}

	public void setWentTo(String wentTo) {
		this.wentTo = wentTo;
	}


	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}

	public Date getTemporaryPasswordExpiryDate() {
		return temporaryPasswordExpiryDate;
	}

	public void setTemporaryPasswordExpiryDate(Date temporaryPasswordExpiryDate) {
		this.temporaryPasswordExpiryDate = temporaryPasswordExpiryDate;
	}
}
