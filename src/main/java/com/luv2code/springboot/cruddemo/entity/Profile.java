package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private int profile_id;

    @Column(name = "related_account_id")
    private int relatedAccountId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="related_account_id" , referencedColumnName = "id_account" , insertable = false , updatable = false , nullable = false)
    private Account account;

	@OneToMany( cascade = CascadeType.ALL)
	@JoinColumn(name = "post_creator_id" , referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
    private List<Post> posts;


	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false )
	private List<Comment> comments;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "post_like_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<PostLike> postLikes;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_like_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<CommentLike> commentLikes;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sender", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<Message> messages;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_one_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<Relationship> relationship;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "not_creator", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<Notification> notification;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<Page> page;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "page_like_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<PageLike> pageLike;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<Event> events;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_react_creator_id", referencedColumnName = "profile_id", insertable = false, updatable = false, nullable = false)
	private List<EventReact> eventReact;

	public Profile(){

    }

    public Profile(int relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public int getRelatedAccountId() {
        return relatedAccountId;
    }

    public void setRelatedAccountId(int relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(List<Page> page) {
        this.page = page;
    }

    public List<PageLike> getPageLike() {
        return pageLike;
    }

    public void setPageLike(List<PageLike> pageLike) {
        this.pageLike = pageLike;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<EventReact> getEventReact() {
        return eventReact;
    }

    public void setEventReact(List<EventReact> eventReact) {
        this.eventReact = eventReact;
    }
}
