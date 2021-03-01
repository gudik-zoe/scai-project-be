package com.luv2code.springboot.cruddemo.utility;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.PageLike;

public class PageBasicData {

	private int idPage;
	

	private String name;

	private String profilePhoto;

	private String coverPhoto;

	private int pageCreatorId;

	private List<PageLike> likers;

	public PageBasicData() {

	}

	public PageBasicData(int idPage, String name, String profilePhoto, String coverPhoto, int pageCreatorId,
			List<PageLike> likers) {
		this.idPage = idPage;
		this.name = name;
		this.profilePhoto = profilePhoto;
		this.coverPhoto = coverPhoto;
		this.pageCreatorId = pageCreatorId;
		this.likers = likers;
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

	public int getPageCreatorId() {
		return pageCreatorId;
	}

	public void setPageCreatorId(int pageCreatorId) {
		this.pageCreatorId = pageCreatorId;
	}

	public List<PageLike> getLikers() {
		return likers;
	}

	public void setLikers(List<PageLike> likers) {
		this.likers = likers;
	}

}
