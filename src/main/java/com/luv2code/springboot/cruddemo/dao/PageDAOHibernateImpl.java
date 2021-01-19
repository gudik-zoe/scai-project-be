package com.luv2code.springboot.cruddemo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.ImageUrl;
import com.luv2code.utility.PageBasicData;
import com.luv2code.utility.UpdatePage;

@Repository
public class PageDAOHibernateImpl implements PageDAO {

	private EntityManager entityManager;

	private StorageService storageService;

	@Autowired
	public PageDAOHibernateImpl(EntityManager theEntityManager, StorageService theStorageService) {
		entityManager = theEntityManager;
		storageService = theStorageService;
	}

	@Override
	public List<Page> getPages() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Page> theQuery = currentSession.createQuery("from Page", Page.class);
		List<Page> pages = theQuery.getResultList();
		return pages;
	}

	@Override
	public PageLike likePage(int accountId, int pageId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Page theLikedPage = currentSession.get(Page.class, pageId);
		if (theLikedPage == null) {
			throw new CustomeException("there is no such a page");
		}
		Query<PageLike> theQuery = currentSession.createQuery(
				"from PageLike where related_page_id=" + pageId + "and page_like_creator_id = " + accountId,
				PageLike.class);
		try {
			PageLike theLike = theQuery.getSingleResult();
			currentSession.delete(theLike);
			return null;
		} catch (Exception e) {
			PageLike theNewLike = new PageLike();
			if (theLikedPage.getPageCreatorId() != accountId) {
				theNewLike.setPageLikeCreatorId(accountId);
				theNewLike.setRelatedPageId(pageId);
				currentSession.save(theNewLike);
				return theNewLike;
			} else {
				throw new CustomeException("you can't like your own page");
			}
		}
	}

	@Override
	public List<Integer> getMyLikedPages(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<PageLike> theQuery = currentSession.createQuery("from PageLike where page_like_creator_id = " + accountId,
				PageLike.class);
		List<PageLike> pageLikes = theQuery.getResultList();
		List<Integer> thePagesIds = new ArrayList<Integer>();
		for (PageLike page : pageLikes) {
			thePagesIds.add(page.getRelatedPageId());
		}
		return thePagesIds;
	}

	@Override
	public PageBasicData getPageById(int pageId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Page thePage = currentSession.get(Page.class, pageId);
		if (thePage == null) {
			throw new CustomeException("this page doesn't exist");
		}
		PageBasicData pageBasicData = new PageBasicData(thePage.getIdPage(), thePage.getName(),
				thePage.getProfilePhoto(), thePage.getCoverPhoto(), thePage.getPageCreatorId());
		return pageBasicData;
	}

	@Override
	public Page createPage(Page page) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.save(page);
		return page;
	}

	@Override
	public List<Page> getMyPages(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Page> theQuery = currentSession.createQuery("from Page where page_creator_id = " + accountId, Page.class);
		if (theQuery.getResultList() != null) {
			return theQuery.getResultList();
		} else {
			return null;
		}

	}

	@Override
	public List<Post> getPagePosts(int pageId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession
				.createQuery("from Post where page_creator_id = " + pageId + "order by date desc", Post.class);
		if (theQuery.getResultList() != null) {
			return theQuery.getResultList();
		} else {
			return null;
		}
	}

	@Override
	public Post addPost(int accountId, int pageId, Post thePost) {
		Session currentSession = entityManager.unwrap(Session.class);
		try {
			Query<Page> theQuery = currentSession.createQuery(
					"from Page where id_page = " + pageId + " and page_creator_id = " + accountId, Page.class);
			if (theQuery != null) {
				thePost.setPageCreatorId(pageId);
				thePost.setStatus(0);
				thePost.setDate(new Date(System.currentTimeMillis()));
				currentSession.save(thePost);
			}
		} catch (Exception e) {
			throw new CustomeException("cannot add a post to an unexisting page or the page is not yours");
		}
		return thePost;
	}

	@Override
	public Post editPost(Post post) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.update(post);
		return post;
	}

	@Override
	public Comment addCommentAsPage(int accountId, int postId, String commentText) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post thePost = currentSession.get(Post.class, postId);
		if (thePost == null) {
			throw new CustomeException("there is no such post");
		} else {
			Page thePage = currentSession.get(Page.class, thePost.getPageCreatorId());
			if (thePage.getPageCreatorId() == accountId && commentText != null || commentText != "") {
				Comment pageComment = new Comment(commentText, postId, new Date(System.currentTimeMillis()), null,
						thePage.getIdPage());
				currentSession.save(pageComment);
				return pageComment;
			} else {
				throw new CustomeException("cannot enter an empty comment ");
			}
		}
	}

	@Override
	public CommentLike addLikeAsPage(int accountId, int pageId, int commentId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<CommentLike> theQuery = currentSession.createQuery(
				"from CommentLike where page_creator_id=" + pageId + "and related_comment_id = " + commentId,
				CommentLike.class);
		Comment theComment = currentSession.get(Comment.class, commentId);
		try {
			CommentLike theLikedComment = theQuery.getSingleResult();
			currentSession.delete(theLikedComment);
			return null;
		} catch (Exception e) {
			Page thePage = currentSession.get(Page.class, pageId);
			if (thePage.getPageCreatorId() != accountId) {
				throw new CustomeException("this is not your page");
			} else {
				CommentLike theCommentLike = new CommentLike(commentId, null, pageId);
				currentSession.save(theCommentLike);
				return theCommentLike;
			}
		}
	}

	@Override
	public Page getPageFullData(int pageId, int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Page theRequestedPage = currentSession.get(Page.class, pageId);
		if (theRequestedPage != null) {
			return theRequestedPage;
		} else {
			throw new CustomeException("there is no such page");
		}
	}

	@Override
	public Page updatePage(UpdatePage theNewPage, Page theOldPage) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		if (theNewPage.getProfilePhoto() != null) {
			ImageUrl profilePhoto = storageService.pushImage(theNewPage.getProfilePhoto());
			theOldPage.setProfilePhoto(profilePhoto.getImageUrl());
		}
		if (theNewPage.getCoverPhoto() != null) {
			ImageUrl coverPhoto = storageService.pushImage(theNewPage.getCoverPhoto());
			theOldPage.setCoverPhoto(coverPhoto.getImageUrl());
		}

		theOldPage.setName(theNewPage.getName());
		theOldPage.setDescription(theNewPage.getDescription());
		currentSession.update(theOldPage);
		return theOldPage;
	}
	
	@Override
	public List<String> getPagePhotos(int pageId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession.createQuery(
				"from Post where page_creator_id=" + pageId , Post.class);
		List<Post> posts = theQuery.getResultList();
		List<String> photos  = new ArrayList<String>();
		Page thePage = currentSession.get(Page.class, pageId);
		for(Post post:posts) {
			if(post.getImage() != null && !photos.contains(post.getImage())) {
				photos.add(post.getImage());
			}
		}

		photos.add(thePage.getProfilePhoto());
		photos.add(thePage.getCoverPhoto());
		
		return photos;
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	

}
