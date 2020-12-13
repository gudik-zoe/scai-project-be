package com.luv2code.utility;

public class AccountData {
	
	private String firstName;
	
	private String lastName;
	
	private String profilePhoto;
	
	private int idAccount;
	
	private String coverPhoto;
	
	private String livesIn;
	
	private String email;
	
	private String wentTo;
	
	private String study;
	
	public AccountData() {
		
	}

	public AccountData(String firstName, String lastName, String profilePhoto, int idAccount, String coverPhoto,
			String livesIn, String email, String wentTo, String study) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePhoto = profilePhoto;
		this.idAccount = idAccount;
		this.coverPhoto = coverPhoto;
		this.livesIn = livesIn;
		this.email = email;
		this.wentTo = wentTo;
		this.study = study;
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

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public int getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public String getLivesIn() {
		return livesIn;
	}

	public void setLivesIn(String livesIn) {
		this.livesIn = livesIn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWentTo() {
		return wentTo;
	}

	public void setWentTo(String wentTo) {
		this.wentTo = wentTo;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}
	
	
	
	
}
