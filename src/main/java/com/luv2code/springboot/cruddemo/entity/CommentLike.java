package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comment_like")
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comment_like")
	private int idCommentLike;

	@Column(name = "related_comment_id")
	private int relatedCommentId;

	@Column(name = "comment_like_creator_id")
	private int commentLikeCreatorId;

	public CommentLike() {

	}

	public CommentLike(int relatedCommentId, int commentLikeCreatorId) {
		this.relatedCommentId = relatedCommentId;
		this.commentLikeCreatorId = commentLikeCreatorId;
	}

	public int getIdCommentLike() {
		return idCommentLike;
	}

	public void setIdCommentLike(int idCommentLike) {
		this.idCommentLike = idCommentLike;
	}

	public int getRelatedCommentId() {
		return relatedCommentId;
	}

	public void setRelatedCommentId(int relatedCommentId) {
		this.relatedCommentId = relatedCommentId;
	}

	public int getCommentLikeCreatorId() {
		return commentLikeCreatorId;
	}

	public void setCommentLikeCreatorId(int commentLikeCreatorId) {
		this.commentLikeCreatorId = commentLikeCreatorId;
	}

}
