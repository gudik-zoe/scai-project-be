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
@Table(name = "post")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_post")
	private int idPost;

	@Column(name = "text")
	private String text;

	@Column(name = "post_original_id")
	private Integer postOriginalId;

	@Column(name = "image")
	private String image;
	
	
	@Column(name = "status")
	private Integer status;

	
	@Column(name = "post_creator_id")
	private int postCreatorId;

	@Column(name = "posted_on")
	private Integer postedOn;

	@Column(name = "extra_text")
	private String extraText;

	@Column(name = "date")
	private Date date;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_post_id", referencedColumnName = "id_post", insertable = false, updatable = false, nullable = false)
	private List<PostLike> postLikes;
	
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_post_id", referencedColumnName = "id_post", insertable = false, updatable = false, nullable = false)
	private List<Comment> comments;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "related_post_id", referencedColumnName = "id_post", insertable = false, updatable = false, nullable = false)
	private List<Notification> notifications;

	public Post() {

	}

	public Post(String text, Integer postOriginalId, String image, int postCreatorId, Integer postedOn,
			String extraText, Date date, Integer status , List<PostLike> postLikes, List<Comment> comments) {
		this.text = text;
		this.postOriginalId = postOriginalId;
		this.image = image;
		this.postCreatorId = postCreatorId;
		this.status = status;
		this.postedOn = postedOn;
		this.extraText = extraText;
		this.date = date;
		this.postLikes = postLikes;
		this.comments = comments;
	}

	public int getIdPost() {
		return idPost;
	}

	public void setIdPost(int idPost) {
		this.idPost = idPost;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getExtraText() {
		return extraText;
	}

	public void setExtraText(String extraText) {
		this.extraText = extraText;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Integer postedOn) {
		this.postedOn = postedOn;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getPostOriginalId() {
		return postOriginalId;
	}

	public void setPostOriginalId(Integer postOriginalId) {
		this.postOriginalId = postOriginalId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPostCreatorId() {
		return postCreatorId;
	}

	public void setPostCreatorId(int postCreatorId) {
		this.postCreatorId = postCreatorId;
	}

	public List<PostLike> getPostLikes() {
		return postLikes;
	}

	public void setPostLikes(List<PostLike> postLikes) {
		this.postLikes = postLikes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
