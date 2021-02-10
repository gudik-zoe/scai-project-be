package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_account")
	private int idAccount;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "post_creator_id" , referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Post> posts;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Comment> comments;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "post_like_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<PostLike> postLikes;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_like_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<CommentLike> commentLikes;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sender", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Message> messages;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_one_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Relationship> relationship;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "not_creator", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Notification> notification;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Page> page;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_like_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<PageLike> pageLike;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<Event> events;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_react_creator_id", referencedColumnName = "id_account", insertable = false, updatable = false, nullable = false)
	private List<EventReact> eventReact;


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

	public List<Page> getPage() {
		return page;
	}

	public void setPage(List<Page> page) {
		this.page = page;
	}

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

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<PostLike> getPostLikes() {
		return postLikes;
	}

	public void setPostLikes(List<PostLike> postLikes) {
		this.postLikes = postLikes;
	}

	public List<CommentLike> getCommentLikes() {
		return commentLikes;
	}

	public void setCommentLikes(List<CommentLike> commentLikes) {
		this.commentLikes = commentLikes;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Relationship> getRelationship() {
		return relationship;
	}

	public void setRelationship(List<Relationship> relationship) {
		this.relationship = relationship;
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

	@Override
	public String toString() {
		return "Account [idAccount=" + idAccount + ", posts=" + posts + ", comments=" + comments + ", postLikes="
				+ postLikes + ", commentLikes=" + commentLikes + ", messages=" + messages + ", relationship="
				+ relationship + ", notification=" + notification + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", password=" + password + ", gender=" + gender + ", livesIn="
				+ livesIn + ", study=" + study + ", wentTo=" + wentTo + ", profilePhoto=" + profilePhoto
				+ ", coverPhoto=" + coverPhoto + "]";
	}

}
