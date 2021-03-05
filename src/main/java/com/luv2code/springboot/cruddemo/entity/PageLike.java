package com.luv2code.springboot.cruddemo.entity;

import javax.persistence.*;



@Entity
@Table(name = "page_like")
public class PageLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_page_like")
	private int idPageLike;

	@Column(name = "related_page_id")
	private int relatedPageId;

	@Column(name = "page_like_creator_id")
	private int pageLikeCreatorId;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "page_like_creator_id", insertable = false, updatable = false, nullable = false)
//	private Account account;

	public PageLike() {

	}

	public PageLike(int pageLikeCreatorId, int relatedPageId) {
		this.pageLikeCreatorId = pageLikeCreatorId;
		this.relatedPageId = relatedPageId;
	}

	public int getIdPageLike() {
		return idPageLike;
	}

	public void setIdPageLike(int idPageLike) {
		this.idPageLike = idPageLike;
	}

	public int getPageLikeCreatorId() {
		return pageLikeCreatorId;
	}

	public void setPageLikeCreatorId(int pageLikeCreatorId) {
		this.pageLikeCreatorId = pageLikeCreatorId;
	}

	public int getRelatedPageId() {
		return relatedPageId;
	}

	public void setRelatedPageId(int relatedPageId) {
		this.relatedPageId = relatedPageId;
	}

}
