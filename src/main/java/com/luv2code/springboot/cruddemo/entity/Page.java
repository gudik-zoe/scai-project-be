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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "page")
public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_page")
	private int idPage;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "profile_photo")
	private String profilePhoto;

	@Column(name = "cover_photo")
	private String coverPhoto;

	@Column(name = "page_creator_id")
	private Integer pageCreatorId;
	
	

	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY )
	@JoinColumn(name = "page_creator_id", referencedColumnName = "id_page", insertable = false, updatable = false, nullable = false)
	private List<Post> posts;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_page_id", referencedColumnName = "id_page", insertable = false, updatable = false, nullable = false)
	private List<PageLike> pageLike;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_creator_id", referencedColumnName = "id_page", insertable = false, updatable = false, nullable = false)
	private List<Comment> comments;
	
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_creator_id", referencedColumnName = "id_page", insertable = false, updatable = false, nullable = false)
	private List<CommentLike> commentLikes;

	public Page() {

	}

	public Page(String name, String description, String profilePhoto, String coverPhoto, Integer pageCreatorId) {
		this.name = name;
		this.description = description;
		this.profilePhoto = profilePhoto;
		this.coverPhoto = coverPhoto;
		this.pageCreatorId = pageCreatorId;
	}

	public int getIdPage() {
		return idPage;
	}

	public List<PageLike> getPageLike() {
		return pageLike;
	}

	public void setPageLike(List<PageLike> pageLike) {
		this.pageLike = pageLike;
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

	public Integer getPageCreatorId() {
		return pageCreatorId;
	}

	public void setPageCreatorId(Integer pageCreatorId) {
		this.pageCreatorId = pageCreatorId;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	


}
