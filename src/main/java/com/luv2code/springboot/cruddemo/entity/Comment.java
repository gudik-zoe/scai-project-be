package com.luv2code.springboot.cruddemo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private int commentCreatorId;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "related_comment_id", referencedColumnName = "id_comment", insertable = false, updatable = false, nullable = false)
	private List<CommentLike> commentLike;

	public Comment() {

	}

	public Comment(String text, int relatedPostId, int commentCreatorId , Date date) {
		this.text = text;
		this.relatedPostId = relatedPostId;
		this.commentCreatorId = commentCreatorId;
		this.date = date;

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

	public int getCommentCreatorId() {
		return commentCreatorId;
	}

	public void setCommentCreatorId(int commentCreatorId) {
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

	
}
