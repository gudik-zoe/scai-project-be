package com.luv2code.springboot.cruddemo.utility;

import org.springframework.web.multipart.MultipartFile;

public class UpdatePage {

	private int idPage;

	private String name;

	private String description;

	private MultipartFile profilePhoto;

	private MultipartFile coverPhoto;

	public UpdatePage() {

	}



	public UpdatePage(int idPage, String name, String description, MultipartFile profilePhoto,
			MultipartFile coverPhoto) {
		this.idPage = idPage;
		this.name = name;
		this.description = description;
		this.profilePhoto = profilePhoto;
		this.coverPhoto = coverPhoto;
	}



	public int getIdPage() {
		return idPage;
	}

	public void setIdPage(int idPage) {
		this.idPage = idPage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(MultipartFile profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public MultipartFile getCoverPhoto() {
		return coverPhoto;
	}

	public void setCoverPhoto(MultipartFile coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

}
