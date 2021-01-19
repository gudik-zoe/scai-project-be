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
	private Integer commentLikeCreatorId;
	
	@Column(name = "page_creator_id")
	private Integer pageCreatorId;

	public CommentLike() {

	}


	public CommentLike(int relatedCommentId, Integer commentLikeCreatorId, Integer pageCreatorId) {
		this.relatedCommentId = relatedCommentId;
		this.commentLikeCreatorId = commentLikeCreatorId;
		this.pageCreatorId = pageCreatorId;
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

	public Integer getCommentLikeCreatorId() {
		return commentLikeCreatorId;
	}

	public void setCommentLikeCreatorId(Integer commentLikeCreatorId) {
		this.commentLikeCreatorId = commentLikeCreatorId;
	}


	public Integer getPageCreatorId() {
		return pageCreatorId;
	}


	public void setPageCreatorId(Integer pageCreatorId) {
		this.pageCreatorId = pageCreatorId;
	}
	
	

}
