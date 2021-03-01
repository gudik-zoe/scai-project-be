package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;



@Entity
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comment")
	private int idComment;

	@Column(name = "text")
	private String text;

	@Column(name = "related_post_id")
	private int relatedPostId;
	@Column(name = "date")
	private Date date;

	@Column(name = "comment_creator_id")
	private Integer commentCreatorId;
	
	
	@Column(name = "page_Creator_id")
	private Integer pageCreatorId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "related_comment_id", referencedColumnName = "id_comment", insertable = false, updatable = false, nullable = false)
	private List<CommentLike> commentLike;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_creator_id", insertable = false, updatable = false, nullable = false)
	private Account account;

	public Comment() {

	}

	public Comment(String text, int relatedPostId, Date date, Integer commentCreatorId, Integer pageCreatorId) {
		this.text = text;
		this.relatedPostId = relatedPostId;
		this.date = date;
		this.commentCreatorId = commentCreatorId;
		this.pageCreatorId = pageCreatorId;
	}

	public int getIdComment() {
		return idComment;
	}

	public void setIdComment(int idComment) {
		this.idComment = idComment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRelatedPostId() {
		return relatedPostId;
	}

	public void setRelatedPostId(int relatedPostId) {
		this.relatedPostId = relatedPostId;
	}

	public Integer getCommentCreatorId() {
		return commentCreatorId;
	}

	public void setCommentCreatorId(Integer commentCreatorId) {
		this.commentCreatorId = commentCreatorId;
	}

	public List<CommentLike> getCommentLike() {
		return commentLike;
	}

	public void setCommentLike(List<CommentLike> commentLike) {
		this.commentLike = commentLike;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getPageCreatorId() {
		return pageCreatorId;
	}

	public void setPageCreatorId(Integer pageCreatorId) {
		this.pageCreatorId = pageCreatorId;
	}

}
