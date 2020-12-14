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
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.springboot.cruddemo.service.NotificationService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.utility.AccountBasicData;

@Repository
public class PostsDAOHibernateImpl implements PostsDAO {

	private EntityManager entityManager;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private AccountService accountservice;

	@Autowired
	private RelationshipService relatopnshipService;

	@Autowired
	public PostsDAOHibernateImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public List<Post> getAllPosts() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession.createQuery("from Post", Post.class);
		List<Post> allPosts = theQuery.getResultList();
		return allPosts;
	}

	@Override
	public List<Post> getHomePagePosts(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		List<AccountBasicData> myFriends = accountservice.getMyFriends(accountId);
		List<Integer> ids = new ArrayList<Integer>();
		Query<Post> theQuery = null;
		List<Post> thePosts = new ArrayList<Post>();
		if (myFriends.size() > 0) {
			for (AccountBasicData friend : myFriends) {
				ids.add(friend.getIdAccount());
			}
			ids.add(accountId);

			for (Integer id : ids) {

				theQuery = currentSession.createQuery("from Post where posted_on =" + null + " and post_creator_id = "
						+ id + " and status != " + 2 + " order by date DESC", Post.class);
				thePosts.addAll(theQuery.getResultList());

			}

		} else {
			theQuery = currentSession.createQuery("from Post where posted_on =" + null + "and status !=" + 2
					+ " and post_creator_id = " + accountId + " order by date desc", Post.class);
			thePosts = theQuery.getResultList();
		}
		return thePosts;
	}

	@Override
	public List<Post> findPostByAccountId(int accountId, int loggedInUserId) {
		Session currentSession = entityManager.unwrap(Session.class);
		if (accountId == loggedInUserId) {
			Query<Post> theQuery = currentSession
					.createQuery("from Post where post_creator_id=" + accountId + " order by id_post DESC", Post.class);
			List<Post> thePosts = theQuery.getResultList();

			return thePosts;

		}
		Integer RelationshipStatus = relatopnshipService.getStatus(accountId, loggedInUserId);
		if (RelationshipStatus == 1) {
			Query<Post> theQuery = currentSession.createQuery(
					"from Post where post_creator_id=" + accountId + " and status != " + 2 + "order by id_post DESC",
					Post.class);
			List<Post> thePosts = theQuery.getResultList();
			return thePosts;
		} else {
			Query<Post> theQuery = currentSession.createQuery("from Post where post_creator_id=" + accountId
					+ " and status = " + 0 + " order by id_post DESC", Post.class);
			List<Post> thePosts = theQuery.getResultList();
			return thePosts;

		}
	}

	public Post findPostByPostId(int theId) {
		Session currentSession = entityManager.unwrap(Session.class);

		Post thePost = currentSession.get(Post.class, theId);

		return thePost;

	}

	@Override
	public Post savePost(int accountId, Post post) {
		Session currentSession = entityManager.unwrap(Session.class);
		post.setPostCreatorId(accountId);
		currentSession.save(post);
		return post;
	}

	@Override
	public Post postOnWall(int accountId, Post thePost) {
		Session currentSession = entityManager.unwrap(Session.class);
		thePost.setPostCreatorId(accountId);
		if (thePost.getPostedOn() != null) {
			Account theRequestedAccount = currentSession.get(Account.class, thePost.getPostedOn());
			if (theRequestedAccount != null) {
				currentSession.save(thePost);
				Notification WroteOnYourPost = new Notification(accountId, thePost.getPostedOn(), "posted on your wall",
						new Date(System.currentTimeMillis()), thePost.getIdPost(), false);
				notificationService.addNotification(WroteOnYourPost);
			} else {
				throw new CustomeException("this user doesn't exist");
			}
		}
		return thePost;

	}

	@Override
	public Post updatePost(Post updatedPost) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post theOldPost = currentSession.get(Post.class, updatedPost.getIdPost());
		currentSession.update(updatedPost);
		return updatedPost;
	}

	@Override
	public void deletePostById(int theId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post thePost = currentSession.get(Post.class, theId);
		try {
			currentSession.delete(thePost);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Post resharePost(int accountId, int idPost, String extraText, Integer status) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post theOriginalPost = currentSession.get(Post.class, idPost);
		Post resharedPost = new Post();
		resharedPost.setPostCreatorId(accountId);
		resharedPost.setPostOriginalId(idPost);
		resharedPost.setStatus(status);
		resharedPost.setDate(new Date(System.currentTimeMillis()));
		resharedPost.setExtraText(extraText);
		currentSession.save(resharedPost);

		Notification shareNotification = new Notification(accountId, theOriginalPost.getPostCreatorId(),
				"shared your post", new Date(System.currentTimeMillis()), idPost, false);
		notificationService.addNotification(shareNotification);
		return resharedPost;

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
