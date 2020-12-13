package com.luv2code.utility;

public class AccountBasicData {
	
	private String firstName;
	
	private String lastName;
	
	private String profilePhoto;
	
	private int idAccount;
	
	private String coverPhoto;
	
	public AccountBasicData() {
		
	}

	

	public AccountBasicData(String firstName, String lastName, String profilePhoto, int idAccount, String coverPhoto) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePhoto = profilePhoto;
		this.idAccount = idAccount;
		this.coverPhoto = coverPhoto;
	}



	public String getCoverPhoto() {
		return coverPhoto;
	}



	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
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

	
	
	
	
	
}
