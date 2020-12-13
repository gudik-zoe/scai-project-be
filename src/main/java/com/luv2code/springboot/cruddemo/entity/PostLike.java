package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="post_like")
public class PostLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_like")
	private int idLike;
	
	
	@Column(name="related_post_id")
	private int relatedPostId;
	
	
	@Column(name="post_like_creator_id")
	private int postLikeCreatorId;
	
	public PostLike() {	
	}

	public PostLike(int relatedPostId, int postLikeCreatorId) {
		this.relatedPostId = relatedPostId;
		this.postLikeCreatorId = postLikeCreatorId;
	}

	public int getIdLike() {
		return idLike;
	}

	public void setIdLike(int idLike) {
		this.idLike = idLike;
	}

	public int getRelatedPostId() {
		return relatedPostId;
	}

	public void setRelatedPostId(int relatedPostId) {
		this.relatedPostId = relatedPostId;
	}

	public int getPostLikeCreatorId() {
		return postLikeCreatorId;
	}

	public void setPostLikeCreatorId(int postLikeCreatorId) {
		this.postLikeCreatorId = postLikeCreatorId;
	}
	
	

	
	
	
	
	
	

}
